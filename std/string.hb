class string {

  private final array<char> buffer;

  public string() {
    this.buffer = new array<char>();
  }

  public void appendInternal(char c) {
    buffer.add(c);
  }

  char char_at(int index) {
    return buffer.get(index);
  }
}