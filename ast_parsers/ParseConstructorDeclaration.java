package njast.ast_parsers;

import jscan.symtab.Ident;
import njast.ast_class.ConstructorDeclaration;
import njast.parse.Parse;

public class ParseConstructorDeclaration {
  private final Parse parser;

  public ParseConstructorDeclaration(Parse parser) {
    this.parser = parser;
  }

  public ConstructorDeclaration parse() {
    Ident identifier = parser.getIdent();
    ConstructorDeclaration constructorDeclaration = new ConstructorDeclaration(identifier);

    // TODO: params
    parser.lparen();
    parser.rparen();

    // TODO: body
    parser.lbrace();
    parser.rbrace();

    return constructorDeclaration;
  }

}
