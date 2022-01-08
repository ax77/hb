
class str {

  mut arr<char>buffer;

  str(str str) {
    this.buffer = new arr<char>();
    int len = str.length();
    
    for(mut int i = 0; i < len; i += 1) {
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
    int len = length();
    
    if(count == 0 || len == 0) {
      return this;
    }
    
    if(count >= len) {
      return this;
    }
    
    arr<char> res = new arr<char>();
    for(mut int i = 0; i < count; i += 1) {
      res.add(get(i));
    }
    
    return new str(res);
  }

  str right(int count) {
    int len = length();
    
    if(count == 0 || len == 0) {
      return this;
    }
    if(count >= len) {
      return this;
    }
    
    arr<char> res = new arr<char>();
    
    int start = len - count;
    for(mut int i = start; i < len; i += 1) {
      res.add(get(i));
    }
    
    return new str(res);
  }

  str mid(int begin, mut int much) {
    int len = length();
    
    if(begin >= len) {
      return this;
    }
    if(much >= len) {
      much = len;
    }
    mut int end = begin + much;
    if(end >= len) {
      end = len;
    }
    
    arr<char> res = new arr<char>();
    for(mut int i = begin; i < end; i += 1) {
      res.add(get(i));
    }
    return new str(res);
  }

  boolean starts_with(str pattern) {
    int len1 = length();
    int len2 = pattern.length();
    
    if (len2 > len1) {
      return false;
    }
    if (len1 == 0 || len2 == 0) {
      return false;
    }
    
    for (mut int i = 0, j = 0; i < len1 && j < len2; i += 1, j += 1) {
      char c1 = get(i);
      char c2 = pattern.get(j);
      if (c1 != c2) {
        return false;
      }
    }
    return true;
  }

  boolean ends_with(str pattern) {
    int len1 = length();
    int len2 = pattern.length();
    
    if (len2 > len1) {
      return false;
    }
    if (len1 == 0 || len2 == 0) {
      return false;
    }
    mut int i = len1 - 1;
    mut int j = len2 - 1;
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
    int len = length();
    
    if(len == 0) {
      return this;
    }
    
    mut int start = 0;
    mut int end = 0;
    
    for(start = 0; start < len; start += 1) {
      char c = get(start);
      if(c > ' ') {
        break;
      }
    }
    
    for(end = len-1; end >=0; end -= 1) {
      char c = get(end);
      if(c > ' ') {
        break;
      }
    }
    
    arr<char> res = new arr<char>();
    for(mut int i = start; i <= end; i += 1) {
      res.add(get(i));
    }
    return new str(res);
    
  }

}
