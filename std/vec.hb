import std.mem;
import std.stdio;

class vec<T> 
{
  mem<T> data;
  int size;
  int alloc;
  
  vec() {
    this.size = 0;
    this.alloc = 2;
    this.data = new mem<T>(sizeof(T) * this.alloc);
  }
  
  void add(T e) {
    if(size >= alloc) {
      alloc *= 2;
      
      mem<T> ndata = new mem<T>(sizeof(T) * this.alloc);
      for(int i = 0; i < size; i += 1) {
        ndata.set_at(i, data.access_at(i));
      }
      data.free();
      data = ndata;
    }
    data.set_at(size, e);
    size += 1;
  }
  
  int size() {
    return size;
  }
  
  T get(int index) {
    stdio.assert_true(index >= 0);
    stdio.assert_true(index < size);
    return data.access_at(index);
  }
  
  T set(int index, T e) {
    stdio.assert_true(index >= 0);
    stdio.assert_true(index < size);
    return data.set_at(index, e);
  }
}