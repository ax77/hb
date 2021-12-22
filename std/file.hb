import std.natives.fd::fd;

class file {

  private int file_descriptor;
  private boolean is_open;
  private string fullname;

  private array<char> buffer;

  file(string fullname) {
    this.file_descriptor = -1;
    this.is_open = false;
    this.fullname = fullname;
    
    this.buffer = new array<char>(2);
    fill_buffer();
  }

  private void fill_buffer() {
    this.buffer.add('\0');
    this.buffer.add('\0');
  }

  void open() {
    assert_true(!is_open);
    file_descriptor = fd.native_open(fullname, 0);
    assert_true(file_descriptor != -1);
    is_open = true;
  }

  void close() {
    assert_true(is_open);
    fd.native_close(file_descriptor);
    is_open = false;
  }

  int read() {
    assert_true(is_open);
    int c = fd.native_read(file_descriptor, buffer, 1);
    return c;
  }

  char getc() {
    assert_true(is_open);
    return buffer.get(0);
  }

}
