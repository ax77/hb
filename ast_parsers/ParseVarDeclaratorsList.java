package njast.ast_parsers;

import java.util.ArrayList;
import java.util.List;

import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ModTypeNameHeader;
import njast.ast_checkers.TypeRecognizer;
import njast.ast_nodes.clazz.vars.VarBase;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.clazz.vars.VarInitializer;
import njast.ast_nodes.expr.ExprExpression;
import njast.modifiers.Modifiers;
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

  public List<VarDeclarator> parse(VarBase base) {

    // weak only by now.
    Modifiers modifiers = new ParseModifiers(parser).parse();
    List<VarDeclarator> variableDeclarators = new ArrayList<VarDeclarator>();

    // var counter: int = 0;
    // let counter: int = 0;

    Token tok = parser.checkedMove(T.TOKEN_IDENT);
    Ident id = tok.getIdent();
    SourceLocation location = new SourceLocation(tok);

    Token colon = parser.colon();
    Type type = new TypeRecognizer(parser).getType();

    ModTypeNameHeader header = new ModTypeNameHeader(modifiers, type, id, location);
    VarDeclarator var = new VarDeclarator(base, header);

    if (parser.is(T.T_ASSIGN)) {
      parser.moveget();
      var.setInitializer(parseInitializer());
    }

    variableDeclarators.add(var);

    parser.semicolon();
    return variableDeclarators;
  }

  private VarInitializer parseInitializer() {
    ExprExpression init = new ParseExpression(parser).e_assign();
    return new VarInitializer(init);
  }

}
