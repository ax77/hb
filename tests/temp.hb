
import std.ArrayList;
import std.String;

class test {
  void fn() {
    ArrayList<ArrayList<int>> opts = new ArrayList<ArrayList<int>>();
    opts.add(new ArrayList<int>());
    
    ArrayList<String> argv = new ArrayList<String>();
    argv.add("1");
    argv.add("2");
    argv.add("3");
    for(int i = 0; i < argv.size(); i += 1) {
      String s = argv.get(i);
    }
  }
}
