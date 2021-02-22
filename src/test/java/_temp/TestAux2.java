package _temp;

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
      str t16 = null;
      t16 = str_opAssign_7(t16, __this);
      int t17 = t16.value;
      int t18 = value;
      t16.value = t18;
    }

    void str_deinit_6(final str __this) {
    }

    str str_opAssign_7(final str __this, str rvalue) {
      return rvalue;
    }
  }

}
