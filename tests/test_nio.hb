


class stub {
  int x;
  stub() { x = 8192; }
}

class main_class {
  int main() {
    fmt<int>.print(2048);
    fmt<string>.print("str 2048");
    int x = hash<int>.hashcode(32);
    
    stub s = new stub();
    fmt<stub>.print(s);
    fmt<int>.print(hash<stub>.hashcode(s));
    
    return x;
  }
}
