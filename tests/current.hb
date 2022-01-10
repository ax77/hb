
enum test_type {
  t_plain, t_suite
}

static class tests {
  static int version = 32;
  static int version() {
    return version;
  }
}

class utest {
  test_type typ;
  str name;
  int ver;
  utest() {
    typ = test_type.t_suite;
    name = "unit test";
    ver = tests.version + tests.version();
  }
}

static class main {
  static int main() {
    utest t = new utest();
    if(t.typ == test_type.t_plain) {}
    assert_true(t.ver == 64);
    return 0;
  }
}
