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
    sb.append(" /*010*/    Y z(int i) { return this.y; }      \n");
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
        // System.out.println(vis.getText());
      }
    }

  }

  @Test
  public void testSymtab() throws Exception {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  class C {                                \n");
    sb.append(" /*002*/    void f_error_1() {                     \n");
    sb.append(" /*003*/      a = 64;                              \n");
    sb.append(" /*004*/      int a = 32;                          \n");
    sb.append(" /*005*/    }                                      \n");
    sb.append(" /*006*/    void f_error_2() {                     \n");
    sb.append(" /*007*/      int a = 32;                          \n");
    sb.append(" /*008*/      {                                    \n");
    sb.append(" /*009*/        int a = 64;                        \n");
    sb.append(" /*010*/      }                                    \n");
    sb.append(" /*011*/    }                                      \n");
    sb.append(" /*012*/    void f_error_3(int a) {                \n");
    sb.append(" /*013*/      int a = 32;                          \n");
    sb.append(" /*014*/    }                                      \n");
    sb.append(" /*015*/    void f_error_4() {                     \n");
    sb.append(" /*016*/      int i = 32;                          \n");
    sb.append(" /*017*/      for (int i = 0; i < 8; i += 1) {     \n");
    sb.append(" /*018*/      }                                    \n");
    sb.append(" /*019*/    }                                      \n");
    sb.append(" /*020*/    void f_error_5() {                     \n");
    sb.append(" /*021*/      for (int i = 0; i < 8; i += 1) {     \n");
    sb.append(" /*022*/        int i = 32;                        \n");
    sb.append(" /*023*/      }                                    \n");
    sb.append(" /*024*/    }                                      \n");
    sb.append(" /*025*/    void f_error_6() {                     \n");
    sb.append(" /*026*/      for (int i = 0; i < 8; i += 1) {     \n");
    sb.append(" /*027*/        for (int i = 0; i < 8; i += 1) {   \n");
    sb.append(" /*028*/        }                                  \n");
    sb.append(" /*029*/      }                                    \n");
    sb.append(" /*030*/    }                                      \n");
    sb.append(" /*031*/    void f_error_7() {                     \n");
    sb.append(" /*032*/      int j = 0;                           \n");
    sb.append(" /*033*/      for (int i = 0; i < 8; i += 1) {     \n");
    sb.append(" /*034*/        for (int j = 0; j < 8; j += 1) {   \n");
    sb.append(" /*035*/        }                                  \n");
    sb.append(" /*036*/      }                                    \n");
    sb.append(" /*037*/    }                                      \n");
    sb.append(" /*038*/  }                                        \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TopLevelCompilationUnit unit = p.parse();

  }

}
