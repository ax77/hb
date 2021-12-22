//////////////////////////////////////////////////////////////////////
native class array<T> 
{
  native array();         
  native array(int size); 
  
  native void add(T element);
  native T get(int index);
  native T set(int index, T element);
  
  native int size();
  native boolean is_empty();
  native boolean equals(array<T> another);
  
  test "get" {
    array<char> arr = new array<char>();
    arr.add('1');
    arr.add('2');
    arr.add('3');
    
    assert_true(arr.get(0) == '1');
    assert_true(arr.size() == 3);
  }
}
//////////////////////////////////////////////////////////////////////
native class string 
{
  native string(string buffer);
  native int length();
  native char get(int index);
  native boolean equals(string another);
  
  test "get first char" {
    string s = "a.b.c";
    char c = s.get(0);
    assert_true(c == 'a');
  }
}
//////////////////////////////////////////////////////////////////////
static class fd 
{
  native int native_open(string filename, int mode);
  native int native_close(int fd);
  native int native_read(int fd, array<char> buffer, int size);  
}
//////////////////////////////////////////////////////////////////////
static class fmt {
  native void print(array<char> arr);
  native void print(char c);
  native void print(int i);
  native void print(string s);
}
//////////////////////////////////////////////////////////////////////
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
//////////////////////////////////////////////////////////////////////
static class stdio 
{

  static array<char> read_file(string fullname) 
  {
    file fp = new file(fullname);
    fp.open();
  
    array<char> rv = new array<char>();
    
    int sz = fp.read();
    while(sz > 0) {
      char c = fp.getc();
      rv.add(c);
      sz = fp.read();
    }
    rv.add('\0');
    
    fp.close();
    return rv;
  }

}


