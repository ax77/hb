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

    final String dir = System.getProperty("user.dir");
    InstantiationUnit unit = new ParserMain(dir + "/__test_src.txt").parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
       // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    System.out.println(result.toString());

  }
}
