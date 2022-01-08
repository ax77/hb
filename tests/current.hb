
class doc {
  mut str xxx;
  doc() {
    xxx = "xxx";
  }
  ~doc() {
    delete(xxx);
  }
}

class node {
  mut node prev;
  mut node next;
  mut str item;
  node () {
    item = "item";
  }
}

class tag {
  mut str i;
  mut node args;
  mut doc dc;
  tag() {
    i = "i";
    args = new node();
    dc = new doc();
  }
}


class main {
  int main() {
    tag t = new tag();
    delete(t);

    return 0;
  }
}
