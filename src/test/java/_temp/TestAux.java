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

      // strtemp x1 = new strtemp(1);
      int __t13 = 1;
      strtemp __t14 = new strtemp();
      strtemp_init_0(__t14, __t13);
      strtemp x1 = null;
      x1 = strtemp_opAssign_12(x1, __t14);

      // type x2 = new type(x1);
      strtemp __t15 = null;
      __t15 = strtemp_opAssign_12(__t15, x1);
      type __t16 = new type();
      type_init_1(__t16, __t15);
      type x2 = null;
      x2 = type_opAssign_10(x2, __t16);

      // token x3 = new token(x2);
      type __t17 = null;
      __t17 = type_opAssign_10(__t17, x2);
      token __t18 = new token();
      token_init_2(__t18, __t17);
      token x3 = null;
      x3 = token_opAssign_8(x3, __t18);

      // token tok1 = new token(new type(new strtemp(1)));
      int __t19 = 1;
      strtemp __t20 = new strtemp();
      strtemp_init_0(__t20, __t19);
      type __t21 = new type();
      type_init_1(__t21, __t20);
      token __t22 = new token();
      token_init_2(__t22, __t21);
      token tok1 = null;
      tok1 = token_opAssign_8(tok1, __t22);
    }

    main_class main_class_opAssign_6(main_class lvalue, main_class rvalue) {

      // return rvalue
      main_class __t23 = rvalue;
      return __t23;
    }

    /// METHODS: token
    void token_init_2(token _this_, type type) {

      // this.type = type
      token __t24 = null;
      __t24 = token_opAssign_8(__t24, _this_);
      type __t25 = __t24.type;
      type __t26 = null;
      __t26 = type_opAssign_10(__t26, type);
      __t24.type = __t26;
    }

    void token_deinit_7(token _this_) {
    }

    token token_opAssign_8(token lvalue, token rvalue) {

      // return rvalue
      token __t27 = rvalue;
      return __t27;
    }

    /// METHODS: type
    void type_init_1(type _this_, strtemp value) {

      // this.value = value
      type __t28 = null;
      __t28 = type_opAssign_10(__t28, _this_);
      strtemp __t29 = __t28.value;
      strtemp __t30 = null;
      __t30 = strtemp_opAssign_12(__t30, value);
      __t28.value = __t30;
    }

    void type_deinit_9(type _this_) {
    }

    type type_opAssign_10(type lvalue, type rvalue) {

      // return rvalue
      type __t31 = rvalue;
      return __t31;
    }

    /// METHODS: strtemp
    void strtemp_init_0(strtemp _this_, int x) {

      // this.x = x
      strtemp __t32 = null;
      __t32 = strtemp_opAssign_12(__t32, _this_);
      int __t33 = __t32.x;
      int __t34 = x;
      __t32.x = __t34;
    }

    void strtemp_deinit_11(strtemp _this_) {
    }

    strtemp strtemp_opAssign_12(strtemp lvalue, strtemp rvalue) {

      // return rvalue
      strtemp __t35 = rvalue;
      return __t35;
    }
  }

}
