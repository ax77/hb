package ast_st2_annotate;

import ast_class.ClassDeclaration;
import ast_vars.VarDeclarator;

public class Symbol {
  private ClassDeclaration classType;
  private VarDeclarator variable;
  private final boolean isClassType;

  public Symbol(ClassDeclaration classType) {
    this.isClassType = true;
    this.classType = classType;
  }

  public Symbol(VarDeclarator variable) {
    this.isClassType = false;
    this.variable = variable;
  }

  public ClassDeclaration getClassType() {
    return classType;
  }

  public VarDeclarator getVariable() {
    return variable;
  }

  public boolean isClassType() {
    return isClassType;
  }

  @Override
  public String toString() {
    if (isClassType) {
      return "SYM: " + classType.toString();
    }
    return "VAR: " + variable.toString();
  }

}
