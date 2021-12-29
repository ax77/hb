package _st7_codeout.parts;

import java.util.Map;
import java.util.Map.Entry;

import _st3_linearize_expr.BuiltinsFnSet;
import _st3_linearize_expr.CEscaper;
import _st3_linearize_expr.leaves.Var;

public abstract class GenBuiltinStringStaticLabels {

  public static String gen() {

    StringBuilder sb = new StringBuilder();
    Map<String, Var> strings = BuiltinsFnSet.getStringsmap();

    for (Entry<String, Var> ent : strings.entrySet()) {
      String s = ent.getKey();
      Var v = ent.getValue();

      int[] esc = CEscaper.escape(s);
      StringBuilder content = new StringBuilder();

      for (int j = 0; j < esc.length; j += 1) {
        final char c = (char) esc[j];
        if (c == '\0') {
          continue;
        }
        content.append(CEscaper.unesc(c));
      }

      String initBuffer = content.toString();

      // We have to 'bootstrap' a string class here, so we have to use 
      // a structures instead of char*.
      // struct string *t1024 = &(struct string) { .buffer = "1.2.3", .length = 5 };
      //
      sb.append("struct string * " + v.getName().getName() + " = &(struct string) { .buffer = \"" + initBuffer
          + "\", .length = " + initBuffer.length() + " };\n");

    }

    return sb.toString();
  }

}
