package _st7_codeout.parts;

import java.util.List;

import _st7_codeout.ToStringsInternal;
import ast_class.ClassDeclaration;

public abstract class GenStructsForwards {

  public static String gen(List<ClassDeclaration> pods) {

    StringBuilder sb = new StringBuilder();

    for (ClassDeclaration c : pods) {
      if (c.isMainClass()) {
        continue;
      }
      if (c.isNamespace()) {
        continue;
      }
      sb.append("struct " + ToStringsInternal.classHeaderToString(c) + ";\n");
    }

    return sb.toString();
  }

}
