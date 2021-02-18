package ast_st3_tac;

import static ast_expr.ExpressionBase.EASSIGN;
import static ast_expr.ExpressionBase.EBINARY;
import static ast_expr.ExpressionBase.ECAST;
import static ast_expr.ExpressionBase.ECLASS_CREATION;
import static ast_expr.ExpressionBase.EFIELD_ACCESS;
import static ast_expr.ExpressionBase.EMETHOD_INVOCATION;
import static ast_expr.ExpressionBase.EPRIMARY_IDENT;
import static ast_expr.ExpressionBase.EPRIMARY_NUMBER;
import static ast_expr.ExpressionBase.ETHIS;
import static ast_expr.ExpressionBase.EUNARY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ast_class.ClassDeclaration;
import ast_expr.ExprAssign;
import ast_expr.ExprBinary;
import ast_expr.ExprClassCreation;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_expr.ExprUnary;
import ast_expr.ExpressionBase;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_st2_annotate.LvalueUtil;
import ast_st2_annotate.Mods;
import ast_st3_tac.vars.Code;
import ast_st3_tac.vars.CodeItem;
import ast_st3_tac.vars.CopierNamer;
import ast_st3_tac.vars.StoreLeaf;
import ast_st3_tac.vars.TempVarAssign;
import ast_st3_tac.vars.arith.Binop;
import ast_st3_tac.vars.arith.Unop;
import ast_st3_tac.vars.store.AllocObject;
import ast_st3_tac.vars.store.Call;
import ast_st3_tac.vars.store.FieldAccess;
import ast_st3_tac.vars.store.Lvalue;
import ast_st3_tac.vars.store.Rvalue;
import ast_st3_tac.vars.store.Var;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeBindings;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import hashed.Hash_ident;
import literals.IntLiteral;
import tokenize.Ident;
import tokenize.Token;
import utils_oth.NullChecker;

public class TacGenerator {

  /// temporary expansion-stack
  private final Code temproraries;

  /// the 'raw' result
  private final Code rawResult;

  /// the result after rewriting
  private final Code rewrittenResult;

  /// all names, for constant pasting
  private final Map<Ident, Rvalue> allNames;

  private final CodegenContext context;

  public TacGenerator(ExprExpression expr, CodegenContext context) {
    this.temproraries = new Code();
    this.rawResult = new Code();
    this.rewrittenResult = new Code();
    this.allNames = new HashMap<>();
    this.context = context;

    gen(expr);
    rewrite();
  }

  public TacGenerator(VarDeclarator var, CodegenContext context) {
    this.temproraries = new Code();
    this.rawResult = new Code();
    this.rewrittenResult = new Code();
    this.allNames = new HashMap<>();
    this.context = context;

    ExprExpression expr = var.getSimpleInitializer();
    if (expr == null) {
      throw new AstParseException("variable has no initializer: " + var.getLocationToString());
    }

    gen(expr);
    rewrite();

    /// strtemp x1 = new strtemp(1); 
    /// ::
    /// int __t13 = 1;
    /// strtemp __t14 = new strtemp();
    /// strtemp_init_0(__t14, __t13);
    /// strtemp x1 = null;
    /// x1 = opAssign(x1, __t14);

    if (var.getType().is_class()) {
      CodeItem last = rewrittenResult.getLast();
      if (!last.isVoidCall()) {
        throw new AstParseException("expected void-call for class-creation");
      }

      //1
      Var lvalue = CopierNamer.copyVarDecl(var);
      TempVarAssign lhsvar = new TempVarAssign(lvalue, new Rvalue(0));

      //2
      ClassMethodDeclaration opAssign = lvalue.getType().getClassTypeFromRef()
          .getPredefinedMethod(Hash_ident.getHashedIdent("opAssign"));
      final Ident fn = Hash_ident.getHashedIdent(CopierNamer.getMethodName(opAssign));
      List<Var> args = new ArrayList<>();
      args.add(lvalue);
      args.add(last.getVoidCall().getArgs().get(0));
      final Call call = new Call(opAssign.getType(), fn, args, false);
      StoreLeaf leaf = new StoreLeaf(new Lvalue(lvalue), new Rvalue(call));

      rewrittenResult.appendItemLast(new CodeItem(lhsvar));
      rewrittenResult.appendItemLast(new CodeItem(leaf));
    }
  }

  private void rewrite() {
    for (CodeItem item : rawResult.getItems()) {
      if (isConstructorTempVarAssignCall(item)) {
        rewriteAllocObject(item);
        continue;
      }
      if (isTempVarClassAssignToVar(item)) {
        rewriteToAssignOp(item);
        continue;
      }
      rewrittenResult.appendItemLast(item);
    }
  }

