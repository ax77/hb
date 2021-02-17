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

  class tree {
    tree lhs;
    tree rhs;
    int value;
  }

  class main_class {

    /// METHODS: main_class
    void main() {

      // tree tree = new tree(new tree(1), new tree(2));
      int __t0 = 1;
      tree __t1 = new tree();
      tree_init_0(__t1, __t0);
      int __t2 = 2;
      tree __t3 = new tree();
      tree_init_0(__t3, __t2);
      tree __t4 = new tree();
      tree_init_1(__t4, __t1, __t3);
      tree tree = __t4;

      // strtemp x1 = new strtemp(1);
      int __t5 = 1;
      strtemp __t6 = new strtemp();
      strtemp_init_2(__t6, __t5);
      strtemp x1 = __t6;

      // type x2 = new type(x1);
      strtemp __t7 = x1;
      type __t8 = new type();
      type_init_4(__t8, __t7);
      type x2 = __t8;

      // token x3 = new token(x2);
      type __t9 = x2;
      token __t10 = new token();
      token_init_6(__t10, __t9);
      token x3 = __t10;

      // token tok1 = new token(new type(new strtemp(1)));
      int __t11 = 1;
      strtemp __t12 = new strtemp();
      strtemp_init_2(__t12, __t11);
      type __t13 = new type();
      type_init_4(__t13, __t12);
      token __t14 = new token();
      token_init_6(__t14, __t13);
      token tok1 = __t14;

      // int res = tok1.type.value.x;
      token __t15 = tok1;
      type __t16 = __t15.type;
      strtemp __t17 = __t16.value;
      int __t18 = __t17.x;
      int res = __t18;

      // tok1.type.value.x = 1024
      token __t19 = tok1;
      type __t20 = __t19.type;
      strtemp __t21 = __t20.value;
      int __t22 = __t21.x;
      int __t23 = 1024;
      __t21.x = __t23;

      // int a = 0;
      int __t24 = 0;
      int a = __t24;

      // int b = 1;
      int __t25 = 1;
      int b = __t25;

      // int c = 2;
      int __t26 = 2;
      int c = __t26;

      // int e = a + b + c;
      int __t27 = a;
      int __t28 = b;
      int __t29 = __t27 + __t28;
      int __t30 = c;
      int __t31 = __t29 + __t30;
      int e = __t31;

      // int f = x1.getX();
      strtemp __t32 = x1;
      int __t33 = strtemp_getX_3(__t32);
      int f = __t33;

      // int g = x2.getValue().getX();
      type __t34 = x2;
      strtemp __t35 = type_getValue_5(__t34);
      int __t36 = strtemp_getX_3(__t35);
      int g = __t36;
    }

    /// METHODS: token
    void token_init_6(token _this_, type type) {

      // this.type = type
      token __t37 = _this_;
      type __t38 = __t37.type;
      type __t39 = type;
      __t37.type = __t39;
    }

    /// METHODS: type
    void type_init_4(type _this_, strtemp value) {

      // this.value = value
      type __t40 = _this_;
      strtemp __t41 = __t40.value;
      strtemp __t42 = value;
      __t40.value = __t42;
    }

    strtemp type_getValue_5(type _this_) {

      // return value
      type __t43 = _this_;
      strtemp __t44 = __t43.value;
      return __t44;
    }

    /// METHODS: strtemp
    void strtemp_init_2(strtemp _this_, int x) {

      // this.x = x
      strtemp __t45 = _this_;
      int __t46 = __t45.x;
      int __t47 = x;
      __t45.x = __t47;
    }

    int strtemp_getX_3(strtemp _this_) {

      // return x
      strtemp __t48 = _this_;
      int __t49 = __t48.x;
      return __t49;
    }

    /// METHODS: tree
    void tree_init_0(tree _this_, int value) {

      // this.value = value
      tree __t50 = _this_;
      int __t51 = __t50.value;
      int __t52 = value;
      __t50.value = __t52;
    }

    void tree_init_1(tree _this_, tree lhs, tree rhs) {

      // this.lhs = lhs
      tree __t53 = _this_;
      tree __t54 = __t53.lhs;
      tree __t55 = lhs;
      __t53.lhs = __t55;

      // this.rhs = rhs
      tree __t56 = _this_;
      tree __t57 = __t56.rhs;
      tree __t58 = rhs;
      __t56.rhs = __t58;
    }
  }

}
