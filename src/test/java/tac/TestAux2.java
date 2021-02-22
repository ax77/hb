package tac;

import org.junit.Test;

public class TestAux2 {
  class str {
    int value;
  }

  class main_class {

    /// METHODS: main_class
    void main_class_init_3(final main_class __this) {
    }

    void main_class_deinit_4(final main_class __this) {
    }

    void main() {

      /////// str s1 = new str();
      str t8 = new str();
      str_init_0(t8);
      str s1 = null;
      s1 = str_opAssign_7(s1, t8);

      /////// str s2 = new str(1);
      int t9 = 1;
      str t10 = new str();
      str_init_1(t10, t9);
      str s2 = null;
      s2 = str_opAssign_7(s2, t10);

      /////// str s3 = s2;
      str t11 = null;
      t11 = str_opAssign_7(t11, s2);
      str s3 = null;
      s3 = str_opAssign_7(s3, t11);
      {

        /////// str s4 = new str();
        str t12 = new str();
        str_init_0(t12);
        str s4 = null;
        s4 = str_opAssign_7(s4, t12);

        /////// str s5 = s4;
        str t13 = null;
        t13 = str_opAssign_7(t13, s4);
        str s5 = null;
        s5 = str_opAssign_7(s5, t13);
        {
          str_deinit_6(s5);
          str_deinit_6(t13);
          str_deinit_6(s4);
          str_deinit_6(t12);
        }
      }
      {

        /////// int i = 0;
        int t42 = 0;
        int i = t42;
        for (;;) {

          /////// !i < 8
          int t50 = i;
          int t51 = 8;
          boolean t52 = t50 < t51;
          boolean t53 = !t52;
          if (t53) {
            // {
            //   str_deinit_6(s6);
            //   str_deinit_6(t14);
            // }
            break;
          }

          /////// str s6 = new str();
          str t14 = new str();
          str_init_0(t14);
          str s6 = null;
          s6 = str_opAssign_7(s6, t14);

          /////// i == 5
          int t19 = i;
          int t20 = 5;
          boolean t21 = t19 == t20;
          if (t21) {
            {

              /////// i = i + 1
              int t15 = i;
              int t16 = i;
              int t17 = 1;
              int t18 = t16 + t17;
              i = t18;
              {
                str_deinit_6(s6);
                str_deinit_6(t14);
              }
              continue;
            }
          }

          /////// i == 7
          int t39 = i;
          int t40 = 7;
          boolean t41 = t39 == t40;
          if (t41) {
            {

              /////// int j = 0;
              int t27 = 0;
              int j = t27;
              for (;;) {

                /////// !j < 32
                int t35 = j;
                int t36 = 32;
                boolean t37 = t35 < t36;
                boolean t38 = !t37;
                if (t38) {
                  // {
                  //   str_deinit_6(s7);
                  //   str_deinit_6(t23);
                  // }
                  break;
                }

                /////// str s7 = new str(j);
                int t22 = j;
                str t23 = new str();
                str_init_1(t23, t22);
                str s7 = null;
                s7 = str_opAssign_7(s7, t23);

                /////// j == 1
                int t24 = j;
                int t25 = 1;
                boolean t26 = t24 == t25;
                if (t26) {
                  {
                    str_deinit_6(s7);
                    str_deinit_6(t23);
                  }
                  break;
                }
                {

                  /////// j = j + 1
                  int t31 = j;
                  int t32 = j;
                  int t33 = 1;
                  int t34 = t32 + t33;
                  j = t34;
                }
                {
                  str_deinit_6(s7);
                  str_deinit_6(t23);
                }
              }
            }
          }
          {

            /////// i = i + 1
            int t46 = i;
            int t47 = i;
            int t48 = 1;
            int t49 = t47 + t48;
            i = t49;
          }
          {
            str_deinit_6(s6);
            str_deinit_6(t14);
          }
        }
      }
      {
        str_deinit_6(s3);
        str_deinit_6(t11);
        str_deinit_6(s2);
        str_deinit_6(t10);
        str_deinit_6(s1);
        str_deinit_6(t8);
      }
    }

    main_class main_class_opAssign_5(final main_class __this, main_class rvalue) {
      return rvalue;
    }

    /// METHODS: str
    void str_init_0(final str __this) {
    }

    void str_init_1(final str __this, int value) {

      /////// this.value = value
      str t56 = null;
      t56 = str_opAssign_7(t56, __this);
      int t57 = t56.value;
      int t58 = value;
      t56.value = t58;
      {
        str_deinit_6(t56);
      }
    }

    void str_deinit_6(final str __this) {
    }

    str str_opAssign_7(final str __this, str rvalue) {
      return rvalue;
    }
  }

}
