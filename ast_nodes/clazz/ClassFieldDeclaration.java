package njast.ast_nodes.clazz;

import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

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

  private final ClassDeclaration owner;
  private final VarDeclarator field;

  public ClassFieldDeclaration(ClassDeclaration owner, VarDeclarator field) {
    this.owner = owner;
    this.field = field;
  }

  public VarDeclarator getField() {
    return field;
  }

  public ClassDeclaration getOwner() {
    return owner;
  }

  @Override
  public String toString() {
    return field.toString() + " owner=" + owner.toString();
  }

}
