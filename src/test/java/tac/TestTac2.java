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
    sb.append("class str {                 //001 \n");
    sb.append("  int value;                //002 \n");
    sb.append("  str() {                   //003 \n");
    sb.append("  }                         //004 \n");
    sb.append("  str(int value) {          //005 \n");
    sb.append("    this.value = value;     //006 \n");
    sb.append("  }                         //007 \n");
    sb.append("}                           //008 \n");
    sb.append("class main_class {          //009 \n");
    sb.append("  void main() {             //010 \n");
    sb.append("    str s1 = new str();     //011 \n");
    sb.append("    str s2 = new str(1);    //012 \n");
    sb.append("    str s3 = s2;            //013 \n");
    sb.append("    {                       //014 \n");
    sb.append("      str s4 = new str();   //015 \n");
    sb.append("      str s5 = s4;          //016 \n");
    sb.append("    }                       //017 \n");
    sb.append("  }                         //018 \n");
    sb.append("}                           //019 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    UnitToText text = new UnitToText(unit);
    System.out.println(UtilSrcToStringLevel.tos(text.toString()));

  }

}
