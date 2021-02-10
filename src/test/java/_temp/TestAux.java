package _temp;

public class TestAux {
  class ArrayList_int {
    private int[] table;

    void add(int element) {
      array_add_int(table, element);
    }

    private void array_add_int(int[] table2, int element) {
      // TODO Auto-generated method stub

    }
  }

  class ArrayList_ArrayList_int {
    private ArrayList_int[] table;

    void add(ArrayList_int element) {
      array_add_ArrayList_int(table, element);
    }

    private void array_add_ArrayList_int(ArrayList_int[] table2, ArrayList_int element) {
      // TODO Auto-generated method stub

    }
  }

  class test {
    void fn() {
      ArrayList_ArrayList_int opts = new ArrayList_ArrayList_int();
      opts.add(new ArrayList_int());
    }
  }

}
