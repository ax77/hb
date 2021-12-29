package _st7_codeout.parts;

import java.util.List;

import _st7_codeout.ToStringsInternal;
import ast_class.ClassDeclaration;
import ast_vars.VarDeclarator;

public abstract class GenStaticFields {

  ///TODO:static_semantic
  public static String gen(List<ClassDeclaration> classes) {
    StringBuilder sb = new StringBuilder();

    for (ClassDeclaration c : classes) {
      if (c.isMainClass()) {
        continue;
      }
      if (!c.isStaticClass()) {
        continue;
      }
      if (c.getFields().isEmpty()) {
        continue;
      }
      for (VarDeclarator field : c.getFields()) {
        StringBuilder varname = new StringBuilder();
        varname.append(c.getIdentifier().toString());
        varname.append("_");
        varname.append(field.getIdentifier().toString());

        sb.append("static const " + ToStringsInternal.typeToString(field.getType()) + " " + varname + " = "
            + field.getSimpleInitializer().toString() + ";\n");
      }
    }

    return sb.toString();
  }

}
