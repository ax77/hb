package op_assign;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TestAssign {

  @Test
  public void test1() {
    MyRefCountedClass ref = new MyRefCountedClass();
    assertEquals(1, ref.getRefCount());

    ref.unref();
    assertEquals(0, ref.getRefCount());
  }

  @Test
  public void test2() {
    MyRefCountedClass lhs = new MyRefCountedClass();
    MyRefCountedClass rhs = null;
    rhs = Rc.opAssign(rhs, lhs);

    assertEquals(lhs, rhs);
    assertEquals(2, lhs.getRefCount());
    assertEquals(2, rhs.getRefCount());

    lhs.outOfScope();
    assertEquals(1, lhs.getRefCount());
    assertEquals(1, rhs.getRefCount());

    rhs.outOfScope();
    assertEquals(0, lhs.getRefCount());
    assertEquals(0, rhs.getRefCount());
  }

  @Test
  public void test3() {
    MyRefCountedClass lhs = new MyRefCountedClass();
    MyRefCountedClass rhs = new MyRefCountedClass();

    assertNotEquals(lhs, rhs);
    assertEquals(1, lhs.getRefCount());
    assertEquals(1, rhs.getRefCount());

    MyRefCountedClass oldRhs = rhs;
    rhs = Rc.opAssign(rhs, lhs);

    assertEquals(lhs, rhs);
    assertEquals(2, lhs.getRefCount());
    assertEquals(2, rhs.getRefCount());

    assertEquals(0, oldRhs.getRefCount());

    lhs.outOfScope();
    rhs.outOfScope();
    assertEquals(0, lhs.getRefCount());
    assertEquals(0, rhs.getRefCount());
  }

  @Test
  public void test4() {
    MyRefCountedClass lhs = new MyRefCountedClass();
    assertEquals(1, lhs.getRefCount());

    lhs = Rc.deinit(lhs);
    assertNull(lhs);
  }

  @Test
  public void test5() {
    MyRefCountedClass lhs = new MyRefCountedClass();
    assertEquals(1, lhs.getRefCount());

    lhs = Rc.opAssign(lhs, null);
    assertNull(lhs);
  }

  @Test //(expected = RuntimeException.class)
  public void test6() {
    MyRefCountedClass lhs = new MyRefCountedClass();
    MyRefCountedClass rhs = null;

    rhs = Rc.opAssign(rhs, lhs);

    assertEquals(2, lhs.getRefCount());
    assertEquals(2, rhs.getRefCount());

    // lhs still depends on RHS, and assign it to null
    // may cause a memory corruption
    // or it may not - if ew just do not destroy the pointer
    // and just unref it silently.
    lhs = Rc.opAssign(lhs, null);

    assertNull(lhs);
    assertNotNull(rhs);

    assertEquals(1, rhs.getRefCount());

    lhs = Rc.deinit(lhs);
    rhs = Rc.deinit(rhs);

    assertNull(lhs);
    assertNull(rhs);

  }
  
  

}







