  private void rewriteToAssignOp(CodeItem item) {

    /// strtemp __t29 = x1
    /// ::
    /// 1) strtemp __t29 = null
    /// 2) __t29 = opAssign(__t29, x1)

    final TempVarAssign assign = item.getVarAssign();
    final Var lvalue = assign.getVar();
    final Rvalue rvalue = assign.getRvalue();

    //1
    TempVarAssign lhsvar = new TempVarAssign(lvalue, new Rvalue(0));

    //2
    ClassMethodDeclaration opAssign = lvalue.getType().getClassTypeFromRef()
        .getPredefinedMethod(Hash_ident.getHashedIdent("opAssign"));
    final Ident fn = Hash_ident.getHashedIdent(CopierNamer.getMethodName(opAssign));
    List<Var> args = new ArrayList<>();
    args.add(lvalue);
    args.add(rvalue.getVar());
    final Call call = new Call(opAssign.getType(), fn, args, false);
    StoreLeaf leaf = new StoreLeaf(new Lvalue(lvalue), new Rvalue(call));

    rewrittenResult.appendItemLast(new CodeItem(lhsvar));
    rewrittenResult.appendItemLast(new CodeItem(leaf));
  }

  private boolean isTempVarClassAssignToVar(final CodeItem item) {
    if (!item.isVarAssign()) {
      return false;
    }

    final TempVarAssign assign = item.getVarAssign();
    final Var lvalue = assign.getVar();
    final Rvalue rvalue = assign.getRvalue();

    if (!lvalue.getType().is_class()) {
      return false;
    }

    if (!rvalue.isVar()) {
      return false;
    }

    if (context.getCurrentMethodName().getName().equals("opAssign")) {
      return false;
    }

    return true;
  }

  private boolean isConstructorTempVarAssignCall(final CodeItem item) {
    if (!item.isVarAssign()) {
      return false;
    }

    final TempVarAssign assign = item.getVarAssign();
    final Rvalue rvalue = assign.getRvalue();

    if (!rvalue.isCall()) {
      return false;
    }

    Call fcall = assign.getRvalue().getCall();
    if (!fcall.isConstructor()) {
      return false;
    }

    return true;
  }

  private void rewriteAllocObject(final CodeItem item) {

    final TempVarAssign assign = item.getVarAssign();
    final Var lvalue = assign.getVar();
    Call fcall = assign.getRvalue().getCall();

    // strtemp __t1 = strtemp_init_0(__t0)
    // ::
    // ::
    // 1) strtemp __t1 = new strtemp()
    // 2) strtemp_init_0(__t1, __t0)
    //
    AllocObject allocObject = new AllocObject(lvalue.getType());
    TempVarAssign newTempVarAssign = new TempVarAssign(lvalue, new Rvalue(allocObject));
    rewrittenResult.appendItemLast(new CodeItem(newTempVarAssign));

    fcall.getArgs().add(0, lvalue);
    rewrittenResult.appendItemLast(new CodeItem(fcall));

  }

  /// if (result_name)
  /// return (result_name)
  public String getLastResultNameToString() {

    /// TODO:TODO:TODO!!!

    final List<CodeItem> items = rewrittenResult.getItems();
    if (items.isEmpty()) {
      throw new AstParseException("there is no code for result-name");
    }

    final CodeItem lastItem = items.get(items.size() - 1);
    if (lastItem.isVarAssign()) {
      final Var lastVar = lastItem.getVarAssign().getVar();
      return lastVar.getName().getName();
    }
    if (lastItem.isStore() && lastItem.getStore().getLvalue().isDstVar()) {
      final Var lastVar = lastItem.getStore().getLvalue().getDstVar();
      return lastVar.getName().getName();
    }
    if (lastItem.isVoidCall()) {
      return lastItem.getVoidCall().getArgs().get(0).getName().toString();
    }

    throw new AstParseException("there is no code for result-name");
  }

  private void genRaw(CodeItem item) {
    temproraries.pushItem(item);
    rawResult.appendItemLast(item);

    if (item.isVarAssign()) {
      final TempVarAssign varAssign = item.getVarAssign();
      allNames.put(varAssign.getVar().getName(), varAssign.getRvalue());
    }
  }

  private CodeItem popCode() {
    return temproraries.popItem();
  }

  public String txt1(String end) {
    StringBuilder sb = new StringBuilder();
    for (CodeItem item : rewrittenResult.getItems()) {
      sb.append(item.toString().trim() + end);
    }
    return sb.toString().trim();
  }

