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
    sb.append(" class str { str(){} } class main_class {                                           \n");
    sb.append("  void main() {                                             \n");
    sb.append("    int a=0; if(a==0) {a=1;}else if(a==1){a=2;}else if(a==-2){a=3;}else{a=-32;} \n");
    sb.append("  }                                                        \n");
    sb.append("}                                                        \n");
    //@formatter:on
    
    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    UnitToText text = new UnitToText(unit);
    System.out.println(UtilSrcToStringLevel.tos(text.toString()));

  }

}
