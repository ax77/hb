import std.box;
import std.vec;
import std.string;

static class stdio 
{

  static vec<char> read_file(string fullname) 
  {
    file fp = new file(fullname);
    fp.open();
  
    vec<char> rv = new vec<char>();
    
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
  
  static void printf(string s, char c) {
    native_printf(s.get_buffer().raw_data(), c);
  }
  
  static void printf(string s, int c) {
    native_printf(s.get_buffer().raw_data(), c);
  }
  
  native void native_printf(*char fmt, char c);
  native void native_printf(*char fmt, int c);
}








