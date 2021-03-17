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

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class main_class                                           \n");
    sb.append("{                                                          \n");
    sb.append("  int main()                                               \n");
    sb.append("  {                                                        \n");
    sb.append("    array<char> x = new array<char>();                         \n");
    sb.append("    x.add(\'a\');                                          \n");
    sb.append("    x.add(\'b\');                                          \n");
    sb.append("    x.add(\'c\');                                          \n");
    sb.append("    x.add(\'d\');                                          \n");
    sb.append("    x.add(\'e\');                                          \n");
    sb.append("    for(int i = 0; i < x.size(); i += 1) {                 \n");
    sb.append("      char c = x.get(i);                                   \n");
    sb.append("      std.print(\"%c\\n\", c);                             \n");
    sb.append("    }                                                      \n");
    sb.append("    return 0;                                              \n");
    sb.append("  }                                                        \n");
    sb.append("}                                                          \n");
    //@formatter:on


    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      //System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    System.out.println(UtilSrcToStringLevel.tos(result.toString()));

  }
}
