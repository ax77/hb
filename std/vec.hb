
import std.assert;

class vec<T> 
{
  arr<T> data;
  int size;
  int alloc;
  
  vec() {
    this.size = 0;
    this.alloc = 2;
    this.data = new arr<T>(this.alloc);
  }
  
  void add(T e) {
    if(size >= alloc) {
      alloc += 1;
      alloc *= 2;
      
      arr<T> ndata = new arr<T>(this.alloc);
      for(int i = 0; i < size; i += 1) {
        ndata.set(i, data.get(i));
      }
      //data.free();
      data = ndata;
    }
    data.set(size, e);
    size += 1;
  }
  
  int size() {
    return size;
  }
  
  T get(int index) {
    //assert.is_true(index >= 0);
    //assert.is_true(index < size);
    return data.get(index);
  }
  
  T set(int index, T e) {
    //assert.is_true(index >= 0);
    //assert.is_true(index < size);
    return data.set(index, e);
  }
  
  arr<T> data() {
    return data;
  }
}
