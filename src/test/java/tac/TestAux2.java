package tac;

public class TestAux2 {
  class str {
    int value;
  }

  class main_class {
    void main_class_init_32(final main_class __this) {
    }

    void main_class_main_30(final main_class __this) {
      str t41 = new str();
      str_init_1(t41);
      str s1 = null;
      s1 = str_opAssign_40(s1, t41);
      int t42 = 1;
      str t43 = new str();
      str_init_3(t43, t42);
      str s2 = null;
      s2 = str_opAssign_40(s2, t43);
      str t44 = null;
      t44 = str_opAssign_40(t44, s1);
      boolean t45 = str_isEmpty_6(t44);
      if (t45) {
        str_deinit_38(t44);
        str_deinit_38(s2);
        str_deinit_38(t43);
        str_deinit_38(s1);
        str_deinit_38(t41);
        return;
      }
      for (;;) {
        str t46 = null;
        t46 = str_opAssign_40(t46, s1);
        boolean t47 = str_isEmpty_6(t46);
        boolean t48 = !t47;
        boolean t49 = !t48;
        if (t49) {
          str_deinit_38(t46);
          break;
        }
        str t50 = null;
        t50 = str_opAssign_40(t50, s2);
        str t51 = null;
        t51 = str_opAssign_40(t51, s1);
        int t52 = str_remove_12(t51);
        str_append_9(t50, t52);
        str_deinit_38(t51);
        str_deinit_38(t50);
        str_deinit_38(t46);
      }
      {
        int t53 = 0;
        int i = t53;
        for (;;) {
          int t54 = i;
          str t55 = null;
          t55 = str_opAssign_40(t55, s2);
          int t56 = str_length_15(t55);
          boolean t57 = t54 < t56;
          boolean t58 = !t57;
          if (t58) {
            str_deinit_38(t55);
            break;
          }
          str t63 = null;
          t63 = str_opAssign_40(t63, s1);
          str t64 = null;
          t64 = str_opAssign_40(t64, s2);
          int t65 = i;
          int t66 = str_get_18(t64, t65);
          str_append_9(t63, t66);
          int t67 = i;
          int t68 = 2;
          boolean t69 = t67 == t68;
          if (t69) {
            int t59 = i;
            int t60 = i;
            int t61 = 1;
            int t62 = t60 + t61;
            i = t62;
            str_deinit_38(t64);
            str_deinit_38(t63);
            str_deinit_38(t55);
            continue;
          }
          str t70 = null;
          t70 = str_opAssign_40(t70, s1);
          int t71 = str_length_15(t70);
          int t72 = 2;
          boolean t73 = t71 == t72;
          if (t73) {
            str_deinit_38(t70);
            str_deinit_38(t64);
            str_deinit_38(t63);
            str_deinit_38(t55);
            break;
          }
          int t74 = i;
          str t75 = new str();
          str_init_3(t75, t74);
          str s3 = null;
          s3 = str_opAssign_40(s3, t75);
          str t76 = null;
          t76 = str_opAssign_40(t76, s3);
          boolean t77 = str_isEmpty_6(t76);
          if (t77) {
            str_deinit_38(t76);
            str_deinit_38(s3);
            str_deinit_38(t75);
            str_deinit_38(t70);
            str_deinit_38(t64);
            str_deinit_38(t63);
            str_deinit_38(t55);
            str_deinit_38(t44);
            str_deinit_38(s2);
            str_deinit_38(t43);
            str_deinit_38(s1);
            str_deinit_38(t41);
            return;
          }
          int t59 = i;
          int t60 = i;
          int t61 = 1;
          int t62 = t60 + t61;
          i = t62;
          str_deinit_38(t76);
          str_deinit_38(s3);
          str_deinit_38(t75);
          str_deinit_38(t70);
          str_deinit_38(t64);
          str_deinit_38(t63);
          str_deinit_38(t55);
        }
      }
      {
        int t78 = 32;
        str t79 = new str();
        str_init_3(t79, t78);
        str s5 = null;
        s5 = str_opAssign_40(s5, t79);
        for (;;) {
          str t80 = null;
          t80 = str_opAssign_40(t80, s5);
          boolean t81 = str_isEmpty_6(t80);
          boolean t82 = !t81;
          boolean t83 = !t82;
          if (t83) {
            str_deinit_38(t80);
            break;
          }
          str t84 = new str();
          str_init_1(t84);
          str s6tmp = null;
          s6tmp = str_opAssign_40(s6tmp, t84);
          str t85 = null;
          t85 = str_opAssign_40(t85, s6tmp);
          int t86 = 1;
          str_append_9(t85, t86);
          str_deinit_38(t85);
          str_deinit_38(s6tmp);
          str_deinit_38(t84);
          str_deinit_38(t80);
        }
        str_deinit_38(s5);
        str_deinit_38(t79);
      }
      str_deinit_38(t44);
      str_deinit_38(s2);
      str_deinit_38(t43);
      str_deinit_38(s1);
      str_deinit_38(t41);
    }

    main_class main_class_opAssign_36(final main_class __this, main_class rvalue) {
      main_class t87 = rvalue;
      return t87;
    }

    void main_class_deinit_34(final main_class __this) {
    }

    void str_init_1(final str __this) {
    }

    void str_init_3(final str __this, int value) {
      str t88 = null;
      t88 = str_opAssign_40(t88, __this);
      int t89 = t88.value;
      int t90 = value;
      t88.value = t90;
      str_deinit_38(t88);
    }

    boolean str_isEmpty_6(final str __this) {
      int t91 = 0;
      int t92 = 0;
      boolean t93 = t91 == t92;
      return t93;
    }

    void str_append_9(final str __this, int i) {
    }

    int str_remove_12(final str __this) {
      int t94 = 0;
      return t94;
    }

    int str_length_15(final str __this) {
      int t95 = 0;
      return t95;
    }

    int str_get_18(final str __this, int at) {
      int t96 = 0;
      return t96;
    }

    str str_opAssign_40(final str __this, str rvalue) {
      str t97 = rvalue;
      return t97;
    }

    void str_deinit_38(final str __this) {
    }
  }

}
