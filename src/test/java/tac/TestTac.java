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
    sb.append("class main_class { int fff; int hhh(){return 0;} void ggg(){}                                    \n");
    sb.append("  void main() {                                         \n");
    sb.append("    int a = 1;                                       \n");
    sb.append("    int b = 2;                            \n");
    sb.append("    int c = 0;                            \n");
    sb.append("    fff = -a;                            \n");
    sb.append("  }                                                    \n");
    sb.append("}                                                      \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    ExprExpression expr = unit.getClassByName("main_class").getMethods().get(2).getBlock().getBlockItems().get(3).getStatement().getExprStmt();
    TacGenerator gen=new TacGenerator(expr);
    System.out.println(gen.txt1(";\n"));

  }

}
