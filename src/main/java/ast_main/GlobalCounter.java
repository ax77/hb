package ast_main;

public class GlobalCounter {
  private static int count = 0;

  public static int next() {
    final int res = count;
    count += 1;
    return res;
  }
}
