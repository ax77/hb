class String {

  private final std.array_declare<char> buffer;

  public String() {
    this.buffer = std.array_allocate<char>(1);
  }

  public String(std.array_declare<char> buffer) {
    this.buffer = buffer;
  }

  char get(int index) {
    if(index < 0 || index > std.array_size<char>(buffer)) {
      aux.panic("index is out of bounds");
    }
    return std.array_get<char>(buffer, index);
  }
}