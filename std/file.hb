import std.assert;

class file {

  private int fd;
  private boolean is_open;
  private string fullname;
  private array<char> buffer;

  file(string fullname) {
    this.fd = -1;
    this.is_open = false;
    this.fullname = fullname;
    
    this.buffer = new array<char>(2);
    fill_buffer();
  }

  private void fill_buffer() {
    buffer.set(0, '\0');
    buffer.set(1, '\0');
  }

  void open() {
    assert.is_true(!is_open);
    fd = native_open(fullname.bytes(), 0);
    assert.is_true(fd != -1);
    is_open = true;
  }

  void close() {
    assert.is_true(is_open);
    native_close(fd);
    is_open = false;
  }
  
  int read() {
    assert.is_true(is_open);
    int c = native_read(fd, buffer, 1);
    return c;
  }
  
  char getc() {
    assert.is_true(is_open);
    return buffer.get(0);
  }
  
  native int native_open(array<char> filename, int mode);
  native int native_close(int fd);
  native int native_read(int fd, array<char> buffer, int size);

}
