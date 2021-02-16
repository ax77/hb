package ast_st3_tac.vars.store;

public class Literal {
  private Boolean boolLiteral;
  private String nullLiteral;

  public Literal(boolean boolLiteral) {
    this.boolLiteral = boolLiteral;
  }

  public Literal(String nullLiteral) {
    this.nullLiteral = nullLiteral;
  }

  public boolean isBoolLit() {
    return boolLiteral != null;
  }

  public boolean isNullLit() {
    return nullLiteral != null;
  }

  public Boolean getBoolLiteral() {
    return boolLiteral;
  }

  public String getNullLiteral() {
    return nullLiteral;
  }

  @Override
  public String toString() {
    if (isBoolLit()) {
      return boolLiteral.toString();
    }
    if (isNullLit()) {
      return "null";
    }
    return "???Literals";
  }

}
