import std.natives.str::str;
import std.list::list;

class dummy_object {
  int i;

  dummy_object() {
    i = -1;
  }
  
  ~dummy_object() {
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
	
	test "test if-s" {
	  int a = 32;
	  int b = -1;
	  if(a == 10) {
	    b = 1;
	  } else if(a == 11) {
	    b = 2;
	  } else if(a == 12) {
	    b = 3;
	  } else {
	    b = 5;
	  }
	  assert_true(b == 5);
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
  
  test "test str equality" {
    str a = "1";
    str b = "2";
    str c = "1";
    
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
  test "test str left" {
    str s = "123";
    assert_true(s.left(1) == "1");
    assert_true(s.left(2) == "12");
    assert_true(s.left(3) == "123");
  }
  
  test "test str right" {
    str s = "123";
    assert_true(s.right(1) == "3");
    assert_true(s.right(2) == "23");
    assert_true(s.right(3) == "123");
  }
  
  test "test str mid" {
    str s = "123";
    assert_true(s.mid(0, 2) == "12");
    assert_true(s.mid(1, 2) == "23");
  }
  
  test "test str starts with some prefix" {
    str s = "a.b.c";
    assert_true(s.starts_with("a"));
    assert_true(s.starts_with("a."));
    assert_true(s.starts_with("a.b"));
    assert_true(s.starts_with("a.b."));
    assert_true(s.starts_with("a.b.c"));
    assert_true(!s.starts_with("a.b.c "));
  }
  
  test "test str ends with" {
    str s = "123";
    assert_true(s.ends_with("3"));
    assert_true(s.ends_with("23"));
    assert_true(s.ends_with("123"));
  }
  
  test "test str access" {
    assert_true("1.2.3".left(2) == "1.");
  }
  
  test "test str trim" {
    str s = " \n \n 123 \t \t \n \r\n  ";
    str res = s.trim();
    assert_true(res == "123");
  }
  
  test "test str constructor from a literal" {
    str s = new str(new str("a.b.c"));
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
  
  static arr<int> get_1d() {
    arr<int> arr = new arr<int>();
    arr.add(32);
    arr.add(64);
    arr.add(128);
    return arr;
  }
  
  static arr<arr<int>> get_2d() {
    arr<arr<int>> arr = new arr<arr<int>>();
    arr.add(get_1d());
    arr.add(get_1d());
    return arr;
  }
  
  static arr<arr<arr<int>>> get_3d() {
    arr<arr<arr<int>>> arr = new arr<arr<arr<int>>>();
    arr.add(get_2d());
    arr.add(get_2d());
    return arr;
  }
  
  test "test 2d array" {
    arr<arr<int>> arr = new arr<arr<int>>();
    arr.add(new arr<int>());
    arr<int> first = arr.get(0);
    assert_true(first.is_empty());
  }
  
  test "test 1d array" {
    arr<int> arr = new arr<int>();
    arr.add(1);
    arr.add(2);
    assert_true(arr.size() == 2);
    assert_true(arr.get(1) == 2);
  }
  
  test "test 3d array" {
    arr<arr<arr<int>>> arr = get_3d();
    assert_true(arr.size() == 2);
    arr<arr<int>> arr2 = arr.get(0);
    assert_true(arr2.size() == 2);
  }
  
  test "test simple arrays of ints" {
    arr<int> a= new arr<int>();
    a.add(32);
    
    arr<int> b= new arr<int>();
    b.add(32);
    
    arr<int> c= new arr<int>();
    c.add(64);
    
    assert_true(a.equals(b));
    assert_true(!a.equals(c));
    assert_true(!b.equals(c));
  }
  
  test "test simple arrays of strings" {
    arr<str> a= new arr<str>();
    a.add("abc");
    
    arr<str> b= new arr<str>();
    b.add("abc");
    
    assert_true(a.equals(b));
  }
}


class main {
  int main() {

    return 0;
  }
}




















