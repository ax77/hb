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
//    sb.append("class tree {                                                //001 \n");
//    sb.append("  tree lhs;                                                 //002 \n");
//    sb.append("  tree rhs;                                                 //003 \n");
//    sb.append("  int value;                                                //004 \n");
//    sb.append("  tree(int value) {                                         //005 \n");
//    sb.append("    this.value = value;                                     //006 \n");
//    sb.append("  }                                                         //007 \n");
//    sb.append("  tree(tree lhs, tree rhs) {                                //008 \n");
//    sb.append("    this.lhs = lhs;                                         //009 \n");
//    sb.append("    this.rhs = rhs;                                         //010 \n");
//    sb.append("  }                                                         //011 \n");
//    sb.append("}                                                           //012 \n");
//    sb.append("class strtemp {                                             //013 \n");
//    sb.append("  int x;                                                    //014 \n");
//    sb.append("  strtemp(int x) {                                          //015 \n");
//    sb.append("    this.x = x;                                             //016 \n");
//    sb.append("  }                                                         //017 \n");
//    sb.append("  int getX() {                                              //018 \n");
//    sb.append("    return x;                                               //019 \n");
//    sb.append("  }                                                         //020 \n");
//    sb.append("}                                                           //021 \n");
//    sb.append("class type {                                                //022 \n");
//    sb.append("  strtemp value;                                            //023 \n");
//    sb.append("  type(strtemp value) {                                     //024 \n");
//    sb.append("    this.value = value;                                     //025 \n");
//    sb.append("  }                                                         //026 \n");
//    sb.append("  strtemp getValue() {                                      //027 \n");
//    sb.append("    return value;                                           //028 \n");
//    sb.append("  }                                                         //029 \n");
//    sb.append("}                                                           //030 \n");
//    sb.append("class token {                                               //031 \n");
//    sb.append("  type type;                                                //032 \n");
//    sb.append("  token(type type) {                                        //033 \n");
//    sb.append("    this.type = type;                                       //034 \n");
//    sb.append("  }                                                         //035 \n");
//    sb.append("}                                                           //036 \n");
    sb.append("class main_class {   int fff;                                        //037 \n");
    sb.append("  void main() {                                            //038 \n");
//    sb.append("    tree tree = new tree(new tree(1), new tree(2));         //039 \n");
//    sb.append("    strtemp x1 = new strtemp(1);                            //040 \n");
//    sb.append("    type x2 = new type(x1);                                 //041 \n");
//    sb.append("    token x3 = new token(x2);                               //042 \n");
//    sb.append("    token tok1 = new token(new type(new strtemp(1)));       //043 \n");
//    sb.append("    int res = tok1.type.value.x;                            //044 \n");
//    sb.append("    tok1.type.value.x = 1024;                               //045 \n");
    sb.append("    int a = 0;                                              //046 \n");
    sb.append("    int b = 1;                                              //047 \n");
    sb.append("    int c = 2;                                              //048 \n");
    sb.append("    int e = -1; e = fff+ a + b + c;                                      //049 \n");
//    sb.append("    int f = x1.getX();                                      //050 \n");
//    sb.append("    int g = x2.getValue().getX();                           //051 \n");
    sb.append("  }                                                         //052 \n");
    sb.append("}                                                           //053 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    ClassDeclaration c = unit.getClassByName("main_class");
    ExprExpression expr = c.getMethods().get(0).getBlock().getBlockItems().get(4).getStatement().getExprStmt();
    TacGenerator tcg = new TacGenerator();
    tcg.gen(expr);
    System.out.println(tcg.txt1(";\n"));

    //    UnitToText text = new UnitToText(unit);
    //    System.out.println(UtilSrcToStringLevel.tos(text.toString()));

    //    // and try reparse it again
    //    InstantiationUnit cleanUnit = new ParserMain(new StringBuilder(text.toString())).parseInstantiationUnit();
    //    for (ClassDeclaration c : cleanUnit.getClasses()) {
    //      //System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    //    }
  }

}
