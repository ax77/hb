package ast_st5_stmts.execs;

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

public class TestRewriter {

  @Test
  public void testTac() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class str {                               //001 \n");
    sb.append("  int value;                              //002 \n");
    sb.append("  str() {                                 //003 \n");
    sb.append("  }                                       //004 \n");
    sb.append("  str(int value) {                        //005 \n");
    sb.append("    this.value = value;                   //006 \n");
    sb.append("  }                                       //007 \n");
    sb.append("  boolean isEmpty() {                     //008 \n");
    sb.append("    return 0 == 0;                        //009 \n");
    sb.append("  }                                       //010 \n");
    sb.append("  void append(int i) {                    //011 \n");
    sb.append("  }                                       //012 \n");
    sb.append("  int remove() {                          //013 \n");
    sb.append("    return 0;                             //014 \n");
    sb.append("  }                                       //015 \n");
    sb.append("  int length() {                          //016 \n");
    sb.append("    return 0;                             //017 \n");
    sb.append("  }                                       //018 \n");
    sb.append("  int get(int at) {                       //019 \n");
    sb.append("    return 0;                             //020 \n");
    sb.append("  }                                       //021 \n");
    sb.append("}                                         //022 \n");
    sb.append("class main_class {                        //023 \n");
    sb.append("  void main() {                           //024 \n");
    sb.append("    str s1 = new str();                   //025 \n");
    sb.append("    str s2 = new str(1);                  //026 \n");
    sb.append("    while(!s1.isEmpty()) {                //027 \n");
    sb.append("      s2.append(s1.remove());             //028 \n");
    sb.append("    }                                     //029 \n");
    sb.append("    for(int i=0; i<s2.length(); i+=1) {   //030 \n");
    sb.append("      s1.append(s2.get(i));               //031 \n");
    sb.append("      if(i == 2) {                        //032 \n");
    sb.append("        continue;                         //033 \n");
    sb.append("      }                                   //034 \n");
    sb.append("      if(s1.length() == 2) {              //035 \n");
    sb.append("        break;                            //036 \n");
    sb.append("      }                                   //037 \n");
    sb.append("      str s3 = new str(i);                //038 \n");
    sb.append("    }                                     //039 \n");
    sb.append("  }                                       //040 \n");
    sb.append("}                                         //041 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();

    final ClassMethodDeclaration mainMethod = unit.getClassByName("main_class").getMethods().get(0);

    Rewriter rw = new Rewriter(mainMethod);
    LinearBlock result = rw.getResult();
    System.out.println(UtilSrcToStringLevel.tos(result.toString()));

  }

}
