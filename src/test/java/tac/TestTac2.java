package tac;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_method.ClassMethodDeclaration;
import ast_st5_stmts.StmtGenerator;
import ast_st5_stmts.execs.LinearBlock;
import ast_st5_stmts.execs.Rewriter;
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
    sb.append("  }  boolean isEmpty() { return 0==0; }                                     \n");
    sb.append("}                                         \n");
    sb.append("class main_class {                        \n");
    sb.append("  void main() {                           \n");
    sb.append("    str s1 = new str(); if(s1.isEmpty()){}                   \n");
    sb.append("  }                                       \n");
    sb.append("}                                         \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();

  }

}