  private List<Var> genArgsVars(final List<ExprExpression> arguments) {

    for (ExprExpression arg : arguments) {
      gen(arg);
    }

    List<Var> args = new ArrayList<>();
    for (int i = 0; i < arguments.size(); i++) {
      final CodeItem item = popCode();
      if (!item.isVarAssign()) {
        throw new AstParseException("unimpl.");
      }
      final TempVarAssign varAssign = item.getVarAssign();
      args.add(0, varAssign.getVar());
    }

    return args;
  }

  private void store(Type resultType) {

    final CodeItem srcItem = popCode();
    final CodeItem dstItem = popCode();

    if (dstItem.isVarAssign() && srcItem.isVarAssign()) {

      final TempVarAssign dstVarAssign = dstItem.getVarAssign();
      final TempVarAssign srcVarAssign = srcItem.getVarAssign();

      if (dstVarAssign.getRvalue().isVar()) {

        final Lvalue lvalueE = new Lvalue(dstVarAssign.getRvalue().getVar());

        final StoreLeaf storeOp = new StoreLeaf(lvalueE, new Rvalue(srcVarAssign.getVar()));
        genRaw(new CodeItem(storeOp));
      }

      else if (dstVarAssign.getRvalue().isFieldAccess()) {
        final Lvalue lvalueE = new Lvalue(dstVarAssign.getRvalue().getFieldAccess());

        final StoreLeaf storeOp = new StoreLeaf(lvalueE, new Rvalue(srcVarAssign.getVar()));
        genRaw(new CodeItem(storeOp));
      }

      else {
        throw new AstParseException("unimpl...");
      }

    } else {
      throw new AstParseException("unimpl...");
    }

  }

