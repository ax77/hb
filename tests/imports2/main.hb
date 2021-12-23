import std.natives.opt::opt;
import std.natives.fmt::fmt;

class dummy_object {
  int i;
  dummy_object() { i = -1; }
}

static class test_expressions {
	
	test "test_ternary" {
		int a = 1024;
		int b = ?(a == 1024, 32, -1);
		assert_true(b == 32);
	}
	
	test "test_arith" {
		int a = 2+2*2;
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
  
  test "test casting between int and char" {
    
    int i = 32; // whitespace
    char c = cast(i : char);
    char d = cast(97 : char);
    char x = cast(98: char);
    
    assert_true(c == ' ');
    assert_true(d == 'a');
    assert_true(x == 'b');
  }
  
}

class main_class {
  int main(int argc) {
	
    return 0;
  }
}






















