import std.natives.arr::array;

native class string 
{
  native string(string buffer);
  native string(array<char> buffer);
  
  native int length();
  native char get(int index);
  native boolean equals(string another);
  
  // TODO: implement these
  // native string left(int count);
  // native string right(int count);
  // native string mid(int begin, int count);
  // native string trim();
  // native string replace(string pattern, string replacement);
  // native array<string> split(char sep);
  // native boolean starts_with(string prefix);
  // native boolean ends_with(string suffix);
  //
  // native array<char> to_array();
  
  string left(int count) {
    
    if(count == 0 || length() == 0) {
      return this;
    }
    
    if(count >= length()) {
      return this;
    }
    
    array<char> res = new array<char>();
    for(int i = 0; i < count; i += 1) {
      res.add(get(i));
    }
    
    return new string(res);
  }
  
  string right(int count) {
    
    if(count == 0 || length() == 0) {
      return this;
    }
    if(count >= length()) {
      return this;
    }
    
    array<char> res = new array<char>();
    
    int start = length() - count;
    for(int i = start; i < length(); i += 1) {
      res.add(get(i));
    }
    
    return new string(res);
  }
  
  string mid(int begin, int much) {
    if(begin >= length()) {
      return this;
    }
    if(much >= length()) {
      much = length();
    }
    int end = begin + much;
    if(end >= length()) {
      end = length();
    }
    
    array<char> res = new array<char>();
    for(int i = begin; i < end; i += 1) {
      res.add(get(i));
    }
    return new string(res);
  }
  
  boolean starts_with(string prefix) {
    if(prefix.length() > this.length()) {
      return false;
    }
    int i = 0;
    int j = 0;
    for(; i < length() && j < prefix.length(); ) {
      char c1 = get(i);
      char c2 = prefix.get(j);
      if(c1 != c2) {
        return false;
      }
      i += 1;
      j += 1;
    }
    return true;
  }
  
}