  private void gen(ExprExpression e) {
    NullChecker.check(e);
    ExpressionBase base = e.getBase();

    if (base == EASSIGN) {

      final ExprAssign assign = e.getAssign();

      final ExprExpression lvalue = assign.getLvalue();
      gen(lvalue);
      LvalueUtil.checkHard(lvalue);

      final ExprExpression rvalue = assign.getRvalue();
      gen(rvalue);

      store(e.getResultType());

    }

    else if (base == EBINARY) {

      final ExprBinary binary = e.getBinary();
      final Token op = binary.getOperator();

      gen(binary.getLhs());
      gen(binary.getRhs());

      final CodeItem Ritem = popCode();
      final CodeItem Litem = popCode();

      if (Ritem.isVarAssign() && Litem.isVarAssign()) {
        final TempVarAssign lvar = Litem.getVarAssign();
        final TempVarAssign rvar = Ritem.getVarAssign();

        final Var lvarRes = lvar.getVar();
        final Var rvarRes = rvar.getVar();

        final Binop binop = new Binop(lvarRes, op.getValue(), rvarRes);
        final TempVarAssign tempVarAssign = new TempVarAssign(CopierNamer.copyVarAddNewName(lvarRes),
            new Rvalue(binop));

        genRaw(new CodeItem(tempVarAssign));

      } else {
        throw new AstParseException("unimpl...");
      }

    }

    else if (base == EUNARY) {
      final ExprUnary unary = e.getUnary();
      final Token op = unary.getOperator();
      gen(unary.getOperand());

      final CodeItem Litem = popCode();

      if (Litem.isVarAssign()) {
        final TempVarAssign lvar = Litem.getVarAssign();

        final Var lvarRes = lvar.getVar();

        final Unop unop = new Unop(op.getValue(), lvarRes);
        final TempVarAssign tempVarAssign = new TempVarAssign(CopierNamer.copyVarAddNewName(lvarRes), new Rvalue(unop));

        genRaw(new CodeItem(tempVarAssign));

      } else {
        throw new AstParseException("unimpl...");
      }
    }

    else if (base == EPRIMARY_IDENT) {
      final ExprIdent exprId = e.getIdent();
      final VarDeclarator var = exprId.getVar();

      /// rewrite an identifier to [this.field] form
      /// if it is possible
      if (var.is(VarBase.CLASS_FIELD)) {

        final ClassDeclaration clazz = var.getClazz();
        final ExprExpression ethis = new ExprExpression(clazz, clazz.getBeginPos());
        final ExprFieldAccess eFaccess = new ExprFieldAccess(ethis, var.getIdentifier());
        eFaccess.setField(var);

        final ExprExpression generated = new ExprExpression(eFaccess, clazz.getBeginPos());
        gen(generated);

        return;
      }

      final Var lvalueTmp = CopierNamer.copyVarDeclAddNewName(var);
      final Rvalue rvalueTmp = new Rvalue(CopierNamer.copyVarDecl(var));
      final TempVarAssign tempVarAssign = new TempVarAssign(lvalueTmp, rvalueTmp);
      genRaw(new CodeItem(tempVarAssign));

    }

    else if (base == ExpressionBase.EPRIMARY_STRING) {
      throw new RuntimeException(base.toString() + " ???");
    }

    else if (base == EPRIMARY_NUMBER) {
      final IntLiteral number = e.getNumber();
      final Var lhsVar = new Var(VarBase.LOCAL_VAR, new Modifiers(), number.getType(), CopierNamer.tmpIdent());
      final Rvalue rhsValue = new Rvalue(number);
      final CodeItem item = new CodeItem(new TempVarAssign(lhsVar, rhsValue));
      genRaw(item);
    }

    else if (base == ECAST) {
      throw new RuntimeException(base.toString() + " ???");
    }

    else if (base == EMETHOD_INVOCATION) {
      final ExprMethodInvocation fcall = e.getMethodInvocation();

      //1
      gen(fcall.getObject());
      final CodeItem obj = popCode();

      //2
      final List<Var> args = genArgsVars(fcall.getArguments());
      args.add(0, obj.getVarAssign().getVar());

      //3
      final ClassMethodDeclaration method = fcall.getMethod();
      final Ident fn = Hash_ident.getHashedIdent(CopierNamer.getMethodName(method));
      final Call call = new Call(method.getType(), fn, args, false);

      if (method.isVoid()) {
        final CodeItem item = new CodeItem(call);
        genRaw(item);
      } else {
        final Var resultVar = new Var(VarBase.LOCAL_VAR, new Modifiers(), method.getType(), CopierNamer.tmpIdent());
        final CodeItem item = new CodeItem(new TempVarAssign(resultVar, new Rvalue(call)));
        genRaw(item);
      }

    }

    else if (base == EFIELD_ACCESS) {
      final ExprFieldAccess fieldAccess = e.getFieldAccess();
      gen(fieldAccess.getObject());

      final VarDeclarator field = fieldAccess.getField();

      final CodeItem thisItem = popCode();
      if (thisItem.isVarAssign()) {

        final FieldAccess access = new FieldAccess(thisItem.getVarAssign().getVar(), CopierNamer.copyVarDecl(field));
        final Var lhsvar = CopierNamer.copyVarDeclAddNewName(field);
        final CodeItem item = new CodeItem(new TempVarAssign(lhsvar, new Rvalue(access)));
        genRaw(item);

      } else {
        throw new AstParseException("unimpl...");
      }

    }

    else if (base == ECLASS_CREATION) {

      //1
      final ExprClassCreation fcall = e.getClassCreation();
      final Type typename = fcall.getType();

      //2
      final List<Var> args = genArgsVars(fcall.getArguments());

      //3
      final ClassMethodDeclaration constructor = fcall.getConstructor();
      final Ident fn = Hash_ident.getHashedIdent(CopierNamer.getMethodName(constructor));
      final Call call = new Call(constructor.getType(), fn, args, true);
      final Var lvalue = new Var(VarBase.LOCAL_VAR, new Modifiers(), typename, CopierNamer.tmpIdent());
      final Rvalue rvalue = new Rvalue(call);
      final TempVarAssign varAssign = new TempVarAssign(lvalue, rvalue);
      final CodeItem item = new CodeItem(varAssign);
      genRaw(item);

    }

    else if (base == ETHIS) {

      final ClassDeclaration clazz = e.getSelfExpression();
      final Type classType = new Type(new ClassTypeRef(clazz, clazz.getTypeParametersT()), e.getBeginPos());

      // main_class __t2 = _this_
      final Var lhsVar = new Var(VarBase.LOCAL_VAR, new Modifiers(), classType, CopierNamer.tmpIdent());
      final Var rhsVar = new Var(VarBase.METHOD_PARAMETER, Mods.letMods(), classType, CopierNamer._this_());
      final CodeItem item = new CodeItem(new TempVarAssign(lhsVar, new Rvalue(rhsVar)));
      genRaw(item);
    }

    else if (base == ExpressionBase.EBOOLEAN_LITERAL) {
      final Var lvalueTmp = new Var(VarBase.LOCAL_VAR, new Modifiers(), TypeBindings.make_boolean(e.getBeginPos()),
          CopierNamer.tmpIdent());
      final Rvalue rvalueTmp = new Rvalue(e.getBooleanLiteral());
      final TempVarAssign tempVarAssign = new TempVarAssign(lvalueTmp, rvalueTmp);
      genRaw(new CodeItem(tempVarAssign));
    }

    else {
      throw new RuntimeException(base.toString() + ": unimplemented");
    }

  }

}
