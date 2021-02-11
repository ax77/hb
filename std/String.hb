class String {

  private final builtin.array_declare<char> buffer;

  public String() {
    this.buffer = builtin.array_allocate<char>(1);
  }

  public String(builtin.array_declare<char> buffer) {
    this.buffer = buffer;
  }

  char get(int index) {
    if(index < 0 || index > builtin.array_size<char>(buffer)) {
      builtin.panic("index is out of bounds");
    }
    return builtin.array_get<char>(buffer, index);
  }
}