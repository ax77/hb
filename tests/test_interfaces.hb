

interface markable {
  void mark();
  void unmark();
  int flag();
}

class token implements markable {
  mut boolean is_marked;
  void mark() {
    this.is_marked = true;
  }
  void unmark() {
    this.is_marked = false;
  }
  int flag() {
    return 32;
  }
}

class main {
  int main() {
    markable m = new token();
    m.mark();
    int x = m.flag();
    return x;
  }
}
