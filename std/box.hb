import std.assert;

class box<T> 
{
  final *T raw_data;
  final int element_size;
  final int num_of_elements;
  
  box(int element_size, int num_of_elements) {
    assert.is_true(element_size > 0);
    assert.is_true(num_of_elements > 0);
    this.element_size = element_size;
    this.num_of_elements = num_of_elements;
    this.raw_data = malloc();
  }

  int element_size() {
    return element_size;
  }
  
  int num_of_elements() {
    return num_of_elements;
  }
  
  *T raw_data() {
    return raw_data;
  }
  
  *T malloc() {
    return native_malloc(raw_data, element_size * num_of_elements);
  }
  
  *T calloc() {
    return native_calloc(raw_data, num_of_elements, element_size);
  }
  
  void free() {
    native_free(raw_data);
  }
  
  T access_at(int offset) {
    assert.is_true(offset < num_of_elements);
    return native_ptr_access_at(raw_data, offset);
  }
  
  T set_at(int offset, T value) {
    assert.is_true(offset < num_of_elements);
    return native_ptr_set_at(raw_data, offset, value);
  }
  
  *T memcpy(*T src, int count) {
    //assert.is_true(src);
    assert.is_true(count > 0);
    assert.is_true(count <= num_of_elements);
    return native_memcpy(raw_data, src, count);
  }

  native *T native_malloc(*T ptr, int size);
  native *T native_calloc(*T ptr, int count, int size);
  native void native_free(*T ptr);
  native T native_ptr_access_at(*T ptr, int offset);       // char c = p[i]
  native T native_ptr_set_at(*T ptr, int offset, T value); // p[i] = c
  native *T native_memcpy(*T dst, *T src, int count);
}
