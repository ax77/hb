package rew;

import java.io.IOException;
import java.util.Optional;

import org.junit.Test;

import _st7_codeout.Codeout;
import _st7_codeout.CodeoutBuilder;
import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class TestImports {

  @Test
  public void test1() throws IOException {

    final String dir = System.getProperty("user.dir");
    InstantiationUnit unit = new ParserMain(dir + "/tests/imports/test_imports.hb").parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    System.out.println(result.toString());

  }
}