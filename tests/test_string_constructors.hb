import std.natives.string::string;

static class some {
  
  test "test string constructor from a literal" {
    string s = new string(new string("a.b.c"));
    assert_true(s == "a.b.c");
  }
  
}

class main_class {
  int main() {

    return 0;
  }
}
