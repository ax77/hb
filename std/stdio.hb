abstract class stdio {

  native void panic(string because);
  
  native string read_file(string absolute_path);

  native boolean write_file(string absolute_path, string content);
  
  native void printf(string fmt, string arg1);
  
  native void printf(string fmt, string arg1, string arg2);
}
