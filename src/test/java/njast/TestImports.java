package njast;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.InstantiationUnit;

public class TestImports {

  @Test
  public void testImports() throws Exception {

    List<String> files = new ArrayList<>();
    files.add("/tests/test_array");
    files.add("/tests/test_import_list");
    files.add("/tests/test_scope");

    for (String f : files) {
      InstantiationUnit result = new ParserMain(f).parseInstantiationUnit();
      for (ClassDeclaration clazz : result.getClasses()) {
        // System.out.println(UtilSrcToStringLevel.tos(clazz.toString()));
      }
    }

  }

}
