package njast.ast_parsers;

import njast.ast_nodes.clazz.FieldDeclaration;
import njast.ast_nodes.clazz.vars.VarDeclaratorsList;
import njast.modifiers.Modifiers;
import njast.parse.Parse;
import njast.types.Type;

public class ParseFieldDeclaration {
  private final Parse parser;

  public ParseFieldDeclaration(Parse parser) {
    this.parser = parser;
  }

  public FieldDeclaration parse() {
    Modifiers modifiers = new ParseModifiers(parser).parse();
    Type type = new ParseType(parser).parse();
    VarDeclaratorsList variableDeclarators = new ParseVarDeclaratorsList(parser).parse();

    return new FieldDeclaration(type, variableDeclarators);
  }

}
