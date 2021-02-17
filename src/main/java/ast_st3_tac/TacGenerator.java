package ast_st3_tac;

import static ast_expr.ExpressionBase.EASSIGN;
import static ast_expr.ExpressionBase.EBINARY;
import static ast_expr.ExpressionBase.ECAST;
import static ast_expr.ExpressionBase.ECLASS_INSTANCE_CREATION;
import static ast_expr.ExpressionBase.EFIELD_ACCESS;
import static ast_expr.ExpressionBase.EMETHOD_INVOCATION;
import static ast_expr.ExpressionBase.EPRIMARY_IDENT;
import static ast_expr.ExpressionBase.EPRIMARY_NULL_LITERAL;
import static ast_expr.ExpressionBase.EPRIMARY_NUMBER;
import static ast_expr.ExpressionBase.EPRIMARY_STRING;
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
import ast_st3_tac.vars.store.Literal;
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

  /// the result-name for 'if' and 'return'
  private Var lastResultVar;

  /// all names, for constant pasting
  private final Map<Ident, Rvalue> allNames;

  public TacGenerator(ExprExpression expr) {
    temproraries = new Code();
    rawResult = new Code();
    rewrittenResult = new Code();
    allNames = new HashMap<>();

    gen(expr);
    rewrite();
  }

  private void rewrite() {
    for (CodeItem item : rawResult.getItems()) {
      if (!item.isVarAssign()) {
        rewrittenResult.appendItemLast(item);
        continue;
      }

      final TempVarAssign assign = item.getVarAssign();
      final Var lvalue = assign.getVar();
      final Rvalue rvalue = assign.getRvalue();

      if (!rvalue.isCall()) {
        rewrittenResult.appendItemLast(item);
        continue;
      }

      Call fcall = assign.getRvalue().getCall();
      if (!fcall.isConstructor()) {
        rewrittenResult.appendItemLast(item);
        continue;
      }

      // strtemp __t1 = strtemp_init_0(__t0)
      // ::
      // 1) strtemp __t1 = new strtemp()
      // 2) strtemp_init_0(__t1, __t0)
      AllocObject allocObject = new AllocObject(lvalue.getType());
      TempVarAssign newTempVarAssign = new TempVarAssign(lvalue, new Rvalue(allocObject));
      rewrittenResult.appendItemLast(new CodeItem(newTempVarAssign));

      fcall.getArgs().add(0, lvalue);
      rewrittenResult.appendItemLast(new CodeItem(fcall));
      lastResultVar = lvalue;
    }
  }

  /// if (result_name)
  /// return (result_name)
  public String getLastResultNameToString() {
    if (lastResultVar != null) {
      return lastResultVar.getName().toString();
    }

    final List<CodeItem> items = rewrittenResult.getItems();
    if (items.isEmpty()) {
      throw new AstParseException("there is no code for result-name");
    }

    final CodeItem lastItem = items.get(items.size() - 1);
    if (!lastItem.isVarAssign()) {
      throw new AstParseException("there is no code for result-name");
    }

    final Var lastVar = lastItem.getVarAssign().getVar();
    return lastVar.getName().getName();
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

    else if (base == EPRIMARY_STRING) {
      throw new RuntimeException(base.toString() + " ???");
    }

    else if (base == EPRIMARY_NUMBER) {
      final IntLiteral number = e.getNumber();
      final Var lhsVar = new Var(VarBase.LOCAL_VAR, new Modifiers(), number.getType(), CopierNamer.tmpIdent());
      final Rvalue rhsValue = new Rvalue(number);
      final CodeItem item = new CodeItem(new TempVarAssign(lhsVar, rhsValue));
      genRaw(item);
    }

    else if (base == EPRIMARY_NULL_LITERAL) {
      throw new RuntimeException(base.toString() + " ???");
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

    else if (base == ECLASS_INSTANCE_CREATION) {

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
      Literal lit = new Literal(e.getBooleanLiteral());
      final Var lvalueTmp = new Var(VarBase.LOCAL_VAR, new Modifiers(), TypeBindings.make_boolean(e.getBeginPos()),
          CopierNamer.tmpIdent());
      final Rvalue rvalueTmp = new Rvalue(lit);
      final TempVarAssign tempVarAssign = new TempVarAssign(lvalueTmp, rvalueTmp);
      genRaw(new CodeItem(tempVarAssign));
    }

    else {
      throw new RuntimeException(base.toString() + ": unimplemented");
    }

  }

}
