package templates;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.junit.Test;

import utils_fio.FileWrapper;

public class TestTemplates {

  @Test
  public void test() throws IOException {

    final String dir = System.getProperty("user.dir");

    final LinkedHashMap<String, String> paths = new LinkedHashMap<>();
    paths.put("/tests/test_templates_1", dir + "/tests/test_templates_1_exp");
    paths.put("/tests/test_templates_2", dir + "/tests/test_templates_2_exp");

    for (Entry<String, String> ent : paths.entrySet()) {
      check(ent.getValue());

      TokenCmp cmp = new TokenCmp(ent.getKey(), ent.getValue());
      cmp.compare();

      System.out.println("OK: " + ent.getKey());
    }

  }

  private void check(String fname1) {
    FileWrapper fw = new FileWrapper(fname1);
    fw.assertIsExists();
    fw.assertIsFile();
  }

}
