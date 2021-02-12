package _temp;

public class TestAux {
  class type {
    tok token;

    void fn() {
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
        // a == 0
        int t17 = a;
        int t18 = 0;
        boolean t19 = t17 == t18;
        if (t19) {
          // b = b + 1
          int t20 = b;
          int t21 = b;
          int t22 = 1;
          int t23 = t21 + t22;
          b = t23;
        } else {
          // a == 1
          int t25 = a;
          int t26 = 1;
          boolean t27 = t25 == t26;
          if (t27) {
            // b = b - 1
            int t28 = b;
            int t29 = b;
            int t30 = 1;
            int t31 = t29 - t30;
            b = t31;
          } else {
            // b - 1 == 0
            int t33 = b;
            int t34 = 1;
            int t35 = t33 - t34;
            int t36 = 0;
            boolean t37 = t35 == t36;
            if (t37) {
              // a = a - 1 + 2 - 3
              int t38 = a;
              int t39 = a;
              int t40 = 1;
              int t41 = 2;
              int t42 = t40 + t41;
              int t43 = 3;
              int t44 = t42 - t43;
              int t45 = t39 - t44;
              a = t45;
            } else {
              // a = a - 2
              int t47 = a;
              int t48 = a;
              int t49 = 2;
              int t50 = t48 - t49;
              a = t50;
            }
          }
        }
        // a = a + token.fn()
        int t52 = a;
        int t53 = a;
        type t54 = this;
        tok t55 = t54.token;
        int t56 = t55.fn();
        int t57 = t53 + t56;
        a = t57;
        // b = 1
        int t59 = b;
        int t60 = 1;
        b = t60;
      }
    }
  }

  class tok {
    int value;

    int fn() {
      // value
      tok t62 = this;
      int t63 = t62.value;
      return t63;
    }
  }

}
