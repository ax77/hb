static class io {

  static array<char> read_file(string fullname) {
    file fp = new file(fullname);
    fp.open();
  
    array<char> rv = new array<char>();
    
    int sz = fp.read(1);
    while(sz > 0) {
      char c = fp.getc();
      rv.add(c);
      sz = fp.read(1);
    }
    rv.add('\0');
    
    fp.close();
    return rv;
  }
}
