

interface markable {
  void mark();
  void unmark();
  int flag();
}

class token implements markable {
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
}

class literal implements markable {
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
}

namespace tests {
  
  void fun(markable m) {
    fmt<int>.print(m.flag());
  }
  
  test "simple" {
    arr<markable> arr = new arr<markable>();
    markable a = new token();
    markable b = new literal();
    
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
