package _st7_codeout;

import java.io.FileWriter;
import java.io.IOException;

public abstract class GenGeneratedTypesFile {

  public static void genGeneratedTypesFile(String src) throws IOException {
    // generated_types.h

    StringBuilder prebuf = new StringBuilder();
    prebuf.append("#ifndef GENERATED_TYPES_H_                  \n");
    prebuf.append("#define GENERATED_TYPES_H_                  \n");
    prebuf.append("#include \"hrt/headers.h\"                  \n\n");
    prebuf.append("typedef int boolean;                        \n");
    prebuf.append("typedef struct string * string;             \n\n");
    prebuf.append("struct string                               \n");
    prebuf.append("{                                           \n");
    prebuf.append("    char *buffer;                           \n");
    prebuf.append("    size_t len;                             \n");
    prebuf.append("};                                          \n\n");
    prebuf.append("void string_init(string __this, char *buf); \n");
    prebuf.append("void string_deinit(string __this);          \n");
    prebuf.append("void string_destroy(string __this);         \n\n");
    prebuf.append("struct type_descr;                          \n");

    final String fileName = "generated_types.h";
    FileWriter fw = new FileWriter(fileName);
    fw.write(prebuf.toString());
    fw.write(src);
    fw.write("\n#endif\n");
    fw.close();
  }
}
