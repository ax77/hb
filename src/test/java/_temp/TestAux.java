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
  }

  /// METHODS: test
  void test_fn_5(test _this_) {
    // tree tree = new tree(new tree(), new tree());
    tree tree = new tree();
    tree _t_0 = new tree();
    tree_init_0(_t_0);
    tree _t_1 = new tree();
    tree_init_0(_t_1);
    tree_init_1(tree, _t_0, _t_1);
    // strtemp x1 = new strtemp(1);
    strtemp x1 = new strtemp();
    int _t_2 = 1;
    strtemp_init_2(x1, _t_2);
    // type x2 = new type(x1);
    type x2 = new type();
    strtemp _t_3 = x1;
    type_init_3(x2, _t_3);
    // token x3 = new token(x2);
    token x3 = new token();
    type _t_4 = x2;
    token_init_4(x3, _t_4);
    // token tok1 = new token(new type(new strtemp(1)));
    token tok1 = new token();
    type _t_5 = new type();
    strtemp _t_6 = new strtemp();
    int _t_7 = 1;
    strtemp_init_2(_t_6, _t_7);
    type_init_3(_t_5, _t_6);
    token_init_4(tok1, _t_5);
  } // vars: [M:tree, M:x1, M:x2, M:x3, M:tok1]
  /// METHODS: token

  void token_init_4(token _this_, type type) {
    // this.type = type
    token _t_8 = _this_;
    type _t_9 = _t_8.type;
    type _t_10 = type;
    _t_8.type = _t_10;
  }

  /// METHODS: type
  void type_init_3(type _this_, strtemp value) {
    // this.value = value
    type _t_12 = _this_;
    strtemp _t_13 = _t_12.value;
    strtemp _t_14 = value;
    _t_12.value = _t_14;
  }

  /// METHODS: strtemp
  void strtemp_init_2(strtemp _this_, int x) {
    // this.x = x
    strtemp _t_16 = _this_;
    int _t_17 = _t_16.x;
    int _t_18 = x;
    _t_16.x = _t_18;
  }

  /// METHODS: tree
  void tree_init_0(tree _this_) {
  }

  void tree_init_1(tree _this_, tree lhs, tree rhs) {
    // this.lhs = lhs
    tree _t_20 = _this_;
    tree _t_21 = _t_20.lhs;
    tree _t_22 = lhs;
    _t_20.lhs = _t_22;
    // this.rhs = rhs
    tree _t_24 = _this_;
    tree _t_25 = _t_24.rhs;
    tree _t_26 = rhs;
    _t_24.rhs = _t_26;
  }

}
