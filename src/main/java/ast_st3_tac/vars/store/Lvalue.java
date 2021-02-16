package ast_st3_tac.vars.store;

public class Lvalue {

  // a = b
  // a.b = c
  // a[b] = c

  private Var dstVar;
  private FieldAccess dstField;
  private ArrayAccess dstArray;

  public Lvalue(Var dstVar) {
    this.dstVar = dstVar;
  }

  public Lvalue(FieldAccess dstField) {
    this.dstField = dstField;
  }

  public Lvalue(ArrayAccess dstArray) {
    this.dstArray = dstArray;
  }

  public boolean isDstVar() {
    return dstVar != null;
  }

  public boolean isDstField() {
    return dstField != null;
  }

  public boolean isDstArray() {
    return dstArray != null;
  }

  public Var getDstVar() {
    return dstVar;
  }

  public FieldAccess getDstField() {
    return dstField;
  }

  public ArrayAccess getDstArray() {
    return dstArray;
  }

  @Override
  public String toString() {
    if (isDstVar()) {
      return dstVar.toString();
    }
    if (isDstField()) {
      return dstField.toString();
    }
    if (isDstArray()) {
      return dstArray.toString();
    }
    return "??? Lvalue";
  }

}
