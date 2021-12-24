import std.natives.string::string;

static class some {
  test "test string left" {
    string s = "123";
    assert_true(s.left(1) == "1");
    assert_true(s.left(2) == "12");
    assert_true(s.left(3) == "123");
  }
  
  test "test string right" {
    string s = "123";
    assert_true(s.right(1) == "3");
    assert_true(s.right(2) == "23");
    assert_true(s.right(3) == "123");
  }
  
  test "test string mid" {
    string s = "123";
    assert_true(s.mid(0, 2) == "12");
    assert_true(s.mid(1, 2) == "23");
  }
  
  test "test string starts with some prefix" {
    string s = "a.b.c";
    assert_true(s.starts_with("a"));
    assert_true(s.starts_with("a."));
    assert_true(s.starts_with("a.b"));
    assert_true(s.starts_with("a.b."));
    assert_true(s.starts_with("a.b.c"));
    assert_true(!s.starts_with("a.b.c "));
  }
  
  test "test string ends with" {
    string s = "123";
    assert_true(s.ends_with("3"));
    assert_true(s.ends_with("23"));
    assert_true(s.ends_with("123"));
  }
  
  test "test string access" {
    assert_true("1.2.3".left(2) == "1.");
  }
}

class main_class {
  int main() {

    return 0;
  }
}
