
import std.natives.fmt::fmt;

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

class main_class {
  int main(int argc) {
	
    return 0;
  }
}

