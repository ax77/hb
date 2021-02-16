package ast_st3_tac.vars.store;

import literals.IntLiteral;

public class Value {
  private Var var;
  private IntLiteral num;
  private String str;

  public Value(Var var) {
    this.var = var;
  }

  public Value(IntLiteral num) {
    this.num = num;
  }

  public Value(String str) {
    this.str = str;
  }

  public boolean isVar() {
    return var != null;
  }

  public boolean isNum() {
    return num != null;
  }

  public boolean isStr() {
    return str != null;
  }

  public Var getVar() {
    return var;
  }

  public IntLiteral getNum() {
    return num;
  }

  public String getStr() {
    return str;
  }

  @Override
  public String toString() {
    if (isVar()) {
      return var.toString();
    }
    if (isNum()) {
      return num.toString();
    }
    if (isStr()) {
      return str;
    }
    return "???Value";
  }

}
