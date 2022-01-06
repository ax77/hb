package rew;

import java.util.ArrayList;

import org.junit.Test;

import rew.TestJavaInterace.markable;

public class TestJavaInterace {

  interface markable {
    void mark();

    void unmark();
  }
  
  interface equitable {
    boolean isEq();
  }

  class token implements markable,equitable {
    boolean is_marked;

    @Override
    public void mark() {
      this.is_marked = true;
    }

    @Override
    public void unmark() {
      this.is_marked = false;
    }

    @Override
    public boolean isEq() {
      return false;
    }
  }

  class literal implements markable,equitable {
    boolean is_marked;

    @Override
    public void mark() {
      this.is_marked = true;
    }

    @Override
    public void unmark() {
      this.is_marked = false;
    }

    @Override
    public boolean isEq() {
      return true;
    }
  }

  @Test
  public void test() {
    ArrayList<markable> arr = new ArrayList<>();
    equitable eq = new token();
    markable mk = new literal();
    arr.add(new token());
    arr.add(mk);
  }

}


























