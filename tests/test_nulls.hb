
static class printer<T> {
  static void print(T arg) {
    static_assert(is_int(arg) || is_char(arg));
  }
}

class main_class {
  int main() {
    printer<int>.print(1);
    printer<char>.print('1');
    return 0;
  }
}
