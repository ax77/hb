import std.natives.str.str;

namespace io {
  native int open(str filename, int mode);
  native int close(int fd);
  native int read(int fd, arr<char> buffer, int size); 
}

namespace fmt<T> {
  native void print(T arg);
}

namespace hash<T> {
  native int hashcode(T arg);
}
