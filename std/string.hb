import std.assert;

class string {

  private arr<char> buffer;

  string(arr<char> buffer) {
    this.buffer = buffer;
  }

  int length() {
    /// buffer should be terminated with '\0'
    assert.is_true(buffer.size() > 0);
    return buffer.size() - 1;
  }

  char get(int index) {
    assert.is_true(index >= 0);
    assert.is_true(index < buffer.size());
    return buffer.get(index);
  }
  
  arr<char> bytes() {
    return buffer;
  }
}
