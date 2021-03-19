import std.stdio;

class mem<T> 
{
  final *T raw_data;
  final int size;
  
  mem(int size) {
    stdio.assert_true(size > 0);
    this.size = size;
    this.raw_data = malloc();
  }

  int size() {
    return size;
  }
  
  *T get_data() {
    return raw_data;
  }
  
  *T malloc() {
    return malloc(raw_data, size);
  }
  
  *T calloc(int count) {
    stdio.assert_true(count > 0);
    return calloc(raw_data, count, size);
  }
  
  void free() {
    free(raw_data);
  }
  
  T access_at(int offset) {
    stdio.assert_true(offset < size);
    return access_at(raw_data, offset);
  }
  
  T set_at(int offset, T value) {
    stdio.assert_true(offset < size);
    return set_at(raw_data, offset, value);
  }
  
  *T memcpy(*T src, int count) {
    stdio.assert_true(count <= size);
    return memcpy(raw_data, src, count);
  }

  native *T malloc(*T ptr, int size);
  native *T calloc(*T ptr, int count, int size);
  native void free(*T ptr);
  native T access_at(*T ptr, int offset);       // char c = p[i]
  native T set_at(*T ptr, int offset, T value); // p[i] = c
  native *T memcpy(*T dst, *T src, int count);
}














