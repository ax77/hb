package njast.ast_parsers;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_nodes.clazz.methods.FormalParameter;
import njast.ast_nodes.clazz.methods.FormalParameterList;
import njast.parse.Parse;
import njast.types.Type;

public class ParseFormalParameterList {
  private final Parse parser;

  public ParseFormalParameterList(Parse parser) {
    this.parser = parser;
  }

  //  <formal parameter list> ::= <formal parameter> | <formal parameter list> , <formal parameter>
  //
  //  <formal parameter> ::= <type> <variable declarator id>

  public FormalParameterList parse() {
    FormalParameterList parameters = new FormalParameterList();

    Token lparen = parser.lparen();

    if (parser.is(T.T_RIGHT_PAREN)) {
      Token rparen = parser.rparen();
      return parameters;
    }

    FormalParameter oneparam = parseOneParam();
    parameters.put(oneparam);

    while (parser.is(T.T_COMMA)) {
      Token comma = parser.moveget();

      FormalParameter oneparamRest = parseOneParam();
      parameters.put(oneparamRest);
    }

    Token rparen = parser.rparen();

    return parameters;
  }

  private FormalParameter parseOneParam() {
    Type type = new ParseType(parser).parse();
    Ident name = parser.getIdent();
    return new FormalParameter(type, name);
  }

}
