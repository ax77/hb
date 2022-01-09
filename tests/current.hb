class doc {
  mut str zzz;
  ~doc() {
    delete(zzz);
  }
}

class node {
  mut str value;
  mut node next;
  mut str xxx;
  mut str yyy;
  doc d;
  mut int flag;
  mut char cc;
  node() {
    d = new doc();
    value = "abc";
    next = this;
  }
}


class main {
  int main() {
    node n = new node();
    delete(n);
    return 0;
  }
}
