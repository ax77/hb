package njast.ast_parsers;

import java.util.ArrayList;
import java.util.List;

import njast.ast_nodes.clazz.ClassFieldDeclaration;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.modifiers.Modifiers;
import njast.parse.Parse;

public class ParseFieldDeclaration {
  private final Parse parser;

  public ParseFieldDeclaration(Parse parser) {
    this.parser = parser;
  }

  public List<ClassFieldDeclaration> parse() {
    Modifiers modifiers = new ParseModifiers(parser).parse();

    List<VarDeclarator> variableDeclarators = new ParseVarDeclaratorsList(parser).parse();

    List<ClassFieldDeclaration> fields = new ArrayList<ClassFieldDeclaration>();
    for (VarDeclarator var : variableDeclarators) {
      fields.add(new ClassFieldDeclaration(var));
    }

    return fields;
  }

}
