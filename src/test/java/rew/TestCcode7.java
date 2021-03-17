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
    sb.append("class ptr<T> {                                            \n");
    sb.append("  std.pointer<T> raw_data;                                \n");
    sb.append("  int size;                                               \n");
    sb.append("  ptr(int size) {                                         \n");
    sb.append("    this.raw_data = std.mem_malloc<T>(raw_data, size);    \n");
    sb.append("    this.size = size;                                     \n");
    sb.append("  }                                                       \n");
    sb.append("  void destroy() {                                        \n");
    sb.append("    std.mem_free<T>(raw_data);                            \n");
    sb.append("  }                                                       \n");
    sb.append("  T get(int at) {                                         \n");
    sb.append("    std.assert_true(at < size);                           \n");
    sb.append("    return std.mem_get<T>(raw_data, at);                  \n");
    sb.append("  }                                                       \n");
    sb.append("  T set(int at, T e) {                                    \n");
    sb.append("    std.assert_true(at < size);                           \n");
    sb.append("    T old = std.mem_get<T>(raw_data, at);                 \n");
    sb.append("    std.mem_set<T>(raw_data, at, e);                      \n");
    sb.append("    return old;                                           \n");
    sb.append("  }                                                       \n");
    sb.append("}                                                         \n");
    sb.append("class main_class                                          \n");
    sb.append("{                                                         \n");
    sb.append("  int main()                                              \n");
    sb.append("  {                                                       \n");
    sb.append("    ptr<char> x = new ptr<char>(32);                      \n");
    sb.append("    return 0;                                             \n");
    sb.append("  }                                                       \n");
    sb.append("}                                                         \n");
    //@formatter:on


    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    System.out.println(UtilSrcToStringLevel.tos(result.toString()));

  }
}
