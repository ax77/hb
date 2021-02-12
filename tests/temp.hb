
class test {
  int f = 32;
  int f2 = 53;

  void fn0() {
    int empty = 0;
    return;
  }

  int fn1() {
    int a = 0;
    int b = 0;
    int c = 0;
    int e = 1024;
    return a + b + c + f;
  }

  int fn3() {

    int a = 0;
    int b = 1;

    if (f == 32) {
      int inif = 128;
      return a-1;
    }

    return b+2-a-f2;

  }
}
