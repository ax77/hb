package _st3_linearize_expr.items;

public class BuiltinFuncDropPtr {

  private final String name;
  private final String defaultEmptyPtr;

  public BuiltinFuncDropPtr(String name, String defaultEmptyPtr) {
    this.name = name;
    this.defaultEmptyPtr = defaultEmptyPtr;
  }

  public String getName() {
    return name;
  }

  public String getDefaultEmptyPtr() {
    return defaultEmptyPtr;
  }

  @Override
  public String toString() {
    return "drop_ptr(&" + name + ", " + defaultEmptyPtr + ")";
  }

}
