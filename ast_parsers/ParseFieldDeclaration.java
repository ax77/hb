package njast.ast_parsers;

import jscan.symtab.Ident;
import njast.ast_class.FieldDeclaration;
import njast.ast_class.vars.VariableDeclarator;
import njast.ast_class.vars.VariableDeclaratorsList;
import njast.parse.Parse;
import njast.types.Type;

public class ParseFieldDeclaration {
  private final Parse parser;

  public ParseFieldDeclaration(Parse parser) {
    this.parser = parser;
  }

  public FieldDeclaration parse() {
    Type type = new ParseType(parser).parse();
    VariableDeclaratorsList variableDeclarators = parseVariableDeclarators();

    parser.semicolon();
    return new FieldDeclaration(type, variableDeclarators);
  }

  private VariableDeclaratorsList parseVariableDeclarators() {

    VariableDeclaratorsList variableDeclarators = new VariableDeclaratorsList();
    variableDeclarators.put(parseVariableDeclarator());

    // while(is comma) { rest }

    return variableDeclarators;
  }

  private VariableDeclarator parseVariableDeclarator() {
    Ident id = parser.getIdent();
    return new VariableDeclarator(id);
  }

}
