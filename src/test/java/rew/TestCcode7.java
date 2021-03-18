package rew;

import java.io.IOException;

import org.junit.Test;

import _st7_codeout.Codeout;
import _st7_codeout.CodeoutBuilder;
import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class TestCcode7 {

  @Test
  public void test1() throws IOException {

//    //@formatter:off
//    StringBuilder sb = new StringBuilder();
//    sb.append("class main_class                      \n");
//    sb.append("{                                     \n");
//    sb.append("  int main()                          \n");
//    sb.append("  {                                   \n");
//    sb.append("    file fp = new file(\"main.c\");   \n");
//    sb.append("    fp.open();                        \n");
//    sb.append("    int sz = fp.read(1);              \n");
//    sb.append("    while(sz > 0) {                   \n");
//    sb.append("      char c = fp.getc();             \n");
//    sb.append("      std.print(\"%c\", c);           \n");
//    sb.append("      sz = fp.read(1);                \n");
//    sb.append("    }                                 \n");
//    sb.append("    fp.close();                       \n");
//    sb.append("    return 0;                         \n");
//    sb.append("  }                                   \n");
//    sb.append("}                                     \n");
//    //@formatter:on

//    //@formatter:off
//    StringBuilder sb = new StringBuilder();
//    sb.append("class main_class                                       \n");
//    sb.append("{                                                      \n");
//    sb.append("  int main()                                           \n");
//    sb.append("  {                                                    \n");
//    sb.append("    io tmp = new io();                                 \n");
//    sb.append("    array<char> content = tmp.read_file(\"main.c\");   \n");
//    sb.append("    for(int i = 0; i < content.size(); i += 1) {       \n");
//    sb.append("      char c = content.get(i);                         \n");
//    sb.append("      std.print(\"%c\", c);                            \n");
//    sb.append("    }                                                  \n");
//    sb.append("    return content.size();                             \n");
//    sb.append("  }                                                    \n");
//    sb.append("}                                                      \n");
//    //@formatter:on

//    //@formatter:off
//    StringBuilder sb = new StringBuilder();
//    sb.append("public class c_file_reader {                                                  \n");
//    sb.append("  private final array<char>buffer;                                            \n");
//    sb.append("  private final int size;                                                     \n");
//    sb.append("  private int offset;                                                         \n");
//    sb.append("  private int line;                                                           \n");
//    sb.append("  private int column;                                                         \n");
//    sb.append("  private char prevc;                                                         \n");
//    sb.append("  private int eofs;                                                           \n");
//    sb.append("  public c_file_reader(final string input) {                                  \n");
//    sb.append("    this.buffer = new array<char>();                                          \n");
//    sb.append("    this.size = input.length() + 8;                                             \n");
//    sb.append("    this.offset = 0;                                                          \n");
//    sb.append("    this.line = 1;                                                            \n");
//    sb.append("    this.column = 0;                                                          \n");
//    sb.append("    this.prevc = \'\\0\';                                                     \n");
//    sb.append("    this.eofs = -1;                                                           \n");
//    sb.append("    fill_buffer(input);                                                       \n");
//    sb.append("  }                                                                           \n");
//    sb.append("  void fill_buffer(string input) {                                            \n");
//    sb.append("    for (int i = 0; i < input.length(); i += 1) {                               \n");
//    sb.append("      char c = input.get(i);                                                  \n");
//    sb.append("      buffer.add(c);                                                          \n");
//    sb.append("    }                                                                         \n");
//    sb.append("    for (int i = 0; i < 8; i += 1) {                                          \n");
//    sb.append("      buffer.add(\'\\0\');                                                    \n");
//    sb.append("    }                                                                         \n");
//    sb.append("  }                                                                           \n");
//    sb.append("  public boolean isEof() {                                                    \n");
//    sb.append("    return eofs >= 8;                                                         \n");
//    sb.append("  }                                                                           \n");
//    sb.append("  public char peekc() {                                                       \n");
//    sb.append("    // don\'t be too smart ;)                                                 \n");
//    sb.append("    int save_offset = offset;                                                 \n");
//    sb.append("    int save_line = line;                                                     \n");
//    sb.append("    int save_column = column;                                                 \n");
//    sb.append("    char save_prevc = prevc;                                                  \n");
//    sb.append("    int save_eofs = eofs;                                                     \n");
//    sb.append("    char c = nextc();                                                         \n");
//    sb.append("    offset = save_offset;                                                     \n");
//    sb.append("    line = save_line;                                                         \n");
//    sb.append("    column = save_column;                                                     \n");
//    sb.append("    prevc = save_prevc;                                                       \n");
//    sb.append("    eofs = save_eofs;                                                         \n");
//    sb.append("    return c;                                                                 \n");
//    sb.append("  }                                                                           \n");
//    sb.append("  public array<char> peekc3() {                                               \n");
//    sb.append("    array<char> res = new array<char>();                                      \n");
//    sb.append("    // don\'t be too smart ;)                                                 \n");
//    sb.append("    int save_offset = offset;                                                 \n");
//    sb.append("    int save_line = line;                                                     \n");
//    sb.append("    int save_column = column;                                                 \n");
//    sb.append("    char save_prevc = prevc;                                                  \n");
//    sb.append("    int save_eofs = eofs;                                                     \n");
//    sb.append("    res.add(nextc());                                                         \n");
//    sb.append("    res.add(nextc());                                                         \n");
//    sb.append("    res.add(nextc());                                                         \n");
//    sb.append("    offset = save_offset;                                                     \n");
//    sb.append("    line = save_line;                                                         \n");
//    sb.append("    column = save_column;                                                     \n");
//    sb.append("    prevc = save_prevc;                                                       \n");
//    sb.append("    eofs = save_eofs;                                                         \n");
//    sb.append("    return res;                                                               \n");
//    sb.append("  }                                                                           \n");
//    sb.append("  public char nextc() {                                                       \n");
//    sb.append("    while (!isEof()) {                                                        \n");
//    sb.append("      if (eofs > 8) {                                                         \n");
//    sb.append("        //throw new RuntimeException(\"Infinite loop handling...\");          \n");
//    sb.append("      }                                                                       \n");
//    sb.append("      if (prevc == \'\\n\') {                                                 \n");
//    sb.append("        line += 1;                                                            \n");
//    sb.append("        column = 0;                                                           \n");
//    sb.append("      }                                                                       \n");
//    sb.append("      if (buffer.get(offset) == \'\\\\\') {                                   \n");
//    sb.append("        if (buffer.get(offset + 1) == \'\\r\') {                              \n");
//    sb.append("          if (buffer.get(offset + 2) == \'\\n\') {                            \n");
//    sb.append("            // DOS: [\\][\\r][\\n]                                            \n");
//    sb.append("            offset += 3;                                                      \n");
//    sb.append("          } else {                                                            \n");
//    sb.append("            // OSX: [\\][\\r]                                                 \n");
//    sb.append("            offset += 2;                                                      \n");
//    sb.append("          }                                                                   \n");
//    sb.append("          prevc = \'\\n\';                                                    \n");
//    sb.append("          continue;                                                           \n");
//    sb.append("        }                                                                     \n");
//    sb.append("        // UNX: [\\][\\n]                                                     \n");
//    sb.append("        if (buffer.get(offset + 1) == \'\\n\') {                              \n");
//    sb.append("          offset += 2;                                                        \n");
//    sb.append("          prevc = \'\\n\';                                                    \n");
//    sb.append("          continue;                                                           \n");
//    sb.append("        }                                                                     \n");
//    sb.append("      }                                                                       \n");
//    sb.append("      if (buffer.get(offset) == \'\\r\') {                                    \n");
//    sb.append("        if (buffer.get(offset + 1) == \'\\n\') {                              \n");
//    sb.append("          // DOS: [\\r][\\n]                                                  \n");
//    sb.append("          offset += 2;                                                        \n");
//    sb.append("        } else {                                                              \n");
//    sb.append("          // OSX: [\\r]                                                       \n");
//    sb.append("          offset += 1;                                                        \n");
//    sb.append("        }                                                                     \n");
//    sb.append("        prevc = \'\\n\';                                                      \n");
//    sb.append("        return \'\\n\';                                                       \n");
//    sb.append("      }                                                                       \n");
//    sb.append("      if (offset == size) {                                                   \n");
//    sb.append("        eofs += 1;                                                            \n");
//    sb.append("        return \'\\0\';                                                       \n");
//    sb.append("      }                                                                       \n");
//    sb.append("      char next = buffer.get(offset);                                         \n");
//    sb.append("      offset += 1;                                                            \n");
//    sb.append("      column += 1;                                                            \n");
//    sb.append("      prevc = next;                                                           \n");
//    sb.append("      if (next == \'\\0\') {                                                  \n");
//    sb.append("        eofs += 1;                                                            \n");
//    sb.append("        return \'\\0\';                                                       \n");
//    sb.append("      }                                                                       \n");
//    sb.append("      return next;                                                            \n");
//    sb.append("    }                                                                         \n");
//    sb.append("    return \'\\0\';                                                           \n");
//    sb.append("  }                                                                           \n");
//    sb.append("}                                                                             \n");
//    sb.append("class main_class                                                              \n");
//    sb.append("{                                                                             \n");
//    sb.append("  int main() {                                                                \n");
//    sb.append("    c_file_reader reader = new c_file_reader(\"int main() { return 0; }\");   \n");
//    sb.append("    while(!reader.isEof()) {                                                  \n");
//    sb.append("      char c = reader.nextc();                                                \n");
//    sb.append("      std.print(\"%c\", c);                                                   \n");
//    sb.append("    }                                                                         \n");
//    sb.append("    return 0;                                                                 \n");
//    sb.append("  }                                                                           \n");
//    sb.append("}                                                                             \n");
//    //@formatter:on

    InstantiationUnit unit = new ParserMain("__test_src.txt").parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      //System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    System.out.println(result.toString());

  }
}
