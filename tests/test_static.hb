
namespace printer {
  void printi(int i) {}
  void printc(char c) {}
}

class plain_class {
  int i;
}

class main {
  int main() {
    plain_class obj = new plain_class();
    printer.printi(obj.i);
    fmt<int>.print(1);
    fmt<char>.print('2');
    return 0;
  }
}

