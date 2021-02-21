package _temp;

import org.junit.Test;

public class TestAux2 {

  int f(int p, int g) {
    int a = 0;
    int x = p;
    int z = g;
    boolean b = x > z ? false : true;
    if (b) {
      return a;
    } else {
      return x;
    }
  }

  @Test
  public void test() {
    boolean b = false;
    if (b) {
      return;
    } else {
      if (b) {
        return;
      } else if (b) {
        return;
      }
      return;
    }
  }

}
