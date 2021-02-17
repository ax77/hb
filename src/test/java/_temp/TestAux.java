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
      int __t8 = 1;
      tree __t9 = new tree();
      tree_init_0(__t9, __t8);
      int __t10 = 2;
      tree __t11 = new tree();
      tree_init_0(__t11, __t10);
      tree __t12 = new tree();
      tree_init_1(__t12, __t9, __t11);
      tree tree = __t12;

      // strtemp x1 = new strtemp(1);
      int __t13 = 1;
      strtemp __t14 = new strtemp();
      strtemp_init_2(__t14, __t13);
      strtemp x1 = __t14;

      // type x2 = new type(x1);
      strtemp __t15 = x1;
      type __t16 = new type();
      type_init_4(__t16, __t15);
      type x2 = __t16;

      // token x3 = new token(x2);
      type __t17 = x2;
      token __t18 = new token();
      token_init_6(__t18, __t17);
      token x3 = __t18;

      // token tok1 = new token(new type(new strtemp(1)));
      int __t19 = 1;
      strtemp __t20 = new strtemp();
      strtemp_init_2(__t20, __t19);
      type __t21 = new type();
      type_init_4(__t21, __t20);
      token __t22 = new token();
      token_init_6(__t22, __t21);
      token tok1 = __t22;

      // int res = tok1.type.value.x;
      token __t23 = tok1;
      type __t24 = __t23.type;
      strtemp __t25 = __t24.value;
      int __t26 = __t25.x;
      int res = __t26;

      // tok1.type.value.x = 1024
      token __t27 = tok1;
      type __t28 = __t27.type;
      strtemp __t29 = __t28.value;
      int __t30 = __t29.x;
      int __t31 = 1024;
      __t29.x = __t31;

      // int a = 0;
      int __t32 = 0;
      int a = __t32;

      // int b = 1;
      int __t33 = 1;
      int b = __t33;

      // int c = 2;
      int __t34 = 2;
      int c = __t34;

      // int e = a + b + c;
      int __t35 = a;
      int __t36 = b;
      int __t37 = __t35 + __t36;
      int __t38 = c;
      int __t39 = __t37 + __t38;
      int e = __t39;

      // int f = x1.getX();
      strtemp __t40 = x1;
      int __t41 = strtemp_getX_3(__t40);
      int f = __t41;

      // int g = x2.getValue().getX();
      type __t42 = x2;
      strtemp __t43 = type_getValue_5(__t42);
      int __t44 = strtemp_getX_3(__t43);
      int g = __t44;
    }

    /// METHODS: token
    void token_init_6(token _this_, type type) {

      // this.type = type
      token __t45 = _this_;
      type __t46 = __t45.type;
      type __t47 = type;
      __t45.type = __t47;
    }

    /// METHODS: type
    void type_init_4(type _this_, strtemp value) {

      // this.value = value
      type __t48 = _this_;
      strtemp __t49 = __t48.value;
      strtemp __t50 = value;
      __t48.value = __t50;
    }

    strtemp type_getValue_5(type _this_) {

      // return value
      type __t51 = _this_;
      strtemp __t52 = __t51.value;
      return __t52;
    }

    /// METHODS: strtemp
    void strtemp_init_2(strtemp _this_, int x) {

      // this.x = x
      strtemp __t53 = _this_;
      int __t54 = __t53.x;
      int __t55 = x;
      __t53.x = __t55;
    }

    int strtemp_getX_3(strtemp _this_) {

      // return x
      strtemp __t56 = _this_;
      int __t57 = __t56.x;
      return __t57;
    }

    /// METHODS: tree
    void tree_init_0(tree _this_, int value) {

      // this.value = value
      tree __t58 = _this_;
      int __t59 = __t58.value;
      int __t60 = value;
      __t58.value = __t60;
    }

    void tree_init_1(tree _this_, tree lhs, tree rhs) {

      // this.lhs = lhs
      tree __t61 = _this_;
      tree __t62 = __t61.lhs;
      tree __t63 = lhs;
      __t61.lhs = __t63;

      // this.rhs = rhs
      tree __t64 = _this_;
      tree __t65 = __t64.rhs;
      tree __t66 = rhs;
      __t64.rhs = __t66;
    }
  }

}
