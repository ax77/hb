package _st7_codeout;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import ast_class.ClassDeclaration;

public abstract class GenIsCompoundFile {

  public static String g(List<ClassDeclaration> pods) throws IOException {

    // if (datatype == TD_STR) {
    //   return 1;
    // }

    StringBuilder sb = new StringBuilder();

    for (ClassDeclaration c : pods) {
      if (c.isMainClass()) {
        continue;
      }
      if (c.isNativeArray() || c.isNativeString()) {
        continue;
      }

      sb.append("if (datatype == " + c.typedescName() + ") \n{\n");
      sb.append("    return 1;\n");
      sb.append("\n}\n");
    }

    final String fileName = "generated_is_compound.txt";
    FileWriter fw = new FileWriter(fileName);
    fw.write(sb.toString());
    fw.close();

    return sb.toString();
  }

}
