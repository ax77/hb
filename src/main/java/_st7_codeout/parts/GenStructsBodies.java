package _st7_codeout.parts;

import java.util.List;

import _st7_codeout.ToStringsInternal;
import ast_class.ClassDeclaration;
import ast_vars.VarDeclarator;

public abstract class GenStructsBodies {

  public static String gen(List<ClassDeclaration> classes) {
    StringBuilder sb = new StringBuilder();

    for (ClassDeclaration c : classes) {
      if (c.isMainClass()) {
        continue;
      }
      if (c.isNamespace()) {
        continue;
      }
      if (c.isNativeArray()) {
        continue;
      }
      if (c.isInterface()) {
        continue;
      }

      sb.append(classToString(c));
      sb.append("\n");
    }

    return sb.toString();
  }

  private static String classToString(ClassDeclaration c) {
    StringBuilder sb = new StringBuilder();
    sb.append("struct ");
    sb.append(c.getIdentifier().getName());

    if (!c.getTypeParametersT().isEmpty()) {
      sb.append("_");
      sb.append(ToStringsInternal.typeArgumentsToString(c.getTypeParametersT()));
    }

    sb.append("\n{\n");

    for (VarDeclarator var : c.getFields()) {
      sb.append(ToStringsInternal.varToString(var) + ";\n");
    }

    sb.append("\n};\n");
    return sb.toString();
  }

}
