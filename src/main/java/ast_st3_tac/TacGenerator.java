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
import java.util.List;

import ast_builtins.BuiltinNames;
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
import ast_st3_tac.assign_ops.VarVarAssignOp;
import ast_st3_tac.ir.CopierNamer;
import ast_st3_tac.ir.FlatCodeItem;
import ast_st3_tac.items.AssignVarAllocObject;
import ast_st3_tac.items.AssignVarBinop;
import ast_st3_tac.items.AssignVarFieldAccess;
import ast_st3_tac.items.AssignVarFlatCallClassCreationTmp;
import ast_st3_tac.items.AssignVarFlatCallResult;
import ast_st3_tac.items.AssignVarNull;
import ast_st3_tac.items.AssignVarNum;
import ast_st3_tac.items.AssignVarUnop;
import ast_st3_tac.items.AssignVarVar;
import ast_st3_tac.items.FlatCallConstructor;
import ast_st3_tac.items.FlatCallVoid;
import ast_st3_tac.items.StoreFieldVar;
import ast_st3_tac.items.StoreVarVar;
import ast_st3_tac.items.StoreVarVarAssignOp;
import ast_st3_tac.leaves.Binop;
import ast_st3_tac.leaves.FieldAccess;
import ast_st3_tac.leaves.PureFunctionCallWithResult;
import ast_st3_tac.leaves.Unop;
import ast_st3_tac.leaves.Var;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import hashed.Hash_ident;
import literals.IntLiteral;
import tokenize.Ident;
import tokenize.Token;
import utils_oth.NullChecker;

public class TacGenerator {

  private final List<FlatCodeItem> temproraries;
  private final List<FlatCodeItem> rawResult;
  private final List<FlatCodeItem> rv;
  private final String exprTos;
  private final List<Var> allVars;

  public TacGenerator(ExprExpression expr) {
    this.temproraries = new ArrayList<>();
    this.rawResult = new ArrayList<>();
    this.rv = new ArrayList<>();
    this.exprTos = expr.toString();
    this.allVars = new ArrayList<>();

    gen(expr);
    rewriteRaw();
  }

  public TacGenerator(VarDeclarator var) {
    this.temproraries = new ArrayList<>();
    this.rawResult = new ArrayList<>();
    this.rv = new ArrayList<>();
    this.exprTos = var.toString();
    this.allVars = new ArrayList<>();

    if (var.getSimpleInitializer() == null) {
      throw new AstParseException(
          var.getLocationToString() + ":error: variable without an initializer: " + var.toString());
    }

    gen(var.getSimpleInitializer());
    rewriteRaw();

    Var lvaluevar = copyVarDecl(var);
    Var rvaluevar = getLast().getDest();

    if (var.getType().is_class()) {
      genOpAssign(lvaluevar, rvaluevar);
    }

    else {
      AssignVarVar assignVarVar = new AssignVarVar(lvaluevar, rvaluevar);
      rv.add(new FlatCodeItem(assignVarVar));
    }
  }

  /// var holder-creator

  private Var copyVarAddNewName(Var src) {
    final Var result = new Var(checkBase(src.getBase()), src.getMods(), src.getType(), CopierNamer.tmpIdent());
    if (!allVars.contains(result)) {
      allVars.add(result);
    }
    return result;
  }

  private Var copyVarDecl(VarDeclarator src) {
    final Var result = new Var(checkBase(src.getBase()), src.getMods(), src.getType(), src.getIdentifier());
    if (!allVars.contains(result)) {
      allVars.add(result);
    }
    return result;
  }

  private Var copyVarDeclAsFieldNoBindings(VarDeclarator src) {
    final Var result = new Var(checkBase(src.getBase()), src.getMods(), src.getType(), src.getIdentifier());
    return result;
  }

  private Var copyVarDeclAddNewName(VarDeclarator var) {
    final Var result = new Var(checkBase(var.getBase()), var.getMods(), var.getType(), CopierNamer.tmpIdent());
    if (!allVars.contains(result)) {
      allVars.add(result);
    }
    return result;
  }

  private Var justNewVar(Type type) {
    final Var result = new Var(VarBase.LOCAL_VAR, new Modifiers(), type, CopierNamer.tmpIdent());
    if (!allVars.contains(result)) {
      allVars.add(result);
    }
    return result;
  }

