package ast_st3_tac.vars.store;

public class ArrayAccess {
  private final Var array;
  private final Value index;

  public ArrayAccess(Var array, Value index) {
    this.array = array;
    this.index = index;
  }

  public Var getArray() {
    return array;
  }

  public Value getIndex() {
    return index;
  }

  @Override
  public String toString() {
    return array.toString() + "[" + index.toString() + "]";
  }

}
