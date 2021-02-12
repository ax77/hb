package _temp;

public class TestAux {

  class type {
    tok token;

    void fn()

    {

      int a = 0;
      int b = 1;
      // token.value = a + b
      type t0 = this;
      tok t1 = t0.token;
      int t2 = t1.value;
      int t3 = a;
      int t4 = b;
      int t5 = t3 + t4;
      t1.value = t5;
      // a = token.value
      int t7 = a;
      type t8 = this;
      tok t9 = t8.token;
      int t10 = t9.value;
      a = t10;
      // b = a + a
      int t12 = b;
      int t13 = a;
      int t14 = a;
      int t15 = t13 + t14;
      b = t15;

      {

        // a = a + 1
        int t17 = a;
        int t18 = a;
        int t19 = 1;
        int t20 = t18 + t19;
        a = t20;
        // b = 1
        int t22 = b;
        int t23 = 1;
        b = t23;

      }

    }

  }

  class tok {
    int value;

  }

}
