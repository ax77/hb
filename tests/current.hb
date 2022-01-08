
class tag {
  mut int i;
}


class main {
  int main() {
    tag t = new tag();
    delete(t);

    return 0;
  }
}
