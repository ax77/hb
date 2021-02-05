package imports;

import org.junit.Test;

import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class TestImports {

  @Test
  public void test() throws Exception {

    InstantiationUnit unit = new ParserMain("tests/test_imports").parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }
  }
}
