
class utest {
  int version;
  utest(int v) {
    version = v;
  }
}

static class main {
  static int main() {
    utest a = new utest(32);
    utest b = a;
    str s = "1.2.3";
    fmt<str>.print(s);
    arr<str> ar = new arr<str>();
    ar.add(s);
    ar.add("4.5.6");
    
    assert_true(ar.size() == 2);
    assert_true(ar.get(0) == "1.2.3");
    
    for(int i=0; i<ar.size(); i+=1){
      str content = ar.get(i);
      fmt<str>.print(content);
    }
    
    return 0;
  }
}
