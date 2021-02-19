package op_deepcopy;

public class SomeTree {

  public static final SomeTree EMPTY = new SomeTree();

  public SomeTree lhs;
  public SomeTree rhs;
  public int value;

  private SomeTree() {
    this.lhs = EMPTY;
    this.rhs = EMPTY;
    this.value = 0;
  }

  public SomeTree(int value) {
    this.lhs = EMPTY;
    this.rhs = EMPTY;
    this.value = value;
  }

  public SomeTree(SomeTree lhs, SomeTree rhs) {
    this.lhs = lhs;
    this.rhs = rhs;
    this.value = 0;
  }

  public SomeTree deepcopy() {
    SomeTree clone = new SomeTree();

    if (this.lhs == EMPTY) {
      clone.lhs = EMPTY;
    } else {
      clone.lhs = this.lhs.deepcopy();
    }

    if (this.rhs == EMPTY) {
      clone.rhs = EMPTY;
    } else {
      clone.rhs = this.rhs.deepcopy();
    }

    clone.value = this.value;
    return clone;
  }
}
