package tac;

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
    void main_class_init_41(final main_class __this) {
    }

    void main_class_main_39(final main_class __this) {
      str t56 = new str();
      str_init_1(t56);
      str s1 = null;
      s1 = str_opAssign_55(s1, t56);
      int t57 = 1;
      str t58 = new str();
      str_init_3(t58, t57);
      str s2 = null;
      s2 = str_opAssign_55(s2, t58);
      str t59 = null;
      t59 = str_opAssign_55(t59, s1);
      boolean t60 = str_isEmpty_6(t59);
      if (t60) {
        str_deinit_20(t59);
        str_deinit_20(s2);
        str_deinit_20(t58);
        str_deinit_20(s1);
        str_deinit_20(t56);
        return;
      }
      for (;;) {
        str t61 = null;
        t61 = str_opAssign_55(t61, s1);
        boolean t62 = str_isEmpty_6(t61);
        boolean t63 = !t62;
        boolean t64 = !t63;
        if (t64) {
          str_deinit_20(t61);
          break;
        }
        str t65 = null;
        t65 = str_opAssign_55(t65, s2);
        str t66 = null;
        t66 = str_opAssign_55(t66, s1);
        int t67 = str_remove_12(t66);
        str_append_9(t65, t67);
        str_deinit_20(t66);
        str_deinit_20(t65);
        str_deinit_20(t61);
      }
      {
        int t68 = 0;
        int i = t68;
        for (;;) {
          int t69 = i;
          str t70 = null;
          t70 = str_opAssign_55(t70, s2);
          int t71 = str_length_15(t70);
          boolean t72 = t69 < t71;
          boolean t73 = !t72;
          if (t73) {
            str_deinit_20(t70);
            break;
          }
          str t78 = null;
          t78 = str_opAssign_55(t78, s1);
          str t79 = null;
          t79 = str_opAssign_55(t79, s2);
          int t80 = i;
          int t81 = str_get_18(t79, t80);
          str_append_9(t78, t81);
          int t82 = i;
          int t83 = 2;
          boolean t84 = t82 == t83;
          if (t84) {
            int t74 = i;
            int t75 = i;
            int t76 = 1;
            int t77 = t75 + t76;
            i = t77;
            str_deinit_20(t79);
            str_deinit_20(t78);
            str_deinit_20(t70);
            continue;
          }
          str t85 = null;
          t85 = str_opAssign_55(t85, s1);
          int t86 = str_length_15(t85);
          int t87 = 2;
          boolean t88 = t86 == t87;
          if (t88) {
            str_deinit_20(t85);
            str_deinit_20(t79);
            str_deinit_20(t78);
            str_deinit_20(t70);
            break;
          }
          int t89 = i;
          str t90 = new str();
          str_init_3(t90, t89);
          str s3 = null;
          s3 = str_opAssign_55(s3, t90);
          str t91 = null;
          t91 = str_opAssign_55(t91, s3);
          boolean t92 = str_isEmpty_6(t91);
          if (t92) {
            str_deinit_20(t91);
            str_deinit_20(s3);
            str_deinit_20(t90);
            str_deinit_20(t85);
            str_deinit_20(t79);
            str_deinit_20(t78);
            str_deinit_20(t70);
            str_deinit_20(t59);
            str_deinit_20(s2);
            str_deinit_20(t58);
            str_deinit_20(s1);
            str_deinit_20(t56);
            return;
          }
          int t74 = i;
          int t75 = i;
          int t76 = 1;
          int t77 = t75 + t76;
          i = t77;
          str_deinit_20(t91);
          str_deinit_20(s3);
          str_deinit_20(t90);
          str_deinit_20(t85);
          str_deinit_20(t79);
          str_deinit_20(t78);
          str_deinit_20(t70);
        }
      }
      {
        int t93 = 32;
        str t94 = new str();
        str_init_3(t94, t93);
        str s5 = null;
        s5 = str_opAssign_55(s5, t94);
        for (;;) {
          str t95 = null;
          t95 = str_opAssign_55(t95, s5);
          boolean t96 = str_isEmpty_6(t95);
          boolean t97 = !t96;
          boolean t98 = !t97;
          if (t98) {
            str_deinit_20(t95);
            break;
          }
          str t99 = new str();
          str_init_1(t99);
          str s6tmp = null;
          s6tmp = str_opAssign_55(s6tmp, t99);
          str t100 = null;
          t100 = str_opAssign_55(t100, s6tmp);
          int t101 = 1;
          str_append_9(t100, t101);
          str_deinit_20(t100);
          str_deinit_20(s6tmp);
          str_deinit_20(t99);
          str_deinit_20(t95);
        }
        str_deinit_20(s5);
        str_deinit_20(t94);
      }
      str t102 = null;
      t102 = str_opAssign_55(t102, s1);
      boolean t103 = str_isEmpty_6(t102);
      if (t103) {
      } else {
      }
      str_deinit_20(t102);
      str_deinit_20(t59);
      str_deinit_20(s2);
      str_deinit_20(t58);
      str_deinit_20(s1);
      str_deinit_20(t56);
    }

    main_class main_class_opAssign_45(final main_class __this, main_class rvalue) {
      main_class t104 = rvalue;
      return t104;
    }

    void main_class_deinit_43(final main_class __this) {
    }

    void type_init_47(final type __this) {
    }

    type type_opAssign_51(final type __this, type rvalue) {
      type t105 = rvalue;
      return t105;
    }

    void type_deinit_49(final type __this) {
      type t106 = __this;
      node t107 = t106.n;
      node_deinit_24(t107);
      type t108 = __this;
      str t109 = t108.v;
      str_deinit_20(t109);
    }

    void node_init_22(final node __this, node prev, node last, int value) {
      node t110 = null;
      t110 = node_opAssign_53(t110, __this);
      node t111 = t110.prev;
      node t112 = null;
      t112 = node_opAssign_53(t112, prev);
      t110.prev = t112;
      node t113 = null;
      t113 = node_opAssign_53(t113, __this);
      node t114 = t113.last;
      node t115 = null;
      t115 = node_opAssign_53(t115, last);
      t113.last = t115;
      node t116 = null;
      t116 = node_opAssign_53(t116, __this);
      int t117 = t116.value;
      int t118 = value;
      t116.value = t118;
      node_deinit_24(t116);
      node_deinit_24(t115);
      node_deinit_24(t114);
      node_deinit_24(t113);
      node_deinit_24(t112);
      node_deinit_24(t111);
      node_deinit_24(t110);
    }

    node node_opAssign_53(final node __this, node rvalue) {
      node t119 = rvalue;
      return t119;
    }

    void node_deinit_24(final node __this) {
      node t120 = __this;
      node t121 = t120.prev;
      node t122 = __this;
      node t123 = t122.last;
      t120.prev = t123;
      node t124 = __this;
      node t125 = t124.last;
      node_deinit_24(t125);
      node t126 = __this;
      node t127 = t126.prev;
      node_deinit_24(t127);
    }

    void str_init_1(final str __this) {
    }

    void str_init_3(final str __this, int value) {
      str t128 = null;
      t128 = str_opAssign_55(t128, __this);
      int t129 = t128.value;
      int t130 = value;
      t128.value = t130;
      str_deinit_20(t128);
    }

    boolean str_isEmpty_6(final str __this) {
      boolean t131 = true;
      return t131;
    }

    void str_append_9(final str __this, int i) {
    }

    int str_remove_12(final str __this) {
      int t132 = 0;
      return t132;
    }

    int str_length_15(final str __this) {
      int t133 = 0;
      return t133;
    }

    int str_get_18(final str __this, int at) {
      int t134 = 0;
      return t134;
    }

    str str_opAssign_55(final str __this, str rvalue) {
      str t135 = rvalue;
      return t135;
    }

    void str_deinit_20(final str __this) {
      str t136 = __this;
      int t137 = t136.value;
      int t138 = 1;
      int t139 = -t138;
      t136.value = t139;
    }
  }

}
