
class doc {
  mut str i;
  doc() {
    i = "i";
  }
}

class node {
  str x;
  str y;
  doc d;
  node() {
    x = "x";
    y = "y";
    d = new doc();
  }
}

class tok {
  doc d;
  node n;
  str s;
  tok() {
    d = new doc();
    n = new node();
    s = "1";
  }
}

class main {
  int main() {
    tok t = new tok();
    delete(t);
    return 0;
  }
}
