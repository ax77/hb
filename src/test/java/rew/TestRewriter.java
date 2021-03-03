package rew;

import java.io.IOException;

import org.junit.Test;

import _st7_codeout.Codeout;
import _st7_codeout.CodeoutBuilder;
import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class TestRewriter {

  @Test
  public void testTac() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class ArrayList<T> {                              //001 \n");
    sb.append("  private final std.array_declare<T> table;       //002 \n");
    sb.append("  void add(T element) {                           //003 \n");
    sb.append("    std.array_add<T>(table, element);             //004 \n");
    sb.append("  }                                               //005 \n");
    sb.append("  int size() {                                    //006 \n");
    sb.append("    return std.array_size < T > (table);          //007 \n");
    sb.append("  }                                               //008 \n");
    sb.append("  T get(int index) {                              //009 \n");
    sb.append("    return std.array_get < T > (table, index);    //010 \n");
    sb.append("  }                                               //011 \n");
    sb.append("  T set(int index, T element) {                   //012 \n");
    sb.append("    final T old = get(index);                     //013 \n");
    sb.append("    std.array_set<T>(table, index, element);      //014 \n");
    sb.append("    return old;                                   //015 \n");
    sb.append("  }                                               //016 \n");
    sb.append("}                                                 //017 \n");
    sb.append("class str {                                       //018 \n");
    sb.append("  ArrayList<char> buffer;                         //019 \n");
    sb.append("  str() {                                         //020 \n");
    sb.append("  }                                               //021 \n");
    sb.append("  char char_at(int pos) {                         //022 \n");
    sb.append("    return buffer.get(pos);                       //023 \n");
    sb.append("  }                                               //024 \n");
    sb.append("}                                                 //025 \n");
    sb.append("class main_class {                                //026 \n");
    sb.append("  void main() {                                   //027 \n");
    sb.append("    ArrayList<int> args = new ArrayList<int>();   //028 \n");
    sb.append("    args.add(1);                                  //029 \n");
    sb.append("    str s = new str();                            //030 \n");
    sb.append("    char c = s.char_at(0);                        //031 \n");
    sb.append("  }                                               //032 \n");
    sb.append("}                                                 //033 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    System.out.println(UtilSrcToStringLevel.tos(result.toString()));
  }

}
