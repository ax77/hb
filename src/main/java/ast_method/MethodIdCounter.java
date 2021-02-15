package ast_method;

public class MethodIdCounter {
  private static int count = 0;

  public static int next() {
    final int res = count;
    count += 1;
    return res;
  }
}
