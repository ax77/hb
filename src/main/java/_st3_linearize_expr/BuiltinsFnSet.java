package _st3_linearize_expr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import _st3_linearize_expr.ir.FlatCodeItem;
import _st3_linearize_expr.leaves.Var;

public abstract class BuiltinsFnSet {

  private static final Set<FlatCodeItem> builtins = new HashSet<>();
  private static final Map<String, Var> stringsMap = new HashMap<>();

  public static void register(FlatCodeItem item) {
    builtins.add(item);
  }

  public static void register(String s, Var v) {
    if (stringsMap.containsKey(s)) {
      return;
    }
    stringsMap.put(s, v);
  }

  public static List<FlatCodeItem> getBuiltins() {
    return new ArrayList<>(builtins);
  }

  public static Map<String, Var> getStringsmap() {
    return stringsMap;
  }

  public static Var getVar(String s) {
    return stringsMap.get(s);
  }

  public static void clear() {
    builtins.clear();
    stringsMap.clear();
  }

}
