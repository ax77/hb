

interface markable {
  void mark();
  void unmark();
  int flag();
}

interface equitable {
  void iseq();
}

class token implements markable, equitable {
  mut boolean is_marked;
  void mark() {
    this.is_marked = true;
  }
  void unmark() {
    this.is_marked = false;
  }
  int flag() {
    return 32;
  }
  void iseq() {}
}

class literal implements markable, equitable {
  mut boolean is_marked;
  void mark() {
    this.is_marked = true;
  }
  void unmark() {
    this.is_marked = false;
  }
  int flag() {
    return 64;
  }
  void iseq() {}
}

namespace tests {
  
  void fun(markable m) {
    fmt<int>.print(m.flag());
  }
  
  test "simple" {
    arr<markable> arr = new arr<markable>();
    arr<token> tokens = new arr<token>();
    markable a = new token();
    markable b = new literal();
    equitable c = new token();
    
    tokens.add(new token());
    for(mut int i=0; i<tokens.size(); i+=1) {
      markable m = tokens.get(i);
      arr.add(m);
    }
    
    arr.add(a);
    arr.add(b);
    
    for(mut int i=0; i<arr.size(); i+=1) {
      markable m = arr.get(i);
      fun(m);
    }
  }
}

class main {

  
  int main() {

    return 0;
  }
}
