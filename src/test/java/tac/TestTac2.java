package tac;

import java.io.IOException;

import org.junit.Test;

import ast_expr.ExprExpression;
import ast_main.ParserMain;
import ast_st3_tac.TacGenerator;
import ast_st3_tac.UnitToText;
import ast_unit.InstantiationUnit;
import ast_vars.VarDeclarator;
import utils.UtilSrcToStringLevel;

public class TestTac2 {

  @Test
  public void testTac() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" class str { str(){} } class main_class {                                          //037 \n");
    sb.append("  void main() {                                             //038 \n");
    sb.append("    str str1 = new str(); str str2 = str1;                                              //046 \n");
    sb.append("  }                                                         //052 \n");
    sb.append("}                                                           //053 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    UnitToText text = new UnitToText(unit);
    System.out.println(UtilSrcToStringLevel.tos(text.toString()));

  }

}
