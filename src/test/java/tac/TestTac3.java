package tac;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import ast_main.ParserMain;
import ast_method.ClassMethodDeclaration;
import ast_st3_tac.TacGenerator;
import ast_st3_tac.ir.FlatCodeItem;
import ast_unit.InstantiationUnit;
import ast_vars.VarDeclarator;

public class TestTac3 {

  @Test
  public void testTac() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class str {                                      //001 \n");
    sb.append("  int value;                                     //002 \n");
    sb.append("  str() {                                        //003 \n");
    sb.append("  }                                              //004 \n");
    sb.append("  str(int value) {                               //005 \n");
    sb.append("    this.value = value;                          //006 \n");
    sb.append("  }                                              //007 \n");
    sb.append("  boolean isEmpty() {                            //008 \n");
    sb.append("    return 0 == 0;                               //009 \n");
    sb.append("  }                                              //010 \n");
    sb.append("}                                                //011 \n");
    sb.append("class main_class {                               //012 \n");
    sb.append("  void main() {                                  //013 \n");
    sb.append("    str s1 = new str();                          //014 \n");
    sb.append("    str s2 = new str(1);                         //015 \n");
    sb.append("    str s3 = ?(s1.isEmpty(), s2, new str(32));   //016 \n");
    sb.append("    //int c = ?(0==0, 1, 2);   //016 \n");
    sb.append("  }                                              //017 \n");
    sb.append("}                                                //018 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    final ClassMethodDeclaration mainMethod = unit.getClassByName("main_class").getMethods().get(0);
    VarDeclarator var = mainMethod.getBlock().getBlockItems().get(2).getLocalVariable();
    TacGenerator tcg = new TacGenerator(var, mainMethod);
    List<FlatCodeItem> items = tcg.getRv();
    for (FlatCodeItem item : items) {
      System.out.println(item.toString());
    }
  }

}
