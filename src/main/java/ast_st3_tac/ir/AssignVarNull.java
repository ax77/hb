package ast_st3_tac.ir;

import ast_st3_tac.vars.store.Var;

public class AssignVarNull {
  private final Var lvalue;
  private final String nullLiteral;

  public AssignVarNull(Var lvalue) {
    this.lvalue = lvalue;
    this.nullLiteral = "null";
  }

  public Var getLvalue() {
    return lvalue;
  }

  public String getNullLiteral() {
    return nullLiteral;
  }

}
