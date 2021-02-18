package tac;

import java.io.IOException;

import org.junit.Test;

import ast_expr.ExprExpression;
import ast_main.ParserMain;
import ast_st3_tac.TacGenerator;
import ast_unit.InstantiationUnit;

public class TestTac {

  @Test
  public void testTac() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class strtemp {                                             //013 \n");
    sb.append("  int x;                                                    //014 \n");
    sb.append("  strtemp(int x) {                                          //015 \n");
    sb.append("    this.x = x;                                             //016 \n");
    sb.append("  }                                                         //017 \n");
    sb.append("}                                                           //021 \n");
    sb.append("class type {                                                //022 \n");
    sb.append("  strtemp value;                                            //023 \n");
    sb.append("  type(strtemp value) {                                     //024 \n");
    sb.append("    this.value = value;                                     //025 \n");
    sb.append("  }                                                         //026 \n");
    sb.append("}                                                           //030 \n");
    sb.append("class token {                                               //031 \n");
    sb.append("  type type;                                                //032 \n");
    sb.append("  token(type type) {                                        //033 \n");
    sb.append("    this.type = type;                                       //034 \n");
    sb.append("  }                                                         //035 \n");
    sb.append("}                                                           //036 \n");
    sb.append("class main_class {                                          //037 \n");
    sb.append("  void main() {                                             //038 \n");
    sb.append("    strtemp x1 = new strtemp(1);                            //040 \n");
    sb.append("    type x2 = new type(x1);                                 //041 \n");
    sb.append("    token x3 = new token(x2);                               //042 \n");
    sb.append("    token tok1 = new token(new type(new strtemp(1)));       //043 \n");
    sb.append("    tok1 = new token(new type(new strtemp(1)));             //051 \n");
    sb.append("  }                                                         //052 \n");
    sb.append("}                                                           //053 \n");
    //@formatter:on


    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    ExprExpression expr = unit.getClassByName("main_class").getMethods().get(0).getBlock().getBlockItems().get(4).getStatement().getExprStmt();
    TacGenerator gen=new TacGenerator(expr);
    System.out.println(gen.txt1(";\n"));

  }

}
