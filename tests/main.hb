import std.natives.string::string;
import std.list::list;

class dummy_object {
  int i;

  dummy_object() {
    i = -1;
  }
  
  deinit {
  }
  
  test "empty" {}
  test "empty " {}
  test "empty  " {}
}

static class test_expressions {

  test"test_ternary"
  {
		int a = 1024;
		int b = ?(a == 1024, 32, -1);
		assert_true(b == 32);
	}

  test"test_arith"
  {
    int a = 2 + 2 * 2;
    assert_true(a == 6);
  }

}

static class test_statements {
	test "test for loop break" {
		int a = 0;
		for(int i=0; i<8; i+=1) {
			a+=1;
			break;
		}
		assert_true(a==1);
	}
	
	test "test for loop continue" {
        int a=0;
        int b=0;
        for(int i=0; i<8; i+=1) {
          if(a==1) {
            b+=1;
            continue;
          }
          a+=1;
        }
		assert_true(a==1);
		assert_true(b==7);
	}
}

static class test_casting {

  test"test casting between int and char"
  {
    
    int i = 32; // whitespace
    char c = cast(i : char);
    char d = cast(97 : char);
    char x = cast(98: char);
    char z = cast(32+1 : char);
    
    assert_true(c == ' ');
    assert_true(d == 'a');
    assert_true(x == 'b');
    assert_true(z == '!');
  }

}

static class test_strings {
  
  test "test string equality" {
    string a = "1";
    string b = "2";
    string c = "1";
    
    assert_true(a == c);
    assert_true(b != a);
    assert_true(b != c);
  }
  
}

static class test_type_traits {

  /// is_void 
  /// is_boolean  
  /// is_char     
  /// is_short     
  /// is_int      
  /// is_long    
  /// is_float    
  /// is_double    
  /// is_integral 
  /// is_floating  
  /// is_class 
  /// is_primitive
  /// is_arithmetic
  /// is_reference
  
  test "test primitive types" {
    char c = '1';
    int i = 0;
    boolean b = false;
    dummy_object d = new dummy_object();
    
    assert_true(is_char(c));
    assert_true(is_int(i));
    assert_true(is_boolean(b));
    
    assert_true(is_primitive(c) && is_primitive(i) && is_primitive(b));
    assert_true(!is_class(c));
    
    assert_true(is_class(d));
    assert_true(!is_primitive(d));
  }
}

static class test_static {
  
  test "test static assert" {
    int a = 0;
    int b = 0;
    
    // these are work at compile time
    static_assert(0 == 0);
    static_assert(is_int(a));
    static_assert(!is_char(a));
    static_assert(types_are_same(a, b));
    
    // and of course, this works at a runtime
    assert_true(a == b && types_are_same(a, b));
  }
  
  test "test macros file, line" {
    static_assert(is_class(__FILE__));
    static_assert(is_int(__LINE__));
  }
  
}

static class test_string_class {
  test "test string left" {
    string s = "123";
    assert_true(s.left(1) == "1");
    assert_true(s.left(2) == "12");
    assert_true(s.left(3) == "123");
  }
  
  test "test string right" {
    string s = "123";
    assert_true(s.right(1) == "3");
    assert_true(s.right(2) == "23");
    assert_true(s.right(3) == "123");
  }
  
  test "test string mid" {
    string s = "123";
    assert_true(s.mid(0, 2) == "12");
    assert_true(s.mid(1, 2) == "23");
  }
  
  test "test string starts with some prefix" {
    string s = "a.b.c";
    assert_true(s.starts_with("a"));
    assert_true(s.starts_with("a."));
    assert_true(s.starts_with("a.b"));
    assert_true(s.starts_with("a.b."));
    assert_true(s.starts_with("a.b.c"));
    assert_true(!s.starts_with("a.b.c "));
  }
  
  test "test string ends with" {
    string s = "123";
    assert_true(s.ends_with("3"));
    assert_true(s.ends_with("23"));
    assert_true(s.ends_with("123"));
  }
  
  test "test string access" {
    assert_true("1.2.3".left(2) == "1.");
  }
  
  test "test string trim" {
    string s = " \n \n 123 \t \t \n \r\n  ";
    string res = s.trim();
    fmt<string>.print(res);
    //assert_true(res == "123");
  }
  
  test "test string constructor from a literal" {
    string s = new string(new string("a.b.c"));
    assert_true(s == "a.b.c");
  }
}

static class tok_type {
  static final int T_STRING = 1;
  static final int T_CHAR = 2;
}

static class test_static_semantic {
  
  test "test static constants" {
    int x = tok_type.T_CHAR;
    assert_true(x == 2);
    assert_true(x == tok_type.T_CHAR);
  }
  
}

static class test_short_circuits {
  test "? ternary operator short circuit division by zero shoud pass" {
    int x = ?(1>0, 12, 1/0);
    assert_true(x==12);
    
    int y = ?(x==12, ?(1>0, 21, 1/0), 1/0);
    assert_true(y==21);
  }
}

static class test_list {
  
  test "test fill list with the values" {
    list<int> list = new list<int>();
    for(int i=0; i<32; i+=1) {
      list.push_front(i);
    }
    while(!list.is_empty()) {
      int i = list.pop_front();
    }
  }
  
}

static class test_arrays {
  
  static array<int> get_1d() {
    array<int> arr = new array<int>();
    arr.add(32);
    arr.add(64);
    arr.add(128);
    return arr;
  }
  
  static array<array<int>> get_2d() {
    array<array<int>> arr = new array<array<int>>();
    arr.add(get_1d());
    arr.add(get_1d());
    return arr;
  }
  
  static array<array<array<int>>> get_3d() {
    array<array<array<int>>> arr = new array<array<array<int>>>();
    arr.add(get_2d());
    arr.add(get_2d());
    return arr;
  }
  
  test "test 2d array" {
    array<array<int>> arr = new array<array<int>>();
    arr.add(new array<int>());
    array<int> first = arr.get(0);
    assert_true(first.is_empty());
  }
  
  test "test 1d array" {
    array<int> arr = new array<int>();
    arr.add(1);
    arr.add(2);
    assert_true(arr.size() == 2);
    assert_true(arr.get(1) == 2);
  }
  
  test "test 3d array" {
    array<array<array<int>>> arr = get_3d();
    assert_true(arr.size() == 2);
    array<array<int>> arr2 = arr.get(0);
    assert_true(arr2.size() == 2);
  }
}

class main {
  int main() {

    return 0;
  }
}




















