package rew;

import java.io.IOException;

import org.junit.Test;

import _st7_codeout.CgMain;
import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.InstantiationUnit;

public class TestCcode7 {

  @Test
  public void test1() throws IOException {

    final String dir = System.getProperty("user.dir");
    InstantiationUnit unit = new ParserMain(dir + "/tests/test_string.hb").parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      //System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    CgMain result = new CgMain(unit.getClasses());
    System.out.println(result.toString());

  }
}
