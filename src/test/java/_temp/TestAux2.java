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
    void main_class_init_8(main_class _this_) {
    }

    void main_class_deinit_9(main_class _this_) {
    }

    void main() {

      /////// tree tree = new tree(new tree(1), new tree(2));

      // AssignVarNum
      int __t19 = 1;

      // AssignVarAllocObject
      tree __t20 = new tree();

      // FlatCallConstructor
      tree_init_0(__t20, __t19);

      // AssignVarNum
      int __t21 = 2;

      // AssignVarAllocObject
      tree __t22 = new tree();

      // FlatCallConstructor
      tree_init_0(__t22, __t21);

      // AssignVarAllocObject
      tree __t23 = new tree();

      // FlatCallConstructor
      tree_init_1(__t23, __t20, __t22);
      tree tree = __t23;

      /////// strtemp x1 = new strtemp(1);

      // AssignVarNum
      int __t24 = 1;

      // AssignVarAllocObject
      strtemp __t25 = new strtemp();

      // FlatCallConstructor
      strtemp_init_2(__t25, __t24);
      strtemp x1 = __t25;

      /////// type x2 = new type(x1);

      // AssignVarNull
      strtemp __t26 = null;

      // StoreVarVarAssignOp
      __t26 = strtemp_opAssign_16(__t26, x1);

      // AssignVarAllocObject
      type __t27 = new type();

      // FlatCallConstructor
      type_init_4(__t27, __t26);
      type x2 = __t27;

      /////// token x3 = new token(x2);

      // AssignVarNull
      type __t28 = null;

      // StoreVarVarAssignOp
      __t28 = type_opAssign_14(__t28, x2);

      // AssignVarAllocObject
      token __t29 = new token();

      // FlatCallConstructor
      token_init_6(__t29, __t28);
      token x3 = __t29;

      /////// token tok1 = new token(new type(new strtemp(1)));

      // AssignVarNum
      int __t30 = 1;

      // AssignVarAllocObject
      strtemp __t31 = new strtemp();

      // FlatCallConstructor
      strtemp_init_2(__t31, __t30);

      // AssignVarAllocObject
      type __t32 = new type();

      // FlatCallConstructor
      type_init_4(__t32, __t31);

      // AssignVarAllocObject
      token __t33 = new token();

      // FlatCallConstructor
      token_init_6(__t33, __t32);
      token tok1 = __t33;

      /////// int res = tok1.type.value.x;

      // AssignVarNull
      token __t34 = null;

      // StoreVarVarAssignOp
      __t34 = token_opAssign_12(__t34, tok1);

      // AssignVarFieldAccess
      type __t35 = __t34.type;

      // AssignVarFieldAccess
      strtemp __t36 = __t35.value;

      // AssignVarFieldAccess
      int __t37 = __t36.x;
      int res = __t37;

      /////// tok1.type.value.x = 1024

      // AssignVarNull
      token __t38 = null;

      // StoreVarVarAssignOp
      __t38 = token_opAssign_12(__t38, tok1);

      // AssignVarFieldAccess
      type __t39 = __t38.type;

      // AssignVarFieldAccess
      strtemp __t40 = __t39.value;

      // AssignVarNum
      int __t42 = 1024;

      // StoreFieldVar
      __t40.x = __t42;

      /////// int a = 0;

      // AssignVarNum
      int __t43 = 0;
      int a = __t43;

      /////// int b = 1;

      // AssignVarNum
      int __t44 = 1;
      int b = __t44;

      /////// int c = 2;

      // AssignVarNum
      int __t45 = 2;
      int c = __t45;

      /////// int e = a + b + c;

      // AssignVarVar
      int __t46 = a;

      // AssignVarVar
      int __t47 = b;

      // AssignVarBinop
      int __t48 = __t46 + __t47;

      // AssignVarVar
      int __t49 = c;

      // AssignVarBinop
      int __t50 = __t48 + __t49;
      int e = __t50;

      /////// int f = x1.getX();

      // AssignVarNull
      strtemp __t51 = null;

      // StoreVarVarAssignOp
      __t51 = strtemp_opAssign_16(__t51, x1);

      // AssignVarFlatCallResult
      int __t52 = strtemp_getX_3(__t51);
      int f = __t52;

      /////// int g = x2.getValue().getX();

      // AssignVarNull
      type __t53 = null;

      // StoreVarVarAssignOp
      __t53 = type_opAssign_14(__t53, x2);

      // AssignVarFlatCallResult
      strtemp __t54 = type_getValue_5(__t53);

      // AssignVarFlatCallResult
      int __t55 = strtemp_getX_3(__t54);
      int g = __t55;
    }

    main_class main_class_opAssign_10(main_class lvalue, main_class rvalue) {
      return rvalue;
    }

    /// METHODS: token
    void token_init_6(token _this_, type type) {

      /////// this.type = type

      // AssignVarNull
      token __t56 = null;

      // StoreVarVarAssignOp
      __t56 = token_opAssign_12(__t56, _this_);

      // AssignVarNull
      type __t58 = null;

      // StoreVarVarAssignOp
      __t58 = type_opAssign_14(__t58, type);

      // StoreFieldVar
      __t56.type = __t58;
    }

    void token_deinit_11(token _this_) {
    }

    token token_opAssign_12(token lvalue, token rvalue) {
      return rvalue;
    }

    /// METHODS: type
    void type_init_4(type _this_, strtemp value) {

      /////// this.value = value

      // AssignVarNull
      type __t59 = null;

      // StoreVarVarAssignOp
      __t59 = type_opAssign_14(__t59, _this_);

      // AssignVarNull
      strtemp __t61 = null;

      // StoreVarVarAssignOp
      __t61 = strtemp_opAssign_16(__t61, value);

      // StoreFieldVar
      __t59.value = __t61;
    }

    void type_deinit_13(type _this_) {
    }

    strtemp type_getValue_5(type _this_) {

      // return value

      // AssignVarNull
      type __t62 = null;

      // StoreVarVarAssignOp
      __t62 = type_opAssign_14(__t62, _this_);

      // AssignVarFieldAccess
      strtemp __t63 = __t62.value;
      return __t63;
    }

    type type_opAssign_14(type lvalue, type rvalue) {
      return rvalue;
    }

    /// METHODS: strtemp
    void strtemp_init_2(strtemp _this_, int x) {

      /////// this.x = x

      // AssignVarNull
      strtemp __t64 = null;

      // StoreVarVarAssignOp
      __t64 = strtemp_opAssign_16(__t64, _this_);

      // AssignVarVar
      int __t66 = x;

      // StoreFieldVar
      __t64.x = __t66;
    }

    void strtemp_deinit_15(strtemp _this_) {
    }

    int strtemp_getX_3(strtemp _this_) {

      // return x

      // AssignVarNull
      strtemp __t67 = null;

      // StoreVarVarAssignOp
      __t67 = strtemp_opAssign_16(__t67, _this_);

      // AssignVarFieldAccess
      int __t68 = __t67.x;
      return __t68;
    }

    strtemp strtemp_opAssign_16(strtemp lvalue, strtemp rvalue) {
      return rvalue;
    }

    /// METHODS: tree
    void tree_init_0(tree _this_, int value) {

      /////// this.value = value

      // AssignVarNull
      tree __t69 = null;

      // StoreVarVarAssignOp
      __t69 = tree_opAssign_18(__t69, _this_);

      // AssignVarVar
      int __t71 = value;

      // StoreFieldVar
      __t69.value = __t71;
    }

    void tree_init_1(tree _this_, tree lhs, tree rhs) {

      /////// this.lhs = lhs

      // AssignVarNull
      tree __t72 = null;

      // StoreVarVarAssignOp
      __t72 = tree_opAssign_18(__t72, _this_);

      // AssignVarNull
      tree __t74 = null;

      // StoreVarVarAssignOp
      __t74 = tree_opAssign_18(__t74, lhs);

      // StoreFieldVar
      __t72.lhs = __t74;

      /////// this.rhs = rhs

      // AssignVarNull
      tree __t75 = null;

      // StoreVarVarAssignOp
      __t75 = tree_opAssign_18(__t75, _this_);

      // AssignVarNull
      tree __t77 = null;

      // StoreVarVarAssignOp
      __t77 = tree_opAssign_18(__t77, rhs);

      // StoreFieldVar
      __t75.rhs = __t77;
    }

    void tree_deinit_17(tree _this_) {
    }

    tree tree_opAssign_18(tree lvalue, tree rvalue) {
      return rvalue;
    }
  }

}
