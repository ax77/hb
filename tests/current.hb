
interface markable {
  void mark();
  void unmark();
  int set(int a, int b, int c);
}

class token implements markable {
  mut boolean is_marked;
  void mark() {is_marked=true;}
  void unmark() {is_marked=false;}
  int set(int a, int b, int c) {
    return 32;
  }
}

class literal implements markable {
  mut boolean is_marked;
  void mark() {}
  void unmark() {}
  int set(int a, int b, int c) {
    return 64;
  }
}

class main {
  int main() {
    token tok = new token();
    markable m = tok;
    m.mark();
    assert_true(tok.is_marked);
    m.unmark();
    assert_true(!tok.is_marked);
    
    markable m2 = new literal();
    m2.mark();
    m2.unmark();
    
    return 0;
  }
}
