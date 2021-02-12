
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

    // final String content = std.read_file("main.c");
    // final boolean res = std.write_file("out.txt", content);
    // if(!res) {
    //   std.panic("error with file-writing");
    // }

    stdio.panic("something...");

    String s = stdio.read_file("main.c");
    boolean res = stdio.write_file("out.txt", s);

    int a = 0;
    ArrayList<String> argv = new ArrayList<String>();

    stdio.printf("%s", argv.get(0));
    stdio.printf("%s", argv.get(0), argv.get(0));
    
    return 1;
  }
}
