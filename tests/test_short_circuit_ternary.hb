class main_class {
  int main() {
    
    int x = ?(1>0, 12, 1/0);
    assert_true(x==12);
    
    int y = ?(x==12, ?(1>0, 21, 1/0), 1/0);
    assert_true(y==21);
    
    return 0;
  }
}
