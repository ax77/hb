package _temp;

public class TestAux2 {
  class str {
  }

  class main_class {

    /// METHODS: main_class
    void main_class_init_2(main_class __this) {
    }

    void main_class_deinit_3(main_class __this) {
    }

    void main() {

      /////// int a = 0;

      // []
      int t7 = 0;
      int a = t7;

      /////// a == 0

      // []
      int t8 = a;
      int t9 = 0;
      boolean t10 = t8 == t9;
      if (t10) {

        /////// a = 1

        // []
        int t11 = a;
        int t12 = 1;
        a = t12;
      } else {

        /////// a == 1

        // []
        int t13 = a;
        int t14 = 1;
        boolean t15 = t13 == t14;
        if (t15) {

          /////// a = 2

          // []
          int t16 = a;
          int t17 = 2;
          a = t17;
        } else {

          /////// a == 2

          // []
          int t18 = a;
          int t19 = 2;
          boolean t20 = t18 == t19;
          if (t20) {

            /////// a = 3

            // []
            int t21 = a;
            int t22 = 3;
            a = t22;
          } else {

            /////// a = 32

            // []
            int t23 = a;
            int t24 = 32;
            a = t24;
          }
        }
      }
    }

    main_class main_class_opAssign_4(main_class lvalue, main_class rvalue) {
      return rvalue;
    }

    /// METHODS: str
    void str_init_0(str __this) {
    }

    void str_deinit_5(str __this) {
    }

    str str_opAssign_6(str lvalue, str rvalue) {
      return rvalue;
    }
  }

}
