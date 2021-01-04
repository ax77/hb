package njast.main;

import static jscan.tokenize.T.TOKEN_EOF;

import java.util.List;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_parsers.ParseModifiers;
import njast.ast_parsers.ParseTypeParameters;
import njast.modifiers.Modifiers;
import njast.parse.Parse;
import njast.parse.ParseState;
import njast.symtab.IdentMap;
import njast.types.ReferenceType;

public class BindAllClassTypes {

  public static void bind(Parse parser) {

    ParseState state = new ParseState(parser);

    moveStraySemicolon(parser);
    while (!parser.isEof()) {

      moveStraySemicolon(parser);
      Modifiers modifiers = new ParseModifiers(parser).parse();

      if (!parser.is(IdentMap.class_ident)) {
        parser.perror("expected class, but was: " + parser.tok().getValue());
      }

      parser.moveget(); // class kw

      Ident classname = parser.getIdent();
      parser.defineClassName(new ClassDeclaration(classname));

      List<ReferenceType> tp = new ParseTypeParameters(parser).parse(); // maybe empty

      if (!parser.is(T.T_LEFT_BRACE)) {
        parser.perror("expect {");
      }

      int nest = 0;
      for (; !parser.isEof();) {
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

    parser.restoreState(state);
  }

  private static void moveStraySemicolon(Parse parser) {
    while (parser.is(T.T_SEMI_COLON)) {
      parser.move();
    }
  }
}
