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
import _st2_annotate.TypeTraitsUtil;
import _st3_linearize_expr.ir.FlatCodeItem;
import _st3_linearize_expr.ir.VarCreator;
import _st3_linearize_expr.items.AssignVarAllocObject;
import _st3_linearize_expr.items.AssignVarBinop;
import _st3_linearize_expr.items.AssignVarBool;
import _st3_linearize_expr.items.AssignVarCastExpression;
import _st3_linearize_expr.items.AssignVarDefaultValueForType;
import _st3_linearize_expr.items.AssignVarFieldAccess;
import _st3_linearize_expr.items.AssignVarFieldAccessStatic;
import _st3_linearize_expr.items.AssignVarFlatCallClassCreationTmp;
import _st3_linearize_expr.items.AssignVarFlatCallResult;
import _st3_linearize_expr.items.AssignVarFlatCallResultStatic;
import _st3_linearize_expr.items.AssignVarFlatCallStringCreationTmp;
import _st3_linearize_expr.items.AssignVarNum;
import _st3_linearize_expr.items.AssignVarSizeof;
import _st3_linearize_expr.items.AssignVarUnop;
import _st3_linearize_expr.items.AssignVarVar;
import _st3_linearize_expr.items.BuiltinFuncAssertTrue;
import _st3_linearize_expr.items.BuiltinFuncDropPtr;
import _st3_linearize_expr.items.FlatCallConstructor;
import _st3_linearize_expr.items.FlatCallVoid;
import _st3_linearize_expr.items.FlatCallVoidStatic;
import _st3_linearize_expr.items.SelectionShortCircuit;
import _st3_linearize_expr.items.StoreFieldVar;
import _st3_linearize_expr.items.StoreVarVar;
import _st3_linearize_expr.leaves.Binop;
import _st3_linearize_expr.leaves.FieldAccess;
import _st3_linearize_expr.leaves.FunctionCallWithResult;
import _st3_linearize_expr.leaves.FunctionCallWithResultStatic;
import _st3_linearize_expr.leaves.Unop;
import _st3_linearize_expr.leaves.Var;
import _st7_codeout.ToStringsInternal;
import ast_class.ClassDeclaration;
import ast_expr.ExprAssign;
import ast_expr.ExprBinary;
import ast_expr.ExprBuiltinFunc;
import ast_expr.ExprCast;
import ast_expr.ExprClassCreation;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprForLoopStepComma;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_expr.ExprSizeof;
import ast_expr.ExprStaticAccess;
import ast_expr.ExprTernaryOperator;
import ast_expr.ExprTypeof;
import ast_expr.ExprUnary;
import ast_expr.ExpressionBase;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_sourceloc.SourceLocation;
import ast_symtab.Keywords;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeBindings;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import errors.ErrorLocation;
import literals.IntLiteral;
import tokenize.Ident;
import tokenize.Token;
import utils_oth.Normalizer;
import utils_oth.NullChecker;

public class RewriterExpr {

  private final List<FlatCodeItem> temproraries;
  private final List<FlatCodeItem> rawResult;
  private final List<FlatCodeItem> rv;
  private final ClassMethodDeclaration method;
  private final SourceLocation location;

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
    this.method = method;
    this.location = expr.getLocation();

