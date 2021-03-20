import std.box;
import std.assert;

class string {

  private box<char> buffer;

  string(box<char> buffer) {
    this.buffer = buffer;
  }

  int length() {
    return buffer.num_of_elements();
  }

  char get(int index) {
    assert.is_true(index >= 0);
    assert.is_true(index < buffer.num_of_elements());
    return buffer.access_at(index);
  }
  
  box<char> get_buffer() {
    return buffer;
  }
}
