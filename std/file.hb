class file {

  private int fd;
  private boolean is_open;
  private string fullname;
  private ptr<char> buffer;

  file(string fullname) {
    this.fd = -1;
    this.is_open = false;
    this.fullname = fullname;
    
    this.buffer = new ptr<char>(2);
    fill_buffer();
  }

  private void fill_buffer() {
    this.buffer.set(0, '\0');
    this.buffer.set(1, '\0');
  }

  void open() {
    std.assert_true(!is_open);
    fd = std.open(fullname.get_buffer().get_data(), 0);
    std.assert_true(fd != -1);
    is_open = true;
  }

  void close() {
    std.assert_true(is_open);
    std.close(fd);
    is_open = false;
  }
  
  int read(int size) {
    std.assert_true(is_open);
    int c = std.read(fd, buffer.get_data(), size);
    return c;
  }
  
  char getc() {
    std.assert_true(is_open);
    return buffer.get(0);
  }

}
