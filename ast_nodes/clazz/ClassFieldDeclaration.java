package njast.ast_nodes.clazz;

import jscan.symtab.Ident;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.clazz.vars.VarDeclaratorsList;
import njast.ast_nodes.clazz.vars.VarInitializer;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;
import njast.types.Type;

public class ClassFieldDeclaration implements AstTraverser {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

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

  private final Type type;
  private final Ident identifier;
  private final VarInitializer initializer;

  public ClassFieldDeclaration(Type type, Ident identifier, VarInitializer initializer) {
    this.type = type;
    this.identifier = identifier;
    this.initializer = initializer;
  }

  public Type getType() {
    return type;
  }

  public Ident getIdentifier() {
    return identifier;
  }

  public VarInitializer getInitializer() {
    return initializer;
  }

  @Override
  public String toString() {
    return "Field [type=" + type + ", identifier=" + identifier.getName() + ", initializer="
        + ((initializer != null) ? initializer.getInitializer().toString() : "no_init") + "]";
  }

}
