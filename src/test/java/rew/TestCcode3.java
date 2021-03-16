package rew;

import java.io.IOException;

import org.junit.Test;

import _st7_codeout.Codeout;
import _st7_codeout.CodeoutBuilder;
import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class TestCcode3 {

  @Test
  public void test1() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class main_class {                             //001 \n");
    sb.append("  int main() {                                 //002 \n");
    sb.append("    array<char> str = new array<char>();       //003 \n");
    sb.append("    str.add(\'a\');                            //004 \n");
    sb.append("    str.add(\'b\');                            //005 \n");
    sb.append("    str.add(\'c\');                            //006 \n");
    sb.append("    for(int i = 0; i < str.size(); i += 1) {   //007 \n");
    sb.append("      char c = str.get(i);                     //008 \n");
    sb.append("      std.print(\"%c\", c);                    //009 \n");
    sb.append("    }                                          //010 \n");
    sb.append("    return str.size();                         //011 \n");
    sb.append("  }                                            //012 \n");
    sb.append("}                                              //013 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    System.out.println(UtilSrcToStringLevel.tos(result.toString()));

  }
}
