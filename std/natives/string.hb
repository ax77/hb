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
  // native boolean starts_with(string pattern);
  // native boolean ends_with(string pattern);
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
  
  boolean starts_with(string pattern) {
    if(pattern.length() > this.length()) {
      return false;
    }
    if(length()==0 || pattern.length() == 0) {
      return false;
    }
    int i = 0;
    int j = 0;
    for(; i < length() && j < pattern.length(); ) {
      char c1 = get(i);
      char c2 = pattern.get(j);
      if(c1 != c2) {
        return false;
      }
      i += 1;
      j += 1;
    }
    return true;
  }
  
  boolean ends_with(string pattern) {
    if(pattern.length() > this.length()) {
      return false;
    }
    if(length()==0 || pattern.length() == 0) {
      return false;
    }
    int i = length()-1;
    int j = pattern.length()-1;
    for(; i >=0 && j >= 0; ) {
      char c1 = get(i);
      char c2 = pattern.get(j);
      if(c1 != c2) {
        return false;
      }
      i-=1;
      j-=1;
    }
    return true;
  }
  
  string trim() {
    if(length() == 0) {
      return this;
    }
    
    int start = 0;
    int end = 0;
    
    for(start = 0; start < length(); start += 1) {
      char c = get(start);
      if(c > ' ') {
        break;
      }
    }
    
    for(end = length()-1; end >=0; end -= 1) {
      char c = get(end);
      if(c > ' ') {
        break;
      }
    }
    
    array<char> res = new array<char>();
    for(int i = start; i <= end; i += 1) {
      res.add(get(i));
    }
    return new string(res);
    
  }
  
}













