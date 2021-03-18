/// std.pointer<T> -> type             std.pointer<char> -> char*, std.pointer<opt> -> opt*, where opt is [typedef struct opt * opt], i.e. [struct opt **]
/// std.mem_malloc<T>(size);           raw_data = malloc(size)
/// std.mem_free<T>(raw_data);         free(raw_data)
/// std.mem_get<T>(raw_data, at);      return raw_data[at]
/// std.mem_set<T>(raw_data, at, e);   raw_data[at] = e
/// sizeof(typename)

/// auxilary pointer wrapper,
/// only for array/string and other std-classes
///
class ptr<T> {
  private std.pointer<T> raw_data;
  private int size;

  ptr(int size) {
    std.assert_true(size > 0);
    this.raw_data = std.mem_malloc<T>(raw_data, size);
    this.size = size;
  }

  void destroy() {
    std.mem_free<T>(raw_data);
  }

  T get(int at) {
    std.assert_true(at < size);
    return std.mem_get<T>(raw_data, at);
  }

  T set(int at, T e) {
    std.assert_true(at < size);
    T old = std.mem_get<T>(raw_data, at);
    std.mem_set<T>(raw_data, at, e);
    return old;
  }

  void cpy(ptr<T> from, int count) {
    std.assert_true(count <= size);
    std.mem_cpy<T>(raw_data, from.get_data(), count);
  }

  int size() {
    return size;
  }
  
  std.pointer<T> get_data() {
    return raw_data;
  }
}
