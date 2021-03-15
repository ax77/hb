package _st7_codeout;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import ast_class.ClassDeclaration;

public abstract class GenDeinitsFile {

  public static void g(List<ClassDeclaration> pods) throws IOException {

    // if (m->datatype == TD_STRING) {
    //   string e = (string) m->ptr;
    //   string_deinit(e);
    // }

    StringBuilder sb = new StringBuilder();

    for (ClassDeclaration c : pods) {
      if (c.isMainClass()) {
        continue;
      }

      String deinitCall = c.getDestructor().signToStringCall();
      String cName = c.getIdentifier().toString();

      sb.append("if (m->datatype == " + c.typedescName() + ")\n{\n");
      sb.append("    struct " + cName + " *e = (struct " + cName + "*) m->ptr;\n");
      sb.append("    " + deinitCall + "(e);\n");
      sb.append("\n}\n");
    }

    final String fileName = "generated_deinits.txt";
    FileWriter fw = new FileWriter(fileName);
    fw.write(sb.toString());
    fw.close();

  }
}
