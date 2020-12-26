package njast;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import njast.ast_top.TypeDeclarationsList;
import njast.ast_visitors.AstVisitor;
import njast.ast_visitors.AstVisitorXml;
import njast.main.ParserMain;
import njast.parse.Parse;

public class TestParseClass {

  @Test
  public void test() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  class First {   \n");
    sb.append(" /*002*/    int a;        \n");
    sb.append(" /*003*/    int b;        \n");
    sb.append(" /*004*/    int c;        \n");
    sb.append(" /*004*/    First() {}    \n");
    sb.append(" /*005*/    int x() {    \n");
    sb.append(" /*006*/    }             \n");
    sb.append(" /*007*/    int y() {    \n");
    sb.append(" /*008*/    }             \n");
    sb.append(" /*009*/    int z() {    \n");
    sb.append(" /*010*/    }             \n");
    sb.append(" /*011*/  }               \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TypeDeclarationsList unit = p.parse();
    System.out.println();
  }

  @Ignore
  @Test
  public void testVisitor() throws Exception {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  class First {   \n");
    sb.append(" /*002*/    int a;        \n");
    sb.append(" /*003*/    int b;        \n");
    sb.append(" /*004*/    int c;        \n");
    sb.append(" /*005*/    int x() {    \n");
    sb.append(" /*006*/    }             \n");
    sb.append(" /*007*/    int y() {    \n");
    sb.append(" /*008*/    }             \n");
    sb.append(" /*009*/    int z() {    \n");
    sb.append(" /*010*/    }             \n");
    sb.append(" /*011*/  }               \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TypeDeclarationsList unit = p.parse();

    AstVisitor walker = new AstVisitorXml();
    unit.accept(walker);

    String s = walker.getText().toString();
    System.out.println(s);

    //    AstVisitorXml visitor = new AstVisitorXml();
    //    unit.accept(visitor);
    //
    //    System.out.println(visitor.getText().toString());
  }

}
