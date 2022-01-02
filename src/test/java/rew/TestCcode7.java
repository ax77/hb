package rew;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
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

  @Test
  public void test1() throws IOException {

    final String dir = System.getProperty("user.dir");
    final String filename = dir + "/tests/test_arrays_are_equal.hb";

    InstantiationUnit unit = new ParserMain(filename).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      //System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    CgMain result = new CgMain(unit.getClasses());
    System.out.println(result.toString());

    //    String s = " \n \n 123 \t \t \n \r\n  ";
    //    for (int i = s.length() - 1; i >= 0; i -= 1) {
    //      char c = s.charAt(i);
    //      System.out.printf("%d ", (int)c);
    //    }

  }
}
