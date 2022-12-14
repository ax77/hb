package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class UtilSrcToStringGenerator {

  private String pad(String s, int c) {
    String pad = "";
    for (int i = 0; i < c - s.length(); ++i) {
      pad += " ";
    }
    return pad;
  }

  private String lineno(int n) {
    return "//" + String.format("%03d ", n);
  }

  private String esc(String what) {
    char q2 = '\"';
    char b = '\\';
    char q1 = '\'';
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < what.length(); i++) {
      char c = what.charAt(i);
      if (c == q2 || c == q1 || c == b) {
        sb.append(b);
        sb.append(c);
        continue;
      }
      if (c == '\t') {
        sb.append("  ");
        continue;
      }
      sb.append(c);
    }
    return sb.toString();
  }

  @Ignore
  @Test
  public void tostr() throws IOException {
    final String dir = System.getProperty("user.dir");
    final String fnameinput = dir + "/tests/__test_src_input.txt";
    final String fnameout = dir + "/tests/__test_src_out.txt";

    String q = "\"";
    String n = "\\n";
    String b = "\\";
    int mlen = 0;
    List<String> lines = new ArrayList<String>();

    try {
      File f = new File(fnameinput);
      BufferedReader bufreader = new BufferedReader(new FileReader(f));

      String readLine = "";
      while ((readLine = bufreader.readLine()) != null) {

        readLine = esc(readLine);
        lines.add(readLine);

        int slen = readLine.length();
        if (mlen < slen) {
          mlen = slen;
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    FileWriter fw = new FileWriter(fnameout);

    fw.write("    //@formatter:off\n");
    fw.write("    StringBuilder sb = new StringBuilder();\n");
    int cnt = 1;
    for (String line : lines) {
      String tmp = line.trim();
      if (tmp.isEmpty()) {
        continue;
      }
      fw.write("    sb.append(" + q + line + pad(line, mlen + 3) + /*lineno(cnt++) +*/ n + q + ");\n");
    }
    fw.write("    //@formatter:on\n");
    fw.close();
  }

}