    gen(expr);
    this.rv = new RewriteRaw(expr, rawResult, method, location).getRv();
  }

  public RewriterExpr(VarDeclarator var, ClassMethodDeclaration method) {
    NullChecker.check(var, method);

    this.temproraries = new ArrayList<>();
    this.rawResult = new ArrayList<>();
    this.method = method;
    this.location = var.getLocation();

    if (var.getSimpleInitializer() == null) {
      throw new AstParseException(
          var.getLocationToString() + ":error: variable without an initializer: " + var.toString());
    }

    if (var.getSimpleInitializer().getResultType() == null) {
      ErrorLocation.errorExpression("the result-type of the expression is undefined: ", var.getSimpleInitializer());
    }

    gen(var.getSimpleInitializer());
    this.rv = new RewriteRaw(var.getSimpleInitializer(), rawResult, method, location).getRv();

    Var lvaluevar = VarCreator.copyVarDecl(var);
    Var rvaluevar = getLast().getDest();

    //xxxxx
    if (lvaluevar.getType().isInterface()) {
      final AssignVarAllocObject assignVarAllocObject = new AssignVarAllocObject(lvaluevar, lvaluevar.getType());
      final ClassDeclaration interfaceClazz = lvaluevar.getType().getClassTypeFromRef();
      final ClassDeclaration realClazz = rvaluevar.getType().getClassTypeFromRef();

      rv.add(new FlatCodeItem(assignVarAllocObject));

      String funcname = interfaceClazz.getIdentifier() + "_init_for_" + realClazz.getIdentifier();
      List<Var> args = new ArrayList<>();
      args.add(lvaluevar);
      args.add(rvaluevar);
      FlatCallConstructor flatCallConstructor = new FlatCallConstructor(funcname, args, lvaluevar);
      rv.add(new FlatCodeItem(flatCallConstructor));

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

      LvalueUtil.checkHard(lvalue, e, method);
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

    else if (base == ExpressionBase.EFOR_LOOP_STEP_COMMA) {

      final ExprForLoopStepComma commaE = e.getExprForLoopStepComma();
      gen(commaE.getLhs());
      gen(commaE.getRhs());

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

      if (exprId.isVar()) {
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

      /// int f = constants.os_version;
      /// constants.write_settings(f);
      ///
      if (exprId.isStaticClass()) {
        final Var lvalueTmp = VarCreator.justNewVar(e.getResultType());

        final ClassDeclaration staticClass = exprId.getStaticClass();
        final String name = staticClass.getIdentifier().getName();
        final Var rvalueTmp = BuiltinsFnSet.getNameFromStatics(name, e.getResultType());
        final FlatCodeItem item = new FlatCodeItem(new AssignVarVar(lvalueTmp, rvalueTmp));
        genRaw(item);
      }
    }

    else if (base == ExpressionBase.EPRIMARY_STRING) {

      final String sconst = e.getBeginPos().getValue();
      Var lvalue = BuiltinsFnSet.getVar(sconst);
      if (lvalue == null) {
        lvalue = VarCreator.justNewVar(e.getResultType());
      }

      List<Var> args = new ArrayList<>();
      args.add(lvalue);

      // TODO:
      final ClassMethodDeclaration constructor = lvalue.getType().getClassTypeFromRef().getConstructors().get(0);
      final FunctionCallWithResult callWithResult = new FunctionCallWithResult(constructor,
          ToStringsInternal.signToStringCall(constructor), lvalue.getType(), args);

      final AssignVarFlatCallStringCreationTmp res = new AssignVarFlatCallStringCreationTmp(lvalue, sconst,
          callWithResult);
      final FlatCodeItem item = new FlatCodeItem(res);
      genRaw(item);

      /// label
      BuiltinsFnSet.registerStringLabel(sconst, lvalue);

    }

    else if (base == EPRIMARY_NUMBER) {
      final IntLiteral number = e.getNumber();
      final Var lhsVar = VarCreator.justNewVar(number.getType());
      AssignVarNum assignVarNum = new AssignVarNum(lhsVar, number);
      genRaw(new FlatCodeItem(assignVarNum));
    }

    else if (base == ECAST) {
      ExprCast cast = e.getCastExpression();
      ExprExpression whatWeNeedCast = cast.getExpressionForCast();
      Type theTypeOfTheResult = cast.getToType();

      gen(whatWeNeedCast);

      final FlatCodeItem Litem = popCode();
      final Var rvalue = Litem.getDest();
      final Var lvalue = VarCreator.justNewVar(theTypeOfTheResult);

      genRaw(new FlatCodeItem(new AssignVarCastExpression(lvalue, rvalue, theTypeOfTheResult)));
    }

    else if (base == EMETHOD_INVOCATION) {
      final ExprMethodInvocation fcall = e.getMethodInvocation();
      final ClassMethodDeclaration method = fcall.getMethod();
      final ClassDeclaration clazz = method.getClazz();

      //1
      gen(fcall.getObject());
      final FlatCodeItem obj = popCode();

      //2
      final List<Var> args = genArgs(fcall.getArguments());
      args.add(0, obj.getDest());

      ///TODO:static_semantic
      if (clazz.isNamespace()) {

        args.remove(0); // __this

        if (method.isVoid()) {

          final FlatCallVoidStatic call = new FlatCallVoidStatic(method, ToStringsInternal.signToStringCall(method),
              args);
          final FlatCodeItem item = new FlatCodeItem(call);
          genRaw(item);
        }

        else {
          final FunctionCallWithResultStatic call = new FunctionCallWithResultStatic(method,
              ToStringsInternal.signToStringCall(method), method.getType(), args);
          final Var resultVar = VarCreator.justNewVar(method.getType());
          final FlatCodeItem item = new FlatCodeItem(new AssignVarFlatCallResultStatic(resultVar, call));
          genRaw(item);
        }
      }

      /// method calls from object which is allocated
      else {

        if (method.isVoid()) {
          final FlatCallVoid call = new FlatCallVoid(method, ToStringsInternal.signToStringCall(method), args);
          final FlatCodeItem item = new FlatCodeItem(call);
          genRaw(item);
        }

        else {
          final FunctionCallWithResult call = new FunctionCallWithResult(method,
              ToStringsInternal.signToStringCall(method), method.getType(), args);
          final Var resultVar = VarCreator.justNewVar(method.getType());
          final FlatCodeItem item = new FlatCodeItem(new AssignVarFlatCallResult(resultVar, call));
          genRaw(item);
        }

      }

    }

    else if (base == EFIELD_ACCESS) {
      final ExprFieldAccess fieldAccess = e.getFieldAccess();
      gen(fieldAccess.getObject());

      final VarDeclarator field = fieldAccess.getField();
      final ClassDeclaration clazz = field.getClazz();

      ///TODO:static_semantic

      if (clazz.isNamespace()) {

        final FlatCodeItem thisItem = popCode();
        final Var obj = thisItem.getDest();

        /// int f = constants.os_version;
        /// ::
        /// int t8 = constants->os_version;
        /// int f = t8;
        ///
        final Var staticClassName = new Var(VarBase.STATIC_VAR, new Modifiers(), field.getType(),
            clazz.getIdentifier());
        final FieldAccess access = new FieldAccess(staticClassName, VarCreator.copyVarDecl(field));
        final Var lhsvar = VarCreator.justNewVar(field.getType());
        final FlatCodeItem item = new FlatCodeItem(new AssignVarFieldAccessStatic(lhsvar, access));

        genRaw(item);
      }

      else if (clazz.isEnum()) {
        final Var staticClassName = new Var(VarBase.STATIC_VAR, new Modifiers(), field.getType(),
            clazz.getIdentifier());
        final FieldAccess access = new FieldAccess(staticClassName, VarCreator.copyVarDecl(field));
        final Var lhsvar = VarCreator.justNewVar(field.getType());
        final FlatCodeItem item = new FlatCodeItem(new AssignVarFieldAccessStatic(lhsvar, access));

        genRaw(item);
      }

      else {

        final FlatCodeItem thisItem = popCode();
        final Var obj = thisItem.getDest();

        final FieldAccess access = new FieldAccess(obj, VarCreator.copyVarDecl(field));
        final Var lhsvar = VarCreator.justNewVar(field.getType());

        final FlatCodeItem item = new FlatCodeItem(new AssignVarFieldAccess(lhsvar, access));
        genRaw(item);
      }

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
      ClassMethodDeclaration constructor = fcall.getConstructor();
      if (constructor == null) {
        if (typename.getClassTypeFromRef().isNativeString()) {
          constructor = typename.getClassTypeFromRef().getConstructors().get(0);
        }
      }
      NullChecker.check(constructor);

      final FunctionCallWithResult call = new FunctionCallWithResult(constructor,
          ToStringsInternal.signToStringCall(constructor), constructor.getType(), args);
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
        AssignVarBool node = new AssignVarBool(VarCreator.justNewVar(e.getResultType()), "true");
        FlatCodeItem item = new FlatCodeItem(node);
        genRaw(item);
      } else {
        AssignVarBool node = new AssignVarBool(VarCreator.justNewVar(e.getResultType()), "false");
        FlatCodeItem item = new FlatCodeItem(node);
        genRaw(item);
      }
    }

    else if (base == ExpressionBase.ETERNARY_OPERATOR) {

      ExprTernaryOperator ternaryOperator = e.getTernaryOperator();

      List<FlatCodeItem> condblock = new ArrayList<>();
      List<FlatCodeItem> trueblock = new ArrayList<>();
      List<FlatCodeItem> elseblock = new ArrayList<>();

      gen(ternaryOperator.getCondition());
      while (!rawResult.isEmpty()) {
        condblock.add(rawResult.remove(0));
      }

      gen(ternaryOperator.getTrueResult());
      while (!rawResult.isEmpty()) {
        trueblock.add(rawResult.remove(0));
      }

      gen(ternaryOperator.getFalseResult());
      while (!rawResult.isEmpty()) {
        elseblock.add(rawResult.remove(0));
      }

      /// extract the variables
      final FlatCodeItem Fitem = popCode();
      final FlatCodeItem Titem = popCode();
      final FlatCodeItem Citem = popCode();

      final Var condVar = Citem.getDest();
      final Var trueVar = Titem.getDest();
      final Var elseVar = Fitem.getDest();

      // result = condition ? trueres : falseres
      // and here we must implement a short circuit
      final Var resultVar = VarCreator.justNewVar(e.getResultType());
      trueblock.add(new FlatCodeItem(new StoreVarVar(resultVar, trueblock.get(trueblock.size() - 1).getDest())));
      elseblock.add(new FlatCodeItem(new StoreVarVar(resultVar, elseblock.get(elseblock.size() - 1).getDest())));

      /// condBlock;
      /// type result = 0;
      /// if(condVar) {
      ///    trueBlockItems;
      ///    result = trueBlockItems.last();
      /// } else {
      ///    elseBlockItems;
      ///    result = elseBlockItems.last();
      /// }

      /// int main_class_main_8()
      /// {
      ///     int t13 = 1;
      ///     int t14 = 0;
      ///     boolean t15 = (t13 > t14);
      /// 
      ///     int t20;
      ///     if (t15) {
      ///         int t16 = 0;
      ///         t20 = t16;
      ///     } else {
      ///         int t17 = 1;
      ///         int t18 = 0;
      ///         int t19 = (t17 / t18);
      ///         t20 = t19;
      ///     }
      ///     int x = t20;
      /// 
      ///     return 0;
      /// }

      SelectionShortCircuit sel = new SelectionShortCircuit(resultVar, condVar, trueVar, elseVar, condblock, trueblock,
          elseblock);
      FlatCodeItem item = new FlatCodeItem(sel);
      genRaw(item);

    }

    else if (base == ExpressionBase.EPRIMARY_CHAR) {
      Token holder = e.getBeginPos();
      String value = holder.getValue();
      int[] esc = CEscaper.escape(value);

      if (esc.length != 2) {
        ErrorLocation.errorExpression("char constant incorrect: " + value, e);
      }

      //public IntLiteralInfo(String originalInput, char mainSign, String dec, String mnt, String exp, String suf, char exponentSign)
      IntLiteral number = new IntLiteral(value, '+', String.format("%d", (long) esc[0]), "", "", "", '+');
      number.setType(TypeBindings.make_char());
      number.setLong((long) esc[0]);

      final Var lhsVar = VarCreator.justNewVar(number.getType());
      AssignVarNum assignVarNum = new AssignVarNum(lhsVar, number);
      genRaw(new FlatCodeItem(assignVarNum));
    }

    else if (base == ExpressionBase.ESIZEOF) {
      ExprSizeof node = e.getExprSizeof();

      final Var lhsVar = VarCreator.justNewVar(e.getResultType());
      AssignVarSizeof assignVarSizeof = new AssignVarSizeof(lhsVar, node.getType());
      genRaw(new FlatCodeItem(assignVarSizeof));

    }

    else if (base == ExpressionBase.ETYPEOF) {
      final ExprTypeof exprTypeof = e.getExprTypeof();
      final Type resultType = exprTypeof.getExpr().getResultType();
      final Type expectedType = exprTypeof.getType();
      if (resultType.isEqualTo(expectedType)) {
        AssignVarBool node = new AssignVarBool(VarCreator.justNewVar(e.getResultType()), "true");
        FlatCodeItem item = new FlatCodeItem(node);
        genRaw(item);
      } else {
        AssignVarBool node = new AssignVarBool(VarCreator.justNewVar(e.getResultType()), "false");
        FlatCodeItem item = new FlatCodeItem(node);
        genRaw(item);
      }
    }

    else if (base == ExpressionBase.EBUILTIN_FUNC) {
      ExprBuiltinFunc node = e.getExprBuiltinFunc();
      Ident name = node.getName();

      final List<Var> args = genArgs(node.getArgs());

      if (name.equals(Keywords.assert_true_ident)) {

        genAssertTrueBuiltinFunc(node, args);

      }

      else if (TypeTraitsUtil.isBuiltinTypeTraitsIdent(name)) {
        final Type tp = args.get(0).getType();
        final int res = TypeBindings.getResultForTypeTraits(name, tp);
        if (res == -1) {
          ErrorLocation.errorExpression("not a type-traits expression ", e);
        }

        final boolean result = (res == 0) ? false : true;
        if (result) {
          AssignVarBool trueNode = new AssignVarBool(VarCreator.justNewVar(e.getResultType()), "true");
          FlatCodeItem item = new FlatCodeItem(trueNode);
          genRaw(item);
        } else {
          AssignVarBool falseNode = new AssignVarBool(VarCreator.justNewVar(e.getResultType()), "false");
          FlatCodeItem item = new FlatCodeItem(falseNode);
          genRaw(item);
        }
      }

      else if (name.equals(Keywords.static_assert_ident)) {

      }

      else if (name.equals(Keywords.types_are_same_ident)) {
        final boolean result = args.get(0).getType().isEqualTo(args.get(1).getType());
        if (result) {
          AssignVarBool trueNode = new AssignVarBool(VarCreator.justNewVar(e.getResultType()), "true");
          FlatCodeItem item = new FlatCodeItem(trueNode);
          genRaw(item);
        } else {
          AssignVarBool falseNode = new AssignVarBool(VarCreator.justNewVar(e.getResultType()), "false");
          FlatCodeItem item = new FlatCodeItem(falseNode);
          genRaw(item);
        }
      }

      else if (name.equals(Keywords.is_alive_ident) || name.equals(Keywords.set_deletion_bit_ident)) {
        Var tmp = VarCreator.justNewVar(e.getResultType());

        FunctionCallWithResult fcallres = new FunctionCallWithResult(method, name.getName(),
            TypeBindings.make_boolean(), args);
        AssignVarFlatCallResult assignVarFlatCallResult = new AssignVarFlatCallResult(tmp, fcallres);
        genRaw(new FlatCodeItem(assignVarFlatCallResult));
      }

      else {
        ErrorLocation.errorExpression("unimpl.builtin function:", e);
      }
    }

    //TODO:NULLS
    else if (e.is(ExpressionBase.EDEFAULT_VALUE_FOR_TYPE)) {
      final Type tp = e.getResultType();

      //struct dummy* t380 = &dummy_default_empty_var;
      //struct dummy* d = t380;

      final Var lhsVar = VarCreator.justNewVar(tp);
      final Var rhsVar = VarCreator.varWithName(tp, ToStringsInternal.defaultVarNameForType(tp));
      AssignVarDefaultValueForType node = new AssignVarDefaultValueForType(lhsVar, rhsVar);
      FlatCodeItem item = new FlatCodeItem(node);
      genRaw(item);
    }

    //TODO:STATIC_ACCESS
    else if (e.is(ExpressionBase.ESTATIC_ACCESS)) {
      ExprStaticAccess access = e.getExprStaticAccess();

      final Type classType = access.getType();

      final Var lhsVar = VarCreator.justNewVar(classType);
      final Var rhsVar = VarCreator.justNewVar(classType);
      final FlatCodeItem item = new FlatCodeItem(new AssignVarVar(lhsVar, rhsVar));

      // This item is temporary, it won't be presented in the output.
      // We need it to save the stack, and it will be handled later.
      // Whether is a method invocation or a field access.
      // I am not sure how to make it properly now.
      genRaw(item);
    }

    //TODO: this one should be a special ITEM-node...
    //TODO:TODO:TODO:delete_expr
    else if (e.is(ExpressionBase.EDELETE)) {
      // delete(sym)
      //
      VarDeclarator var = e.getExprDelete().getVar();
      NullChecker.check(var);

      String name = var.getIdentifier().getName();
      if (var.is(VarBase.CLASS_FIELD)) {
        name = "__this->" + name;
      }

      Type vartype = var.getType();
      if (vartype.isClass()) {

        ClassDeclaration clazz = vartype.getClassTypeFromRef();
        List<Var> args = new ArrayList<>();
        final Var varToDelete = VarCreator.varWithName(vartype, name);
        args.add(varToDelete);
        final ClassMethodDeclaration destructor = clazz.getDestructor();
        FlatCallVoid flatCallVoid = new FlatCallVoid(destructor, ToStringsInternal.signToStringCall(destructor), args);
        genRaw(new FlatCodeItem(flatCallVoid));

        /// final Var rhsVar = VarCreator.varWithName(vartype, ToStringsInternal.defaultVarNameForType(vartype));
        /// final BuiltinFuncDropPtr builtinFuncDropPtr = new BuiltinFuncDropPtr(name, rhsVar.getName().getName());
        /// genRaw(new FlatCodeItem(builtinFuncDropPtr));
      }
    }

    else {
      throw new AstParseException(base.toString() + ": unimplemented");
    }

  }

  private void genAssertTrueBuiltinFunc(ExprBuiltinFunc node, final List<Var> args) {

    /// void assert_true(int cnd, const char *file, int line, const char *expr)

    String cond = args.get(0).getName().getName();
    String file = labelName(CEscaper.toCString(Normalizer.normalize(node.getFileToString())));
    String line = node.getLineToString();
    String expr = labelName(CEscaper.toCString(node.getExprToString()));

    BuiltinFuncAssertTrue builtinFuncAssertTrue = new BuiltinFuncAssertTrue(cond, file, line, expr);
    genRaw(new FlatCodeItem(builtinFuncAssertTrue));
  }

  private String labelName(String sconst) {
    String e = getStrlabel(sconst).getName().getName();
    return e;
  }

  private Var getStrlabel(String sconst) {
    Var lvalue = BuiltinsFnSet.getVar(sconst);
    if (lvalue == null) {
      lvalue = VarCreator.justNewVar(TypeBindings.make_char()); // that's wrong :)
    }
    BuiltinsFnSet.registerStringLabel(sconst, lvalue);
    return lvalue;
  }

}
