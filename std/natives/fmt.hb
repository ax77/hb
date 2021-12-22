import std.natives.arr::array;
import std.natives.string::string;

static class fmt {
  native void print(array<char> arr);
  native void print(char c);
  native void print(int i);
  native void print(string s);
}