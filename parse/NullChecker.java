package njast.parse;

import njast.errors.EParseException;

public abstract class NullChecker {

  public static void check(Object... what) {
    for (Object o : what) {
      if (o == null) {
        throw new EParseException("non-nullable property was null...");
      }
    }
  }

}
