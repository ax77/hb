package _temp;

import org.junit.Test;

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
    void main_class_init_8(final main_class __this) {
    }

    void main_class_deinit_9(final main_class __this) {
    }

    void main() {

      /////// tree tree = new tree(new tree(1), new tree(2));
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
      int t24 = 1;
      strtemp t25 = new strtemp();
      strtemp_init_2(t25, t24);
      strtemp x1 = null;
      x1 = strtemp_opAssign_16(x1, t25);

      /////// type x2 = new type(x1);
      strtemp t26 = null;
      t26 = strtemp_opAssign_16(t26, x1);
      type t27 = new type();
      type_init_4(t27, t26);
      type x2 = null;
      x2 = type_opAssign_14(x2, t27);

      /////// token x3 = new token(x2);
      type t28 = null;
      t28 = type_opAssign_14(t28, x2);
      token t29 = new token();
      token_init_6(t29, t28);
      token x3 = null;
      x3 = token_opAssign_12(x3, t29);

      /////// token tok1 = new token(new type(new strtemp(1)));
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
      token t34 = null;
      t34 = token_opAssign_12(t34, tok1);
      type t35 = t34.type;
      strtemp t36 = t35.value;
      int t37 = t36.x;
      int res = t37;

      /////// tok1.type.value.x = 1024
      token t38 = null;
      t38 = token_opAssign_12(t38, tok1);
      type t39 = t38.type;
      strtemp t40 = t39.value;
      int t41 = t40.x;
      int t42 = 1024;
      t40.x = t42;

      /////// int a = 0;
      int t43 = 0;
      int a = t43;

      /////// int b = 1;
      int t44 = 1;
      int b = t44;

      /////// int c = 2;
      int t45 = 2;
      int c = t45;

      /////// int e = a + b + c;
      int t46 = a;
      int t47 = b;
      int t48 = t46 + t47;
      int t49 = c;
      int t50 = t48 + t49;
      int e = t50;

      /////// int f = x1.getX();
      strtemp t51 = null;
      t51 = strtemp_opAssign_16(t51, x1);
      int t52 = strtemp_getX_3(t51);
      int f = t52;

      /////// int g = x2.getValue().getX();
      type t53 = null;
      t53 = type_opAssign_14(t53, x2);
      strtemp t54 = type_getValue_5(t53);
      int t55 = strtemp_getX_3(t54);
      int g = t55;

      /////// a == 0
      int t69 = a;
      int t70 = 0;
      boolean t71 = t69 == t70;
      if (t71) {

        /////// b = b + 1
        int t56 = b;
        int t57 = b;
        int t58 = 1;
        int t59 = t57 + t58;
        b = t59;
      } else {

        /////// b == 1
        int t66 = b;
        int t67 = 1;
        boolean t68 = t66 == t67;
        if (t68) {

          /////// c = c + 2
          int t60 = c;
          int t61 = c;
          int t62 = 2;
          int t63 = t61 + t62;
          c = t63;
        } else {

          /////// e = 32
          int t64 = e;
          int t65 = 32;
          e = t65;
        }
      }
      {

        /////// int j = 0;
        int t90 = 0;
        int j = t90;
        for (;;) {

          /////// !j < 8
          int t98 = j;
          int t99 = 8;
          boolean t100 = t98 < t99;
          boolean t101 = !t100;
          if (t101) {
            break;
          }

          /////// e = e + 2
          int t72 = e;
          int t73 = e;
          int t74 = 2;
          int t75 = t73 + t74;
          e = t75;

          /////// a = a - 1
          int t76 = a;
          int t77 = a;
          int t78 = 1;
          int t79 = t77 - t78;
          a = t79;

          /////// a == 1
          int t80 = a;
          int t81 = 1;
          boolean t82 = t80 == t81;
          if (t82) {
            break;
          }

          /////// j == 2
          int t87 = j;
          int t88 = 2;
          boolean t89 = t87 == t88;
          if (t89) {
            {

              /////// j = j + 1
              int t83 = j;
              int t84 = j;
              int t85 = 1;
              int t86 = t84 + t85;
              j = t86;
              continue;
            }
          }
          {

            /////// j = j + 1
            int t94 = j;
            int t95 = j;
            int t96 = 1;
            int t97 = t95 + t96;
            j = t97;
          }
        }
      }
    }

    main_class main_class_opAssign_10(main_class lvalue, main_class rvalue) {
      return rvalue;
    }

    /// METHODS: token
    void token_init_6(final token __this, type type) {

      /////// this.type = type
      token t104 = null;
      t104 = token_opAssign_12(t104, __this);
      type t105 = t104.type;
      type t106 = null;
      t106 = type_opAssign_14(t106, type);
      t104.type = t106;
    }

    void token_deinit_11(final token __this) {
    }

    token token_opAssign_12(token lvalue, token rvalue) {
      return rvalue;
    }

    /// METHODS: type
    void type_init_4(final type __this, strtemp value) {

      /////// this.value = value
      type t110 = null;
      t110 = type_opAssign_14(t110, __this);
      strtemp t111 = t110.value;
      strtemp t112 = null;
      t112 = strtemp_opAssign_16(t112, value);
      t110.value = t112;
    }

    void type_deinit_13(final type __this) {
    }

    strtemp type_getValue_5(final type __this) {

      /////// return value
      type t107 = null;
      t107 = type_opAssign_14(t107, __this);
      strtemp t108 = t107.value;
      return t108;
    }

    type type_opAssign_14(type lvalue, type rvalue) {
      return rvalue;
    }

    /// METHODS: strtemp
    void strtemp_init_2(final strtemp __this, int x) {

      /////// this.x = x
      strtemp t116 = null;
      t116 = strtemp_opAssign_16(t116, __this);
      int t117 = t116.x;
      int t118 = x;
      t116.x = t118;
    }

    void strtemp_deinit_15(final strtemp __this) {
    }

    int strtemp_getX_3(final strtemp __this) {

      /////// return x
      strtemp t113 = null;
      t113 = strtemp_opAssign_16(t113, __this);
      int t114 = t113.x;
      return t114;
    }

    strtemp strtemp_opAssign_16(strtemp lvalue, strtemp rvalue) {
      return rvalue;
    }

    /// METHODS: tree
    void tree_init_0(final tree __this, int value) {

      /////// this.value = value
      tree t120 = null;
      t120 = tree_opAssign_18(t120, __this);
      int t121 = t120.value;
      int t122 = value;
      t120.value = t122;
    }

    void tree_init_1(final tree __this, tree lhs, tree rhs) {

      /////// this.lhs = lhs
      tree t123 = null;
      t123 = tree_opAssign_18(t123, __this);
      tree t124 = t123.lhs;
      tree t125 = null;
      t125 = tree_opAssign_18(t125, lhs);
      t123.lhs = t125;

      /////// this.rhs = rhs
      tree t126 = null;
      t126 = tree_opAssign_18(t126, __this);
      tree t127 = t126.rhs;
      tree t128 = null;
      t128 = tree_opAssign_18(t128, rhs);
      t126.rhs = t128;
    }

    void tree_deinit_17(final tree __this) {
    }

    tree tree_opAssign_18(tree lvalue, tree rvalue) {
      return rvalue;
    }
  }

}
