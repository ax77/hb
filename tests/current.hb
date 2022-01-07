
/// interface markable {
///   void mark();
///   void unmark();
/// }
/// 
/// class token implements markable {
///   mut boolean is_marked;
///   void mark() {}
///   void unmark() {}
/// }
/// 
/// enum toktype {
///   tp_ident, tp_string
/// }

class strbuf {
  arr<char> buffer;
  
  strbuf(str input) {
    this.buffer = new arr<char>();
    for(mut int i=0; i<input.len(); i+=1) {
      buffer.add(input.get(i));
    }
  }
}

class main {
  int main() {
    str s = "abc";
    fmt<str>.print(s);
    strbuf buf = new strbuf(s);
    fmt<arr<char>>.print(buf.buffer);
    return 0;
  }
}
