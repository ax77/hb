package ast_parsers;

import java.util.ArrayList;
import java.util.List;

import ast_expr.ExprExpression;
import parse.Parse;
import tokenize.T;

public class ParseFcallArgs {
  private final Parse parser;

  public ParseFcallArgs(Parse parser) {
    this.parser = parser;
  }

  public List<ExprExpression> parse() {

    parser.lparen();
    final List<ExprExpression> arglist = new ArrayList<>();

    if (parser.is(T.T_RIGHT_PAREN)) {
      parser.rparen();
      return arglist;
    }

    arglist.add(getOneArg());
    while (parser.is(T.T_COMMA)) {
      parser.move();
      arglist.add(getOneArg());
    }

    parser.rparen();
    return arglist;
  }

  private ExprExpression getOneArg() {
    return new ParseExpression(parser).e_assign();
  }
}
