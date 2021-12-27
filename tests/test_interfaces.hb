//import std.natives.string::string;
//import std.natives.fmt::fmt;

interface markable {
  int getmark();
  void setmark(int m);
}

class strmark implements markable {
  int f;
  strmark() { this.f = -1; }
  int getmark() { return f; }
  void setmark(int f) { this.f = f; }
}

class pair<K,V> {
  K key;
  V val;
  pair(K key, V val) {
    this.key = key;
    this.val = val;
  }
  K getkey() { return key; }
  V getval() { return val; }
}

class main_class {
  int main() {
    
    strmark m = new strmark();
    int x = m.getmark();
    m.setmark(32);
    
    pair<char, int> p = new pair<char, int>('!', 33);
    assert_true('!' == p.getkey());
    assert_true(33 == p.getval());
    
    fmt.print(p.getkey());
    fmt.print(p.getval());
    
    array<string> argv = new array<string>();
    argv.add("a.");
    argv.add("b");
    for(int i = 0; i < argv.size(); i += 1) {
      fmt.print(argv.get(i));
    }
    
    //markable mkrbl = new strmark();
    //return m.getmark();
    return 0;
  }
}
