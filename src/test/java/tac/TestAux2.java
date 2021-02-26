package tac;

import org.junit.Test;

public class TestAux2 {
  class type {
    int k;
    str v;
    node n;
  }

  class node {
    node prev;
    node last;
    int value;
  }

  class str {
    int value;
  }

  class main_class {
    void main_class_init_36(final main_class __this) {
    }

    void main_class_main_34(final main_class __this) {
      str t55 = new str();
      str_init_1(t55);
      str s1 = null;
      s1 = str_opAssign_22(s1, t55);
      int t56 = 1;
      str t57 = new str();
      str_init_3(t57, t56);
      str s2 = null;
      s2 = str_opAssign_22(s2, t57);
      str t58 = null;
      t58 = str_opAssign_22(t58, s1);
      boolean t59 = str_isEmpty_6(t58);
      if (t59) {
        str_deinit_54(t58);
        str_deinit_54(s2);
        str_deinit_54(t57);
        str_deinit_54(s1);
        str_deinit_54(t55);
        return;
      }
      for (;;) {
        str t60 = null;
        t60 = str_opAssign_22(t60, s1);
        boolean t61 = str_isEmpty_6(t60);
        boolean t62 = !t61;
        boolean t63 = !t62;
        if (t63) {
          str_deinit_54(t60);
          break;
        }
        str t64 = null;
        t64 = str_opAssign_22(t64, s2);
        str t65 = null;
        t65 = str_opAssign_22(t65, s1);
        int t66 = str_remove_12(t65);
        str_append_9(t64, t66);
        str_deinit_54(t65);
        str_deinit_54(t64);
        str_deinit_54(t60);
      }
      {
        int t67 = 0;
        int i = t67;
        for (;;) {
          int t68 = i;
          str t69 = null;
          t69 = str_opAssign_22(t69, s2);
          int t70 = str_length_15(t69);
          boolean t71 = t68 < t70;
          boolean t72 = !t71;
          if (t72) {
            str_deinit_54(t69);
            break;
          }
          str t77 = null;
          t77 = str_opAssign_22(t77, s1);
          str t78 = null;
          t78 = str_opAssign_22(t78, s2);
          int t79 = i;
          int t80 = str_get_18(t78, t79);
          str_append_9(t77, t80);
          int t81 = i;
          int t82 = 2;
          boolean t83 = t81 == t82;
          if (t83) {
            int t73 = i;
            int t74 = i;
            int t75 = 1;
            int t76 = t74 + t75;
            i = t76;
            str_deinit_54(t78);
            str_deinit_54(t77);
            str_deinit_54(t69);
            continue;
          }
          str t84 = null;
          t84 = str_opAssign_22(t84, s1);
          int t85 = str_length_15(t84);
          int t86 = 2;
          boolean t87 = t85 == t86;
          if (t87) {
            str_deinit_54(t84);
            str_deinit_54(t78);
            str_deinit_54(t77);
            str_deinit_54(t69);
            break;
          }
          int t88 = i;
          str t89 = new str();
          str_init_3(t89, t88);
          str s3 = null;
          s3 = str_opAssign_22(s3, t89);
          str t90 = null;
          t90 = str_opAssign_22(t90, s3);
          boolean t91 = str_isEmpty_6(t90);
          if (t91) {
            str_deinit_54(t90);
            str_deinit_54(s3);
            str_deinit_54(t89);
            str_deinit_54(t84);
            str_deinit_54(t78);
            str_deinit_54(t77);
            str_deinit_54(t69);
            str_deinit_54(t58);
            str_deinit_54(s2);
            str_deinit_54(t57);
            str_deinit_54(s1);
            str_deinit_54(t55);
            return;
          }
          int t73 = i;
          int t74 = i;
          int t75 = 1;
          int t76 = t74 + t75;
          i = t76;
          str_deinit_54(t90);
          str_deinit_54(s3);
          str_deinit_54(t89);
          str_deinit_54(t84);
          str_deinit_54(t78);
          str_deinit_54(t77);
          str_deinit_54(t69);
        }
      }
      {
        int t92 = 32;
        str t93 = new str();
        str_init_3(t93, t92);
        str s5 = null;
        s5 = str_opAssign_22(s5, t93);
        for (;;) {
          str t94 = null;
          t94 = str_opAssign_22(t94, s5);
          boolean t95 = str_isEmpty_6(t94);
          boolean t96 = !t95;
          boolean t97 = !t96;
          if (t97) {
            str_deinit_54(t94);
            break;
          }
          str t98 = new str();
          str_init_1(t98);
          str s6tmp = null;
          s6tmp = str_opAssign_22(s6tmp, t98);
          str t99 = null;
          t99 = str_opAssign_22(t99, s6tmp);
          int t100 = 1;
          str_append_9(t99, t100);
          str_deinit_54(t99);
          str_deinit_54(s6tmp);
          str_deinit_54(t98);
          str_deinit_54(t94);
        }
        str_deinit_54(s5);
        str_deinit_54(t93);
      }
      str_deinit_54(t58);
      str_deinit_54(s2);
      str_deinit_54(t57);
      str_deinit_54(s1);
      str_deinit_54(t55);
    }

    main_class main_class_opAssign_40(final main_class __this, main_class rvalue) {
      main_class t101 = rvalue;
      return t101;
    }

    void main_class_deinit_38(final main_class __this) {
    }

    void type_init_42(final type __this) {
    }

    type type_opAssign_46(final type __this, type rvalue) {
      type t102 = rvalue;
      return t102;
    }

    void type_deinit_44(final type __this) {
      type t103 = __this;
      node t104 = t103.n;
      node_deinit_50(t104);
      type t105 = __this;
      str t106 = t105.v;
      str_deinit_54(t106);
    }

    void node_init_48(final node __this) {
    }

    node node_opAssign_52(final node __this, node rvalue) {
      node t107 = rvalue;
      return t107;
    }

    void node_deinit_50(final node __this) {
      node_deinit_50(__this.last);
      node_deinit_50(__this.prev);
    }

    void str_init_1(final str __this) {
    }

    void str_init_3(final str __this, int value) {
      str t112 = null;
      t112 = str_opAssign_22(t112, __this);
      int t113 = t112.value;
      int t114 = value;
      t112.value = t114;
      str_deinit_54(t112);
    }

    boolean str_isEmpty_6(final str __this) {
      int t115 = 0;
      int t116 = 0;
      boolean t117 = t115 == t116;
      return t117;
    }

    void str_append_9(final str __this, int i) {
    }

    int str_remove_12(final str __this) {
      int t118 = 0;
      return t118;
    }

    int str_length_15(final str __this) {
      int t119 = 0;
      return t119;
    }

    int str_get_18(final str __this, int at) {
      int t120 = 0;
      return t120;
    }

    str str_opAssign_22(str lhs, str rhs) {
      str t121 = lhs;
      str t122 = rhs;
      boolean t123 = t121 == t122;
      if (t123) {
        str t124 = lhs;
        str_deinit_54(t122);
        str_deinit_54(t121);
        return t124;
      }
      str t125 = rhs;
      str_deinit_54(t122);
      str_deinit_54(t121);
      return t125;
    }

    void str_deinit_54(final str __this) {
    }
  }

}
