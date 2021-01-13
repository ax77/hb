package njast.ast_utils;

public abstract class UniqueCounter {
  private static int classIdCounter = 0;

  public static int getUniqueId() {
    return classIdCounter++;
  }
}
