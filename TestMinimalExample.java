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
    sb.append(" /*001*/  class Thing {                             \n");
    sb.append(" /*002*/    int z;                                  \n");
    sb.append(" /*003*/  }                                         \n");
    sb.append(" /*004*/  class Some {                              \n");
    sb.append(" /*005*/    Thing th;                               \n");
    sb.append(" /*006*/    int x = 1;                              \n");
    sb.append(" /*007*/  }                                         \n");
    sb.append(" /*008*/  class Idn {                               \n");
    sb.append(" /*009*/    int y = 1;                              \n");
    sb.append(" /*010*/    Some sc;                                \n");
    sb.append(" /*011*/    int func() {                            \n");
    sb.append(" /*012*/      return y + this.y + sc.x + sc.th.z;   \n");
    sb.append(" /*013*/    }                                       \n");
    sb.append(" /*014*/  }                                         \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TopLevelCompilationUnit unit = p.parse();

    AstVisitorTypeApplier applier = new AstVisitorTypeApplier();
    applier.visit(unit);

  }

}
