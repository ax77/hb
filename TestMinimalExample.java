package njast;

import java.io.IOException;

import org.junit.Test;

import njast.ast_nodes.expr.ExprExpression;
import njast.ast_nodes.stmt.StmtBlockStatement;
import njast.ast_nodes.top.TopLevelCompilationUnit;
import njast.ast_visitors.AstVisitorXml;
import njast.main.ParserMain;
import njast.parse.Parse;

public class TestMinimalExample {

  @Test
  public void testMinimalClass() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  class X {                            \n");
    sb.append(" /*002*/    public int x;                      \n");
    sb.append(" /*003*/  }                                    \n");
    sb.append(" /*004*/  class Y {                            \n");
    sb.append(" /*005*/    public X x;                        \n");
    sb.append(" /*006*/    X y(int i) { return x; }           \n");
    sb.append(" /*007*/  }                                    \n");
    sb.append(" /*008*/  class Z {                            \n");
    sb.append(" /*009*/    public Y y;                        \n");
    sb.append(" /*010*/    Y z(int i) { return y; }           \n");
    sb.append(" /*011*/  }                                    \n");
    sb.append(" /*012*/  public class C {                     \n");
    sb.append(" /*013*/    int func(int a) {                  \n");
    sb.append(" /*014*/      Z nclass = new Z();              \n");
    sb.append(" /*015*/      return (nclass).z(32).y(64).x;   \n");
    sb.append(" /*016*/    }                                  \n");
    sb.append(" /*017*/  }                                    \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TopLevelCompilationUnit unit = p.parse();

    for (StmtBlockStatement bs : unit.getTypeDeclarations().get(3).getClassDeclaration().getMethodDeclaration().get(0)
        .getBody().getBlockStatements().getBlockStatements()) {
      if (bs.getStatement() != null) {
        final ExprExpression expr = bs.getStatement().getSreturn().getExpr();
        AstVisitorXml vis = new AstVisitorXml();
        expr.accept(vis);
        System.out.println(vis.getText());
      }
    }

  }

}
