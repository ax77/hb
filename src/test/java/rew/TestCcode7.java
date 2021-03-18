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

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class main_class                                       \n");
    sb.append("{                                                      \n");
    sb.append("  int main()                                           \n");
    sb.append("  {                                                    \n");
    sb.append("    io tmp = new io();                                 \n");
    sb.append("    array<char> content = tmp.read_file(\"main.c\");   \n");
    sb.append("    for(int i = 0; i < content.size(); i += 1) {       \n");
    sb.append("      char c = content.get(i);                         \n");
    sb.append("      std.print(\"%c\", c);                            \n");
    sb.append("    }                                                  \n");
    sb.append("    return content.size();                             \n");
    sb.append("  }                                                    \n");
    sb.append("}                                                      \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      //System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    System.out.println(UtilSrcToStringLevel.tos(result.toString()));

  }
}
