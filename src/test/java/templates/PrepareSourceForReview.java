package templates;

import org.junit.Ignore;
import org.junit.Test;

import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class PrepareSourceForReview {

  @Ignore
  @Test
  public void testPrepareSourceForTemplatesTesting() throws Exception {

    InstantiationUnit unit = new ParserMain("tests/templates/test_templates_1").parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }
  }
}
