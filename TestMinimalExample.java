package njast;

import org.junit.Test;

import njast.ast_nodes.top.TopLevelCompilationUnit;
import njast.ast_visitors.AstVisitorTypeApplier;
import njast.main.ParserMain;
import njast.parse.Parse;

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
    sb.append(" /*001*/  class C {  \n");
    sb.append(" /*031*/    void f_error_7(int paramvar) {                     \n");
    sb.append(" /*032*/      int jxxx = 0;                           \n");
    sb.append(" /*033*/      for (int i = 0; i < 8; i += 1) {     \n");
    sb.append(" /*034*/         for (int j = 0; j < 8; j += 1) {   \n");
    sb.append(" /*035*/        }                                  \n");
    sb.append(" /*036*/      }                                    \n");
    sb.append(" /*037*/    }                                      \n");
    sb.append(" /*038*/  }                                        \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TopLevelCompilationUnit unit = p.parse();

    AstVisitorTypeApplier applier = new AstVisitorTypeApplier();
    applier.visit(unit);

  }

}
