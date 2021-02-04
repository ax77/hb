package templates;

import org.junit.Test;

import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class PrepareSourceForReview {

  @Test
  public void testTestTemplatesRshiftHandling() throws Exception {

    // CompilationUnit unit = new ParserMain("tests/test_class_resolver").parseCompilationUnit();
    // for (ClassDeclaration c : unit.getClasses()) {
    //   System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    // }

    InstantiationUnit unit = new ParserMain("tests/test_class_resolver").parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }
  }
}
