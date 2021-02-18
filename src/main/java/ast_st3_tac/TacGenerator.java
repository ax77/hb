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
import ast_st3_tac.ir.AssignVarBinop;
import ast_st3_tac.ir.AssignVarFieldAccess;
import ast_st3_tac.ir.AssignVarFlatCallClassCreationTmp;
import ast_st3_tac.ir.AssignVarFlatCallResult;
import ast_st3_tac.ir.AssignVarNum;
import ast_st3_tac.ir.AssignVarUnop;
import ast_st3_tac.ir.AssignVarVar;
import ast_st3_tac.ir.FlatCallClassCreationTmp;
import ast_st3_tac.ir.FlatCallResult;
import ast_st3_tac.ir.FlatCallVoid;
import ast_st3_tac.ir.FlatCodeItem;
import ast_st3_tac.ir.Opc;
import ast_st3_tac.ir.StoreFieldVar;
import ast_st3_tac.ir.StoreVarVar;
import ast_st3_tac.vars.CopierNamer;
import ast_st3_tac.vars.arith.Binop;
import ast_st3_tac.vars.arith.Unop;
import ast_st3_tac.vars.store.FieldAccess;
import ast_st3_tac.vars.store.Var;
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

  public TacGenerator(ExprExpression expr) {
    this.temproraries = new ArrayList<>();
    this.rawResult = new ArrayList<>();

    gen(expr);
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
    for (FlatCodeItem item : rawResult) {
      sb.append(item.toString().trim() + end);
    }
    return sb.toString().trim();
  }

  private List<Var> genArgs(final List<ExprExpression> arguments) {

    for (ExprExpression arg : arguments) {
      gen(arg);
    }

    List<Var> args = new ArrayList<>();
    for (int i = 0; i < arguments.size(); i++) {
      final FlatCodeItem item = popCode();
      args.add(0, getDest(item));
    }

    return args;
  }

  private Var getDest(FlatCodeItem item) {
    if (item.isAssignVarBinop()) {
      return item.getAssignVarBinop().getLvalue();
    } else if (item.isAssignVarFieldAccess()) {
      return item.getAssignVarFieldAccess().getLvalue();
    } else if (item.isAssignVarFlatCallResult()) {
      return item.getAssignVarFlatCallResult().getLvalue();
    } else if (item.isAssignVarNull()) {
      return item.getAssignVarNull().getLvalue();
    } else if (item.isAssignVarNum()) {
      return item.getAssignVarNum().getLvalue();
    } else if (item.isAssignVarUnop()) {
      return item.getAssignVarUnop().getLvalue();
    } else if (item.isAssignVarVar()) {
      return item.getAssignVarVar().getLvalue();
    } else if (item.isFlatCallConstructor()) {
      return item.getFlatCallConstructor().getThisVar();
    } else if (item.getOpcode().equals(Opc.AssignVarFlatCallClassCreationTmp)) {
      return item.getAssignVarFlatCallClassCreationTmp().getLvalue();
    }
    throw new AstParseException(item.toString() + " ???");
  }

  private void store(Type resultType) {

    final FlatCodeItem srcItem = popCode();
    final FlatCodeItem dstItem = popCode();

    if (dstItem.isAssignVarVar()) {

      // it was: a = b
      // we need: b = srv
      final Var dst = dstItem.getAssignVarVar().getRvalue();
      final Var src = getDest(srcItem);

      FlatCodeItem item = new FlatCodeItem(new StoreVarVar(dst, src));
      genRaw(item);

    }

    else if (dstItem.isAssignVarFieldAccess()) {

      // it was: a = b.c
      // we need: b.c = src

      final FieldAccess dst = dstItem.getAssignVarFieldAccess().getRvalue();
      final Var src = getDest(srcItem);

      FlatCodeItem item = new FlatCodeItem(new StoreFieldVar(dst, src));
      genRaw(item);
    }

    else {
      throw new AstParseException("unimplimented store for dst: " + dstItem.toString());
    }

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

      final Var lvarRes = getDest(Litem);
      final Var rvarRes = getDest(Ritem);
      final Binop binop = new Binop(lvarRes, op.getValue(), rvarRes);

      FlatCodeItem item = new FlatCodeItem(new AssignVarBinop(CopierNamer.copyVarAddNewName(lvarRes), binop));
      genRaw(item);

    }

    else if (base == EUNARY) {
      final ExprUnary unary = e.getUnary();
      final Token op = unary.getOperator();
      gen(unary.getOperand());

      final FlatCodeItem Litem = popCode();

      final Var lvarRes = getDest(Litem);
      final Unop unop = new Unop(op.getValue(), lvarRes);

      FlatCodeItem item = new FlatCodeItem(new AssignVarUnop(CopierNamer.copyVarAddNewName(lvarRes), unop));
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

      final Var lvalueTmp = CopierNamer.copyVarDeclAddNewName(var);
      final Var rvalueTmp = CopierNamer.copyVarDecl(var);
      final FlatCodeItem item = new FlatCodeItem(new AssignVarVar(lvalueTmp, rvalueTmp));
      genRaw(item);
    }

    else if (base == ExpressionBase.EPRIMARY_STRING) {
      throw new RuntimeException(base.toString() + " ???");
    }

    else if (base == EPRIMARY_NUMBER) {
      final IntLiteral number = e.getNumber();
      final Var lhsVar = new Var(VarBase.LOCAL_VAR, new Modifiers(), number.getType(), CopierNamer.tmpIdent());
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
      args.add(0, getDest(obj));

      //3
      final ClassMethodDeclaration method = fcall.getMethod();
      final Ident fn = Hash_ident.getHashedIdent(CopierNamer.getMethodName(method));

      if (method.isVoid()) {
        final FlatCallVoid call = new FlatCallVoid(fn, args);
        final FlatCodeItem item = new FlatCodeItem(call);
        genRaw(item);
      }

      else {
        final FlatCallResult call = new FlatCallResult(method.getType(), fn, args);
        final Var resultVar = new Var(VarBase.LOCAL_VAR, new Modifiers(), method.getType(), CopierNamer.tmpIdent());
        final FlatCodeItem item = new FlatCodeItem(new AssignVarFlatCallResult(resultVar, call));
        genRaw(item);
      }

    }

    else if (base == EFIELD_ACCESS) {
      final ExprFieldAccess fieldAccess = e.getFieldAccess();
      gen(fieldAccess.getObject());

      final VarDeclarator field = fieldAccess.getField();
      final FlatCodeItem thisItem = popCode();
      final Var obj = getDest(thisItem);

      final FieldAccess access = new FieldAccess(obj, CopierNamer.copyVarDecl(field));
      final Var lhsvar = CopierNamer.copyVarDeclAddNewName(field);

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
      final FlatCallClassCreationTmp call = new FlatCallClassCreationTmp(constructor.getType(), fn, args);
      final Var lvalue = new Var(VarBase.LOCAL_VAR, new Modifiers(), typename, CopierNamer.tmpIdent());
      final AssignVarFlatCallClassCreationTmp assignVarFlatCallResult = new AssignVarFlatCallClassCreationTmp(lvalue,
          call);
      final FlatCodeItem item = new FlatCodeItem(assignVarFlatCallResult);
      genRaw(item);

    }

    else if (base == ETHIS) {

      final ClassDeclaration clazz = e.getSelfExpression();
      final Type classType = new Type(new ClassTypeRef(clazz, clazz.getTypeParametersT()), e.getBeginPos());

      // main_class __t2 = _this_
      final Var lhsVar = new Var(VarBase.LOCAL_VAR, new Modifiers(), classType, CopierNamer.tmpIdent());
      final Var rhsVar = new Var(VarBase.METHOD_PARAMETER, Mods.letMods(), classType, CopierNamer._this_());
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
