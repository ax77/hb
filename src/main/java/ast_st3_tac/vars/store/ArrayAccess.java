package ast_st3_tac.vars.store;

public class ArrayAccess {
  private final Var array;
  private final Var index;

  public ArrayAccess(Var array, Var index) {
    this.array = array;
    this.index = index;
  }

  public Var getArray() {
    return array;
  }

  public Var getIndex() {
    return index;
  }

  @Override
  public String toString() {
    return array.toString() + "[" + index.toString() + "]";
  }

}
