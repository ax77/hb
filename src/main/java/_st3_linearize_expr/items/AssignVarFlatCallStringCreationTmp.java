package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.Var;
import ast_method.ClassMethodDeclaration;
import tokenize.Ident;

public class AssignVarFlatCallStringCreationTmp {
  private final Var lvalue;
  private final String rvalue;

  private final ClassMethodDeclaration constructor;
  private final ClassMethodDeclaration appendMethod;
  private final Ident function;

  public AssignVarFlatCallStringCreationTmp(Var lvalue, String rvalue, ClassMethodDeclaration constructor, Ident function,
      ClassMethodDeclaration appendMethod) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
    this.constructor = constructor;
    this.function = function;
    this.appendMethod = appendMethod;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public String getRvalue() {
    return rvalue;
  }

  public ClassMethodDeclaration getConstructor() {
    return constructor;
  }

  public Ident getFunction() {
    return function;
  }

  public ClassMethodDeclaration getAppendMethod() {
    return appendMethod;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + rvalue.toString();
  }
}
