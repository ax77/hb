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
    sb.append(" /*001*/  class C { public int xxx=1, yyy, zzz=3;  \n");
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

    AstVisitorTypeApplier applier = new AstVisitorTypeApplier();
    applier.visit(unit);

  }

}
