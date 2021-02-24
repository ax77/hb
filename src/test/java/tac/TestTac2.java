package tac;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_st5_stmts.StmtGenerator;
import ast_stmt.StmtBlock;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class TestTac2 {

  @Test
  public void testTac() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class str {                               \n");
    sb.append("  int value;                              \n");
    sb.append("  str() {                                 \n");
    sb.append("  }                                       \n");
    sb.append("  str(int value) {                        \n");
    sb.append("    this.value = value;                   \n");
    sb.append("  }                                       \n");
    sb.append("}                                         \n");
    sb.append("class main_class {                        \n");
    sb.append("  void main() {                           \n");
    sb.append("    str s1 = new str();                   \n");
    sb.append("    str s2 = new str(1);                  \n");
    sb.append("    str s3 = s2;                          \n");
    sb.append("    s3 = ?(0==0, s2, s1);                              \n");
    sb.append("    s2 = s3;                              \n");
    sb.append("    {                                     \n");
    sb.append("      str s4 = new str();                 \n");
    sb.append("      str s5 = s4;                        \n");
    sb.append("      s5 = s4;                            \n");
    sb.append("    }                                     \n");
    sb.append("    for(int i = 0; i < 8; i += 1) {       \n");
    sb.append("      str s6 = new str();                 \n");
    sb.append("      if(i == 5) {                        \n");
    sb.append("        continue;                         \n");
    sb.append("      }                                   \n");
    sb.append("      if(i == 7) {                        \n");
    sb.append("        for(int j = 0; j < 32; j += 1) {  \n");
    sb.append("          str s7 = new str(j);            \n");
    sb.append("          if(j == 1) {                    \n");
    sb.append("            break;                        \n");
    sb.append("          }                               \n");
    sb.append("        }                                 \n");
    sb.append("      }                                   \n");
    sb.append("    } //str s8 = new str();                                     \n");
    sb.append("    return;                               \n");
    sb.append("  }                                       \n");
    sb.append("}                                         \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    StmtBlock block = unit.getClassByName("main_class").getMethods().get(0).getBlock();
    StmtGenerator gn = new StmtGenerator(block);

    assertEquals(1, gn.getAllStmtBreak().size());
    assertEquals(7, gn.getAllVarDecls().size());
    assertEquals(7, gn.getAllStmtBlock().size());
    assertEquals(1, gn.getAllStmtContinue().size());
    assertEquals(3, gn.getAllStmtSelect().size());
    assertEquals(3, gn.getAllStmtExpression().size());
    assertEquals(19, gn.getAllBlockItems().size());
    assertEquals(1, gn.getAllStmtReturn().size());

  }

}
