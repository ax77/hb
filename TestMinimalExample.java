package njast;

import org.junit.Test;

import njast.ast_nodes.top.TopLevelCompilationUnit;
import njast.ast_visitors.ApplyCompilationUnit;
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
    sb.append(" /*001*/  class Thing {           \n");
    sb.append(" /*002*/    int z;                \n");
    sb.append(" /*003*/    int fz() {            \n");
    sb.append(" /*004*/      return z;           \n");
    sb.append(" /*005*/    }                     \n");
    sb.append(" /*006*/  }                       \n");
    sb.append(" /*007*/  class Some {            \n");
    sb.append(" /*008*/    Thing th;             \n");
    sb.append(" /*009*/    int x = 1;            \n");
    sb.append(" /*010*/    int fx() {            \n");
    sb.append(" /*011*/      return x;           \n");
    sb.append(" /*012*/    }                     \n");
    sb.append(" /*013*/  }                       \n");
    sb.append(" /*014*/  class Idn {             \n");
    sb.append(" /*015*/    int y = 1;            \n");
    sb.append(" /*016*/    Some sc;              \n");
    sb.append(" /*017*/    int func() {          \n");
    sb.append(" /*018*/      return zero(1,2,3,4)       \n");
    sb.append(" /*019*/          + sc.fx()       \n");
    sb.append(" /*020*/          + y + this.y    \n");
    sb.append(" /*021*/          + sc.x          \n");
    sb.append(" /*022*/          + sc.th.z       \n");
    sb.append(" /*023*/          + sc.th.fz();   \n");
    sb.append(" /*024*/    }                     \n");
    sb.append(" /*025*/    int zero(int fp1, int fp2, int fp3, int fp4) {          \n");
    sb.append(" /*026*/      return 0;           \n");
    sb.append(" /*027*/    }                     \n");
    sb.append(" /*028*/  }                       \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TopLevelCompilationUnit unit = p.parse();

    ApplyCompilationUnit applier = new ApplyCompilationUnit();
    applier.visit(unit);

  }

}
