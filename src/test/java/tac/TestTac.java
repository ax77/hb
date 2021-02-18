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
    sb.append("class str {                                        \n");
    sb.append("  int x;                                               \n");
    sb.append("  str(int x) {                                     \n");
    sb.append("    this.x = x;                                        \n");
    sb.append("  }                                                    \n");
    sb.append("}                                                      \n");
    sb.append("class main_class {                                     \n");
    sb.append("  void main() {                                         \n");
    sb.append("    str x1 = new str(1);                       \n");
    sb.append("    str x2 = new str(2); str x3 = x2;                            \n");
    sb.append("  }                                                    \n");
    sb.append("}                                                      \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();

    UnitToText text = new UnitToText(unit);
    System.out.println(UtilSrcToStringLevel.tos(text.toString()));

  }

}
