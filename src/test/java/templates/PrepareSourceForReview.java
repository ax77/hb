package templates;

import org.junit.Test;

import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.CompilationUnit;
import ast_unit.InstantiationUnit;
import ast_vars.VarDeclarator;
import hashed.Hash_ident;
import utils.UtilSrcToStringLevel;

public class PrepareSourceForReview {

  @Test
  public void testTestTemplatesRshiftHandling() throws Exception {

    // CompilationUnit unit = new ParserMain("tests/test_templates_3").parseCompilationUnit();
    // ClassDeclaration test = unit.getClassByName("test");
    // VarDeclarator x1 = test.getField(Hash_ident.getHashedIdent("x1"));

    InstantiationUnit unit = new ParserMain("tests/test_templates_3").parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      System.out.println(c);
    }
  }
}
