package tac;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import ast_main.ParserMain;
import ast_st5_stmts.StmtGenerator;
import ast_stmt.StmtBlock;
import ast_unit.InstantiationUnit;

public class TestTac2 {

  @Test
  public void testTac() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class str {                                //001 \n");
    sb.append("  int value;                               //002 \n");
    sb.append("  str() {                                  //003 \n");
    sb.append("  }                                        //004 \n");
    sb.append("  str(int value) {                         //005 \n");
    sb.append("    this.value = value;                    //006 \n");
    sb.append("  }                                        //007 \n");
    sb.append("}                                          //008 \n");
    sb.append("class main_class {                         //009 \n");
    sb.append("  void main() {                            //010 \n");
    sb.append("    str s1 = new str();                    //011 \n");
    sb.append("    str s2 = new str(1);                   //012 \n");
    sb.append("    str s3 = s2;                           //013 \n");
    sb.append("    {                                      //014 \n");
    sb.append("      str s4 = new str();                  //015 \n");
    sb.append("      str s5 = s4;                         //016 \n");
    sb.append("    }                                      //017 \n");
    sb.append("    for(int i = 0; i < 8; i += 1) {        //018 \n");
    sb.append("      str s6 = new str();                  //019 \n");
    sb.append("      if(i == 5) {                         //020 \n");
    sb.append("        continue;                          //021 \n");
    sb.append("      }                                    //022 \n");
    sb.append("      if(i == 7) {                         //023 \n");
    sb.append("        for(int j = 0; j < 32; j += 1) {   //024 \n");
    sb.append("          str s7 = new str(j);             //025 \n");
    sb.append("          if(j == 1) {                     //026 \n");
    sb.append("            break;                         //027 \n");
    sb.append("          }                                //028 \n");
    sb.append("        }                                  //029 \n");
    sb.append("      }                                    //030 \n");
    sb.append("    }                                      //031 \n");
    sb.append("  }                                        //032 \n");
    sb.append("}                                          //033 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    StmtBlock block = unit.getClassByName("main_class").getMethods().get(0).getBlock();
    StmtGenerator gn = new StmtGenerator(block);

    assertEquals(1, gn.getAllStmtBreak().size());
    assertEquals(gn.getAllStmtSelect().get(2).getTrueStatement(), gn.getAllStmtBreak().get(0).getClosestBlock());
    assertEquals(7, gn.getAllVarDecls().size());
    assertEquals(7, gn.getAllStmtBlock().size());
    assertEquals(1, gn.getAllStmtContinue().size());
    assertEquals(3, gn.getAllStmtSelect().size());

  }

}
