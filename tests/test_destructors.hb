
class tok_type {
  int flag;
  tok_type() { this.flag = 1024; }
}

class location {
  str filename;
  int line;
  ~location() {
    filename = default(str);
  }
}

class num_literal {
  char cvalue;
  int ivalue;
}

class token {
  tok_type type;
  location loc;
  num_literal literal;
  str value;
}

namespace test_destructors {
  
  token get_tok() {
    token tok = new token();
    return tok;
  }
  
  test "test one block" {
    token tok = get_tok();
  }
  
}

class main {
  int main() {
    return 0;
  }
}
































