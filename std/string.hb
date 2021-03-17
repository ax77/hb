class string 
{
  ptr<char> buf;  
  
  string() {
    this.buf = new ptr<char>(1);
  }
  
  string(ptr<char> buf) {
    this.buf = buf;
  }
  
  int length() {
    return ptr.size();
  }
  
  char get(int at) {
    std.assert_true(at < ptr.size());
    return ptr.offset(at);
  }
}
