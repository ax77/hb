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

  public List<VarDeclarator> parse(VarBase base, ParseVariableState state) {

    Modifiers modifiers = new ParseModifiers(parser).parse();

    Type type = new TypeRecognizer(parser, false).getType();
    List<VarDeclarator> variableDeclarators = new ArrayList<VarDeclarator>();

    getOneVarAndOptInitializer(base, type, variableDeclarators, modifiers);

    if (parser.is(T.T_COMMA)) {
      if (!state.equals(ParseVariableState.STATE_FOR_LOOP)) {
        parser.errorCommaExpression();
      }

      while (parser.is(T.T_COMMA)) {
        parser.moveget();
        getOneVarAndOptInitializer(base, type, variableDeclarators, modifiers);
      }
    }

    parser.semicolon();
    return variableDeclarators;
  }

  private void getOneVarAndOptInitializer(VarBase base, Type type, List<VarDeclarator> variableDeclarators,
      Modifiers modifiers) {

    Token tok = parser.checkedMove(T.TOKEN_IDENT);
    Ident id = tok.getIdent();

    ModTypeNameHeader header = new ModTypeNameHeader(modifiers, type, id, new SourceLocation(tok));
    VarDeclarator var = new VarDeclarator(base, header);

    if (parser.is(T.T_ASSIGN)) {
      parser.moveget();
      var.setInitializer(parseInitializer());
    }

    variableDeclarators.add(var);

  }

  private VarInitializer parseInitializer() {
    ExprExpression init = new ParseExpression(parser, ParseVariableState.STATE_OTHER).e_assign();
    return new VarInitializer(init);
  }

}
