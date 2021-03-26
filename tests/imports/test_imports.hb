import tests.imports.test1::class1;
import tests.imports.test1::class2;
import tests.imports.test1::class3;
import tests.imports.test1::list;

class main_class {
  int main() {
    class1 c1 = new class1();
    class2 c2 = new class2();
    class3 c3 = new class3();
    list<char> l = new list<char>();
    return 0;
  }
}
