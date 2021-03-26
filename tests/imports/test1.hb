import tests.imports.test2::list_iter;
import std.natives.string::string;
import std.natives.arr::array;

class class1{
  int class1_i;
  class1(){ class1_i=0; }
}

class class2{
  int class2_i;
  class2(){ class2_i=0; }
}

class class3{
  int class3_i;
  class3(){ class3_i=0; }
}


class list<T>{
  list_iter<T> iter;
  list() {
    iter = new list_iter<T>();
  }
}
