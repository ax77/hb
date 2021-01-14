package njast.ast_parsers;

import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_checkers.TypeRecognizer;
import njast.ast_nodes.clazz.vars.VarBase;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.clazz.vars.VarInitializer;
import njast.ast_nodes.expr.ExprExpression;
import njast.modifiers.Modifiers;
import njast.parse.Parse;
import njast.types.Type;

public class ParseVarDeclarator {
  private final Parse parser;

  public ParseVarDeclarator(Parse parser) {
    this.parser = parser;
  }

  public VarDeclarator parse(VarBase base) {

    // we don't support comma-initialization like: int a=1, b=a, c=32;
    // it is easy to make a mess in your code with this.

    // var counter: int = 0;
    // let counter: int = 0;

    final Modifiers modifiers = new ParseModifiers(parser).parse();

    final Token tok = parser.checkedMove(T.TOKEN_IDENT);
    final Ident id = tok.getIdent();
    final SourceLocation location = new SourceLocation(tok);

    final Token colon = parser.colon();
    final Type type = new TypeRecognizer(parser).getType();
    final VarDeclarator var = new VarDeclarator(base, type, id, location);

    if (parser.is(T.T_ASSIGN)) {
      parser.moveget();
      var.setInitializer(parseInitializer());
    }

    parser.semicolon();
    return var;
  }

  private VarInitializer parseInitializer() {
    ExprExpression init = new ParseExpression(parser).e_assign();
    return new VarInitializer(init);
  }

}
