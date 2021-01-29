package ast_vars;

import java.io.Serializable;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_modifiers.Modifiers;
import ast_sourceloc.ILocation;
import ast_sourceloc.SourceLocation;
import ast_st1_templates.TypeSetter;
import ast_types.Type;
import tokenize.Ident;
import tokenize.Token;

public class VarDeclarator implements Serializable, TypeSetter, ILocation {
  private static final long serialVersionUID = -364976996504280849L;

  private final VarBase base;
  private final Token beginPos;
  private final Modifiers modifiers;
  private /*final*/ Type type;
  private final Ident identifier;
  private ExprExpression simpleInitializer;
  private VarArrayInitializer arrayInitializer;
  private ClassDeclaration clazz; // is it is a field

  public VarDeclarator(VarBase base, Modifiers modifiers, Type type, Ident identifier, Token beginPos) {
    this.base = base;
    this.modifiers = modifiers;
    this.type = type;
    this.identifier = identifier;
    this.beginPos = beginPos;
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public void setType(Type typeToSet) {
    this.type = typeToSet;
  }

  public ExprExpression getSimpleInitializer() {
    return simpleInitializer;
  }

  public void setSimpleInitializer(ExprExpression simpleInitializer) {
    this.simpleInitializer = simpleInitializer;
  }

  public VarArrayInitializer getArrayInitializer() {
    return arrayInitializer;
  }

  public void setArrayInitializer(VarArrayInitializer arrayInitializer) {
    this.arrayInitializer = arrayInitializer;
  }

  public Ident getIdentifier() {
    return identifier;
  }

  public boolean isArrayInitializer() {
    return arrayInitializer != null;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(modifiers.toString() + " ");
    sb.append(identifier.getName());
    sb.append(": ");
    sb.append(type.toString());
    if (simpleInitializer != null) {
      sb.append(" = ");
      sb.append(simpleInitializer.toString());
    }
    sb.append("; ");
    return sb.toString();
  }

  public ClassDeclaration getClazz() {
    return clazz;
  }

  public void setClazz(ClassDeclaration clazz) {
    this.clazz = clazz;
  }

  public VarBase getBase() {
    return base;
  }

  @Override
  public SourceLocation getLocation() {
    return beginPos.getLocation();
  }

  @Override
  public String getLocationToString() {
    return beginPos.getLocationToString();
  }

  public Token getBeginPos() {
    return beginPos;
  }

  public boolean is(VarBase want) {
    return base.equals(want);
  }

}