  private Var just_this_(Type type) {
    final Var result = new Var(VarBase.METHOD_PARAMETER, Mods.letMods(), type, CopierNamer._this_());
    if (!allVars.contains(result)) {
      allVars.add(result);
    }
    return result;
  }

  private VarBase checkBase(VarBase base) {
    if (base != VarBase.METHOD_PARAMETER) {
      return VarBase.LOCAL_VAR;
    }
    return VarBase.METHOD_PARAMETER;
  }

  private FlatCodeItem getLast() {
    return rv.get(rv.size() - 1);
  }

  public String getLastResultNameToString() {
    FlatCodeItem item = getLast();
    return item.getDest().getName().getName();
  }

  private void rewriteRaw() {

    for (final FlatCodeItem item : rawResult) {

      if (item.isAssignVarAllocObject()) {
        rv.add(item);
      } else if (item.isAssignVarArrayAccess()) {
        rv.add(item);
      } else if (item.isAssignVarBinop()) {
        rv.add(item);
      } else if (item.isAssignVarFalse()) {
        rv.add(item);
      } else if (item.isAssignVarFieldAccess()) {
        rv.add(item);
      } else if (item.isAssignVarFlatCallClassCreationTmp()) {

        // strtemp __t15 = strtemp_init_0(__t14)
        // ::
        // strtemp __t15 = new strtemp()
        // strtemp_init_0(__t15, __t14)

        final AssignVarFlatCallClassCreationTmp node = item.getAssignVarFlatCallClassCreationTmp();
        final Var lvalueVar = node.getLvalue();
        AssignVarAllocObject assignVarAllocObject = new AssignVarAllocObject(lvalueVar, lvalueVar.getType());
        rv.add(new FlatCodeItem(assignVarAllocObject));

        final PureFunctionCallWithResult rvalue = node.getRvalue();
        final List<Var> args = rvalue.getArgs();
        args.add(0, lvalueVar);
        FlatCallConstructor flatCallConstructor = new FlatCallConstructor(rvalue.getFunction(), args, lvalueVar);
        rv.add(new FlatCodeItem(flatCallConstructor));

      } else if (item.isAssignVarFlatCallResult()) {
        rv.add(item);
      } else if (item.isAssignVarNull()) {
        rv.add(item);
      } else if (item.isAssignVarNum()) {
        rv.add(item);
      } else if (item.isAssignVarString()) {
        rv.add(item);
      } else if (item.isAssignVarTrue()) {
        rv.add(item);
      } else if (item.isAssignVarUnop()) {
        rv.add(item);
      }

      else if (item.isAssignVarVar()) {
        AssignVarVar node = item.getAssignVarVar();
        final Var lvalueVar = node.getLvalue();

        if (lvalueVar.getType().is_class()) {
          // token __t14 = tok1;
          // ::
          // token __t14 = null
          // __t14 = opAssign(__t14, tok1)
          genOpAssign(lvalueVar, node.getRvalue());
        }

        else {
          rv.add(item);
        }

      }

      else if (item.isFlatCallConstructor()) {
        rv.add(item);
      } else if (item.isFlatCallVoid()) {
        rv.add(item);
      } else if (item.isStoreArrayVarAssignOp()) {
        rv.add(item);
      } else if (item.isStoreArrayVar()) {
        rv.add(item);
      } else if (item.isStoreFieldVarAssignOp()) {
        rv.add(item);
      } else if (item.isStoreFieldVar()) {
        rv.add(item);
      } else if (item.isStoreVarVarAssignOp()) {
        rv.add(item);
      }

      else if (item.isStoreVarVar()) {

        final StoreVarVar node = item.getStoreVarVar();
        final Var lvalueVar = node.getDst();
        if (lvalueVar.getType().is_class()) {

          // tok1 = __t17;
          // ::
          // tok1 = null;
          // tok1 = opAssign(tok1, __t17);
          genOpAssign(lvalueVar, node.getSrc());
        }

        else {
          rv.add(item);
        }

      }

      else {
        throw new AstParseException("unknown item: " + item.toString());
      }

    }

  }

