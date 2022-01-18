
class suite {
  int count;
  int count() { return count; }
}

class utest {
  int version;
  char cc;
  suite s;
  utest(int v) {
    version = v;
    cc = 'x';
    s = new suite();
  }
  char getc() { return cc; }
  void fun() { cc == '.'; }
}

static class constants {
  static int ver = 1024;
  static int ver() {
    return 1024;
  }
}

static class main {
  static int main() {
    utest a = new utest(32);
    utest b = a;
    int x = a.version + constants.ver() + constants.ver;
    a.version = 64;
    int jjj = 0;
    jjj = 255;
    str some_string = "1.2.3.4.5.6.7.8.9.0";
    str some_string1 = "1.2.3.4.5.6.7.8.9.0";
    assert_true(some_string == some_string1);
    char cc = a.getc();
    char tt = b.getc();
    if(cc == tt) {
      x = -1;
    }
    boolean f1 = true;
    boolean f2 = f1;
    a.fun();
    if(!f1){
      b.fun();
    }
    int count1 = a.s.count;
    int count2 = a.s.count();
    int zzz = 23+64*3;
    int zzzzzz = 0;
    int yyyyyy = 0;
    yyyyyy = zzzzzz;
    return x < a.version + zzzzzz;
  }
}
