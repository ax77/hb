package ast_vars;

import java.io.Serializable;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_modifiers.Modifiers;
import ast_sourceloc.ILocation;
import ast_sourceloc.SourceLocation;
import ast_templates.TypeSetter;
import ast_types.Type;
import tokenize.Ident;

public class VarDeclarator implements Serializable, TypeSetter, ILocation {
  private static final long serialVersionUID = -364976996504280849L;

  private final VarBase base;
  private final Modifiers modifiers;
  private /*final*/ Type type;
  private final Ident identifier;
  private List<VarInitializer> initializer;
  private final SourceLocation location;
  private ClassDeclaration clazz; // is it is a field

  public VarDeclarator(VarBase base, Modifiers modifiers, Type type, Ident identifier, SourceLocation location) {
    this.base = base;
    this.modifiers = modifiers;
    this.type = type;
    this.identifier = identifier;
    this.location = location;
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public void setType(Type typeToSet) {
    this.type = typeToSet;
  }

  public List<VarInitializer> getInitializer() {
    return initializer;
  }

  public void setInitializer(List<VarInitializer> initializer) {
    this.initializer = initializer;
  }

  public Ident getIdentifier() {
    return identifier;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(modifiers.toString() + " ");
    sb.append(identifier.getName());
    sb.append(": ");
    sb.append(type.toString());
    if (initializer != null) {
      sb.append(" = ");
      sb.append(inits(initializer));
    }
    sb.append("; ");
    return sb.toString();
  }

  private String inits(List<VarInitializer> inits) {
    StringBuilder sb = new StringBuilder();
    if (inits.size() > 1) {
      sb.append("[");
      for (int i = 0; i < inits.size(); i++) {
        VarInitializer init = inits.get(i);
        sb.append(init.toString());
        if (i + 1 < inits.size()) {
          sb.append(", ");
        }
      }
      sb.append("]");
    } else if (inits.size() == 1) {
      sb.append(inits.get(0).getInit().toString());
    } else {
      System.out.println("[???-inits-printer]");
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
    return location;
  }

  @Override
  public String getLocationToString() {
    return location.toString();
  }

}
