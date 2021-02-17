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
    sb.append("class strtemp {                                             //013 \n");
    sb.append("  int x;                                                    //014 \n");
    sb.append("  strtemp(int x) {                                          //015 \n");
    sb.append("    this.x = x;                                             //016 \n");
    sb.append("  }                                                         //017 \n");
    sb.append("}                                                           //021 \n");
    sb.append("class main_class {                                          //037 \n");
    sb.append("  void main() {                                             //038 \n");
    sb.append("    strtemp x1 = new strtemp(1); int zzz = x1.x;                            //040 \n");
    sb.append("  }                                                         //052 \n");
    sb.append("}                                                           //053 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();

    UnitToText text = new UnitToText(unit);
    System.out.println(UtilSrcToStringLevel.tos(text.toString()));

  }

}
