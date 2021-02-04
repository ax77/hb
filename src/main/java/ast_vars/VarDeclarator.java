package ast_vars;

import java.io.Serializable;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_modifiers.Modifiers;
import ast_sourceloc.Location;
import ast_sourceloc.SourceLocation;
import ast_st1_templates.TypeSetter;
import ast_types.Type;
import tokenize.Ident;
import tokenize.Token;
import utils_oth.NullChecker;

public class VarDeclarator implements Serializable, TypeSetter, Location {
  private static final long serialVersionUID = -364976996504280849L;

  // main part, it may be the field, method-parameter, local-var, etc...
  private final VarBase base;
  private final Modifiers mod;
  private /*final*/ Type type;
  private final Ident identifier;
  private final Token beginPos;

  // for variables
  private ExprExpression simpleInitializer;
  private VarArrayInitializer arrayInitializer;

  // is it is a field
  private ClassDeclaration clazz;

  public VarDeclarator(VarBase base, Modifiers mod, Type type, Ident identifier, Token beginPos) {
    NullChecker.check(base, mod, type, identifier, beginPos);

    this.base = base;
    this.mod = mod;
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
    sb.append(mod.toString());
    sb.append(" ");
    sb.append(identifier.getName());
    sb.append(": ");
    sb.append(type.toString());

    if (!is(VarBase.METHOD_PARAMETER)) {

      if (simpleInitializer != null) {
        sb.append(" = ");
        sb.append(simpleInitializer.toString());
      }

      sb.append("; ");
    }

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
