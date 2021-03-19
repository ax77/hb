import std.stdio;

class string {

  private mem<char> buffer;

  string(mem<char> buffer) {
    this.buffer = buffer;
  }

  int length() {
    return buffer.size();
  }

  char get(int index) {
    stdio.assert_true(index >= 0);
    stdio.assert_true(index < buffer.size());
    return buffer.access_at(index);
  }
  
  mem<char> get_buffer() {
    return buffer;
  }
}
