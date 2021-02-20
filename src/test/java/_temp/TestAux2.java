package _temp;

public class TestAux2 {
  class main_class {
    main_class() {

    }

    void main() {
      // int t19 = 1
      // tree t20 = new tree()
      // tree_init_0(t20, t19)
      // int t21 = 2
      // tree t22 = new tree()
      // tree_init_0(t22, t21)
      // tree t23 = new tree()
      // tree_init_1(t23, t20, t22)
      // tree tree = null
      // tree = tree_opAssign_18(tree, t23)
      tree tree = new tree(new tree(1), new tree(2));
      // int t24 = 1
      // strtemp t25 = new strtemp()
      // strtemp_init_2(t25, t24)
      // strtemp x1 = null
      // x1 = strtemp_opAssign_16(x1, t25)
      strtemp x1 = new strtemp(1);
      // strtemp t26 = null
      // t26 = strtemp_opAssign_16(t26, x1)
      // type t27 = new type()
      // type_init_4(t27, t26)
      // type x2 = null
      // x2 = type_opAssign_14(x2, t27)
      type x2 = new type(x1);
      // type t28 = null
      // t28 = type_opAssign_14(t28, x2)
      // token t29 = new token()
      // token_init_6(t29, t28)
      // token x3 = null
      // x3 = token_opAssign_12(x3, t29)
      token x3 = new token(x2);
      // int t30 = 1
      // strtemp t31 = new strtemp()
      // strtemp_init_2(t31, t30)
      // type t32 = new type()
      // type_init_4(t32, t31)
      // token t33 = new token()
      // token_init_6(t33, t32)
      // token tok1 = null
      // tok1 = token_opAssign_12(tok1, t33)
      token tok1 = new token(new type(new strtemp(1)));
      // token t34 = null
      // t34 = token_opAssign_12(t34, tok1)
      // type t35 = t34.type
      // strtemp t36 = t35.value
      // int t37 = t36.x
      // int res = t37
      int res = tok1.type.value.x;
      
      // token t38 = null
      // t38 = token_opAssign_12(t38, tok1)
      // type t39 = t38.type
      // strtemp t40 = t39.value
      // int t41 = t40.x
      // int t42 = 1024
      // t40.x = t42
      tok1.type.value.x = 1024;
      
      // int t43 = 0
      // int a = t43
      int a = 0;
      // int t44 = 1
      // int b = t44
      int b = 1;
      // int t45 = 2
      // int c = t45
      int c = 2;
      // int t46 = a
      // int t47 = b
      // int t48 = t46 + t47
      // int t49 = c
      // int t50 = t48 + t49
      // int e = t50
      int e = a + b + c;
      // strtemp t51 = null
      // t51 = strtemp_opAssign_16(t51, x1)
      // int t52 = strtemp_getX_3(t51)
      // int f = t52
      int f = x1.getX();
      // type t53 = null
      // t53 = type_opAssign_14(t53, x2)
      // strtemp t54 = type_getValue_5(t53)
      // int t55 = strtemp_getX_3(t54)
      // int g = t55
      int g = x2.getValue().getX();

    }

    main_class opAssign(main_class lvalue, main_class rvalue) {
      return rvalue;

    }

  }

  class token {
    type type;

    token(type type) {
      // token t58 = null
      // t58 = token_opAssign_12(t58, __this)
      // type t59 = t58.type
      // type t60 = null
      // t60 = type_opAssign_14(t60, type)
      // t58.type = t60
      this.type = type;

    }

    token opAssign(token lvalue, token rvalue) {
      return rvalue;

    }

  }

  class type {
    strtemp value;

    type(strtemp value) {
      // type t64 = null
      // t64 = type_opAssign_14(t64, __this)
      // strtemp t65 = t64.value
      // strtemp t66 = null
      // t66 = strtemp_opAssign_16(t66, value)
      // t64.value = t66
      this.value = value;

    }

    strtemp getValue() {
      return value;

    }

    type opAssign(type lvalue, type rvalue) {
      return rvalue;

    }

  }

  class strtemp {
    int x;

    strtemp(int x) {
      // strtemp t70 = null
      // t70 = strtemp_opAssign_16(t70, __this)
      // int t71 = t70.x
      // int t72 = x
      // t70.x = t72
      this.x = x;

    }

    int getX() {
      return x;

    }

    strtemp opAssign(strtemp lvalue, strtemp rvalue) {
      return rvalue;

    }

  }

  class tree {
    tree lhs;
    tree rhs;
    int value;

    tree(int value) {
      // tree t74 = null
      // t74 = tree_opAssign_18(t74, __this)
      // int t75 = t74.value
      // int t76 = value
      // t74.value = t76
      this.value = value;

    }

    tree(tree lhs, tree rhs) {
      // tree t77 = null
      // t77 = tree_opAssign_18(t77, __this)
      // tree t78 = t77.lhs
      // tree t79 = null
      // t79 = tree_opAssign_18(t79, lhs)
      // t77.lhs = t79
      this.lhs = lhs;
      // tree t80 = null
      // t80 = tree_opAssign_18(t80, __this)
      // tree t81 = t80.rhs
      // tree t82 = null
      // t82 = tree_opAssign_18(t82, rhs)
      // t80.rhs = t82
      this.rhs = rhs;

    }

    tree opAssign(tree lvalue, tree rvalue) {
      return rvalue;

    }

  }

}