  private void genOpAssign(Var lvalueVar, Var rvalueVar) {
    AssignVarNull assignVarNull = new AssignVarNull(lvalueVar);
    rv.add(new FlatCodeItem(assignVarNull));

    ClassMethodDeclaration opAssign = lvalueVar.getType().getClassTypeFromRef()
        .getPredefinedMethod(BuiltinNames.opAssign_ident);

    Ident fn = Hash_ident.getHashedIdent(CopierNamer.getMethodName(opAssign));
    VarVarAssignOp aux = new VarVarAssignOp(lvalueVar.getType(), fn, lvalueVar, rvalueVar);
    StoreVarVarAssignOp store = new StoreVarVarAssignOp(lvalueVar, aux);
    rv.add(new FlatCodeItem(store));
  }

  private void genRaw(FlatCodeItem item) {
    temproraries.add(0, item);
    rawResult.add(item);
  }

  private FlatCodeItem popCode() {
    return temproraries.remove(0);
  }

  public String txt1(String end) {
    StringBuilder sb = new StringBuilder();
    // sb.append("// " + exprTos + "\n");
    for (FlatCodeItem item : rv) {
      String f = String.format("%s", item.toString().trim() + end);
      // sb.append("// " + item.getOpcode().toString() + "\n");
      sb.append(f);
    }
    return "// " + allVars.toString() + "\n" + sb.toString().trim();
  }

  private List<Var> genArgs(final List<ExprExpression> arguments) {

    for (ExprExpression arg : arguments) {
      gen(arg);
    }

    List<Var> args = new ArrayList<>();
    for (int i = 0; i < arguments.size(); i++) {
      final FlatCodeItem item = popCode();
      args.add(0, item.getDest());
    }

    return args;
  }

  private void store(Type resultType) {

    final FlatCodeItem srcItem = popCode();
    final FlatCodeItem dstItem = popCode();

    if (dstItem.isAssignVarVar()) {

      // it was: a = b
      // we need: b = srv
      final Var dst = dstItem.getAssignVarVar().getRvalue();
      final Var src = srcItem.getDest();

      FlatCodeItem item = new FlatCodeItem(new StoreVarVar(dst, src));
      genRaw(item);

    }

    else if (dstItem.isAssignVarFieldAccess()) {

      // it was: a = b.c
      // we need: b.c = src

      final FieldAccess dst = dstItem.getAssignVarFieldAccess().getRvalue();
      final Var src = srcItem.getDest();

      FlatCodeItem item = new FlatCodeItem(new StoreFieldVar(dst, src));
      genRaw(item);
    }

    else {
      throw new AstParseException("unimplimented store for dst: " + dstItem.toString());
    }

    rawResult.remove(dstItem);

  }

