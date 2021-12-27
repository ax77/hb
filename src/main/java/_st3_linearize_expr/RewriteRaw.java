package _st3_linearize_expr;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.ir.FlatCodeItem;
import _st3_linearize_expr.ir.VarCreator;
import _st3_linearize_expr.items.AssignVarAllocObject;
import _st3_linearize_expr.items.AssignVarBinop;
import _st3_linearize_expr.items.AssignVarFlatCallClassCreationTmp;
import _st3_linearize_expr.items.AssignVarFlatCallResult;
import _st3_linearize_expr.items.AssignVarUnop;
import _st3_linearize_expr.items.AssignVarVar;
import _st3_linearize_expr.items.FlatCallConstructor;
import _st3_linearize_expr.items.IntrinsicText;
import _st3_linearize_expr.leaves.Binop;
import _st3_linearize_expr.leaves.FunctionCallWithResult;
import _st3_linearize_expr.leaves.Unop;
import _st3_linearize_expr.leaves.Var;
import _st7_codeout.ToStringsInternal;
import ast_expr.ExprExpression;
import ast_method.ClassMethodDeclaration;
import ast_sourceloc.SourceLocation;
import ast_symtab.BuiltinNames;
import ast_types.Type;
import ast_types.TypeBindings;
import errors.AstParseException;

public class RewriteRaw {

  private final List<FlatCodeItem> rv;
  private final List<FlatCodeItem> rawResult;
  private final ClassMethodDeclaration method;
  private final SourceLocation location;
  private final ExprExpression inputExpression;

  public RewriteRaw(ExprExpression inputExpression, List<FlatCodeItem> rawResult, ClassMethodDeclaration method,
      SourceLocation location) {
    this.inputExpression = inputExpression;
    this.rawResult = rawResult;
    this.rv = new ArrayList<>();
    this.method = method;
    this.location = location;

    rewriteRaw();
  }

  public List<FlatCodeItem> getRv() {
    return rv;
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

        final String op = assignVarBinop.getRvalue().getOp();
        if (itIsPossibleToReplaceEqNeWithCall(lhsType, op)) {
          genEq(assignVarBinop, lhs, rhs, op);
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
        // pass
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

        // 3
        FlatCallConstructor flatCallConstructor = new FlatCallConstructor(rvalue.getFullname(), args, lvalueVar);
        rv.add(new FlatCodeItem(flatCallConstructor));

      }

      else if (item.isAssignVarFlatCallResult()) {
        rv.add(item);
      }

      else if (item.isAssignVarNull()) {
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
        rv.add(item);
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
        }

        rv.add(item);

        if (item.getIntrinsicText().getText().startsWith("assert_true")) {
        }
      }

      /// cast
      else if (item.isAssignVarCastExpression()) {
        rv.add(item);
      }

      else {
        throw new AstParseException("unknown item: " + item.toString());
      }

    }

  }

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
  ///
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
    return true;
  }

  private void genEq(final AssignVarBinop assignVarBinop, final Var lhs, final Var rhs, final String op) {

    final Type lhsType = lhs.getType();
    final ClassMethodDeclaration meth = lhsType.getClassTypeFromRef().getPredefinedMethod(BuiltinNames.equals_ident);
    final String sign = ToStringsInternal.signToStringCall(meth);

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

  private FlatCodeItem genAssert(Var v) {
    String e = labelName(expr("null pointer field-access"));
    String m = labelName(ToStringsInternal.signToStringCallPushF(method));

    IntrinsicText text = new IntrinsicText(v,
        "assert_true(" + v.getName().getName() + " != NULL, " + m + "->buffer, " + line() + ", " + e + "->buffer)");
    return new FlatCodeItem(text);
  }

  private String line() {
    return String.format("%d", location.getLine());
  }

  private String expr(String msg) {
    return "\"" + msg + ": " + inputExpression.toString() + "\"";
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
