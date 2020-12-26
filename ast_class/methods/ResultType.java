package njast.ast_class.methods;

import njast.types.Type;

public class ResultType {
  private Type type;
  private boolean isVoid;

  public ResultType() {
    this.isVoid = true;
  }

  public ResultType(Type type) {
    this.type = type;
    this.isVoid = false;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public boolean isVoid() {
    return isVoid;
  }

  public void setVoid(boolean isVoid) {
    this.isVoid = isVoid;
  }

}
