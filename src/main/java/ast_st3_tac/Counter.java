package ast_st3_tac;

public class Counter {
  private static int count = 0;

  public static int next() {
    final int res = count;
    count += 1;
    return res;
  }
}
