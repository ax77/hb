package njast;

import java.io.IOException;

import org.junit.Test;

import njast.ast_flow.BlockStatement;
import njast.ast_flow.CExpression;
import njast.ast_top.CompilationUnit;
import njast.main.ParserMain;
import njast.parse.Parse;
import njast.symtab.logic.Symtab;

public class TestMinimalExample {

  @Test
  public void testMinimalClass() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  // package test;                     \n");
    sb.append(" /*002*/  public class C {                     \n");
    sb.append(" /*003*/    private int field;                 \n");
    sb.append(" /*004*/    static {}                          \n");
    sb.append(" /*005*/    static {}                          \n");
    sb.append(" /*006*/    public C() {                       \n");
    sb.append(" /*007*/      field = 128;                     \n");
    sb.append(" /*008*/    }                                  \n");
    sb.append(" /*009*/    public C(int constructorParam) {   \n");
    sb.append(" /*010*/      field = constructorParam;        \n");
    sb.append(" /*011*/    }                                  \n");
    sb.append(" /*012*/    public int func(int a, int b) {    \n");
    sb.append(" /*013*/      int local = 32;                  \n");
    sb.append(" /*014*/      return field + local + a + b;    \n");
    sb.append(" /*015*/    }                                  \n");
    sb.append(" /*016*/  }                                    \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    CompilationUnit unit = p.parse();

    for (BlockStatement bs : unit.getTypeDeclarations().get(0).getClassDeclaration().getMethodDeclaration().get(0)
        .getBody().getBlockStatements().getBlockStatements()) {
      if (bs.getStatement() != null) {
        final CExpression expr = bs.getStatement().getExpr();
      }
    }

  }

  @Test
  public void testSymtab() throws Exception {
    Symtab<String, String> tab = new Symtab<>();

    // class-scope
    tab.pushscope("class:ClassName:Main.java:6");
    tab.addsym("ClassName", "symbol-here");
    tab.addsym("x", "field-type-int");

    // function
    tab.pushscope("function:main():Main.java:14");
    tab.addsym("main", "type=function");
    tab.addsym("a", "type=int->parameter");
    tab.addsym("b", "type=int->parameter");

  }

}
