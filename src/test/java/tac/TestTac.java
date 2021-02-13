package tac;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_st3_tac.UnitToText;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class TestTac {

  @Test
  public void testTac() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class tok {              //001 \n");
    sb.append("  int value;             //002 \n");
    sb.append("}                        //003 \n");
    sb.append("class type {             //007 \n");
    sb.append("  tok tok;               //008 \n");
    sb.append("  void fn() {            //009 \n");
    sb.append("    int a = 0;           //010 \n");
    sb.append("    int b = 1;           //011 \n");
    sb.append("    tok.value = a + b;   //012 \n");
    sb.append("  }                      //014 \n");
    sb.append("}                        //015 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    UnitToText text = new UnitToText(unit);
    System.out.println(UtilSrcToStringLevel.tos(text.toString()));

    // and try reparse it again
    InstantiationUnit cleanUnit = new ParserMain(new StringBuilder(text.toString())).parseInstantiationUnit();
    for (ClassDeclaration c : cleanUnit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }
  }

}
