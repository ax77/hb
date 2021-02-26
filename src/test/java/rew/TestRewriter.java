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
    sb.append("    return true;                                   //009 \n");
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
    sb.append("  deinit {                                         //022 \n");
    sb.append("    value = -1;                                    //023 \n");
    sb.append("  }                                                //024 \n");
    sb.append("}                                                  //025 \n");
    sb.append("class node {                                       //026 \n");
    sb.append("  node prev;                                       //027 \n");
    sb.append("  node last;                                       //028 \n");
    sb.append("  int value;                                       //029 \n");
    sb.append("  node(node prev, node last, int value) {          //030 \n");
    sb.append("    this.prev = prev;                              //031 \n");
    sb.append("    this.last = last;                              //032 \n");
    sb.append("    this.value = value;                            //033 \n");
    sb.append("  }                                                //034 \n");
    sb.append("  deinit {                                         //035 \n");
    sb.append("    this.prev = this.last;                         //036 \n");
    sb.append("  }                                                //037 \n");
    sb.append("}                                                  //038 \n");
    sb.append("class type {                                       //039 \n");
    sb.append("  int k;                                           //040 \n");
    sb.append("  str v;                                           //041 \n");
    sb.append("  node n;                                          //042 \n");
    sb.append("}                                                  //043 \n");
    sb.append("class main_class {                                 //044 \n");
    sb.append("  void main() {                                    //045 \n");
    sb.append("    str s1 = new str();                            //046 \n");
    sb.append("    str s2 = new str(1);                           //047 \n");
    sb.append("    if(s1.isEmpty()) {                             //048 \n");
    sb.append("      return;                                      //049 \n");
    sb.append("    }                                              //050 \n");
    sb.append("    while(!s1.isEmpty()) {                         //051 \n");
    sb.append("      s2.append(s1.remove());                      //052 \n");
    sb.append("    }                                              //053 \n");
    sb.append("    for(int i=0; i<s2.length(); i+=1) {            //054 \n");
    sb.append("      s1.append(s2.get(i));                        //055 \n");
    sb.append("      if(i == 2) {                                 //056 \n");
    sb.append("        continue;                                  //057 \n");
    sb.append("      }                                            //058 \n");
    sb.append("      if(s1.length() == 2) {                       //059 \n");
    sb.append("        break;                                     //060 \n");
    sb.append("      }                                            //061 \n");
    sb.append("      str s3 = new str(i);                         //062 \n");
    sb.append("      if(s3.isEmpty()) {                           //063 \n");
    sb.append("        return;                                    //064 \n");
    sb.append("      }                                            //065 \n");
    sb.append("    }                                              //066 \n");
    sb.append("    for(str s5 = new str(32); !s5.isEmpty(); ) {   //067 \n");
    sb.append("      str s6tmp = new str();                       //068 \n");
    sb.append("      s6tmp.append(1);                             //069 \n");
    sb.append("    }                                              //070 \n");
    sb.append("    if(s1.isEmpty()) {                             //071 \n");
    sb.append("    } else {                                       //072 \n");
    sb.append("    }                                              //073 \n");
    sb.append("  }                                                //074 \n");
    sb.append("}                                                  //075 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    System.out.println(UtilSrcToStringLevel.tos(result.toString()));
  }

}
