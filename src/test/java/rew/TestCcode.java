package rew;

import java.io.IOException;

import org.junit.Test;

import _st7_codeout.Codeout;
import _st7_codeout.CodeoutBuilder;
import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class TestCcode {

  @Test
  public void testTac() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class str {              //001 \n");
    sb.append("  int f;                 //002 \n");
    sb.append("}                        //003 \n");
    sb.append("class tok {              //004 \n");
    sb.append("  str value;             //005 \n");
    sb.append("  int type;              //006 \n");
    sb.append("}                        //007 \n");
    sb.append("class main_class {       //008 \n");
    sb.append("  int  main() {          //009 \n");
    sb.append("    str s = new str(); return 0;   //010 \n");
    sb.append("  }                      //011 \n");
    sb.append("}                        //012 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    System.out.println(UtilSrcToStringLevel.tos(result.toString()));
  }

}
