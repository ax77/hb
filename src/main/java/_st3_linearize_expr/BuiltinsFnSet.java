package _st3_linearize_expr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import _st3_linearize_expr.ir.FlatCodeItem;

public abstract class BuiltinsFnSet {

  private static Set<FlatCodeItem> builtins = new HashSet<>();

  public static void register(FlatCodeItem item) {
    builtins.add(item);
  }

  public static List<FlatCodeItem> getBuiltins() {
    return new ArrayList<>(builtins);
  }

  public static void clear() {
    builtins.clear();
  }

}
