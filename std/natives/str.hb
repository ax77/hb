
class str {

  arr<char>buffer;

  str(str str) {
    this.buffer = new arr<char>();

    for(int i = 0; i < str.length(); i += 1) {
      this.buffer.add(str.get(i));
    }

  }

  str(arr<char> arr) {
    this.buffer = arr;
  }

  int length() {
    return buffer.size();
  }

  char get(int at) {
    return buffer.get(at);
  }

  boolean equals(str another) {
    return buffer.equals(another.buffer);
  }

  str left(int count) {
    
    if(count == 0 || length() == 0) {
      return this;
    }
    
    if(count >= length()) {
      return this;
    }
    
    arr<char> res = new arr<char>();
    for(int i = 0; i < count; i += 1) {
      res.add(get(i));
    }
    
    return new str(res);
  }

  str right(int count) {
    
    if(count == 0 || length() == 0) {
      return this;
    }
    if(count >= length()) {
      return this;
    }
    
    arr<char> res = new arr<char>();
    
    int start = length() - count;
    for(int i = start; i < length(); i += 1) {
      res.add(get(i));
    }
    
    return new str(res);
  }

  str mid(int begin, int much) {
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
    
    arr<char> res = new arr<char>();
    for(int i = begin; i < end; i += 1) {
      res.add(get(i));
    }
    return new str(res);
  }

  boolean starts_with(str pattern) {
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

  boolean ends_with(str pattern) {
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

  str trim() {
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
    
    arr<char> res = new arr<char>();
    for(int i = start; i <= end; i += 1) {
      res.add(get(i));
    }
    return new str(res);
    
  }

}
