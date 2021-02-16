package ast_st3_tac.vars.store;

public class ArrayAccess {
  private final Var array;
  private final ERvalue index;

  public ArrayAccess(Var array, ERvalue index) {
    this.array = array;
    this.index = index;
  }

  public Var getArray() {
    return array;
  }

  public ERvalue getIndex() {
    return index;
  }

  @Override
  public String toString() {
    return array.toString() + "[" + index.toString() + "]";
  }

}
