
class string {

  private array<char> buffer;

  string(array<char> buffer) {
    this.buffer = buffer;
  }

  int length() {
    /// buffer should be terminated with '\0'
    assert_true(buffer.size() > 0);
    return buffer.size() - 1;
  }

  char get(int index) {
    assert_true(index >= 0);
    assert_true(index < buffer.size());
    return buffer.get(index);
  }
  
  array<char> bytes() {
    return buffer;
  }
}
