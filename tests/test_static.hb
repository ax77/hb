import std.natives.string::string;

static class tok_type {
  static final int T_STRING = 1;
  static final int T_CHAR = 2;
}

static class some {
  
  test "test static constants" {
    int x = tok_type.T_CHAR;
    assert_true(x == 2);
    assert_true(x == tok_type.T_CHAR);
  }
  
}

class main_class {
  int main() {

    return 0;
  }
}
