package res;

public class TestEqualsI {

  interface Equalize<T> {
    boolean isEqualTo(T another);
  }

  static class SomeClass implements Equalize<SomeClass> {

    int i;

    public SomeClass(int i) {
      this.i = i;
    }

    @Override
    public boolean isEqualTo(SomeClass another) {
      return i == another.i;
    }

  }

}
