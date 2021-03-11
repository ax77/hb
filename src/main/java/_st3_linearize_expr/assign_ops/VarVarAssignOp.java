package _st3_linearize_expr.assign_ops;

import _st3_linearize_expr.leaves.Var;
import ast_method.ClassMethodDeclaration;
import ast_printers.TypePrinters;
import ast_types.Type;
import tokenize.Ident;

public class VarVarAssignOp {

  // a = opAssign(a, b)

  private final ClassMethodDeclaration method;
  private final Type type;
  private final Ident function;
  private final Var lvalueArg;
  private final Var rvalueArg;

  public VarVarAssignOp(ClassMethodDeclaration method, Type type, Ident function, Var lvalueArg, Var rvalueArg) {
    this.method = method;
    this.type = type;
    this.function = function;
    this.lvalueArg = lvalueArg;
    this.rvalueArg = rvalueArg;
  }

  public Type getType() {
    return type;
  }

  public Ident getFunction() {
    return function;
  }

  public Var getLvalueArg() {
    return lvalueArg;
  }

  public Var getRvalueArg() {
    return rvalueArg;
  }

  public ClassMethodDeclaration getMethod() {
    return method;
  }

  @Override
  public String toString() {
    return function.toString() + TypePrinters.typeArgumentsToString(method.getClazz().getTypeParametersT()) + "("
        + lvalueArg.toString() + ", " + rvalueArg + ")";
  }

}
