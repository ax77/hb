

static class arrs {
  test "test simple arrays of ints" {
    array<int> a= new array<int>();
    a.add(32);
    
    array<int> b= new array<int>();
    b.add(32);
    
    array<int> c= new array<int>();
    c.add(64);
    
    assert_true(a.equals(b));
    assert_true(!a.equals(c));
    assert_true(!b.equals(c));
  }
  
  test "test simple arrays of strings" {
    array<string> a= new array<string>();
    a.add("abc");
    
    array<string> b= new array<string>();
    b.add("abc");
    
    assert_true(a.equals(b));
  }
}

class main {
  int main() {
    return 0;
  }
}
