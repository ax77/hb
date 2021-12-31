package rew;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import _st7_codeout.CgMain;
import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class TestCcode7 {

  private void bufferReaderToList(String path) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(path));
    String line = reader.readLine();

    while (line != null) {
      System.out.println(line);
      line = reader.readLine();
    }

    reader.close();

  }

  @Test
  public void test1() throws IOException {

    final String dir = System.getProperty("user.dir");
    final String filename = dir + "/tests/main.hb";

    InstantiationUnit unit = new ParserMain(filename).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      //System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    CgMain result = new CgMain(unit.getClasses());
    System.out.println(result.toString());

  }
}
