package templates;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Ignore;
import org.junit.Test;

import utils_fio.FileSearchKind;
import utils_fio.FileWrapper;

public class TestTemplates {

  @Ignore
  @Test
  public void test() throws IOException {

    final String dir = System.getProperty("user.dir");
    final LinkedHashMap<String, String> paths = new LinkedHashMap<>();

    // test_templates_1
    // test_templates_1_exp

    List<FileWrapper> files = new FileWrapper(dir + "/tests/templates/")
        .allFilesInThisDirectory(FileSearchKind.FILES_ONLY);
    for (FileWrapper fw : files) {
      final String basename = fw.getBasename();

      final boolean isOk = basename.startsWith("test_templates_") && !basename.endsWith("_exp.hb");
      if (!isOk) {
        continue;
      }

      final String fullname = fw.getFullname();
      paths.put(fullname, fullname + "_exp.hb");
    }

    for (Entry<String, String> ent : paths.entrySet()) {
      final String source = ent.getKey();
      final String expect = ent.getValue();

      check(source);
      check(expect);

      final TokenCmp cmp = new TokenCmp(source, expect);
      cmp.compare();

      System.out.println("OK: " + source);
    }

  }

  private void check(String fname1) {
    FileWrapper fw = new FileWrapper(fname1);
    fw.assertIsExists();
    fw.assertIsFile();
  }

}
