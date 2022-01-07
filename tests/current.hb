//interface settable {
//  void stub();
//}
//
//interface markable {
//  void mark();
//  void unmark();
//  int set(int a, int b, int c);
//}
//
//class token implements markable, settable {
//  mut boolean is_marked;
//  void mark() {is_marked=true;}
//  void unmark() {is_marked=false;}
//  void stub() {}
//  int set(int a, int b, int c) {
//    return 32;
//  }
//}
//
//class literal implements markable, settable {
//  mut boolean is_marked;
//  void mark() {}
//  void unmark() {}
//  void stub() {}
//  int set(int a, int b, int c) {
//    return 64;
//  }
//}

class temp {
  mut str i;
}

class node {
  mut node prev;
  mut node next;
  int item;
}

class main {
  int main() {
    temp t = new temp();
    
    node n = new node();

    //    token tok = new token();
    //    markable m = tok;
    //    m.mark();
    //    assert_true(tok.is_marked);
    //    m.unmark();
    //    assert_true(!tok.is_marked);
    //    
    //    markable m2 = new literal();
    //    m2.mark();
    //    m2.unmark();
    //    
    //    settable sss = new token();

    return 0;
  }
}
