package rew;

import java.io.IOException;

import org.junit.Test;

import _st7_codeout.Codeout;
import _st7_codeout.CodeoutBuilder;
import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class TestCcode7 {

  @Test
  public void test1() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class ptr<T> {                                             \n");
    sb.append("  std.pointer<T> raw_data;                                 \n");
    sb.append("  int size;                                                \n");
    sb.append("  ptr(int size) {                                          \n");
    sb.append("    this.raw_data = std.mem_malloc<T>(raw_data, size);     \n");
    sb.append("    this.size = size;                                      \n");
    sb.append("  }                                                        \n");
    sb.append("  void destroy() {                                         \n");
    sb.append("    std.mem_free<T>(raw_data);                             \n");
    sb.append("  }                                                        \n");
    sb.append("  T get(int at) {                                          \n");
    sb.append("    std.assert_true(at < size);                            \n");
    sb.append("    return std.mem_get<T>(raw_data, at);                   \n");
    sb.append("  }                                                        \n");
    sb.append("  T set(int at, T e) {                                     \n");
    sb.append("    std.assert_true(at < size);                            \n");
    sb.append("    T old = std.mem_get<T>(raw_data, at);                  \n");
    sb.append("    std.mem_set<T>(raw_data, at, e);                       \n");
    sb.append("    return old;                                            \n");
    sb.append("  }                                                        \n");
    sb.append("}                                                          \n");
    sb.append("class vec<T>                                               \n");
    sb.append("{                                                          \n");
    sb.append("  ptr<T> data;                                             \n");
    sb.append("  int size;                                                \n");
    sb.append("  int alloc;                                               \n");
    sb.append("  vec() {                                                  \n");
    sb.append("    this.size = 0;                                         \n");
    sb.append("    this.alloc = 2;                                        \n");
    sb.append("    this.data = new ptr<T>(sizeof(T) * this.alloc);        \n");
    sb.append("  }                                                        \n");
    sb.append("  void add(T e) {                                          \n");
    sb.append("    if(size >= alloc) {                                    \n");
    sb.append("      alloc *= 2;                                          \n");
    sb.append("      ptr<T> ndata = new ptr<T>(sizeof(T) * this.alloc);   \n");
    sb.append("      for(int i = 0; i < size; i += 1) {                   \n");
    sb.append("        ndata.set(i, data.get(i));                         \n");
    sb.append("      }                                                    \n");
    sb.append("      data.destroy();                                      \n");
    sb.append("      data = ndata;                                        \n");
    sb.append("    }                                                      \n");
    sb.append("    data.set(size, e);                                     \n");
    sb.append("    size += 1;                                             \n");
    sb.append("  }                                                        \n");
    sb.append("  int size() {                                             \n");
    sb.append("    return this.size;                                           \n");
    sb.append("  }                                                        \n");
    sb.append("  T get(int index) {                                       \n");
    sb.append("    std.assert_true(index < this.size);                         \n");
    sb.append("    return data.get(index);                                   \n");
    sb.append("  }                                                        \n");
    sb.append("  T set(int index, T e) {                                  \n");
    sb.append("    std.assert_true(index < this.size);                         \n");
    sb.append("    return data.set(index, e);                             \n");
    sb.append("  }                                                        \n");
    sb.append("}                                                          \n");
    sb.append("class main_class                                           \n");
    sb.append("{                                                          \n");
    sb.append("  int main()                                               \n");
    sb.append("  {                                                        \n");
    sb.append("    vec<char> x = new vec<char>();                         \n");
    sb.append("    x.add(\'a\');                                          \n");
    sb.append("    x.add(\'b\');                                          \n");
    sb.append("    x.add(\'c\');                                          \n");
    sb.append("    x.add(\'d\');                                          \n");
    sb.append("    x.add(\'e\');                                          \n");
    sb.append("    for(int i = 0; i < x.size(); i += 1) {                 \n");
    sb.append("      char c = x.get(i);                                   \n");
    sb.append("      //std.print(\"%c\\n\", c);                             \n");
    sb.append("    }                                                      \n");
    sb.append("    return 0;                                              \n");
    sb.append("  }                                                        \n");
    sb.append("}                                                          \n");
    //@formatter:on


    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    System.out.println(UtilSrcToStringLevel.tos(result.toString()));

  }
}
