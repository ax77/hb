class tok {
  int value;
}

class type {
  tok token;
  void fn() {
    int a = 0;
    int b = 1;
    token.value = a + b;
  }
}

//class test {
//  type tp;
//  int f = 32;
//  int f2 = 53;
//
//  void fn0() {
//    tp = new type();
//    int empty = 0;
//    return;
//  }
//
//  int fn1() {
//    int a = 0;
//    int b = 0;
//    int c = 0;
//    int e = 1024;
//    return a + b + c + f;
//  }
//
//  type gt() {
//    type ntype = new type();
//    return new type();
//  }
//
//  int fn3(int p1, int p2) {
//    int a = 0;
//    int b = 1;
//
//    if (f == 32) {
//      int inif = 128;
//      return a - 1 - p2;
//    }
//    {
//      a = 1;
//      b = 2;
//      int c = 3;
//    }
//
//    return b + 2 - a - f2 + p1;
//
//  }
//}
