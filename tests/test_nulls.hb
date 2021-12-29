
static class printer<T> {
  static void print(T arg) {
  }

  static T unwrap(T value) {
    return value;
  }
}

class main_class {
  int main() {
    array<char> arr = new array<char>();
    for(int i=32; i<120; i+=1) {
      arr.add(cast(i: char));
    }
    
    printer<int>.print(1);
    printer<char>.print('1');
    fmt.print(printer<int>.unwrap(32));
    fmt.print(printer<char>.unwrap('&'));
    fmt.print(printer<array<char>>.unwrap(arr));
    fmt.print(printer<string>.unwrap("1.2.3.4.5.6.7.8.9.0"));
    return 0;
  }
}
