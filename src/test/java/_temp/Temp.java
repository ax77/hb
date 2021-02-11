package _temp;

import org.junit.Test;

import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.CompilationUnit;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class Temp {

  @Test
  public void temp() throws Exception {

    //    CompilationUnit unit = new ParserMain("tests/temp.hb").parseCompilationUnit();
    //    for (ClassDeclaration c : unit.getClasses()) {
    //      System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    //    }

    InstantiationUnit unit = new ParserMain("tests/temp.hb").parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

  }
}
