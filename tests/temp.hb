
class test {
  void fn() {

    //    ArrayList<ArrayList<int>> opts = new ArrayList<ArrayList<int>>();
    //    opts.add(new ArrayList<int>());
    //    for(int i = 0; i < opts.size(); i += 1) {
    //      ArrayList<int> elem = opts.get(i);
    //      for(int j = 0; j < elem.size(); j += 1) {
    //        int x = elem.get(j);
    //      }
    //    }
    //    
    //    ArrayList<String> argv = new ArrayList<String>();
    //    argv.add("1");
    //    argv.add("2");
    //    argv.add("3");
    //    for(int i = 0; i < argv.size(); i += 1) {
    //      String s = argv.get(i);
    //      char c = s.get(0);
    //    }
    
    ArrayList<char> content = builtin.read_file("main.c");

  }
}
