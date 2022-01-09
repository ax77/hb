import std.list.list;

class main {
  int main() {
    list<int> root = new list<int>();
    root.push_front(1);
    root.push_front(2);
    delete(root);
    return 0;
  }
}
