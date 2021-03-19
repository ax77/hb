import std.stdio;

class file {

  private int fd;
  private boolean is_open;
  private string fullname;
  private mem<char> buffer;

  file(string fullname) {
    this.fd = -1;
    this.is_open = false;
    this.fullname = fullname;
    
    this.buffer = new mem<char>(2);
    fill_buffer();
  }

  private void fill_buffer() {
    this.buffer.set_at(0, '\0');
    this.buffer.set_at(1, '\0');
  }

  void open() {
    stdio.assert_true(!is_open);
    fd = stdio.open(fullname.get_buffer().get_data(), 0);
    stdio.assert_true(fd != -1);
    is_open = true;
  }

  void close() {
    stdio.assert_true(is_open);
    stdio.close(fd);
    is_open = false;
  }
  
  int read() {
    stdio.assert_true(is_open);
    int c = stdio.read(fd, buffer.get_data(), 1);
    return c;
  }
  
  char getc() {
    stdio.assert_true(is_open);
    return buffer.access_at(0);
  }

}
