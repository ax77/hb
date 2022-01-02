
class node {
  node prev;
  node next;
  
  deinit {
    fmt<string>.print("node is being dropped");
  }
}

class toktype {
  string type;
  string name;
  
  deinit {
    fmt<string>.print("toktype is being dropped");
  }
}

class intlit {
  string svalue;
  int ivalue;
  
  deinit {
    fmt<string>.print("intval is being dropped");
  }
}

class tok {
  toktype typ;
  intlit lit;
  node chain;
  
  deinit {
    fmt<string>.print("token is being dropped");
  }
}

class main {
  int main() {
    array<array<array<array<int>>>> arr = new array<array<array<array<int>>>>();
    tok t = new tok();
    return 0;
  }
}
































