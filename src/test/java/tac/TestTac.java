package tac;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_main.ParserMain;
import ast_st3_tac.CodegenContext;
import ast_st3_tac.TacGenerator;
import ast_st3_tac.UnitToText;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class TestTac {

  @Test
  public void testTac() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class strtemp {                                        \n");
    sb.append("  int x;                                               \n");
    sb.append("  strtemp(int x) {                                     \n");
    sb.append("    this.x = x;                                        \n");
    sb.append("  }                                                    \n");
    sb.append("}                                                      \n");
    sb.append("class type {                                           \n");
    sb.append("  strtemp value;                                       \n");
    sb.append("  type(strtemp value) {                                \n");
    sb.append("    this.value = value;                                \n");
    sb.append("  }                                                    \n");
    sb.append("}                                                      \n");
    sb.append("class token {                                          \n");
    sb.append("  type type;                                           \n");
    sb.append("  token(type type) {                                   \n");
    sb.append("    this.type = type;                                  \n");
    sb.append("  }                                                    \n");
    sb.append("}                                                      \n");
    sb.append("class main_class {                                     \n");
    sb.append("  void main() {                                        \n");
    sb.append("    strtemp x1 = new strtemp(1);                       \n");
    sb.append("    type x2 = new type(x1);                            \n");
    sb.append("    token x3 = new token(x2);                          \n");
    sb.append("    token tok1 = new token(new type(new strtemp(1)));  \n");
    sb.append("  }                                                    \n");
    sb.append("}                                                      \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();

    CodegenContext context = new CodegenContext();
    UnitToText text = new UnitToText(context, unit);
    System.out.println(UtilSrcToStringLevel.tos(text.toString()));

  }

}
