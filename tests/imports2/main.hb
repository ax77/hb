
import std.natives.fmt::fmt;

class main_class {
  int main(int argc) {
	fmt.print('1');    
	fmt.print("2");
	array<char> arr = new array<char>();
	for(int i=0; i<32; i+=1) {
		arr.add('.');
	}
	fmt.print(arr);
    return 0;
  }
}

