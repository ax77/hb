package _st7_codeout.parts;

import java.util.Map;
import java.util.Map.Entry;

import _st3_linearize_expr.rvalues.Var;
import _st3_linearize_expr.symbols.BuiltinsFnSet;
import utils_oth.CEscaper;

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

      final String initBuffer = content.toString();
      final String buflen = "sizeof(\"" + initBuffer + "\")-1";

      //struct str *t44 = &(struct str ) { .buf = "abc", .len = sizeof("abc")-1 };
      sb.append("struct str * " + v.getName().getName() + " = &(struct str) { .buf = \"" + initBuffer + "\", .len = "
          + buflen + " };\n");

    }

    return sb.toString();
  }

}
