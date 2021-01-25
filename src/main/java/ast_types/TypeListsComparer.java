package ast_types;

import java.util.List;

import utils_oth.NullChecker;

public abstract class TypeListsComparer {

  public static boolean typeListsAreEqual(List<Type> first, List<Type> second) {

    NullChecker.check(first, second);

    if (first.size() != second.size()) {
      return false;
    }

    for (int i = 0; i < first.size(); i++) {
      Type tp1 = first.get(i);
      Type tp2 = second.get(i);
      if (!tp1.is_equal_to(tp2)) {
        return false;
      }
    }

    return true;
  }

}
