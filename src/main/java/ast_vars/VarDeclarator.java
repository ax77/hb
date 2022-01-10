package ast_vars;

import java.io.Serializable;

import _st1_templates.TypeSetter;
import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_modifiers.Modifiers;
import ast_sourceloc.Location;
import ast_sourceloc.SourceLocation;
import ast_types.Type;
import tokenize.Ident;
import tokenize.Token;
import utils_oth.NullChecker;

public class VarDeclarator implements Serializable, TypeSetter, Location {
  private static final long serialVersionUID = -364976996504280849L;

  // main part, it may be the field, method-parameter, local-var, etc...
  private final VarBase base;
  private final Modifiers mods;
  private Type type;
  private final Ident identifier;
  private final Token beginPos;

  // for variables
  private ExprExpression simpleInitializer;

  // is it is a field
  private ClassDeclaration clazz;

  public VarDeclarator(VarBase base, Modifiers mods, Type type, Ident identifier, Token beginPos) {
    NullChecker.check(base, mods, type, identifier, beginPos);

    this.base = base;
    this.mods = mods;
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

  public Ident getIdentifier() {
    return identifier;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(mods.toString() + " ");

    sb.append(type.toString());
    sb.append(" ");
    sb.append(identifier.getName());

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

  public Modifiers getMods() {
    return mods;
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
