
namespace tok_type {
  int T_STRING = 1;
  int T_CHAR = 2;
}

namespace test_static_semantic {
  
  test "test static constants" {
    int x = tok_type.T_CHAR;
    assert_true(x == 2);
    assert_true(x == tok_type.T_CHAR);
  }
  
}


class main {
  int main() {
    return 0;
  }
}