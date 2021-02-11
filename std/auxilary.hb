abstract class aux {

  native void print(String s, int i) {}

  native void panic(String because) {}
  
  native String read_file(String absolute_path) {}

  native boolean write_file(String absolute_path, String content) {}
}
