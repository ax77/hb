import std.natives.arr::array;

class string {

  array<char>buffer;

  string(string str) {
    this.buffer = new array<char>();
    
    // TODO: null-terminator for c-compatibility.
    // this null-terminator mustn't have change the length.
    
    for(int i = 0; i < str.length(); i += 1) {
      this.buffer.add(str.get(i));
    }

  }

  string(array<char> arr) {
    this.buffer = arr;
  }

  int length() {
    return buffer.size();
  }

  char get(int at) {
    return buffer.get(at);
  }

  boolean equals(string another) {
    if (length() != another.length()) {
      return false;
    }

    int size = buffer.size();
    for (int i = 0; i < size; i += 1) {
      char c1 = get(i);
      char c2 = another.get(i);
      if (c1 != c2) {
        return false;
      }
    }

    return true;
  }

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
    if (pattern.length() > this.length()) {
      return false;
    }
    if (length() == 0 || pattern.length() == 0) {
      return false;
    }
    for (int i = 0, j = 0; i < length() && j < pattern.length(); i += 1, j += 1) {
      char c1 = get(i);
      char c2 = pattern.get(j);
      if (c1 != c2) {
        return false;
      }
    }
    return true;
  }

  boolean ends_with(string pattern) {
    if (pattern.length() > this.length()) {
      return false;
    }
    if (length() == 0 || pattern.length() == 0) {
      return false;
    }
    int i = length() - 1;
    int j = pattern.length() - 1;
    for (; i >= 0 && j >= 0; i -= 1, j -= 1) {
      char c1 = get(i);
      char c2 = pattern.get(j);
      if (c1 != c2) {
        return false;
      }
    }
    return true;
  }

  // TODO: this one does not work :(
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
