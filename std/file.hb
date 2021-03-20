import std.stdio;
import std.assert;
import std.vec;

class file {

  private int fd;
  private boolean is_open;
  private string fullname;
  private box<char> buffer;

  file(string fullname) {
    this.fd = -1;
    this.is_open = false;
    this.fullname = fullname;
    
    this.buffer = new box<char>(sizeof(char), 2);
    fill_buffer();
  }

  private void fill_buffer() {
    buffer.set_at(0, '\0');
    buffer.set_at(1, '\0');
  }

  void open() {
    assert.is_true(!is_open);
    fd = native_open(fullname.get_buffer().raw_data(), 0);
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
    int c = native_read(fd, buffer.raw_data(), 1);
    return c;
  }
  
  char getc() {
    assert.is_true(is_open);
    return buffer.access_at(0);
  }
  
  native int native_open(*char filename, int mode);
  native int native_close(int fd);
  native int native_read(int fd, *char buffer, int size);

}
