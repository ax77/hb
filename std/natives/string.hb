native class string 
{
  native string(string buffer);
  
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
  // native boolean starts_with(string prefix);
  // native boolean ends_with(string suffix);
  //
  // native array<char> to_array();
  
  test "get first char" {
    string s = "a.b.c";
    char c = s.get(0);
    assert_true(c == 'a');
  }
}
