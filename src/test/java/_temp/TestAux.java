package _temp;

import org.junit.Test;

public class TestAux {

  class test {
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

  /// METHODS: test
  void test_fn_5(test _this_) {

    // tree tree = new tree(new tree(1), new tree(2));
    tree tree = new tree();
    tree _t_0 = new tree();
    int _t_1 = 1;
    tree_init_0(_t_0, _t_1);
    tree _t_2 = new tree();
    int _t_3 = 2;
    tree_init_0(_t_2, _t_3);
    tree_init_1(tree, _t_0, _t_2);

    // strtemp x1 = new strtemp(1);
    strtemp x1 = new strtemp();
    int _t_4 = 1;
    strtemp_init_2(x1, _t_4);

    // type x2 = new type(x1);
    type x2 = new type();
    strtemp _t_5 = x1;
    type_init_3(x2, _t_5);

    // token x3 = new token(x2);
    token x3 = new token();
    type _t_6 = x2;
    token_init_4(x3, _t_6);

    // token tok1 = new token(new type(new strtemp(1)));
    token tok1 = new token();
    type _t_7 = new type();
    strtemp _t_8 = new strtemp();
    int _t_9 = 1;
    strtemp_init_2(_t_8, _t_9);
    type_init_3(_t_7, _t_8);
    token_init_4(tok1, _t_7);

    // int res = tok1.type.value.x;
    token _t_10 = tok1;
    type _t_11 = _t_10.type;
    strtemp _t_12 = _t_11.value;
    int _t_13 = _t_12.x;
    int res = _t_13;

    // tok1.type.value.x = 1024
    token _t_14 = tok1;
    type _t_15 = _t_14.type;
    strtemp _t_16 = _t_15.value;
    int _t_17 = _t_16.x;
    int _t_18 = 1024;
    _t_16.x = _t_18;

    // int a = 0;
    int _t_20 = 0;
    int a = _t_20;

    // int b = 1;
    int _t_21 = 1;
    int b = _t_21;

    // int c = 2;
    int _t_22 = 2;
    int c = _t_22;

    // int e = a + b + c;
    int _t_23 = a;
    int _t_24 = b;
    int _t_25 = _t_23 + _t_24;
    int _t_26 = c;
    int _t_27 = _t_25 + _t_26;
    int e = _t_27;
  }
  // vars: [M:tree, M:x1, M:x2, M:x3, M:tok1, M:res, M:a, M:b, M:c, M:e]

  /// METHODS: token
  void token_init_4(token _this_, type type) {

    // this.type = type
    token _t_28 = _this_;
    type _t_29 = _t_28.type;
    type _t_30 = type;
    _t_28.type = _t_30;
  }

  /// METHODS: type
  void type_init_3(type _this_, strtemp value) {

    // this.value = value
    type _t_32 = _this_;
    strtemp _t_33 = _t_32.value;
    strtemp _t_34 = value;
    _t_32.value = _t_34;
  }

  /// METHODS: strtemp
  void strtemp_init_2(strtemp _this_, int x) {

    // this.x = x
    strtemp _t_36 = _this_;
    int _t_37 = _t_36.x;
    int _t_38 = x;
    _t_36.x = _t_38;
  }

  /// METHODS: tree
  void tree_init_0(tree _this_, int value) {

    // this.value = value
    tree _t_40 = _this_;
    int _t_41 = _t_40.value;
    int _t_42 = value;
    _t_40.value = _t_42;
  }

  void tree_init_1(tree _this_, tree lhs, tree rhs) {

    // this.lhs = lhs
    tree _t_44 = _this_;
    tree _t_45 = _t_44.lhs;
    tree _t_46 = lhs;
    _t_44.lhs = _t_46;

    // this.rhs = rhs
    tree _t_48 = _this_;
    tree _t_49 = _t_48.rhs;
    tree _t_50 = rhs;
    _t_48.rhs = _t_50;
  }

}
