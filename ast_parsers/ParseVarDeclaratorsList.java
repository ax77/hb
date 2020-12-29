package njast.ast_parsers;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.clazz.vars.VarDeclaratorsList;
import njast.ast_nodes.clazz.vars.VarInitializer;
import njast.ast_nodes.expr.Expression;
import njast.parse.Parse;

public class ParseVarDeclaratorsList {
  private final Parse parser;

  //  <variable declarators> ::= <variable declarator> | <variable declarators> , <variable declarator>
  //
  //  <variable declarator> ::= <variable declarator id> | <variable declarator id> = <variable initializer>
  //
  //  <variable declarator id> ::= <identifier>
  //
  //  <variable initializer> ::= <expression>

  public ParseVarDeclaratorsList(Parse parser) {
    this.parser = parser;
  }

  public VarDeclaratorsList parse() {

    VarDeclaratorsList variableDeclarators = new VarDeclaratorsList();

    // while(is comma) { rest }

    Ident id = parser.getIdent();
    VarDeclarator var = new VarDeclarator(id);

    if (parser.is(T.T_ASSIGN)) {
      parser.moveget();
      Expression init = new ParseExpression(parser).e_expression();
      var.setInitializer(new VarInitializer(init));
    }

    variableDeclarators.put(var);

    parser.semicolon();
    return variableDeclarators;
  }

}
