
class tok_type {
  int flag;
  tok_type() { this.flag = 1024; }
}

class location {
  string filename;
  int line;
  location() {
    this.filename = "<builtin>";
    this.line = 1;
  }
}

class num_literal {
  char cvalue;
  int ivalue;
  num_literal() {
    this.cvalue = ' ';
    this.ivalue = 32;
  }
}

class token {
  tok_type type;
  location loc;
  num_literal literal;
  string value;
  int posflag;
  char spec;
}

static class test_destructors {
  
  static token get_tok() {
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
































