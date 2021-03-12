class string {

  private final array<char> buffer;

  public string() {
    this.buffer = new array<char>();
  }

  /// internal constructor
  private void add(char c) {
    buffer.add(c);
  }

  char get(int index) {
    return buffer.get(index);
  }
  
  public array<char> buffer() {
    return buffer;
  }
}