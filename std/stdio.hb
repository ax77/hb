abstract class stdio {

  native void panic(String because) {}
  
  native String read_file(String absolute_path) {}

  native boolean write_file(String absolute_path, String content) {}
  
  native void printf(String fmt, String arg1) {}
  
  native void printf(String fmt, String arg1, String arg2) {}
}
