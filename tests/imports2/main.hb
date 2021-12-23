import std.natives.opt::opt;import std.natives.fmt::fmt;

class dummy_object {
  int i;

  dummy_object() {
    i = -1;
  }
  
  deinit {
    // fmt.print("dummy object destructor");
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
    //fmt.print(__FILE__);
    //fmt.print(__LINE__);
    
    static_assert(is_class(__FILE__));
    static_assert(is_int(__LINE__));
    
    assert_true(__LINE__ >= 140 && __LINE__ <= 160);
    static_assert(__LINE__ == 153); // for sure :)
  }
  
}

class main_class {
  int main() {

    return 0;
  }
}




















