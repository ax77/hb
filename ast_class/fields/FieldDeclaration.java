package njast.ast_class.fields;

import njast.ast_class.vars.VariableDeclarators;
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
  private VariableDeclarators variableDeclarators;

  public FieldDeclaration() {
  }

  public FieldDeclaration(Type type, VariableDeclarators variableDeclarators) {
    this.type = type;
    this.variableDeclarators = variableDeclarators;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public VariableDeclarators getVariableDeclarators() {
    return variableDeclarators;
  }

  public void setVariableDeclarators(VariableDeclarators variableDeclarators) {
    this.variableDeclarators = variableDeclarators;
  }

}
