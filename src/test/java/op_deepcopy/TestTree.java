package op_deepcopy;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestTree {

  @Test
  public void test1() {
    SomeTree tree1 = new SomeTree(32);
    
    SomeTree tree2 = new SomeTree(new SomeTree(128), new SomeTree(256));
    SomeTree tree3 = tree2.deepcopy();
    
    assertNotEquals(tree2, tree3);
    assertNotEquals(tree2.lhs, tree3.rhs);
    assertEquals(tree2.lhs.value, tree3.lhs.value);
    assertEquals(tree2.rhs.value, tree3.rhs.value);
    
    SomeTree t = SomeTree.EMPTY;
  }
  
}
