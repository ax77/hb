package tokenize;

import java.util.Map;
import java.util.TreeMap;

import utils_oth.BitmaskToString;

public class Fposition {
  public static final int fnewline = 1 << 0;
  public static final int fleadws = 1 << 1;
  public static final int fatbol = 1 << 2;

  private static final Map<Integer, String> fnames = new TreeMap<Integer, String>();
  static {
    fnames.put(fnewline, "fnewline");
    fnames.put(fleadws, "fleadws");
    fnames.put(fatbol, "fatbol");
  }

  public static String print(int flag) {
    return BitmaskToString.getBitmaskToString(flag, fnames);
  }

}
