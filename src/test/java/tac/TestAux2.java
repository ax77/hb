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
    void main_class_init_34(final main_class __this) {
    }

    void main_class_main_32(final main_class __this) {
      str t53 = new str();
      str_init_1(t53);
      str s1 = null;
      s1 = str_opAssign_52(s1, t53);
      int t54 = 1;
      str t55 = new str();
      str_init_3(t55, t54);
      str s2 = null;
      s2 = str_opAssign_52(s2, t55);
      str t56 = null;
      t56 = str_opAssign_52(t56, s1);
      boolean t57 = str_isEmpty_6(t56);
      if (t57) {
        str_deinit_50(t56);
        str_deinit_50(s2);
        str_deinit_50(t55);
        str_deinit_50(s1);
        str_deinit_50(t53);
        return;
      }
      for (;;) {
        str t58 = null;
        t58 = str_opAssign_52(t58, s1);
        boolean t59 = str_isEmpty_6(t58);
        boolean t60 = !t59;
        boolean t61 = !t60;
        if (t61) {
          str_deinit_50(t58);
          break;
        }
        str t62 = null;
        t62 = str_opAssign_52(t62, s2);
        str t63 = null;
        t63 = str_opAssign_52(t63, s1);
        int t64 = str_remove_12(t63);
        str_append_9(t62, t64);
        str_deinit_50(t63);
        str_deinit_50(t62);
        str_deinit_50(t58);
      }
      {
        int t65 = 0;
        int i = t65;
        for (;;) {
          int t66 = i;
          str t67 = null;
          t67 = str_opAssign_52(t67, s2);
          int t68 = str_length_15(t67);
          boolean t69 = t66 < t68;
          boolean t70 = !t69;
          if (t70) {
            str_deinit_50(t67);
            break;
          }
          str t75 = null;
          t75 = str_opAssign_52(t75, s1);
          str t76 = null;
          t76 = str_opAssign_52(t76, s2);
          int t77 = i;
          int t78 = str_get_18(t76, t77);
          str_append_9(t75, t78);
          int t79 = i;
          int t80 = 2;
          boolean t81 = t79 == t80;
          if (t81) {
            int t71 = i;
            int t72 = i;
            int t73 = 1;
            int t74 = t72 + t73;
            i = t74;
            str_deinit_50(t76);
            str_deinit_50(t75);
            str_deinit_50(t67);
            continue;
          }
          str t82 = null;
          t82 = str_opAssign_52(t82, s1);
          int t83 = str_length_15(t82);
          int t84 = 2;
          boolean t85 = t83 == t84;
          if (t85) {
            str_deinit_50(t82);
            str_deinit_50(t76);
            str_deinit_50(t75);
            str_deinit_50(t67);
            break;
          }
          int t86 = i;
          str t87 = new str();
          str_init_3(t87, t86);
          str s3 = null;
          s3 = str_opAssign_52(s3, t87);
          str t88 = null;
          t88 = str_opAssign_52(t88, s3);
          boolean t89 = str_isEmpty_6(t88);
          if (t89) {
            str_deinit_50(t88);
            str_deinit_50(s3);
            str_deinit_50(t87);
            str_deinit_50(t82);
            str_deinit_50(t76);
            str_deinit_50(t75);
            str_deinit_50(t67);
            str_deinit_50(t56);
            str_deinit_50(s2);
            str_deinit_50(t55);
            str_deinit_50(s1);
            str_deinit_50(t53);
            return;
          }
          int t71 = i;
          int t72 = i;
          int t73 = 1;
          int t74 = t72 + t73;
          i = t74;
          str_deinit_50(t88);
          str_deinit_50(s3);
          str_deinit_50(t87);
          str_deinit_50(t82);
          str_deinit_50(t76);
          str_deinit_50(t75);
          str_deinit_50(t67);
        }
      }
      {
        int t90 = 32;
        str t91 = new str();
        str_init_3(t91, t90);
        str s5 = null;
        s5 = str_opAssign_52(s5, t91);
        for (;;) {
          str t92 = null;
          t92 = str_opAssign_52(t92, s5);
          boolean t93 = str_isEmpty_6(t92);
          boolean t94 = !t93;
          boolean t95 = !t94;
          if (t95) {
            str_deinit_50(t92);
            break;
          }
          str t96 = new str();
          str_init_1(t96);
          str s6tmp = null;
          s6tmp = str_opAssign_52(s6tmp, t96);
          str t97 = null;
          t97 = str_opAssign_52(t97, s6tmp);
          int t98 = 1;
          str_append_9(t97, t98);
          str_deinit_50(t97);
          str_deinit_50(s6tmp);
          str_deinit_50(t96);
          str_deinit_50(t92);
        }
        str_deinit_50(s5);
        str_deinit_50(t91);
      }
      str_deinit_50(t56);
      str_deinit_50(s2);
      str_deinit_50(t55);
      str_deinit_50(s1);
      str_deinit_50(t53);
    }

    main_class main_class_opAssign_38(final main_class __this, main_class rvalue) {
      main_class t99 = rvalue;
      return t99;
    }

    void main_class_deinit_36(final main_class __this) {
    }

    void type_init_40(final type __this) {
    }

    type type_opAssign_44(final type __this, type rvalue) {
      type t100 = rvalue;
      return t100;
    }

    void type_deinit_42(final type __this) {
      type t101 = __this;
      node t102 = t101.n;
      node_deinit_46(t102);
      type t103 = __this;
      str t104 = t103.v;
      str_deinit_50(t104);
    }

    void node_init_20(final node __this, node prev, node last, int value) {
      node t105 = null;
      t105 = node_opAssign_48(t105, __this);
      node t106 = t105.prev;
      node t107 = null;
      t107 = node_opAssign_48(t107, prev);
      t105.prev = t107;
      node t108 = null;
      t108 = node_opAssign_48(t108, __this);
      node t109 = t108.last;
      node t110 = null;
      t110 = node_opAssign_48(t110, last);
      t108.last = t110;
      node t111 = null;
      t111 = node_opAssign_48(t111, __this);
      int t112 = t111.value;
      int t113 = value;
      t111.value = t113;
      node_deinit_46(t111);
      node_deinit_46(t110);
      node_deinit_46(t109);
      node_deinit_46(t108);
      node_deinit_46(t107);
      node_deinit_46(t106);
      node_deinit_46(t105);
    }

    node node_opAssign_48(final node __this, node rvalue) {
      node t114 = rvalue;
      return t114;
    }

    void node_deinit_46(final node __this) {
      node t115 = __this;
      node t116 = t115.last;
      node_deinit_46(t116);
      node t117 = __this;
      node t118 = t117.prev;
      node_deinit_46(t118);
    }

    void str_init_1(final str __this) {
    }

    void str_init_3(final str __this, int value) {
      str t119 = null;
      t119 = str_opAssign_52(t119, __this);
      int t120 = t119.value;
      int t121 = value;
      t119.value = t121;
      str_deinit_50(t119);
    }

    boolean str_isEmpty_6(final str __this) {
      int t122 = 0;
      int t123 = 0;
      boolean t124 = t122 == t123;
      return t124;
    }

    void str_append_9(final str __this, int i) {
    }

    int str_remove_12(final str __this) {
      int t125 = 0;
      return t125;
    }

    int str_length_15(final str __this) {
      int t126 = 0;
      return t126;
    }

    int str_get_18(final str __this, int at) {
      int t127 = 0;
      return t127;
    }

    str str_opAssign_52(final str __this, str rvalue) {
      str t128 = rvalue;
      return t128;
    }

    void str_deinit_50(final str __this) {
    }
  }

}
