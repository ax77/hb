package std_bootstrap;


import org.junit.Test;

public class TestList {

  @Test
  public void test() {
    LinkedList<String> list = new LinkedList<>();
    list.add("1");
    list.add("2");
    list.add("3");
    
    while(!list.isEmpty()) {
      System.out.println(list.removeFirst());
    }
  }
  
}
