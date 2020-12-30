package njast.ast_parsers;

import java.util.ArrayList;
import java.util.List;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.clazz.vars.VarInitializer;
import njast.ast_nodes.expr.ExprExpression;
import njast.parse.Parse;
import njast.types.Type;

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

  public List<VarDeclarator> parse() {

    Type type = new ParseType(parser).parse();
    List<VarDeclarator> variableDeclarators = new ArrayList<VarDeclarator>();

    getOneVarAndOptInitializer(type, variableDeclarators);
    while (parser.is(T.T_COMMA)) {
      parser.moveget();
      getOneVarAndOptInitializer(type, variableDeclarators);
    }

    parser.semicolon();
    return variableDeclarators;
  }

  private void getOneVarAndOptInitializer(Type type, List<VarDeclarator> variableDeclarators) {

    Ident id = parser.getIdent();
    VarDeclarator var = new VarDeclarator(type, id);

    if (parser.is(T.T_ASSIGN)) {
      parser.moveget();
      var.setInitializer(parseInitializer());
    }

    variableDeclarators.add(var);

  }

  private VarInitializer parseInitializer() {
    ExprExpression init = new ParseExpression(parser).e_assign();
    return new VarInitializer(init);
  }

}
