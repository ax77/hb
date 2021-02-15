package tac;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_main.ParserMain;
import ast_st3_tac.TacGenerator;
import ast_st3_tac.UnitToText;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class TestTac {

  @Test
  public void testTac() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class type {                               //001 \n");
    sb.append("  int value;                               //002 \n");
    sb.append("  type(int value) {                        //003 \n");
    sb.append("    this.value = value;                    //004 \n");
    sb.append("  }                                        //005 \n");
    sb.append("}                                          //006 \n");
    sb.append("class token {                              //007 \n");
    sb.append("  type tp;                               //008 \n");
    sb.append("  token(type tp) {                       //009 \n");
    sb.append("    this.tp = tp;                      //010 \n");
    sb.append("  }                                        //011 \n");
    sb.append("}                                          //012 \n");
    sb.append("class test {                               //013 \n");
    sb.append("  void fn() { type tp = new type(1);                              //014 \n");
    sb.append("    token tok1 = new token(tp);   //015 \n");
    sb.append("  }                                        //016 \n");
    sb.append("}                                          //017 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    ClassDeclaration c = unit.getClassByName("test");
    ExprExpression expr = c.getMethods().get(0).getBlock().getBlockItems().get(0).getLocalVariable().getSimpleInitializer();
    TacGenerator tcg = new TacGenerator();
    tcg.gen(expr);
    System.out.println(tcg.txt1(";\n"));

    //    UnitToText text = new UnitToText(unit);
    //    System.out.println(UtilSrcToStringLevel.tos(text.toString()));

    //    // and try reparse it again
    //    InstantiationUnit cleanUnit = new ParserMain(new StringBuilder(text.toString())).parseInstantiationUnit();
    //    for (ClassDeclaration c : cleanUnit.getClasses()) {
    //      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    //    }
  }

}
