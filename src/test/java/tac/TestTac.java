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

  class some {
    int value;
    int another;
  }

  void some_init_0(some _this_, int value) {
    _this_.value = value;
  }

  void some_init_1(some _this_, int value, int another) {
    _this_.value = value;
    _this_.another = another;
  }

  int some_get_value_0(some _this_) {
    return _this_.value;
  }

  int another_0(some _this_) {
    return _this_.another;
  }

  @Test
  public void testSome() {
    some s = new some();
    some_init_1(s, 32, 64);
  }

  @Test
  public void testTac() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class strtemp {                                         //001 \n");
    sb.append("  int x;                                                //002 \n");
    sb.append("  strtemp(int x) {                                      //003 \n");
    sb.append("    this.x = x;                                         //004 \n");
    sb.append("  }                                                     //005 \n");
    sb.append("}                                                       //006 \n");
    sb.append("class type {                                            //007 \n");
    sb.append("  strtemp value;                                        //008 \n");
    sb.append("  type(strtemp value) {                                 //009 \n");
    sb.append("    this.value = value;                                 //010 \n");
    sb.append("  }                                                     //011 \n");
    sb.append("}                                                       //012 \n");
    sb.append("class token {                                           //013 \n");
    sb.append("  type type;                                            //014 \n");
    sb.append("  token(type type) {                                    //015 \n");
    sb.append("    this.type = type;                                   //016 \n");
    sb.append("  }                                                     //017 \n");
    sb.append("}                                                       //018 \n");
    sb.append("class test {                                            //019 \n");
    sb.append("  void fn() {                                           //020 \n");
    sb.append("    token tok1 = new token(new type(new strtemp(1)));   //021 \n");
    sb.append("  }                                                     //022 \n");
    sb.append("}                                                       //023 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    ClassDeclaration c = unit.getClassByName("test");
//    ExprExpression expr = c.getMethods().get(0).getBlock().getBlockItems().get(0).getLocalVariable()
//        .getSimpleInitializer();
//    TacGenerator tcg = new TacGenerator();
//    tcg.gen(expr);
//    System.out.println(tcg.txt1(";\n"));

        UnitToText text = new UnitToText(unit);
        System.out.println(UtilSrcToStringLevel.tos(text.toString()));

    //    // and try reparse it again
    //    InstantiationUnit cleanUnit = new ParserMain(new StringBuilder(text.toString())).parseInstantiationUnit();
    //    for (ClassDeclaration c : cleanUnit.getClasses()) {
    //      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    //    }
  }

}
