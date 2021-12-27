package ast_parsers;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_modifiers.Modifiers;
import ast_types.Type;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import parse.Parse;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public class ParseVarDeclarator {
  private final Parse parser;

  public ParseVarDeclarator(Parse parser) {
    this.parser = parser;
  }

  public VarDeclarator parse(VarBase base, Modifiers modifiers, Type type, Ident id, Token beginPos) {

    final VarDeclarator var = new VarDeclarator(base, modifiers, type, id, beginPos);

    if (parser.is(T.T_ASSIGN)) {
      parser.move();
      var.setSimpleInitializer(parseInitializer());
    }

    parser.semicolon();
    return var;
  }

  private VarDeclarator getLocalVar(Modifiers mods, Type type) {
    final Token pos = parser.checkedMove(T.TOKEN_IDENT);
    final Ident name = pos.getIdent();
    final VarDeclarator var = new VarDeclarator(VarBase.LOCAL_VAR, mods, type, name, pos);

    if (!parser.is(T.T_ASSIGN)) {
      parser.perror("expected initializer in for-loop declaration statement");
    }

    parser.checkedMove(T.T_ASSIGN);
    var.setSimpleInitializer(parseInitializer());

    final ClassDeclaration currentClass = parser.getCurrentClass(true);
    currentClass.registerTypeSetter(var);

    return var;
  }

  public List<VarDeclarator> parseVarDeclaratorListForLoop() {

    final Modifiers mods = new ParseModifiers(parser).parse();
    final Type type = new ParseType(parser).getType();

    final List<VarDeclarator> vars = new ArrayList<VarDeclarator>();
    vars.add(getLocalVar(mods, type));

    while (parser.is(T.T_COMMA)) {
      parser.move();
      vars.add(getLocalVar(mods, type));
    }

    parser.semicolon();
    return vars;
  }

  private ExprExpression parseInitializer() {
    return new ParseExpression(parser).e_assign();
  }

}
