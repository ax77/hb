package tac;

import java.io.IOException;

import org.junit.Test;

import ast_class.ClassDeclaration;
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
    sb.append(" class main_class {                                           \n");
    sb.append("  void main() {                                             \n");
    sb.append("    for(int i=0;i<8;i+=1){ break; for(int j=0;j<8;j+=1) { continue; }} \n");
    sb.append("  }                                                        \n");
    sb.append("}                                                        \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      System.out.println(c.toString());
    }
    //    UnitToText text = new UnitToText(unit);
    //    System.out.println(UtilSrcToStringLevel.tos(text.toString()));

  }

}
