class string {

  private ptr<char> buffer;

  string(ptr<char> buffer) {
    this.buffer = buffer;
  }

  int length() {
    return buffer.size();
  }

  char get(int index) {
    std.assert_true(index >= 0);
    std.assert_true(index < buffer.size());
    return buffer.get(index);
  }
}
