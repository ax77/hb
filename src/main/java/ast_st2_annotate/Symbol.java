package ast_st2_annotate;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_vars.VarDeclarator;
import errors.AstParseException;

public class Symbol {
  private final SymbolBase base;
  private ClassDeclaration classType;
  private VarDeclarator variable;
  private ClassMethodDeclaration method;

  public Symbol(ClassDeclaration classType) {
    this.base = SymbolBase.ABSTRACT_CLASS;
    this.classType = classType;
  }

  public Symbol(VarDeclarator variable) {
    this.base = SymbolBase.VARIABLE;
    this.variable = variable;
  }

  public Symbol(ClassMethodDeclaration method) {
    this.base = SymbolBase.METHOD_SYM;
    this.method = method;
  }

  public ClassDeclaration getClassType() {
    if (!isAbstractClass()) {
      throw new AstParseException("it is not a class");
    }
    return classType;
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

  public boolean isAbstractClass() {
    return base == SymbolBase.ABSTRACT_CLASS;
  }

  public boolean isVariable() {
    return base == SymbolBase.VARIABLE;
  }

  public boolean isMethod() {
    return base == SymbolBase.METHOD_SYM;
  }

  public Type getType() {
    if (isAbstractClass()) {
      return new Type(new ClassTypeRef(classType, classType.getTypeParametersT()), classType.getBeginPos());
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
    if (isAbstractClass()) {
      return "c:" + classType.getIdentifier().getName();
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
