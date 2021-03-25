import std.natives.arr;

static class fd 
{
  native int native_open(string filename, int mode);
  native int native_close(int fd);
  native int native_read(int fd, array<char> buffer, int size);  
}