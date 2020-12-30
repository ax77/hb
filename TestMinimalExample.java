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
    sb.append(" /*001*/  class Idn {                        \n");
    sb.append(" /*002*/    int fieldvar = 64;               \n");
    sb.append(" /*003*/    int func() {                     \n");
    sb.append(" /*004*/      int methodvar = 32;            \n");
    sb.append(" /*005*/      return methodvar + fieldvar;   \n");
    sb.append(" /*006*/    }                                \n");
    sb.append(" /*007*/  }                                  \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TopLevelCompilationUnit unit = p.parse();

    AstVisitorTypeApplier applier = new AstVisitorTypeApplier();
    applier.visit(unit);

  }

}
