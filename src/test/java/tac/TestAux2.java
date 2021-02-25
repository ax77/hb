package tac;

public class TestAux2 {
  class str {
    int value;
  }

  class main_class {
    void main_class_init_31(final main_class __this) {
    }

    void main_class_main_29(final main_class __this) {
      str t40 = new str();
      str_init_1(t40);
      str s1 = null;
      s1 = str_opAssign_39(s1, t40);
      int t41 = 1;
      str t42 = new str();
      str_init_3(t42, t41);
      str s2 = null;
      s2 = str_opAssign_39(s2, t42);
      str t43 = null;
      t43 = str_opAssign_39(t43, s1);
      boolean t44 = str_isEmpty_6(t43);
      if (t44) {
        return;
      }
      for (;;) {
        str t45 = null;
        t45 = str_opAssign_39(t45, s1);
        boolean t46 = str_isEmpty_6(t45);
        boolean t47 = !t46;
        boolean t48 = !t47;
        if (t48) {
          str_deinit_37(t45);
          break;
        }
        str t49 = null;
        t49 = str_opAssign_39(t49, s2);
        str t50 = null;
        t50 = str_opAssign_39(t50, s1);
        int t51 = str_remove_12(t50);
        str_append_9(t49, t51);
        str_deinit_37(t50);
        str_deinit_37(t49);
        str_deinit_37(t45);
      }
      {
        int t52 = 0;
        int i = t52;
        for (;;) {
          int t53 = i;
          str t54 = null;
          t54 = str_opAssign_39(t54, s2);
          int t55 = str_length_15(t54);
          boolean t56 = t53 < t55;
          boolean t57 = !t56;
          if (t57) {
            str_deinit_37(t54);
            break;
          }
          str t62 = null;
          t62 = str_opAssign_39(t62, s1);
          str t63 = null;
          t63 = str_opAssign_39(t63, s2);
          int t64 = i;
          int t65 = str_get_18(t63, t64);
          str_append_9(t62, t65);
          int t66 = i;
          int t67 = 2;
          boolean t68 = t66 == t67;
          if (t68) {
            int t58 = i;
            int t59 = i;
            int t60 = 1;
            int t61 = t59 + t60;
            i = t61;
            str_deinit_37(t63);
            str_deinit_37(t62);
            str_deinit_37(t54);
            continue;
          }
          str t69 = null;
          t69 = str_opAssign_39(t69, s1);
          int t70 = str_length_15(t69);
          int t71 = 2;
          boolean t72 = t70 == t71;
          if (t72) {
            str_deinit_37(t69);
            str_deinit_37(t63);
            str_deinit_37(t62);
            str_deinit_37(t54);
            break;
          }
          int t73 = i;
          str t74 = new str();
          str_init_3(t74, t73);
          str s3 = null;
          s3 = str_opAssign_39(s3, t74);
          int t58 = i;
          int t59 = i;
          int t60 = 1;
          int t61 = t59 + t60;
          i = t61;
          str_deinit_37(s3);
          str_deinit_37(t74);
          str_deinit_37(t69);
          str_deinit_37(t63);
          str_deinit_37(t62);
          str_deinit_37(t54);
        }
      }
      {
        int t75 = 32;
        str t76 = new str();
        str_init_3(t76, t75);
        str s5 = null;
        s5 = str_opAssign_39(s5, t76);
        for (;;) {
          str t77 = null;
          t77 = str_opAssign_39(t77, s5);
          boolean t78 = str_isEmpty_6(t77);
          boolean t79 = !t78;
          boolean t80 = !t79;
          if (t80) {
            str_deinit_37(t77);
            break;
          }
          str t81 = new str();
          str_init_1(t81);
          str s6tmp = null;
          s6tmp = str_opAssign_39(s6tmp, t81);
          str t82 = null;
          t82 = str_opAssign_39(t82, s6tmp);
          int t83 = 1;
          str_append_9(t82, t83);
          str_deinit_37(t82);
          str_deinit_37(s6tmp);
          str_deinit_37(t81);
          str_deinit_37(t77);
        }
        str_deinit_37(s5);
        str_deinit_37(t76);
      }
      str_deinit_37(t43);
      str_deinit_37(s2);
      str_deinit_37(t42);
      str_deinit_37(s1);
      str_deinit_37(t40);
    }

    main_class main_class_opAssign_35(final main_class __this, main_class rvalue) {
      main_class t84 = rvalue;
      return t84;
    }

    void main_class_deinit_33(final main_class __this) {
    }

    void str_init_1(final str __this) {
    }

    void str_init_3(final str __this, int value) {
      str t85 = null;
      t85 = str_opAssign_39(t85, __this);
      int t86 = t85.value;
      int t87 = value;
      t85.value = t87;
      str_deinit_37(t85);
    }

    boolean str_isEmpty_6(final str __this) {
      int t88 = 0;
      int t89 = 0;
      boolean t90 = t88 == t89;
      return t90;
    }

    void str_append_9(final str __this, int i) {
    }

    int str_remove_12(final str __this) {
      int t91 = 0;
      return t91;
    }

    int str_length_15(final str __this) {
      int t92 = 0;
      return t92;
    }

    int str_get_18(final str __this, int at) {
      int t93 = 0;
      return t93;
    }

    str str_opAssign_39(final str __this, str rvalue) {
      str t94 = rvalue;
      return t94;
    }

    void str_deinit_37(final str __this) {
    }
  }

}
