package njast.ast_parsers;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.clazz.vars.VarDeclaratorsList;
import njast.ast_nodes.clazz.vars.VarInitializer;
import njast.ast_nodes.expr.ExprExpression;
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

    getOneVarAndOptInitializer(variableDeclarators);
    while (parser.is(T.T_COMMA)) {
      parser.moveget();
      getOneVarAndOptInitializer(variableDeclarators);
    }

    parser.semicolon();
    return variableDeclarators;
  }

  private void getOneVarAndOptInitializer(VarDeclaratorsList variableDeclarators) {

    Ident id = parser.getIdent();
    VarDeclarator var = new VarDeclarator(id);

    if (parser.is(T.T_ASSIGN)) {
      parser.moveget();
      var.setInitializer(parseInitializer());
    }

    variableDeclarators.put(var);

  }

  private VarInitializer parseInitializer() {
    ExprExpression init = new ParseExpression(parser).e_assign();
    return new VarInitializer(init);
  }

}
