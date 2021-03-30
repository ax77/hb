package res;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class Xcv {

  interface Markable {
    boolean isMarked();

    void mark();

    void unmark();

    List<Markable> getPtrs();
  }

  class Something implements Markable {

    //@formatter:off
    private boolean isMarked;
    @Override public boolean isMarked() { return isMarked; }
    @Override public void mark() { this.isMarked = true; }
    @Override public void unmark() { this.isMarked = false; }
    @Override public List<Markable> getPtrs() { 
      List<Markable> rv = new ArrayList<>();
      return rv; 
    }
    //@formatter:on
  }

  class MyString implements Markable {

    int value;
    Something s1;
    Something s2;

    public MyString() {
      this.value = 32768;
      this.s2 = new Something();
    }

    //@formatter:off
    private boolean isMarked;
    @Override public boolean isMarked() { return isMarked; }
    @Override public void mark() { this.isMarked = true; }
    @Override public void unmark() { this.isMarked = false; }
    @Override public List<Markable> getPtrs() { 
      List<Markable> rv = new ArrayList<>();
      rv.add(s1);
      rv.add(s2);
      return rv; 
    }
    //@formatter:on
  }

  class MyType implements Markable {
    MyString repr1;
    MyString repr2;
    int kind;
    MyList someArray;

    public MyType() {
      this.repr1 = new MyString();
      this.repr2 = new MyString();
      this.kind = 32;
      this.someArray = new MyList();
    }

    public MyType(int i) {
      this.repr1 = new MyString();
      this.repr2 = new MyString();
      this.kind = i;
    }

    @Override
    public String toString() {
      return String.format("k:%d", kind);
    }

    //@formatter:off
    private boolean isMarked;
    @Override public boolean isMarked() { return isMarked; }
    @Override public void mark() { this.isMarked = true; }
    @Override public void unmark() { this.isMarked = false; }
    @Override public List<Markable> getPtrs() { 
      List<Markable> rv = new ArrayList<>();
      rv.add(repr1);
      rv.add(repr2);
      rv.add(someArray);
      return rv; 
    }
    //@formatter:on
  }

  class MyToken implements Markable {
    MyType value;
    int line;
    MyList someArray;

    public MyToken() {
      this.value = new MyType();
      this.someArray = new MyList();
    }

    //@formatter:off
    private boolean isMarked;
    @Override public boolean isMarked() { return isMarked; }
    @Override public void mark() { this.isMarked = true; }
    @Override public void unmark() { this.isMarked = false; }
    @Override public List<Markable> getPtrs() { 
      List<Markable> rv = new ArrayList<>();
      rv.add(value);
      rv.add(someArray);
      return rv; 
    }
    //@formatter:on
  }

  class MyList implements Markable {

    MyType[] typesArr;

    public MyList() {
      this.typesArr = new MyType[128];
      for (int i = 0; i < 3; i += 1) {
        //this.typesArr[i] = new MyType(i);
      }
    }

    //@formatter:off
    private boolean isMarked;
    @Override public boolean isMarked() { return isMarked; }
    @Override public void mark() { this.isMarked = true; }
    @Override public void unmark() { this.isMarked = false; }
    @Override public List<Markable> getPtrs() { 
      List<Markable> rv = new ArrayList<>();
      for(MyType tp : typesArr) {
        rv.add(tp);
      }
      return rv; 
    }
    //@formatter:on
  }

  public List<Markable> getAllPtrs(Markable from) {
    List<Markable> rv = new ArrayList<>();
    walk(from, rv);
    return rv;
  }

  private void walk(Markable from, List<Markable> rv) {
    for (Markable ptr : from.getPtrs()) {
      if (ptr == null) {
        continue;
      }
      rv.add(ptr);
      walk(ptr, rv);
    }
  }

  @Test
  public void test() {
    MyToken tok = new MyToken();
    List<Markable> toWalk = getAllPtrs(tok);
    int i = 0;
    for (Markable m : toWalk) {
      System.out.printf("%-5d: %s\n", i, m);
      i += 1;
    }
  }

}
