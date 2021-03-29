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
import _st3_linearize_expr.ir.FlatCodeItem;
import _st3_linearize_expr.ir.VarCreator;
import _st3_linearize_expr.items.AssignVarAllocObject;
import _st3_linearize_expr.items.AssignVarBinop;
import _st3_linearize_expr.items.AssignVarFalse;
import _st3_linearize_expr.items.AssignVarFieldAccess;
import _st3_linearize_expr.items.AssignVarFlatCallClassCreationTmp;
import _st3_linearize_expr.items.AssignVarFlatCallResult;
import _st3_linearize_expr.items.AssignVarFlatCallResultStatic;
import _st3_linearize_expr.items.AssignVarFlatCallStringCreationTmp;
import _st3_linearize_expr.items.AssignVarNum;
import _st3_linearize_expr.items.AssignVarSizeof;
import _st3_linearize_expr.items.AssignVarStaticFieldAccess;
import _st3_linearize_expr.items.AssignVarTernaryOp;
import _st3_linearize_expr.items.AssignVarTrue;
import _st3_linearize_expr.items.AssignVarUnop;
import _st3_linearize_expr.items.AssignVarVar;
import _st3_linearize_expr.items.CallListenerResultMethod;
import _st3_linearize_expr.items.CallListenerVoidMethod;
import _st3_linearize_expr.items.FlatCallConstructor;
import _st3_linearize_expr.items.FlatCallVoid;
import _st3_linearize_expr.items.FlatCallVoidStaticClassMethod;
import _st3_linearize_expr.items.IntrinsicText;
import _st3_linearize_expr.items.StoreFieldVar;
import _st3_linearize_expr.items.StoreVarVar;
import _st3_linearize_expr.leaves.Binop;
import _st3_linearize_expr.leaves.FieldAccess;
import _st3_linearize_expr.leaves.FunctionCallWithResult;
import _st3_linearize_expr.leaves.FunctionCallWithResultStatic;
import _st3_linearize_expr.leaves.Ternary;
import _st3_linearize_expr.leaves.Unop;
import _st3_linearize_expr.leaves.Var;
import ast_class.ClassDeclaration;
import ast_expr.ExprAssign;
import ast_expr.ExprBinary;
import ast_expr.ExprBuiltinFunc;
import ast_expr.ExprClassCreation;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_expr.ExprSizeof;
import ast_expr.ExprTernaryOperator;
import ast_expr.ExprTypeof;
import ast_expr.ExprUnary;
import ast_expr.ExpressionBase;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_sourceloc.SourceLocation;
import ast_symtab.BuiltinNames;
import ast_symtab.Keywords;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeBindings;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import errors.ErrorLocation;
import hashed.Hash_ident;
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
    this.rv = new ArrayList<>();
    this.method = method;
    this.location = expr.getLocation();

    gen(expr);
    rewriteRaw();
  }

  public RewriterExpr(VarDeclarator var, ClassMethodDeclaration method) {
    NullChecker.check(var, method);

    this.temproraries = new ArrayList<>();
    this.rawResult = new ArrayList<>();
    this.rv = new ArrayList<>();
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
    rewriteRaw();

    Var lvaluevar = VarCreator.copyVarDecl(var);
    Var rvaluevar = getLast().getDest();

    AssignVarVar assignVarVar = new AssignVarVar(lvaluevar, rvaluevar);
    rv.add(new FlatCodeItem(assignVarVar));
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
      }

      else if (item.isAssignVarBinop()) {

        final AssignVarBinop assignVarBinop = item.getAssignVarBinop();

        final Binop bop = assignVarBinop.getRvalue();
        final Var lhs = bop.getLhs();
        final Var rhs = bop.getRhs();
        final Type lhsType = lhs.getType();

        /// we may replace the '==' operator with 'equals()' call
        /// if and only if the types of variables are classes
        /// and the current method not the 'equals()' itself,
        /// because it may cause the infinite recursion loop, if
        /// inside the 'equals()' method will be another '=='
        /// which will may be replaced with the 'equals()' call and 
        /// so on and so on...
        /// I do not actually how c++ handle situations like that in
        /// operator overloading, the assign overloading in c++ may also
        /// be a cause of infinite recursion, 100%, and compiler 
        /// complains about that... will it be a clean solution to 
        /// just ignore the replacement if we inside the method we
        /// want to use as a replacement???
        ///
        /// and: it is much clean and precise to replace simple '==' with
        /// and method call if and only if the class provides the 'equals()'
        /// method, because: 
        /// generate something like that by default: [return __this == another]
        /// and call that method time after time it's not fine.
        final String op = assignVarBinop.getRvalue().getOp();
        if (itIsPossibleToReplaceEqNeWithCall(lhsType, op)) {

          final ClassMethodDeclaration meth = lhsType.getClassTypeFromRef()
              .getPredefinedMethod(BuiltinNames.equals_ident);
          final String sign = meth.signToStringCall();

          final List<Var> args = new ArrayList<>();
          args.add(lhs);
          args.add(rhs);

          final FunctionCallWithResult cmpMeth = new FunctionCallWithResult(meth, sign, meth.getType(), args);

          //rv.add(genAssert(lhs));
          //rv.add(genAssert(rhs));

          if (op.equals("==")) {
            /// boolean t33 = t31 == t32
            /// ::
            /// boolean t33 = equals(t31, t32)

            final AssignVarFlatCallResult cmpAsgn = new AssignVarFlatCallResult(assignVarBinop.getLvalue(), cmpMeth);
            rv.add(new FlatCodeItem(cmpAsgn));
          }

          if (op.equals("!=")) {
            /// boolean t50 = t48 != t49
            /// ::
            /// boolean tmp = equals(t48, t49)
            /// boolean t50 = !tmp

            final Var tmp = VarCreator.justNewVar(assignVarBinop.getLvalue().getType());
            final AssignVarFlatCallResult cmpAsgn = new AssignVarFlatCallResult(tmp, cmpMeth);
            rv.add(new FlatCodeItem(cmpAsgn));

            final Unop unop = new Unop("!", tmp);
            final AssignVarUnop notEq = new AssignVarUnop(assignVarBinop.getLvalue(), unop);
            rv.add(new FlatCodeItem(notEq));
          }

        }

        else {
          rv.add(item);
        }

      }

      else if (item.isAssignVarFalse()) {
        rv.add(item);
      } else if (item.isAssignVarFieldAccess()) {
        // a = b.c
        rv.add(genAssert(item.getAssignVarFieldAccess().getRvalue().getObject()));
        rv.add(item);
      }

      else if (item.isAssignVarFlatCallStringCreationTmp()) {
        //AssignVarFlatCallStringCreationTmp node = item.getAssignVarFlatCallStringCreationTmp();
        //String sconst = node.getRvalue();
        //final IntrinsicText intrinsicText = new IntrinsicText(node.getLvalue(),
        //    "const char * const " + node.getLvalue().getName().toString() + " = " + sconst);
        //rv.add(new FlatCodeItem(intrinsicText));
      }

      else if (item.isAssignVarFlatCallClassCreationTmp()) {

        // strtemp __t15 = strtemp_init_0(__t14)
        // ::
        // strtemp __t15 = get_memory(sizeof(struct string, TD_STRING))
        // strtemp_init_0(__t15, __t14)

        // 1
        final AssignVarFlatCallClassCreationTmp node = item.getAssignVarFlatCallClassCreationTmp();
        final Var lvalueVar = node.getLvalue();

        AssignVarAllocObject assignVarAllocObject = new AssignVarAllocObject(lvalueVar, lvalueVar.getType());
        rv.add(new FlatCodeItem(assignVarAllocObject));

        final FunctionCallWithResult rvalue = node.getRvalue();
        final List<Var> args = rvalue.getArgs();
        args.add(0, lvalueVar);

        //__pushf("test1::test5", __LINE__);
        //test5(0);
        //__popf();

        // 3
        FlatCallConstructor flatCallConstructor = new FlatCallConstructor(rvalue.getFullname(), args, lvalueVar);
        final String sign = rvalue.getMethod().signToStringCallPushF();
        
        rv.add(makePushFWithDest(flatCallConstructor.getThisVar(), sign));
        rv.add(new FlatCodeItem(flatCallConstructor));
        rv.add(makePopFWithDest(flatCallConstructor.getThisVar(), sign));

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
        AssignVarVar assign = item.getAssignVarVar();
        if (assign.getLvalue().getType().isClass()
            && assign.getLvalue().getType().getClassTypeFromRef().isStaticClass()) {
          continue;
        }

        rv.add(item);
      }

      else if (item.isFlatCallVoid()) {
        final String sign = item.getFlatCallVoid().getMethod().signToStringCallPushF();

        rv.add(makePushF(sign));
        rv.add(item);
        rv.add(makePopF(sign));
      }

      else if (item.isStoreFieldVar()) {
        // a.b = c
        rv.add(genAssert(item.getStoreFieldVar().getDst().getObject()));
        rv.add(item);
      }

      else if (item.isStoreVarVar()) {
        rv.add(item);
      }

      else if (item.isAssignVarTernaryOp()) {
        rv.add(item);
      }

      else if (item.isAssignVarSizeof()) {
        rv.add(item);
      }

      /// statics
      ///
      else if (item.isAssignVarStaticFieldAccess()) {
        rv.add(item);
      }

      else if (item.isFlatCallVoidStaticClassMethod()) {
        rv.add(item);
      }

      else if (item.isAssignVarFlatCallResultStatic()) {
        rv.add(item);
      }

      /// builtins
      ///

      else if (item.isIntrinsicText()) {
        if (item.getIntrinsicText().getText().startsWith("assert_true")) {
          rv.add(makePushF("assert_true"));
        }

        rv.add(item);

        if (item.getIntrinsicText().getText().startsWith("assert_true")) {
          rv.add(makePopF("assert_true"));
        }
      }

      else {
        throw new AstParseException("unknown item: " + item.toString());
      }

    }

  }

  private Ident beforeCallIdent() {
    return Hash_ident.getHashedIdent("__before_call");
  }

  private Ident afterCallIdent() {
    return Hash_ident.getHashedIdent("__after_call");
  }

  private FlatCodeItem makePushF(String methodPureName) {
    final String sign = method.signToStringCallPushF();
    final String namef = sign + "::" + methodPureName;
    CallListenerVoidMethod txt = new CallListenerVoidMethod(beforeCallIdent(), namef, location.getLine());
    return new FlatCodeItem(txt);
  }

  private FlatCodeItem makePopF(String methodPureName) {
    final String sign = method.signToStringCallPushF();
    final String namef = sign + "::" + methodPureName;
    CallListenerVoidMethod txt = new CallListenerVoidMethod(afterCallIdent(), namef, location.getLine());
    return new FlatCodeItem(txt);
  }

  private FlatCodeItem makePushFWithDest(Var dest, String methodPureName) {
    final String sign = method.signToStringCallPushF();
    final String namef = sign + "::" + methodPureName;
    CallListenerResultMethod txt = new CallListenerResultMethod(dest, beforeCallIdent(), namef, location.getLine());
    return new FlatCodeItem(txt);
  }

  private FlatCodeItem makePopFWithDest(Var dest, String methodPureName) {
    final String sign = method.signToStringCallPushF();
    final String namef = sign + "::" + methodPureName;
    CallListenerResultMethod txt = new CallListenerResultMethod(dest, afterCallIdent(), namef, location.getLine());
    return new FlatCodeItem(txt);
  }

  private boolean itIsPossibleToReplaceEqNeWithCall(final Type lhsType, final String op) {
    final boolean itIsEqOp = (op.equals("==") || op.equals("!="));

    if (!itIsEqOp) {
      return false;
    }
    if (method.getIdentifier().equals(BuiltinNames.equals_ident)) {
      return false;
    }
    if (!lhsType.isClass()) {
      return false;
    }

    //final ClassDeclaration clazz = lhsType.getClassTypeFromRef();
    //boolean hasEqMethod = clazz.hasPredefinedMethod(BuiltinNames.equals_ident);
    //boolean itIsNativeSpecClass = clazz.isNativeArray() || clazz.isNativeString() || clazz.isNativeOpt();

    return true;
  }

  private FlatCodeItem genAssert(Var v) {
    IntrinsicText text = new IntrinsicText(v, "assert(" + v.getName().getName() + ")");
    return new FlatCodeItem(text);
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
          constructor.signToStringCall(), lvalue.getType(), args);

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
      throw new RuntimeException(base.toString() + " ???");
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
      if (clazz.isStaticClass()) {

        args.remove(0); // __this

        if (method.isVoid()) {

          final FlatCallVoidStaticClassMethod call = new FlatCallVoidStaticClassMethod(method,
              method.signToStringCall(), args);
          final FlatCodeItem item = new FlatCodeItem(call);
          genRaw(item);
        }

        else {
          final FunctionCallWithResultStatic call = new FunctionCallWithResultStatic(method, method.signToStringCall(),
              method.getType(), args);
          final Var resultVar = VarCreator.justNewVar(method.getType());
          final FlatCodeItem item = new FlatCodeItem(new AssignVarFlatCallResultStatic(resultVar, call));
          genRaw(item);
        }
      }

      /// method calls from object which is allocated
      else {

        if (method.isVoid()) {
          final FlatCallVoid call = new FlatCallVoid(method, method.signToStringCall(), args);
          final FlatCodeItem item = new FlatCodeItem(call);
          genRaw(item);
        }

        else {
          final FunctionCallWithResult call = new FunctionCallWithResult(method, method.signToStringCall(),
              method.getType(), args);
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
      final FlatCodeItem thisItem = popCode();
      final Var obj = thisItem.getDest();

      ///TODO:static_semantic
      final ClassDeclaration clazz = obj.getType().getClassTypeFromRef();
      if (clazz.isStaticClass()) {
        /// int f = constants.os_version;
        /// ::
        /// int t8 = constants->os_version;
        /// int f = t8;
        ///
        final Var staticClassName = new Var(VarBase.STATIC_VAR, new Modifiers(), field.getType(),
            clazz.getIdentifier());
        final FieldAccess access = new FieldAccess(staticClassName, VarCreator.copyVarDecl(field));
        final Var lhsvar = VarCreator.justNewVar(field.getType());
        final FlatCodeItem item = new FlatCodeItem(new AssignVarStaticFieldAccess(lhsvar, access));

        genRaw(item);
      }

      else {
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

      final FunctionCallWithResult call = new FunctionCallWithResult(constructor, constructor.signToStringCall(),
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
        AssignVarTrue node = new AssignVarTrue(VarCreator.justNewVar(e.getResultType()));
        FlatCodeItem item = new FlatCodeItem(node);
        genRaw(item);
      } else {
        AssignVarFalse node = new AssignVarFalse(VarCreator.justNewVar(e.getResultType()));
        FlatCodeItem item = new FlatCodeItem(node);
        genRaw(item);
      }
    }

    else if (base == ExpressionBase.EBUILTIN_FUNC) {
      ExprBuiltinFunc node = e.getExprBuiltinFunc();
      Ident name = node.getName();

      final List<Var> args = genArgs(node.getArgs());

      if (name.equals(Keywords.assert_true_ident)) {

        /// void assert_true(int cnd, const char *file, int line, const char *expr)
        /// assert_true(c == 'a', new struct string*(C:/Users/dvv/Desktop/pr/jsparse/hb/std/natives/string.hb), 10, new struct string*(c == 'a'))

        String file = "\"" + CEscaper.toCString(Normalizer.normalize(node.getFileToString())) + "\"";
        String line = node.getLineToString();
        String expr = "\"" + CEscaper.toCString(node.getExprToString()) + "\"";

        StringBuilder intrArgs = new StringBuilder();
        intrArgs.append("assert_true(");
        intrArgs.append(args.get(0).getName().getName());
        intrArgs.append(", ");
        intrArgs.append(file);
        intrArgs.append(", ");
        intrArgs.append(line);
        intrArgs.append(", ");
        intrArgs.append(expr);
        intrArgs.append(")");

        IntrinsicText intrinsicText = new IntrinsicText(args.get(0), intrArgs.toString());
        genRaw(new FlatCodeItem(intrinsicText));

      }

      else {
        ErrorLocation.errorExpression("unimpl.builtin function:", e);
      }
    }

    else {
      throw new RuntimeException(base.toString() + ": unimplemented");
    }

  }

}
