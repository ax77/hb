package njast;

import org.junit.Test;

import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.top.InstantiationUnit;
import njast.ast_nodes.top.TopLevelCompilationUnit;
import njast.main.ParserMain;
import njast.parse.Parse;
import njast.templates.InstatantiationUnitBuilder;

public class TestMinimalExample {

  //to define a symbol in a function or nested block and check redefinition
  //1) check the whole function scope
  //2) check the current block
  //
  //to bind a symbol in a expression - 
  //1) block scope
  //2) function scope
  //3) class scope
  //4) file scope
  //
  //note: function parameters also a variables in a function scope

  @Test
  public void testSymtab() throws Exception {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*072*/    class C {                                                          \n");
    sb.append(" /*073*/      public void testList() {                  \n");
    sb.append(" /*077*/        for (int x=0,y=1; x<1, y<2; x+=1, y+=2) { int a;int b; int c; a=b; b=c;      \n");
    sb.append(" /*078*/        }                                                              \n");
    sb.append(" /*079*/      }                                                                \n");
    sb.append(" /*080*/    }                                                                  \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TopLevelCompilationUnit unit = p.parse();

    InstantiationUnit instantiationUnit = new InstatantiationUnitBuilder(unit).getInstantiationUnit();
    for (ClassDeclaration clazz : instantiationUnit.getClasses()) {
      System.out.println(UtilSrcToStringLevel.tos(clazz.toString()));
    }

  }

}
