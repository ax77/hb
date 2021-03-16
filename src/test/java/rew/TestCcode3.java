package rew;

import java.io.IOException;

import org.junit.Test;

import _st7_codeout.Codeout;
import _st7_codeout.CodeoutBuilder;
import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.InstantiationUnit;

public class TestCcode3 {

  @Test
  public void test1() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class opt {                                  //001 \n");
    sb.append("  string value;                              //002 \n");
    sb.append("  opt() {                                    //003 \n");
    sb.append("    this.value = \"opt is init\";            //004 \n");
    sb.append("  }                                          //005 \n");
    sb.append("  string get_value() {                       //006 \n");
    sb.append("    return value;                            //007 \n");
    sb.append("  }                                          //008 \n");
    sb.append("  string gets(int i) {                       //009 \n");
    sb.append("    if(i == 1) {                             //010 \n");
    sb.append("      return \"a\";                          //011 \n");
    sb.append("    }                                        //012 \n");
    sb.append("    return \"b\";                            //013 \n");
    sb.append("  }                                          //014 \n");
    sb.append("}                                            //015 \n");
    sb.append("class main_class {                           //016 \n");
    sb.append("  int main() {                               //017 \n");
    sb.append("    opt o = new opt();                       //018 \n");
    sb.append("    std.print(\"[%s]\\n\", o.get_value());   //019 \n");
    sb.append("    string s = o.gets(1);                    //020 \n");
    sb.append("    std.print(\"%s\\n\", s);                 //021 \n");
    sb.append("    return 0;                                //022 \n");
    sb.append("  }                                          //023 \n");
    sb.append("}                                            //024 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    System.out.println(result.toString());

  }
}
