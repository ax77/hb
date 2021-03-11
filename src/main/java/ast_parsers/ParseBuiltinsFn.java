package ast_parsers;

import java.util.List;

import ast_expr.ExprExpression;
import ast_symtab.BuiltinNames;
import parse.Parse;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public class ParseBuiltinsFn {
  private final Parse parser;

  public ParseBuiltinsFn(Parse parser) {
    this.parser = parser;
  }

  public ExprExpression parse() {

    // builtin .
    final Token beginPos = parser.checkedMove(BuiltinNames.std_ident);
    parser.checkedMove(T.T_DOT);

    final Ident funcname = parser.getIdent();
    checkIsCorrectBuiltinIdent(funcname);

    parser.perror("unimplemented builtin function: " + funcname.toString());
    return null;
  }

  private void checkIsCorrectBuiltinIdent(Ident funcname) {
    if (!BuiltinNames.isCorrectBuiltinIdent(funcname)) {
      parser.perror("unimplemented builtin function: " + funcname.toString());
    }
  }

  private List<ExprExpression> parseArglist() {
    return new ParseFcallArgs(parser).parse();
  }

}
