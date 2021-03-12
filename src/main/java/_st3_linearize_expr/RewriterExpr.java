package _st3_linearize_expr;

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

import _st2_annotate.LvalueUtil;
import _st3_linearize_expr.assign_ops.VarVarAssignOp;
import _st3_linearize_expr.ir.FlatCodeItem;
import _st3_linearize_expr.ir.VarCreator;
import _st3_linearize_expr.items.AssignVarAllocObject;
import _st3_linearize_expr.items.AssignVarBinop;
import _st3_linearize_expr.items.AssignVarFalse;
import _st3_linearize_expr.items.AssignVarFieldAccess;
import _st3_linearize_expr.items.AssignVarFlatCallClassCreationTmp;
import _st3_linearize_expr.items.AssignVarFlatCallResult;
import _st3_linearize_expr.items.AssignVarFlatCallStringCreationTmp;
import _st3_linearize_expr.items.AssignVarNull;
import _st3_linearize_expr.items.AssignVarNum;
import _st3_linearize_expr.items.AssignVarTernaryOp;
import _st3_linearize_expr.items.AssignVarTrue;
import _st3_linearize_expr.items.AssignVarUnop;
import _st3_linearize_expr.items.AssignVarVar;
import _st3_linearize_expr.items.FlatCallConstructor;
import _st3_linearize_expr.items.FlatCallVoid;
import _st3_linearize_expr.items.StoreFieldVar;
import _st3_linearize_expr.items.StoreVarVar;
import _st3_linearize_expr.items.StoreVarVarAssignOp;
import _st3_linearize_expr.leaves.Binop;
import _st3_linearize_expr.leaves.FieldAccess;
import _st3_linearize_expr.leaves.PureFunctionCallWithResult;
import _st3_linearize_expr.leaves.Ternary;
import _st3_linearize_expr.leaves.Unop;
import _st3_linearize_expr.leaves.Var;
import ast_class.ClassDeclaration;
import ast_expr.ExprAssign;
import ast_expr.ExprBinary;
import ast_expr.ExprBuiltinFn;
import ast_expr.ExprClassCreation;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_expr.ExprTernaryOperator;
import ast_expr.ExprUnary;
import ast_expr.ExpressionBase;
import ast_method.ClassMethodDeclaration;
import ast_symtab.BuiltinNames;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeBindings;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import errors.ErrorLocation;
import literals.IntLiteral;
import tokenize.Token;
import utils_oth.NullChecker;

public class RewriterExpr {

  private final List<FlatCodeItem> temproraries;
  private final List<FlatCodeItem> rawResult;
  private final List<FlatCodeItem> rv;
  private final ClassMethodDeclaration method;

  public List<FlatCodeItem> getRv() {
    return rv;
  }

  public RewriterExpr(ExprExpression expr, ClassMethodDeclaration method) {
    NullChecker.check(expr, method);

    if (expr.getResultType() == null) {
      ErrorLocation.errorExpression("the result-type of the expression is undefined: ", expr);
    }

    this.temproraries = new ArrayList<>();
    this.rawResult = new ArrayList<>();
    this.rv = new ArrayList<>();
    this.method = method;

    gen(expr);
    rewriteRaw();
  }

  public RewriterExpr(VarDeclarator var, ClassMethodDeclaration method) {
    NullChecker.check(var, method);

    this.temproraries = new ArrayList<>();
    this.rawResult = new ArrayList<>();
    this.rv = new ArrayList<>();
    this.method = method;

    if (var.getSimpleInitializer() == null) {
      throw new AstParseException(
          var.getLocationToString() + ":error: variable without an initializer: " + var.toString());
    }

    if (var.getSimpleInitializer().getResultType() == null) {
      ErrorLocation.errorExpression("the result-type of the expression is undefined: ", var.getSimpleInitializer());
    }

    gen(var.getSimpleInitializer());
    rewriteRaw();

    Var lvaluevar = VarCreator.copyVarDecl(var);
    Var rvaluevar = getLast().getDest();

    if (var.getType().isClass() || var.getType().isString()) {
      genOpAssign(lvaluevar, rvaluevar);
    }

    else {
      AssignVarVar assignVarVar = new AssignVarVar(lvaluevar, rvaluevar);
      rv.add(new FlatCodeItem(assignVarVar));
    }
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
      } else if (item.isAssignVarBinop()) {
        rv.add(item);
      } else if (item.isAssignVarFalse()) {
        rv.add(item);
      } else if (item.isAssignVarFieldAccess()) {
        rv.add(item);
      }

