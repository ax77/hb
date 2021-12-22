import std.natives.string::string;
import std.natives.arr::array;
import std.file::file;

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








