package njast;

import org.junit.Test;

import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class TestImports {

  @Test
  public void testImports() throws Exception {

    String thisFname = "/tests/test_import_list";

    InstantiationUnit result = new ParserMain(thisFname).parseInstantiationUnit();
    for (ClassDeclaration clazz : result.getClasses()) {
      System.out.println(UtilSrcToStringLevel.tos(clazz.toString()));
    }

  }

}
