package _st2_annotate;

import ast_class.ClassDeclaration;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_vars.VarDeclarator;
import errors.AstParseException;

public class Symbol {

  /// TODO:
  /// for each abstract class there is one variable
  /// `global` variable for the whole unit
  /// and remove this clazz from here.

  private ClassDeclaration clazz;
  private VarDeclarator variable;

  public Symbol(ClassDeclaration clazz) {
    this.clazz = clazz;
  }

  public Symbol(VarDeclarator variable) {
    this.variable = variable;
  }

  public ClassDeclaration getClazz() {
    if (!isClazz()) {
      throw new AstParseException("it is not a class");
    }
    return clazz;
  }

  public VarDeclarator getVariable() {
    if (!isVariable()) {
      throw new AstParseException("it is not a variable");
    }
    return variable;
  }

  public boolean isClazz() {
    return clazz != null;
  }

  public boolean isVariable() {
    return variable != null;
  }

  public Type getType() {
    if (isClazz()) {
      return new Type(new ClassTypeRef(clazz, clazz.getTypeParametersT()));
    }
    if (isVariable()) {
      return variable.getType();
    }
    throw new AstParseException("unreachable.");
  }

  @Override
  public String toString() {
    if (isClazz()) {
      return "c:" + clazz.getIdentifier().getName();
    }
    if (isVariable()) {
      return "v:" + variable.getIdentifier().getName();
    }
    return "???SYM";
  }

}
