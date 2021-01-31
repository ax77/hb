package templates;

import org.junit.Test;

import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class PrepareSourceForReview {

  @Test
  public void testTestTemplatesRshiftHandling() throws Exception {

    InstantiationUnit result = new ParserMain("tests/test_templates_2").parseInstantiationUnit();
    for (ClassDeclaration clazz : result.getClasses()) {
      System.out.println(UtilSrcToStringLevel.tos(clazz.toString()));
    }
  }
}
