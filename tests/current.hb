
enum test_type {
  t_plain, t_suite
}

static class tests {
  static int version = 32;
  static int version() {
    return version;
  }
}

interface markable {
  void mark();
}

class utest implements markable {
  test_type typ;
  str name;
  int ver;
  utest() {
    typ = test_type.t_suite;
    name = "unit test";
    ver = tests.version + tests.version();
  }
  void mark() {
    ver = 128;
  }
}

static class main {
  static int main() {
    utest t = new utest();
    if(t.typ == test_type.t_plain) {}
    assert_true(t.ver == 64);
    markable m = t;
    m.mark();
    assert_true(t.ver == 128);
    return 0;
  }
}