  private void gen(ExprExpression e) {
    NullChecker.check(e);
    ExpressionBase base = e.getBase();

    if (base == EASSIGN) {
      final ExprAssign assign = e.getAssign();

      final ExprExpression lvalue = assign.getLvalue();
      gen(lvalue);

      final ExprExpression rvalue = assign.getRvalue();
      gen(rvalue);

      LvalueUtil.checkHard(lvalue);
      store(e.getResultType());
    }

    else if (base == EBINARY) {

      final ExprBinary binary = e.getBinary();
      final Token op = binary.getOperator();

      gen(binary.getLhs());
      gen(binary.getRhs());

      final FlatCodeItem Ritem = popCode();
      final FlatCodeItem Litem = popCode();

      final Var lvarRes = Litem.getDest();
      final Var rvarRes = Ritem.getDest();
      final Binop binop = new Binop(lvarRes, op.getValue(), rvarRes);

      FlatCodeItem item = new FlatCodeItem(new AssignVarBinop(copyVarAddNewName(lvarRes), binop));
      genRaw(item);

    }

    else if (base == EUNARY) {
      final ExprUnary unary = e.getUnary();
      final Token op = unary.getOperator();
      gen(unary.getOperand());

      final FlatCodeItem Litem = popCode();

      final Var lvarRes = Litem.getDest();
      final Unop unop = new Unop(op.getValue(), lvarRes);

      FlatCodeItem item = new FlatCodeItem(new AssignVarUnop(copyVarAddNewName(lvarRes), unop));
      genRaw(item);
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

      final Var lvalueTmp = copyVarDeclAddNewName(var);
      final Var rvalueTmp = copyVarDecl(var);
      final FlatCodeItem item = new FlatCodeItem(new AssignVarVar(lvalueTmp, rvalueTmp));
      genRaw(item);
    }

    else if (base == ExpressionBase.EPRIMARY_STRING) {
      throw new RuntimeException(base.toString() + " ???");
    }

    else if (base == EPRIMARY_NUMBER) {
      final IntLiteral number = e.getNumber();
      final Var lhsVar = justNewVar(number.getType());
      AssignVarNum assignVarNum = new AssignVarNum(lhsVar, number);
      genRaw(new FlatCodeItem(assignVarNum));
    }

    else if (base == ECAST) {
      throw new RuntimeException(base.toString() + " ???");
    }

    else if (base == EMETHOD_INVOCATION) {
      final ExprMethodInvocation fcall = e.getMethodInvocation();

      //1
      gen(fcall.getObject());
      final FlatCodeItem obj = popCode();

      //2
      final List<Var> args = genArgs(fcall.getArguments());
      args.add(0, obj.getDest());

      //3
      final ClassMethodDeclaration method = fcall.getMethod();
      final Ident fn = Hash_ident.getHashedIdent(CopierNamer.getMethodName(method));

      if (method.isVoid()) {
        final FlatCallVoid call = new FlatCallVoid(fn, args);
        final FlatCodeItem item = new FlatCodeItem(call);
        genRaw(item);
      }

      else {
        final PureFunctionCallWithResult call = new PureFunctionCallWithResult(method.getType(), fn, args);
        final Var resultVar = justNewVar(method.getType());
        final FlatCodeItem item = new FlatCodeItem(new AssignVarFlatCallResult(resultVar, call));
        genRaw(item);
      }

    }

    else if (base == EFIELD_ACCESS) {
      final ExprFieldAccess fieldAccess = e.getFieldAccess();
      gen(fieldAccess.getObject());

      final VarDeclarator field = fieldAccess.getField();
      final FlatCodeItem thisItem = popCode();
      final Var obj = thisItem.getDest();

      final FieldAccess access = new FieldAccess(obj, copyVarDeclAsFieldNoBindings(field));
      final Var lhsvar = copyVarDeclAddNewName(field);

      final FlatCodeItem item = new FlatCodeItem(new AssignVarFieldAccess(lhsvar, access));
      genRaw(item);

    }

    else if (base == ECLASS_CREATION) {

      /// AX: do not write any additional code here
      /// because of the stack logic.
      /// or you will ruin the stack...
      /// the class-creation will be rewritten later,
      /// here we need only call, with generated args.

      //1
      final ExprClassCreation fcall = e.getClassCreation();
      final Type typename = fcall.getType();

      //2
      final List<Var> args = genArgs(fcall.getArguments());

      //3
      final ClassMethodDeclaration constructor = fcall.getConstructor();
      final Ident fn = Hash_ident.getHashedIdent(CopierNamer.getMethodName(constructor));
      final PureFunctionCallWithResult call = new PureFunctionCallWithResult(constructor.getType(), fn, args);
      final Var lvalue = justNewVar(typename);
      final AssignVarFlatCallClassCreationTmp assignVarFlatCallResult = new AssignVarFlatCallClassCreationTmp(lvalue,
          call);
      final FlatCodeItem item = new FlatCodeItem(assignVarFlatCallResult);
      genRaw(item);

    }

    else if (base == ETHIS) {

      final ClassDeclaration clazz = e.getSelfExpression();
      final Type classType = new Type(new ClassTypeRef(clazz, clazz.getTypeParametersT()), e.getBeginPos());

      // main_class __t2 = _this_
      final Var lhsVar = justNewVar(classType);
      final Var rhsVar = just_this_(classType);
      final FlatCodeItem item = new FlatCodeItem(new AssignVarVar(lhsVar, rhsVar));
      genRaw(item);
    }

    else if (base == ExpressionBase.EBOOLEAN_LITERAL) {
      throw new RuntimeException(base.toString() + " ???");
      //      final Var lvalueTmp = new Var(VarBase.LOCAL_VAR, new Modifiers(), TypeBindings.make_boolean(e.getBeginPos()),
      //          CopierNamer.tmpIdent());
      //      final Rvalue rvalueTmp = new Rvalue(e.getBooleanLiteral());
      //      final TempVarAssign tempVarAssign = new TempVarAssign(lvalueTmp, rvalueTmp);
      //      genRaw(new CodeItem(tempVarAssign));
    }

    else {
      throw new RuntimeException(base.toString() + ": unimplemented");
    }

  }

}
