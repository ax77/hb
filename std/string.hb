class string {

  private final array<char> buffer;

  public string() {
    this.buffer = new array<char>();
  }

  public string(string buffer) {
    this.buffer = new array<char>(buffer);
  }

  char char_at(int index) {
    return buffer.get(index);
  }
}