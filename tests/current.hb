
interface markable {
  void mark();
  void unmark();
}

class token implements markable {
  mut boolean is_marked;
  void mark() {}
  void unmark() {}
}

enum toktype {
  tp_ident, tp_string
}

class main {
  int main() {
    toktype tp = toktype.tp_ident;
    token t = new token();
    return 0;
  }
}
