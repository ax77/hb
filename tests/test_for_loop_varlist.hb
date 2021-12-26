import std.natives.fmt::fmt;

static class some {
  
  test "test for loop two vars" {
    for(int i=0, j=i; i<8 && j<8; i+=1, j+=2) {
      fmt.print(i);
      fmt.print(j);
    }
    //for(int i=0; i<8; i+=1) {}
  }
  
}

class main_class {
  int main() {

    return 0;
  }
}
