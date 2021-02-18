package _temp;

import org.junit.Test;

public class TestAux {
  class temp {
    int stubfield;
  }

  class token {
    type type;
  }

  class type {
    strtemp value;
  }

  class strtemp {
    int x;
  }

  class tree {
    tree lhs;
    tree rhs;
    int value;
  }

  class main_class {

    /// METHODS: main_class
    void main_class_init_8(main_class _this_) {
    }

    void main_class_deinit_9(main_class _this_) {
    }

    void main() {

      // tree tree = new tree(new tree(1), new tree(2));
      int __t16 = 1;
      tree __t17 = new tree();
      tree_init_0(__t17, __t16);
      int __t18 = 2;
      tree __t19 = new tree();
      tree_init_0(__t19, __t18);
      tree __t20 = new tree();
      tree_init_1(__t20, __t17, __t19);
      tree tree = __t20;

      // strtemp x1 = new strtemp(1);
      int __t21 = 1;
      strtemp __t22 = new strtemp();
      strtemp_init_2(__t22, __t21);
      strtemp x1 = __t22;

      // type x2 = new type(x1);
      strtemp __t23 = x1;
      type __t24 = new type();
      type_init_4(__t24, __t23);
      type x2 = __t24;

      // token x3 = new token(x2);
      type __t25 = x2;
      token __t26 = new token();
      token_init_6(__t26, __t25);
      token x3 = __t26;

      // token tok1 = new token(new type(new strtemp(1)));
      int __t27 = 1;
      strtemp __t28 = new strtemp();
      strtemp_init_2(__t28, __t27);
      type __t29 = new type();
      type_init_4(__t29, __t28);
      token __t30 = new token();
      token_init_6(__t30, __t29);
      token tok1 = __t30;

      // int res = tok1.type.value.x;
      token __t31 = tok1;
      type __t32 = __t31.type;
      strtemp __t33 = __t32.value;
      int __t34 = __t33.x;
      int res = __t34;

      // tok1.type.value.x = 1024
      token __t35 = tok1;
      type __t36 = __t35.type;
      strtemp __t37 = __t36.value;
      int __t38 = __t37.x;
      int __t39 = 1024;
      __t37.x = __t39;

      // int a = 0;
      int __t40 = 0;
      int a = __t40;

      // int b = 1;
      int __t41 = 1;
      int b = __t41;

      // int c = 2;
      int __t42 = 2;
      int c = __t42;

      // int e = a + b + c;
      int __t43 = a;
      int __t44 = b;
      int __t45 = __t43 + __t44;
      int __t46 = c;
      int __t47 = __t45 + __t46;
      int e = __t47;

      // int f = x1.getX();
      strtemp __t48 = x1;
      int __t49 = strtemp_getX_3(__t48);
      int f = __t49;

      // int g = x2.getValue().getX();
      type __t50 = x2;
      strtemp __t51 = type_getValue_5(__t50);
      int __t52 = strtemp_getX_3(__t51);
      int g = __t52;
    }

    /// METHODS: temp
    void temp_init_10(temp _this_) {
    }

    void temp_deinit_11(temp _this_) {
    }

    /// METHODS: token
    void token_init_6(token _this_, type type) {

      // this.type = type
      token __t53 = _this_;
      type __t54 = __t53.type;
      type __t55 = type;
      __t53.type = __t55;
    }

    void token_deinit_12(token _this_) {
    }

    /// METHODS: type
    void type_init_4(type _this_, strtemp value) {

      // this.value = value
      type __t56 = _this_;
      strtemp __t57 = __t56.value;
      strtemp __t58 = value;
      __t56.value = __t58;
    }

    void type_deinit_13(type _this_) {
    }

    strtemp type_getValue_5(type _this_) {

      // return value
      type __t59 = _this_;
      strtemp __t60 = __t59.value;
      return __t60;
    }

    /// METHODS: strtemp
    void strtemp_init_2(strtemp _this_, int x) {

      // this.x = x
      strtemp __t61 = _this_;
      int __t62 = __t61.x;
      int __t63 = x;
      __t61.x = __t63;
    }

    void strtemp_deinit_14(strtemp _this_) {
    }

    int strtemp_getX_3(strtemp _this_) {

      // return x
      strtemp __t64 = _this_;
      int __t65 = __t64.x;
      return __t65;
    }

    /// METHODS: tree
    void tree_init_0(tree _this_, int value) {

      // this.value = value
      tree __t66 = _this_;
      int __t67 = __t66.value;
      int __t68 = value;
      __t66.value = __t68;
    }

    void tree_init_1(tree _this_, tree lhs, tree rhs) {

      // this.lhs = lhs
      tree __t69 = _this_;
      tree __t70 = __t69.lhs;
      tree __t71 = lhs;
      __t69.lhs = __t71;

      // this.rhs = rhs
      tree __t72 = _this_;
      tree __t73 = __t72.rhs;
      tree __t74 = rhs;
      __t72.rhs = __t74;
    }

    void tree_deinit_15(tree _this_) {
    }
  }

}
