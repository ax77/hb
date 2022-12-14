package utils_oth;

import errors.AstParseException;

public abstract class NullChecker {

  public static void check(Object... what) {
    for (Object o : what) {
      if (o == null) {
        throw new AstParseException("non-nullable property was null...");
      }
    }
  }

}
