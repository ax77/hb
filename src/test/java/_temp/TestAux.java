package _temp;

import org.junit.Test;

public class TestAux {
  class token {
    type type;
  }

  class type {
    strtemp value;
  }

  class strtemp {
    int x;
  }

  class main_class {

    /// METHODS: main_class
    void main_class_init_4(main_class _this_) {
    }

    void main_class_deinit_5(main_class _this_) {
    }

    void main() {

      // int a = 1;
      int __t13 = 1;
      int a = __t13;

      // int b = a + 1;
      int __t14 = a;
      int __t15 = 1;
      int __t16 = __t14 + __t15;
      int b = __t16;

      // strtemp x1 = new strtemp(1);
      int __t17 = 1;
      strtemp __t18 = new strtemp();
      strtemp_init_0(__t18, __t17);
      strtemp x1 = null;
      x1 = strtemp_opAssign_12(x1, __t18);

      // type x2 = new type(x1);
      strtemp __t19 = null;
      __t19 = strtemp_opAssign_12(__t19, x1);
      type __t20 = new type();
      type_init_1(__t20, __t19);
      type x2 = null;
      x2 = type_opAssign_10(x2, __t20);

      // token x3 = new token(x2);
      type __t21 = null;
      __t21 = type_opAssign_10(__t21, x2);
      token __t22 = new token();
      token_init_2(__t22, __t21);
      token x3 = null;
      x3 = token_opAssign_8(x3, __t22);

      // token tok1 = new token(new type(new strtemp(1)));
      int __t23 = 1;
      strtemp __t24 = new strtemp();
      strtemp_init_0(__t24, __t23);
      type __t25 = new type();
      type_init_1(__t25, __t24);
      token __t26 = new token();
      token_init_2(__t26, __t25);
      token tok1 = null;
      tok1 = token_opAssign_8(tok1, __t26);
    }

    main_class main_class_opAssign_6(main_class lvalue, main_class rvalue) {
      return rvalue;
    }

    /// METHODS: token
    void token_init_2(token _this_, type type) {

      // this.type = type
      token __t27 = null;
      __t27 = token_opAssign_8(__t27, _this_);
      type __t28 = __t27.type;
      type __t29 = null;
      __t29 = type_opAssign_10(__t29, type);
      __t27.type = __t29;
    }

    void token_deinit_7(token _this_) {
    }

    token token_opAssign_8(token lvalue, token rvalue) {
      return rvalue;
    }

    /// METHODS: type
    void type_init_1(type _this_, strtemp value) {

      // this.value = value
      type __t30 = null;
      __t30 = type_opAssign_10(__t30, _this_);
      strtemp __t31 = __t30.value;
      strtemp __t32 = null;
      __t32 = strtemp_opAssign_12(__t32, value);
      __t30.value = __t32;
    }

    void type_deinit_9(type _this_) {
    }

    type type_opAssign_10(type lvalue, type rvalue) {
      return rvalue;
    }

    /// METHODS: strtemp
    void strtemp_init_0(strtemp _this_, int x) {

      // this.x = x
      strtemp __t33 = null;
      __t33 = strtemp_opAssign_12(__t33, _this_);
      int __t34 = __t33.x;
      int __t35 = x;
      __t33.x = __t35;
    }

    void strtemp_deinit_11(strtemp _this_) {
    }

    strtemp strtemp_opAssign_12(strtemp lvalue, strtemp rvalue) {
      return rvalue;
    }
  }

}
