
class node {
  node prev;
  node next;
}

class toktype {
  string type;
  string name;
}

class intlit {
  string svalue;
  int ivalue;
}

class tok {
  toktype typ;
  intlit lit;
  node chain;
}

class main {
  int main() {
    tok t = new tok();
    array<array<int>> args = new array<array<int>>();
    node node = new node();
    node.set_deletion_mark(true);
    return 0;
  }
}
































