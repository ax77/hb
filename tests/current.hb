
class tag {
  mut int i;
}


class main {
  int main() {
    tag t = new tag();
    delete(t);
    
    arr<str> argv = new arr<str>();
    argv.add("1");
    argv.add("2");
    delete(argv);

    return 0;
  }
}
