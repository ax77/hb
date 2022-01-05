package _st7_codeout.parts;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

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
      final String buflen = "sizeof(\"" + initBuffer + "\")-1";

      final String charArrayName = ToStringsInternal.classHeaderToString(charArray);
      final String randomVarname = getRandomName();

      sb.append("struct " + charArrayName + " " + randomVarname + " = { .data = \"" + initBuffer + "\", .size = "
          + buflen + ", .alloc = " + buflen + " };\n");
      sb.append("struct str * " + v.getName().getName() + " = &(struct str) { .buffer = &" + randomVarname + " };\n");

    }

    for (Entry<String, Var> ent : BuiltinsFnSet.getAuxStringsmap().entrySet()) {
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
      sb.append("static char " + v.getName().getName() + "[] = \"" + initBuffer + "\";\n");
    }

    return sb.toString();
  }

  private static String getRandomName() {
    String r = UUID.randomUUID().toString();
    r = r.replace("-", "_");
    return "a_" + r;
  }

}
