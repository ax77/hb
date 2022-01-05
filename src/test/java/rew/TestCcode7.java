package rew;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import _st7_codeout.CgMain;
import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class TestCcode7 {

  @SuppressWarnings("unused")
  private void bufferReaderToList(String path) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(path));
    String line = reader.readLine();

    while (line != null) {
      System.out.println(line);
      line = reader.readLine();
    }

    reader.close();

  }

  @Ignore
  @Test
  public void testMap() {
    Hashtable<String, String> map = new Hashtable<String, String>();
    map.put("test", "test123");
    map.put("test2", "test456");

    for (Iterator<Map.Entry<String, String>> it = map.entrySet().iterator(); it.hasNext();) {
      Map.Entry<String, String> entry = it.next();
      if (entry.getKey().equals("test")) {
        it.remove();
      }
    }
  }

  @Ignore
  @Test
  public void testSet() {
    HashSet<String> s1 = new HashSet<>();
    s1.add("var_1");
    s1.add("var_2");

    HashSet<String> s2 = new HashSet<>();
    s2.add("var_2");
    s2.add("var_3");

    HashSet<String> intersection = new HashSet<String>(s1); // use the copy constructor
    intersection.retainAll(s2);

    for (String s : intersection) {
      System.out.println(s);
    }
  }

  @Ignore
  @Test
  public void genPath() {
    /// class a {
    ///   int next;
    ///   a() { next = 32; }
    ///   int next() { return next; }
    /// }
    ///
    /// class b {
    ///   a next;
    ///   b() { next = new a(); }
    ///   a next() { return next; }
    /// }
    ///
    /// class c {
    ///   b next;
    ///   c() { next = new b(); }
    ///   b next() { return next; }
    /// }

    String names = "a b c d e f g h i j k l m n o p q r s t u v w x y z";
    final String[] split = names.split(" ");
    StringBuilder sb = new StringBuilder();

    sb.append("class a {                   \n");
    sb.append("  int next;                 \n");
    sb.append("  a() { this.next = 32; };  \n");
    sb.append("  int next() {              \n");
    sb.append("    return this.next;       \n");
    sb.append("  }                         \n");
    sb.append("}                           \n\n");

    for (int i = 1; i < split.length; i += 1) {
      String curr = split[i];
      String prev = split[i - 1];
      sb.append("class " + curr + " {\n");
      sb.append("    " + prev + " next;\n");
      sb.append("    " + curr + "() { this.next = new " + prev + "(); }\n");
      sb.append("    " + prev + " next() { return this.next; }\n");
      sb.append("}\n\n");
    }
    sb.append("z obj = new z();\n");
    sb.append(
        "int res = obj.next.next().next.next.next().next().next.next().next.next().next.next.next().next().next().next.next.next.next.next().next.next().next.next().next().next;\n");

    System.out.println(sb.toString());

  }

  private void printFloat(double f) {
    String s = String.format(Locale.US, "%f", f);
    System.out.println(s);
  }

  @Ignore
  @Test
  public void testParseDouble() {
    double a = 472086.94347999006;
    double b = 4.720869e+05;
    double c = 0x1.cd05bc61f9e57p+18;

    printFloat(Double.parseDouble("472086.94347999006"));
    printFloat(Double.parseDouble("4.720869e+05"));
    printFloat(Double.parseDouble("0x1.cd05bc61f9e57p+18"));

    System.out.printf("%e\n", a);
  }

  @Test
  public void test1() throws IOException {

    final String dir = System.getProperty("user.dir");
    final String filename = dir + "/tests/test_mut.hb";

    InstantiationUnit unit = new ParserMain(filename).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      //System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    CgMain result = new CgMain(unit.getClasses());
    System.out.println(result.toString());

    //    FileWriter fw = new FileWriter("ccode.c");
    //    fw.write(result.toString());
    //    fw.close();

  }
}