      else if (item.isAssignVarFlatCallStringCreationTmp()) {
        rewriteStringCreation(item.getAssignVarFlatCallStringCreationTmp());
      }

      else if (item.isAssignVarFlatCallClassCreationTmp()) {

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

        FlatCallConstructor flatCallConstructor = new FlatCallConstructor(rvalue.getFullname(), args, lvalueVar);
        rv.add(new FlatCodeItem(flatCallConstructor));

      }

      else if (item.isAssignVarFlatCallResult()) {
        rv.add(item);
      } else if (item.isAssignVarNull()) {
        rv.add(item);
      } else if (item.isAssignVarNum()) {
        rv.add(item);
      } else if (item.isAssignVarTrue()) {
        rv.add(item);
      } else if (item.isAssignVarUnop()) {
        rv.add(item);
      }

      else if (item.isAssignVarVar()) {
        AssignVarVar node = item.getAssignVarVar();
        final Var lvalueVar = node.getLvalue();

        if ((lvalueVar.getType().isClass() || lvalueVar.getType().isString()) && !ignoreThisMethod()) {
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
        if ((lvalueVar.getType().isClass() || lvalueVar.getType().isString()) && !ignoreThisMethod()) {

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

      else if (item.isAssignVarTernaryOp()) {
        rv.add(item);
      }

      else {
        throw new AstParseException("unknown item: " + item.toString());
      }

    }

  }

  private void rewriteStringCreation(final AssignVarFlatCallStringCreationTmp node) {

    /// string s = "a.b.c";
    /// ::
    /// 1) string s = new string();
    /// 2) s.appendInternal('a');
    /// n) --//--

    final Var lvalueVar = node.getLvalue();
    final String sconst = node.getRvalue();

    final AssignVarAllocObject assignVarAllocObject = new AssignVarAllocObject(lvalueVar, lvalueVar.getType());
    rv.add(new FlatCodeItem(assignVarAllocObject));

    Var fromMap = BuiltinsFnSet.getVar(sconst);

    final List<Var> argsInstance = new ArrayList<>();
    argsInstance.add(0, lvalueVar);
    argsInstance.add(fromMap);

    final FlatCallConstructor flatCallConstructor = new FlatCallConstructor("string_init", argsInstance, lvalueVar);
    rv.add(new FlatCodeItem(flatCallConstructor));

  }

  private void genOpAssign(Var lvalueVar, Var rvalueVar) {

    /// we cannot generate opAssign call inside the method itself
    /// it will cause a recursive infinite loop.
    if (ignoreThisMethod()) {
      throw new AstParseException("unexpected opAssign method");
    }

    if (lvalueVar.getType().isClass()) {
      AssignVarNull assignVarNull = new AssignVarNull(lvalueVar);
      rv.add(new FlatCodeItem(assignVarNull));

      VarVarAssignOp aux = new VarVarAssignOp(lvalueVar.getType(), lvalueVar, rvalueVar);
      StoreVarVarAssignOp store = new StoreVarVarAssignOp(lvalueVar, aux);
      rv.add(new FlatCodeItem(store));

    }

    else if (lvalueVar.getType().isString()) {

      AssignVarNull assignVarNull = new AssignVarNull(lvalueVar);
      rv.add(new FlatCodeItem(assignVarNull));

      VarVarAssignOp aux = new VarVarAssignOp(lvalueVar.getType(), lvalueVar, rvalueVar);
      StoreVarVarAssignOp store = new StoreVarVarAssignOp(lvalueVar, aux);
      rv.add(new FlatCodeItem(store));

    }
  }

  private boolean ignoreThisMethod() {
    return method.getIdentifier().equals(BuiltinNames.opAssign_ident) || method.isDestructor();
  }

  private void genRaw(FlatCodeItem item) {
    temproraries.add(0, item);
    rawResult.add(item);
  }

  private FlatCodeItem popCode() {
    return temproraries.remove(0);
  }

  private List<Var> genArgs(final List<ExprExpression> arguments) {

    for (ExprExpression arg : arguments) {
      gen(arg);
    }

    List<Var> args = new ArrayList<>();
    for (int i = 0; i < arguments.size(); i++) {
      final FlatCodeItem item = popCode();
      final Var var = item.getDest();
      args.add(0, var);
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

      FlatCodeItem item = new FlatCodeItem(new AssignVarBinop(VarCreator.justNewVar(e.getResultType()), binop));
      genRaw(item);

    }

    else if (base == EUNARY) {
      final ExprUnary unary = e.getUnary();
      final Token op = unary.getOperator();
      gen(unary.getOperand());

      final FlatCodeItem Litem = popCode();

      final Var lvarRes = Litem.getDest();
      final Unop unop = new Unop(op.getValue(), lvarRes);

      FlatCodeItem item = new FlatCodeItem(new AssignVarUnop(VarCreator.justNewVar(e.getResultType()), unop));
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

      final Var lvalueTmp = VarCreator.justNewVar(var.getType());
      final Var rvalueTmp = VarCreator.copyVarDecl(var);
      final FlatCodeItem item = new FlatCodeItem(new AssignVarVar(lvalueTmp, rvalueTmp));
      genRaw(item);
    }

    else if (base == ExpressionBase.EPRIMARY_STRING) {

      /// string s = "a.b.c";
      /// ::
      /// 1) string s = new string();
      /// 2) s.appendInternal('a');
      /// n) --//--

      final String sconst = e.getBeginPos().getValue();
      final Var lvalue = VarCreator.justNewVar(e.getResultType());

      final AssignVarFlatCallStringCreationTmp res = new AssignVarFlatCallStringCreationTmp(lvalue, sconst);
      final FlatCodeItem item = new FlatCodeItem(res);
      genRaw(item);

      BuiltinsFnSet.register(sconst, VarCreator.justNewVar(e.getResultType()));
    }

    else if (base == EPRIMARY_NUMBER) {
      final IntLiteral number = e.getNumber();
      final Var lhsVar = VarCreator.justNewVar(number.getType());
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

      if (method.isVoid()) {
        final FlatCallVoid call = new FlatCallVoid(method.signToStringCall(), args);
        final FlatCodeItem item = new FlatCodeItem(call);
        genRaw(item);
      }

      else {
        final PureFunctionCallWithResult call = new PureFunctionCallWithResult(method.signToStringCall(),
            method.getType(), args);
        final Var resultVar = VarCreator.justNewVar(method.getType());
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

      final FieldAccess access = new FieldAccess(obj, VarCreator.copyVarDecl(field));
      final Var lhsvar = VarCreator.justNewVar(field.getType());

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
      final PureFunctionCallWithResult call = new PureFunctionCallWithResult(constructor.signToStringCall(),
          constructor.getType(), args);
      final Var lvalue = VarCreator.justNewVar(typename);
      final AssignVarFlatCallClassCreationTmp assignVarFlatCallResult = new AssignVarFlatCallClassCreationTmp(lvalue,
          call);
      final FlatCodeItem item = new FlatCodeItem(assignVarFlatCallResult);
      genRaw(item);

    }

    else if (base == ETHIS) {

      final ClassDeclaration clazz = e.getSelfExpression();
      final Type classType = new Type(new ClassTypeRef(clazz, clazz.getTypeParametersT()));

      // main_class __t2 = _this_
      final Var lhsVar = VarCreator.justNewVar(classType);
      final Var rhsVar = VarCreator.just_this_(classType);
      final FlatCodeItem item = new FlatCodeItem(new AssignVarVar(lhsVar, rhsVar));
      genRaw(item);
    }

    else if (base == ExpressionBase.EBOOLEAN_LITERAL) {
      Boolean result = e.getBooleanLiteral();
      if (result) {
        AssignVarTrue node = new AssignVarTrue(VarCreator.justNewVar(e.getResultType()));
        FlatCodeItem item = new FlatCodeItem(node);
        genRaw(item);
      } else {
        AssignVarFalse node = new AssignVarFalse(VarCreator.justNewVar(e.getResultType()));
        FlatCodeItem item = new FlatCodeItem(node);
        genRaw(item);
      }
    }

    else if (base == ExpressionBase.ETERNARY_OPERATOR) {
      ExprTernaryOperator ternaryOperator = e.getTernaryOperator();
      gen(ternaryOperator.getCondition());
      gen(ternaryOperator.getTrueResult());
      gen(ternaryOperator.getFalseResult());

      final FlatCodeItem Fitem = popCode();
      final FlatCodeItem Titem = popCode();
      final FlatCodeItem Citem = popCode();

      final Var condition = Citem.getDest();
      final Var trueResult = Titem.getDest();
      final Var falseResult = Fitem.getDest();

      Ternary ternary = new Ternary(condition, trueResult, falseResult);
      AssignVarTernaryOp assignVarTernaryOp = new AssignVarTernaryOp(VarCreator.justNewVar(e.getResultType()), ternary);
      FlatCodeItem item = new FlatCodeItem(assignVarTernaryOp);
      genRaw(item);

    }

    else if (base == ExpressionBase.EBUILTIN_FN) {

      final ExprBuiltinFn fn = e.getBuiltinFn();
      final Type ret = fn.getReturnType();
      final List<Var> args = genArgs(fn.getCallArguments());

      /// variadic length args we will handle here
      /// that way.
      final StringBuilder fullname = new StringBuilder();
      fullname.append("std_");
      fullname.append(fn.getFunction().getName());
      fullname.append("_");
      for (int i = 0; i < args.size(); i += 1) {
        Var arg = args.get(i);
        fullname.append(arg.getType().toString());
        if (i + 1 < args.size()) {
          fullname.append("_");
        }
      }

      if (ret.isVoid()) {
        FlatCallVoid fc = new FlatCallVoid(fullname.toString(), args);
        FlatCodeItem item = new FlatCodeItem(fc);
        genRaw(item);
        BuiltinsFnSet.register(item);
      }

      else {

        final PureFunctionCallWithResult call = new PureFunctionCallWithResult(fullname.toString(), ret, args);
        final Var lvalue = VarCreator.justNewVar(ret);
        final AssignVarFlatCallResult ops = new AssignVarFlatCallResult(lvalue, call);
        final FlatCodeItem item = new FlatCodeItem(ops);
        genRaw(item);
        BuiltinsFnSet.register(item);
      }
    }

    else if (base == ExpressionBase.EPRIMARY_CHAR) {
      Token holder = e.getBeginPos();
      String value = holder.getValue();
      int[] esc = CEscaper.escape(value);

      if (esc.length != 2) {
        ErrorLocation.errorExpression("char constant incorrect: " + value, e);
      }

      IntLiteral number = new IntLiteral(value, TypeBindings.make_char(), (long) esc[0]);

      final Var lhsVar = VarCreator.justNewVar(number.getType());
      AssignVarNum assignVarNum = new AssignVarNum(lhsVar, number);
      genRaw(new FlatCodeItem(assignVarNum));
    }

    else {
      throw new RuntimeException(base.toString() + ": unimplemented");
    }

  }

}
