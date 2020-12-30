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
    sb.append(" /*001*/  class Some {                                      \n");
    sb.append(" /*002*/    int fieldInSomeClass = 1;                       \n");
    sb.append(" /*003*/    Idn idn;                                        \n");
    sb.append(" /*004*/    Some next;                                      \n");
    sb.append(" /*005*/    int funcInSomeClass() {                         \n");
    sb.append(" /*006*/      return fieldInSomeClass + this.fieldInSomeClass;  \n");
    sb.append(" /*007*/    }                                               \n");
    sb.append(" /*008*/  }                                                 \n");
    sb.append(" /*009*/  class Idn {                                       \n");
    sb.append(" /*010*/    int fieldvar = 1;                               \n");
    sb.append(" /*011*/    Some classTypeField = new Some();               \n");
    sb.append(" /*012*/    Idn next;                                       \n");
    sb.append(" /*013*/    int func(int fnparam) {                         \n");
    sb.append(" /*014*/      int methodvar = 1;                            \n");
    sb.append(" /*015*/      return methodvar                              \n");
    sb.append(" /*016*/          + fieldvar                                \n");
    sb.append(" /*017*/          + this.fieldvar                           \n");
    sb.append(" /*018*/          + anotherFn()                             \n");
    sb.append(" /*019*/          + this.anotherFn()                        \n");
    sb.append(" /*020*/          + classTypeField.funcInSomeClass()        \n");
    sb.append(" /*021*/          + this.classTypeField.funcInSomeClass()   \n");
    sb.append(" /*022*/          + classTypeField.fieldInSomeClass         \n");
    sb.append(" /*023*/          + this.classTypeField.fieldInSomeClass    \n");
    sb.append(" /*024*/          + fnparam;                                \n");
    sb.append(" /*025*/    }                                               \n");
    sb.append(" /*026*/    int anotherFn() {                               \n");
    sb.append(" /*027*/      return fieldvar;                              \n");
    sb.append(" /*028*/    }                                               \n");
    sb.append(" /*029*/  }                                                 \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TopLevelCompilationUnit unit = p.parse();

    AstVisitorTypeApplier applier = new AstVisitorTypeApplier();
    applier.visit(unit);

    System.out.println();

  }

}
