package njast;

import java.io.IOException;

import org.junit.Test;

import njast.ast_flow.BlockStatement;
import njast.ast_flow.expr.CExpression;
import njast.ast_top.CompilationUnit;
import njast.main.ParserMain;
import njast.parse.Parse;

public class TestMinimalExample {

  @Test
  public void testMinimalClass() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  // package test;                              \n");
    sb.append(" /*002*/  public class C {                              \n");
    sb.append(" /*003*/    private int field;                          \n");
    sb.append(" /*004*/    static {                                    \n");
    sb.append(" /*005*/    }                                           \n");
    sb.append(" /*006*/    public C() {                                \n");
    sb.append(" /*007*/      field = 128;                              \n");
    sb.append(" /*008*/    }                                           \n");
    sb.append(" /*009*/    public C(int constructorParam) {            \n");
    sb.append(" /*010*/      field = constructorParam;                 \n");
    sb.append(" /*011*/    }                                           \n");
    sb.append(" /*012*/   // public int func0() {                        \n");
    sb.append(" /*013*/      //return 128;                               \n");
    sb.append(" /*014*/    // }                                           \n");
    sb.append(" /*015*/    public int func1(int a, int b) {            \n");
    sb.append(" /*016*/      int local = 32;                           \n");
    sb.append(" /*017*/      return /*field + local + a + b + */ func0().func().abc;   \n");
    sb.append(" /*018*/    }                                           \n");
    sb.append(" /*019*/  }                                             \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    CompilationUnit unit = p.parse();

    for (BlockStatement bs : unit.getTypeDeclarations().get(0).getClassDeclaration().getMethodDeclaration().get(0)
        .getBody().getBlockStatements().getBlockStatements()) {
      if (bs.getStatement() != null) {
        final CExpression expr = bs.getStatement().getExpr();
        System.out.println();
      }
    }

  }

}
