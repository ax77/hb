package rew;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import _st7_codeout.Codeout;
import _st7_codeout.CodeoutBuilder;
import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class TestCcode {

  @Ignore
  @Test
  public void test1() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class str {              //001 \n");
    sb.append("  int f;                 //002 \n");
    sb.append("}                        //003 \n");
    sb.append("class tok {              //004 \n");
    sb.append("  str value;             //005 \n");
    sb.append("  int type;              //006 \n");
    sb.append("}                        //007 \n");
    sb.append("class main_class {       //008 \n");
    sb.append("  int  main() {          //009 \n");
    sb.append("    str s = new str(); return 0;   //010 \n");
    sb.append("  }                      //011 \n");
    sb.append("}                        //012 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    // System.out.println(UtilSrcToStringLevel.tos(result.toString()));
  }

  @Ignore
  @Test
  public void test2() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class vec<T> {                                        //008 \n");
    sb.append("  private array<T> dat;                               //009 \n");
    sb.append("  public vec() {                                      //010 \n");
    sb.append("    dat = new array<T>();                             //011 \n");
    sb.append("  }                                                   //012 \n");
    sb.append("  void add(T element) {                               //013 \n");
    sb.append("    dat.add(element);                                 //014 \n");
    sb.append("  }                                                   //015 \n");
    sb.append("  int size() {                                        //016 \n");
    sb.append("    return dat.size();                                //017 \n");
    sb.append("  }                                                   //018 \n");
    sb.append("  T get(int index) {                                  //019 \n");
    sb.append("    return dat.get(index);                            //020 \n");
    sb.append("  }                                                   //021 \n");
    sb.append("  T set(int index, T element) {                       //022 \n");
    sb.append("    return dat.set(index, element);                   //023 \n");
    sb.append("  }                                                   //024 \n");
    sb.append("}                                                     //025 \n");
    sb.append("class main_class {                                    //026 \n");
    sb.append("  int main() {                                        //027 \n");
    sb.append("    vec<int> flags = new vec<int>();                  //028 \n");
    sb.append("    flags.add(32);                                    //029 \n");
    sb.append("    return flags.size() == 1 && flags.get(0) == 32;   //030 \n");
    sb.append("  }                                                   //031 \n");
    sb.append("}                                                     //032 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    // System.out.println(UtilSrcToStringLevel.tos(result.toString()));
  }

  @Ignore
  @Test
  public void test3() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class vec<T> {                                                         //008 \n");
    sb.append("  private array<T> dat;                                                //009 \n");
    sb.append("  public vec() {                                                       //010 \n");
    sb.append("    dat = new array<T>();                                              //011 \n");
    sb.append("  }                                                                    //012 \n");
    sb.append("  void add(T element) {                                                //013 \n");
    sb.append("    dat.add(element);                                                  //014 \n");
    sb.append("  }                                                                    //015 \n");
    sb.append("  int size() {                                                         //016 \n");
    sb.append("    return dat.size();                                                 //017 \n");
    sb.append("  }                                                                    //018 \n");
    sb.append("  T get(int index) {                                                   //019 \n");
    sb.append("    return dat.get(index);                                             //020 \n");
    sb.append("  }                                                                    //021 \n");
    sb.append("  T set(int index, T element) {                                        //022 \n");
    sb.append("    return dat.set(index, element);                                    //023 \n");
    sb.append("  }                                                                    //024 \n");
    sb.append("}                                                                      //025 \n");
    sb.append("class opts {                                                           //026 \n");
    sb.append("  vec<int> gen() {                                                     //027 \n");
    sb.append("    vec<int> flags = new vec<int>();                                   //028 \n");
    sb.append("    flags.add(32);                                                     //029 \n");
    sb.append("    flags.add(64);                                                     //030 \n");
    sb.append("    return flags;                                                      //031 \n");
    sb.append("  }                                                                    //032 \n");
    sb.append("}                                                                      //033 \n");
    sb.append("class main_class {                                                     //034 \n");
    sb.append("  int main() {                                                         //035 \n");
    sb.append("    vec<int> flags = new opts().gen();                                 //036 \n");
    sb.append("    return flags.size() == 2 && (flags.get(0) + flags.get(1) == 96);   //037 \n");
    sb.append("  }                                                                    //038 \n");
    sb.append("}                                                                      //039 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    // System.out.println(UtilSrcToStringLevel.tos(result.toString()));
  }

  @Ignore
  @Test
  public void test5() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class vec<T> {                                                                             //008 \n");
    sb.append("  private array<T> dat;                                                                    //009 \n");
    sb.append("  public vec() {                                                                           //010 \n");
    sb.append("    dat = new array<T>();                                                                  //011 \n");
    sb.append("  }                                                                                        //012 \n");
    sb.append("  void add(T element) {                                                                    //013 \n");
    sb.append("    dat.add(element);                                                                      //014 \n");
    sb.append("  }                                                                                        //015 \n");
    sb.append("  int size() {                                                                             //016 \n");
    sb.append("    return dat.size();                                                                     //017 \n");
    sb.append("  }                                                                                        //018 \n");
    sb.append("  T get(int index) {                                                                       //019 \n");
    sb.append("    return dat.get(index);                                                                 //020 \n");
    sb.append("  }                                                                                        //021 \n");
    sb.append("  T set(int index, T element) {                                                            //022 \n");
    sb.append("    return dat.set(index, element);                                                        //023 \n");
    sb.append("  }                                                                                        //024 \n");
    sb.append("}                                                                                          //025 \n");
    sb.append("class opts {                                                                               //026 \n");
    sb.append("  int field;                                                                               //027 \n");
    sb.append("  opts() {                                                                                 //028 \n");
    sb.append("    field = 128;                                                                           //029 \n");
    sb.append("  }                                                                                        //030 \n");
    sb.append("  opts(int f) {                                                                            //031 \n");
    sb.append("    field = f;                                                                             //032 \n");
    sb.append("  }                                                                                        //033 \n");
    sb.append("  vec<int> gen() {                                                                         //034 \n");
    sb.append("    vec<int> flags = new vec<int>();                                                       //035 \n");
    sb.append("    flags.add(32);                                                                         //036 \n");
    sb.append("    flags.add(64);                                                                         //037 \n");
    sb.append("    return flags;                                                                          //038 \n");
    sb.append("  }                                                                                        //039 \n");
    sb.append("}                                                                                          //040 \n");
    sb.append("class main_class {                                                                         //041 \n");
    sb.append("  int main() {                                                                             //042 \n");
    sb.append("    vec<int> flags = new opts().gen();                                                     //043 \n");
    sb.append("    vec<opts> some = new vec<opts>();                                                      //044 \n");
    sb.append("    some.add(new opts());                                                                  //045 \n");
    sb.append("    some.add(new opts(1));                                                                 //046 \n");
    sb.append("    boolean f1 = flags.size() == 2 && (flags.get(0) + flags.get(1) == 96);                 //047 \n");
    sb.append("    boolean f2 = some.size() == 2 && some.get(0).field == 128 && some.get(1).field == 1;   //048 \n");
    sb.append("    return f1 && f2;                                                                       //049 \n");
    sb.append("  }                                                                                        //050 \n");
    sb.append("}                                                                                          //051 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    // System.out.println(UtilSrcToStringLevel.tos(result.toString()));
  }

  @Ignore
  @Test
  public void test7() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class main_class {                                   //008 \n");
    sb.append("  int main() {                                       //009 \n");
    sb.append("    array<char> str = new array<char>();             //010 \n");
    sb.append("    str.add(\'a\');                                  //011 \n");
    sb.append("    str.add(\'b\');                                  //012 \n");
    sb.append("    str.add(\'c\');                                  //013 \n");
    sb.append("    return str.size() == 3 && str.get(1) == \'b\';   //014 \n");
    sb.append("  }                                                  //015 \n");
    sb.append("}                                                    //016 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    // System.out.println(UtilSrcToStringLevel.tos(result.toString()));
  }

  @Ignore
  @Test
  public void test8() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class main_class {                                           //001 \n");
    sb.append("  int main() {                                               //002 \n");
    sb.append("    string s = \"a.b.c\";                                    //003 \n");
    sb.append("    return s.char_at(1) == \'.\' && s.char_at(2) == \'b\';   //004 \n");
    sb.append("  }                                                          //005 \n");
    sb.append("}                                                            //006 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    // System.out.println(UtilSrcToStringLevel.tos(result.toString()));
  }

  @Ignore
  @Test
  public void test9() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class main_class {                      //001 \n");
    sb.append("  int main() {                          //002 \n");
    sb.append("    string s = \"a.b.c.d.e\";           //004 \n");
    sb.append("    std.print(\"%s\\n\", s);            //005 \n");
    sb.append("    return 0;                           //006 \n");
    sb.append("  }                                     //007 \n");
    sb.append("}                                       //008 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    // System.out.println(UtilSrcToStringLevel.tos(result.toString()));
  }

  @Test
  public void test10() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class file_reader {                    //001 \n");
    sb.append("  file_reader() {} void close() {}                      //002 \n");
    sb.append("}                                      //003 \n");
    sb.append("class token {                          //004 \n");
    sb.append("  string value;                        //005 \n");
    sb.append("  int f;                               //006 \n");
    sb.append("  token() {                            //007 \n");
    sb.append("    value = \"x\";                     //008 \n");
    sb.append("    f = 1;                             //009 \n");
    sb.append("  }                                    //010 \n");
    sb.append("}                                      //011 \n");
    sb.append("class type {                           //012 \n");
    sb.append("  token pos;                           //013 \n");
    sb.append("  string repr;                         //014 \n");
    sb.append("  file_reader fp;                      //015 \n");
    sb.append("  type() {                             //016 \n");
    sb.append("    pos = new token();                 //017 \n");
    sb.append("    repr = \"int\"; fp = new file_reader();                   //018 \n");
    sb.append("  }                                    //019 \n");
    sb.append("  deinit {                             //020 \n");
    sb.append("    fp.close();                        //021 \n");
    sb.append("  }                                    //022 \n");
    sb.append("}                                      //023 \n");
    sb.append("class main_class {                     //024 \n");
    sb.append("  int main() {                         //025 \n");
    sb.append("    type tp = new type();              //026 \n");
    sb.append("    token tok = tp.pos;                //027 \n");
    sb.append("    std.print(\"%s\\n\", tok.value);   //028 \n");
    sb.append("    std.print(\"%d\\n\", tok.f);       //029 \n");
    sb.append("    return tok.f;                      //030 \n");
    sb.append("  }                                    //031 \n");
    sb.append("}                                      //032 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    System.out.println(result.toString());

  }

}
