native class string 
{
  native string(string buffer);
  native int length();
  native char get(int index);
  native boolean equals(string another);
  
  test "get first char" {
    string s = "a.b.c";
    char c = s.get(0);
    assert_true(c == 'a');
  }
}
