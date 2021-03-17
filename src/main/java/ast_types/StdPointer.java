package ast_types;

import java.io.Serializable;

import _st1_templates.TypeSetter;

public class StdPointer implements Serializable, TypeSetter {
  private static final long serialVersionUID = 8162849490065346555L;

  private Type type;

  public StdPointer(Type type) {
    this.type = type;
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public void setType(Type typeToSet) {
    this.type = typeToSet;
  }

  public boolean isEqualTo(StdPointer stdPointer) {
    return type.isEqualTo(stdPointer.getType());
  }

  @Override
  public String toString() {
    return type.toString() + "_ptr_t";
  }

}
