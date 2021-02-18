package op_assign;

class MyRefCountedClass {
  public static final MyRefCountedClass EMPTY = new MyRefCountedClass();
  private int refCount;

  public MyRefCountedClass() {
    refCount = 1;
  }

  public void ref() {
    refCount += 1;
  }

  public void unref() {
    refCount -= 1;
  }

  public int getRefCount() {
    return refCount;
  }

  public void outOfScope() {
    unref();
  }

}