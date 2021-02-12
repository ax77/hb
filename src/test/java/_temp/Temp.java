package _temp;

import java.io.IOException;

import org.junit.Test;

import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_st3_tac.TacGenerator;
import ast_st3_tac.UnitToText;
import ast_unit.CompilationUnit;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class Temp {

  @Test
  public void temp() throws Exception {

    //    CompilationUnit unit = new ParserMain("tests/temp.hb").parseCompilationUnit();
    //    for (ClassDeclaration c : unit.getClasses()) {
    //      System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    //    }

    InstantiationUnit unit = new ParserMain("tests/temp.hb").parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

  }

  @Test
  public void testTac() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class tok {                //001 \n");
    sb.append("  int value; int fn(){ int zzz=0; return value;}               //002 \n");
    sb.append("}                          //003 \n");
    sb.append("class type {               //004 \n");
    sb.append("  tok token;               //005 \n");
    sb.append("  void fn() {              //006 \n");
    sb.append("    int a = 0; int b = 1;             //008 \n");
    sb.append("    token.value = a + b;   //009 \n");
    sb.append("    a = token.value;       //010 \n");
    sb.append("    b = a + a;             //011 \n");
    sb.append("    { int xbx=1;    if(a==0){ int rrr=0; b+=1; } else if(a==1) {b-=1;} else if(b-1 == 0) {a-=1+2-3;} else{a-=2;}                  //012 \n");
    sb.append("      a+=token.fn();                //013 \n");
    sb.append("      b=1;                //014 \n");
    sb.append("    }                      //015 \n");
    sb.append("  }                        //016 \n");
    sb.append("}                          //017 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    UnitToText text = new UnitToText(unit);
    System.out.println(UtilSrcToStringLevel.tos(text.toString()));

    // and try reparse it again
    InstantiationUnit cleanUnit = new ParserMain(new StringBuilder(text.toString())).parseInstantiationUnit();
    for (ClassDeclaration c : cleanUnit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }
  }
}
