
enum test_type {
  t_plain, t_suite
}

namespace tests {
  int version = 32;
  int version() {
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

class main {
  int main() {
    utest t = new utest();
    if(t.typ == test_type.t_plain) {}
    assert_true(t.ver == 64);
    return 0;
  }
}
