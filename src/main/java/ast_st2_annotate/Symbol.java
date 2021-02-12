package ast_st2_annotate;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_vars.VarDeclarator;
import errors.AstParseException;

public class Symbol {

  /// TODO:
  /// for each abstract class there is one variable
  /// `global` variable for the whole unit
  /// and remove this clazz from here.

  private final SymbolBase base;
  private ClassDeclaration clazz;
  private VarDeclarator variable;
  private ClassMethodDeclaration method;

  public Symbol(ClassDeclaration clazz) {
    this.base = SymbolBase.CLASS_SYM;
    this.clazz = clazz;
  }

  public Symbol(VarDeclarator variable) {
    this.base = SymbolBase.VARIABLE_SYM;
    this.variable = variable;
  }

  public Symbol(ClassMethodDeclaration method) {
    this.base = SymbolBase.METHOD_SYM;
    this.method = method;
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

  public ClassMethodDeclaration getMethod() {
    if (!isMethod()) {
      throw new AstParseException("it is not a method");
    }
    return method;
  }

  public boolean isClazz() {
    return base == SymbolBase.CLASS_SYM;
  }

  public boolean isVariable() {
    return base == SymbolBase.VARIABLE_SYM;
  }

  public boolean isMethod() {
    return base == SymbolBase.METHOD_SYM;
  }

  public Type getType() {
    if (isClazz()) {
      return new Type(new ClassTypeRef(clazz, clazz.getTypeParametersT()), clazz.getBeginPos());
    }
    if (isMethod()) {
      return method.getType();
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
    if (isMethod()) {
      return "m:" + method.getIdentifier().getName();
    }
    return base.toString();
  }

}
