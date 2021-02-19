package _temp;

public class TestAux2 {
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
    void main_class_init_8(main_class __this) {
    }

    void main_class_deinit_9(main_class __this) {
    }

    void main() {

      /////// tree tree = new tree(new tree(1), new tree(2));

      // [M:tree, L:t23, L:t22, L:t20, ]
      int t19 = 1;
      tree t20 = new tree();
      tree_init_0(t20, t19);
      int t21 = 2;
      tree t22 = new tree();
      tree_init_0(t22, t21);
      tree t23 = new tree();
      tree_init_1(t23, t20, t22);
      tree tree = null;
      tree = tree_opAssign_18(tree, t23);

      /////// strtemp x1 = new strtemp(1);

      // [M:x1, L:t25, ]
      int t24 = 1;
      strtemp t25 = new strtemp();
      strtemp_init_2(t25, t24);
      strtemp x1 = null;
      x1 = strtemp_opAssign_16(x1, t25);

      /////// type x2 = new type(x1);

      // [M:x2, L:t27, M:x1, L:t26, ]
      strtemp t26 = null;
      t26 = strtemp_opAssign_16(t26, x1);
      type t27 = new type();
      type_init_4(t27, t26);
      type x2 = null;
      x2 = type_opAssign_14(x2, t27);

      /////// token x3 = new token(x2);

      // [M:x3, L:t29, M:x2, L:t28, ]
      type t28 = null;
      t28 = type_opAssign_14(t28, x2);
      token t29 = new token();
      token_init_6(t29, t28);
      token x3 = null;
      x3 = token_opAssign_12(x3, t29);

      /////// token tok1 = new token(new type(new strtemp(1)));

      // [M:tok1, L:t33, L:t32, L:t31, ]
      int t30 = 1;
      strtemp t31 = new strtemp();
      strtemp_init_2(t31, t30);
      type t32 = new type();
      type_init_4(t32, t31);
      token t33 = new token();
      token_init_6(t33, t32);
      token tok1 = null;
      tok1 = token_opAssign_12(tok1, t33);

      /////// int res = tok1.type.value.x;

      // [L:t36, L:t35, M:tok1, L:t34, ]
      token t34 = null;
      t34 = token_opAssign_12(t34, tok1);
      type t35 = t34.type;
      strtemp t36 = t35.value;
      int t37 = t36.x;
      int res = t37;

      /////// tok1.type.value.x = 1024

      // [L:t40, L:t39, M:tok1, L:t38, ]
      token t38 = null;
      t38 = token_opAssign_12(t38, tok1);
      type t39 = t38.type;
      strtemp t40 = t39.value;
      int t41 = t40.x;
      int t42 = 1024;
      t40.x = t42;

      /////// int a = 0;

      // []
      int t43 = 0;
      int a = t43;

      /////// int b = 1;

      // []
      int t44 = 1;
      int b = t44;

      /////// int c = 2;

      // []
      int t45 = 2;
      int c = t45;

      /////// int e = a + b + c;

      // []
      int t46 = a;
      int t47 = b;
      int t48 = t46 + t47;
      int t49 = c;
      int t50 = t48 + t49;
      int e = t50;

      /////// int f = x1.getX();

      // [M:x1, L:t51, ]
      strtemp t51 = null;
      t51 = strtemp_opAssign_16(t51, x1);
      int t52 = strtemp_getX_3(t51);
      int f = t52;

      /////// int g = x2.getValue().getX();

      // [L:t54, M:x2, L:t53, ]
      type t53 = null;
      t53 = type_opAssign_14(t53, x2);
      strtemp t54 = type_getValue_5(t53);
      int t55 = strtemp_getX_3(t54);
      int g = t55;
    }

    main_class main_class_opAssign_10(main_class lvalue, main_class rvalue) {
      return rvalue;
    }

    /// METHODS: token
    void token_init_6(token __this, type type) {

      /////// this.type = type

      // [L:t58, L:t57, L:t56, ]
      token t56 = null;
      t56 = token_opAssign_12(t56, __this);
      type t57 = t56.type;
      type t58 = null;
      t58 = type_opAssign_14(t58, type);
      t56.type = t58;
    }

    void token_deinit_11(token __this) {
    }

    token token_opAssign_12(token lvalue, token rvalue) {
      return rvalue;
    }

    /// METHODS: type
    void type_init_4(type __this, strtemp value) {

      /////// this.value = value

      // [L:t61, L:t60, L:t59, ]
      type t59 = null;
      t59 = type_opAssign_14(t59, __this);
      strtemp t60 = t59.value;
      strtemp t61 = null;
      t61 = strtemp_opAssign_16(t61, value);
      t59.value = t61;
    }

    void type_deinit_13(type __this) {
    }

    strtemp type_getValue_5(type __this) {

      // return value

      // [L:t63, L:t62, ]
      type t62 = null;
      t62 = type_opAssign_14(t62, __this);
      strtemp t63 = t62.value;
      return t63;
    }

    type type_opAssign_14(type lvalue, type rvalue) {
      return rvalue;
    }

    /// METHODS: strtemp
    void strtemp_init_2(strtemp __this, int x) {

      /////// this.x = x

      // [L:t64, ]
      strtemp t64 = null;
      t64 = strtemp_opAssign_16(t64, __this);
      int t65 = t64.x;
      int t66 = x;
      t64.x = t66;
    }

    void strtemp_deinit_15(strtemp __this) {
    }

    int strtemp_getX_3(strtemp __this) {

      // return x

      // [L:t67, ]
      strtemp t67 = null;
      t67 = strtemp_opAssign_16(t67, __this);
      int t68 = t67.x;
      return t68;
    }

    strtemp strtemp_opAssign_16(strtemp lvalue, strtemp rvalue) {
      return rvalue;
    }

    /// METHODS: tree
    void tree_init_0(tree __this, int value) {

      /////// this.value = value

      // [L:t69, ]
      tree t69 = null;
      t69 = tree_opAssign_18(t69, __this);
      int t70 = t69.value;
      int t71 = value;
      t69.value = t71;
    }

    void tree_init_1(tree __this, tree lhs, tree rhs) {

      /////// this.lhs = lhs

      // [L:t74, L:t73, L:t72, ]
      tree t72 = null;
      t72 = tree_opAssign_18(t72, __this);
      tree t73 = t72.lhs;
      tree t74 = null;
      t74 = tree_opAssign_18(t74, lhs);
      t72.lhs = t74;

      /////// this.rhs = rhs

      // [L:t77, L:t76, L:t75, ]
      tree t75 = null;
      t75 = tree_opAssign_18(t75, __this);
      tree t76 = t75.rhs;
      tree t77 = null;
      t77 = tree_opAssign_18(t77, rhs);
      t75.rhs = t77;
    }

    void tree_deinit_17(tree __this) {
    }

    tree tree_opAssign_18(tree lvalue, tree rvalue) {
      return rvalue;
    }
  }

}
