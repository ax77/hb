
static class io {
  native int open(str filename, int mode);
  native int close(int fd);
  native int read(int fd, arr<char> buffer, int size); 
}

static class fmt<T> {
  native void print(T arg);
}

static class hash<T> {
  native int hashcode(T arg);
}
