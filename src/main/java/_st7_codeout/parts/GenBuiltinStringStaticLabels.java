package _st7_codeout.parts;

import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import _st3_linearize_expr.BuiltinsFnSet;
import _st3_linearize_expr.CEscaper;
import _st3_linearize_expr.leaves.Var;
import _st7_codeout.ToStringsInternal;
import ast_class.ClassDeclaration;

public abstract class GenBuiltinStringStaticLabels {

  public static String gen(ClassDeclaration charArray) {

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
      final int buflen = initBuffer.length();

      final String charArrayName = ToStringsInternal.classHeaderToString(charArray);
      final String randomVarname = getRandomName();

      // We have to 'bootstrap' a string class here, so we have to use 
      // a structures instead of char*.
      // struct string *t1024 = &(struct string) { .buffer = "1.2.3", .length = 5 };
      //

      // struct array_1024 a_42b7b406_5f99_4b1d_b554_c278a6a80204 = { .data = "xxxxx", .size = 40, .alloc = 40 };
      // struct string * t96 = &(struct string) {.buffer = &a_42b7b406_5f99_4b1d_b554_c278a6a80204 };

      sb.append("struct " + charArrayName + " " + randomVarname + " = { .data = \"" + initBuffer + "\", .size = "
          + buflen + ", .alloc = " + buflen + " };\n");
      sb.append(
          "struct string * " + v.getName().getName() + " = &(struct string) { .buffer = &" + randomVarname + " };\n");

    }

    return sb.toString();
  }

  private static String getRandomName() {
    String r = UUID.randomUUID().toString();
    r = r.replace("-", "_");
    return "a_" + r;
  }

}
