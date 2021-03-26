import tests.imports.test1::list;
import std.natives.string::string;
import std.natives.arr::array;

class list_iter<T> {
  list<T> collection;
  list_iter() {
    collection = new list<T>();
  }
}
