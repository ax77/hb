package rew;

import java.io.IOException;

import org.junit.Test;

import _st7_codeout.Codeout;
import _st7_codeout.CodeoutBuilder;
import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class TestRewriter {

  @Test
  public void testTac() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class str {                                        //001 \n");
    sb.append("  int value;                                       //002 \n");
    sb.append("  str() {                                          //003 \n");
    sb.append("  }                                                //004 \n");
    sb.append("  str(int value) {                                 //005 \n");
    sb.append("    this.value = value;                            //006 \n");
    sb.append("  }                                                //007 \n");
    sb.append("  boolean isEmpty() {                              //008 \n");
    sb.append("    return 0 == 0;                                 //009 \n");
    sb.append("  }                                                //010 \n");
    sb.append("  void append(int i) {                             //011 \n");
    sb.append("  }                                                //012 \n");
    sb.append("  int remove() {                                   //013 \n");
    sb.append("    return 0;                                      //014 \n");
    sb.append("  }                                                //015 \n");
    sb.append("  int length() {                                   //016 \n");
    sb.append("    return 0;                                      //017 \n");
    sb.append("  }                                                //018 \n");
    sb.append("  int get(int at) {                                //019 \n");
    sb.append("    return 0;                                      //020 \n");
    sb.append("  }                                                //021 \n");
    sb.append("  str opAssign(str lhs, str rhs) {                 //022 \n");
    sb.append("    if(lhs == rhs) {                               //026 \n");
    sb.append("      return lhs;                                  //027 \n");
    sb.append("    }                                              //028 \n");
    sb.append("    return rhs;                                    //029 \n");
    sb.append("  }                                                //030 \n");
    sb.append("}                                                  //031 \n");
    sb.append("class node {                                       //032 \n");
    sb.append("  node prev;                                       //033 \n");
    sb.append("  node last;                                       //034 \n");
    sb.append("  int value;                                       //035 \n");
    sb.append("}                                                  //036 \n");
    sb.append("class type {                                       //037 \n");
    sb.append("  int k;                                           //038 \n");
    sb.append("  str v;                                           //039 \n");
    sb.append("  node n;                                          //040 \n");
    sb.append("}                                                  //041 \n");
    sb.append("class main_class {                                 //042 \n");
    sb.append("  void main() {                                    //043 \n");
    sb.append("    str s1 = new str();                            //044 \n");
    sb.append("    str s2 = new str(1);                           //045 \n");
    sb.append("    if(s1.isEmpty()) {                             //046 \n");
    sb.append("      return;                                      //047 \n");
    sb.append("    }                                              //048 \n");
    sb.append("    while(!s1.isEmpty()) {                         //049 \n");
    sb.append("      s2.append(s1.remove());                      //050 \n");
    sb.append("    }                                              //051 \n");
    sb.append("    for(int i=0; i<s2.length(); i+=1) {            //052 \n");
    sb.append("      s1.append(s2.get(i));                        //053 \n");
    sb.append("      if(i == 2) {                                 //054 \n");
    sb.append("        continue;                                  //055 \n");
    sb.append("      }                                            //056 \n");
    sb.append("      if(s1.length() == 2) {                       //057 \n");
    sb.append("        break;                                     //058 \n");
    sb.append("      }                                            //059 \n");
    sb.append("      str s3 = new str(i);                         //060 \n");
    sb.append("      if(s3.isEmpty()) {                           //061 \n");
    sb.append("        return;                                    //062 \n");
    sb.append("      }                                            //063 \n");
    sb.append("    }                                              //064 \n");
    sb.append("    for(str s5 = new str(32); !s5.isEmpty(); ) {   //065 \n");
    sb.append("      str s6tmp = new str();                       //066 \n");
    sb.append("      s6tmp.append(1);                             //067 \n");
    sb.append("    }                                              //068 \n");
    sb.append("  }                                                //069 \n");
    sb.append("}                                                  //070 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    System.out.println(UtilSrcToStringLevel.tos(result.toString()));
  }

}
