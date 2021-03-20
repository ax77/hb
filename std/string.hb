//import std.assert;

class string {

  private arr<char> buffer;

  string(arr<char> buffer) {
    this.buffer = buffer;
  }

  int length() {
    return buffer.size();
  }

  char get(int index) {
    //assert.is_true(index >= 0);
    //assert.is_true(index < buffer.size());
    return buffer.get(index);
  }
  
  arr<char> bytes() {
    return buffer;
  }
}
