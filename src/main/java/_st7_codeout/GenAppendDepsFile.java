package _st7_codeout;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_vars.VarDeclarator;

public abstract class GenAppendDepsFile {

  public static String g(List<ClassDeclaration> pods) throws IOException {

    /// if (datatype == TD_STR) {
    ///     struct string *e = (struct string*) ptr;
    ///     vec_add_unique_ignore_null(inProcessing, try_to_find_markable_by_ptr(e->buffer));
    /// 
    ///     return;
    /// }
    /// 
    /// if (datatype == TD_TOKEN) {
    ///     struct token *e = (struct token*) ptr;
    ///     vec_add_unique_ignore_null(inProcessing, try_to_find_markable_by_ptr(e->value));
    /// 
    ///     return;
    /// }

    // generated_append_deps.txt

    StringBuilder sb = new StringBuilder();

    for (ClassDeclaration c : pods) {
      if (c.isMainClass()) {
        continue;
      }
      if (c.isNativeArray() || c.isNativeString()) {
        continue;
      }

      String cName = c.getIdentifier().toString();

      sb.append("if (datatype == " + c.typedescName() + ")\n{\n");
      sb.append("    struct " + cName + " *e = (struct " + cName + "*) ptr;\n");
      for (VarDeclarator f : c.getFields()) {
        if (!f.getType().isClass()) {
          continue;
        }
        String fName = f.getIdentifier().toString();
        sb.append("    vec_add_unique_ignore_null(inProcessing, try_to_find_markable_by_ptr(e->" + fName + "));\n");
      }

      sb.append("    return;\n}\n");
    }

    final String fileName = "generated_append_deps.txt";
    FileWriter fw = new FileWriter(fileName);
    fw.write(sb.toString());
    fw.close();

    return sb.toString();

  }
}
