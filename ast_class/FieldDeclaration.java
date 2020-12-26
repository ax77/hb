package njast.ast_class;

import njast.ast_class.vars.VariableDeclaratorsList;
import njast.types.Type;

public class FieldDeclaration {

  //  <field declaration> ::= <field modifiers>? <type> <variable declarators> ;
  //
  //  <field modifiers> ::= <field modifier> | <field modifiers> <field modifier>
  //
  //  <field modifier> ::= public | protected | private | static | final | transient | volatile
  //
  //  <variable declarators> ::= <variable declarator> | <variable declarators> , <variable declarator>
  //
  //  <variable declarator> ::= <variable declarator id> | <variable declarator id> = <variable initializer>
  //
  //  <variable declarator id> ::= <identifier> | <variable declarator id> [ ]
  //
  //  <variable initializer> ::= <expression> | <array initializer>

  // private FieldModifier fieldModifier;

  private Type type;
  private VariableDeclaratorsList variables;

  public FieldDeclaration(Type type, VariableDeclaratorsList variables) {
    this.type = type;
    this.variables = variables;
  }

}
