package njast;

import static jscan.tokenize.T.TOKEN_EOF;

import org.junit.Test;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_parsers.ParseModifiers;
import njast.main.ParserMain;
import njast.modifiers.Modifiers;
import njast.parse.Parse;
import njast.parse.ParseState;
import njast.symtab.IdentMap;

public class TestClassPreParse {

  private void moveStraySemicolon(Parse parser) {
    while (parser.is(T.T_SEMI_COLON)) {
      parser.move();
    }
  }

  @Test
  public void test() throws Exception {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  class Some {                                      \n");
    sb.append(" /*002*/    int fieldInSomeClass = 1;                       \n");
    sb.append(" /*003*/    Idn idn;                                        \n");
    sb.append(" /*004*/    Some next;                                      \n");
    sb.append(" /*005*/    int funcInSomeClass() {                         \n");
    sb.append(" /*006*/      return fieldInSomeClass;                      \n");
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

    Parse parser = new ParserMain(sb).initiateParse();
    ParseState state = new ParseState(parser);

    // top-level
    moveStraySemicolon(parser);

    while (!parser.isEof()) {

      moveStraySemicolon(parser);
      Modifiers modifiers = new ParseModifiers(parser).parse();

      if (parser.is(IdentMap.class_ident)) {
        parser.moveget();

        Ident classname = parser.getIdent();
        System.out.println("found class: " + classname.getName());

        if (!parser.is(T.T_LEFT_BRACE)) {
          parser.perror("expect {");
        }

        int nest = 0;
        while (!parser.isEof()) {
          Token next = parser.moveget();

          if (next.ofType(TOKEN_EOF)) {
            parser.unexpectedEof();
          } else if (next.ofType(T.T_LEFT_BRACE)) {
            ++nest;
          } else if (next.ofType(T.T_RIGHT_BRACE)) {
            if (--nest == 0) {
              break;
            }
          } else {

            // class NAME {
            // ^.....^....^
            if (next.isIdent(IdentMap.class_ident)) {
              ParseState tempState = new ParseState(parser);
              Token peektok = parser.moveget();
              if (peektok.ofType(T.TOKEN_IDENT)) {
                if (parser.is(T.T_LEFT_BRACE)) {
                  parser.perror("nested class");
                }
              }
              parser.restoreState(tempState);
            }

          }
        }

      }
    }

    parser.restoreState(state);
  }
}
