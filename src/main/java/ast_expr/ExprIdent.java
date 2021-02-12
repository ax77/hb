package ast_expr;

import java.io.Serializable;

import ast_st2_annotate.Symbol;
import tokenize.Ident;

public class ExprIdent implements Serializable, MirSymbol {
  private static final long serialVersionUID = 7777441284065170375L;
  private final Ident identifier;

  //MIR:TREE
  private Symbol sym;

  public ExprIdent(Ident identifier) {
    this.identifier = identifier;
  }

  public Ident getIdentifier() {
    return identifier;
  }

  @Override
  public Symbol getSym() {
    return sym;
  }

  @Override
  public void setSym(Symbol sym) {
    this.sym = sym;
  }

  @Override
  public String toString() {
    return identifier.getName();
  }

}
