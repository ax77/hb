package rew;

import java.util.ArrayList;

import org.junit.Test;

import rew.TestJavaInterace.markable;

public class TestJavaInterace {

  interface markable {
    void mark();

    void unmark();
  }

  class token implements markable {
    boolean is_marked;

    @Override
    public void mark() {
      this.is_marked = true;
    }

    @Override
    public void unmark() {
      this.is_marked = false;
    }
  }

  class literal implements markable {
    boolean is_marked;

    @Override
    public void mark() {
      this.is_marked = true;
    }

    @Override
    public void unmark() {
      this.is_marked = false;
    }
  }

  @Test
  public void test() {
    token tok = new token();
    markable m = tok;
    m.mark();
  }

}
