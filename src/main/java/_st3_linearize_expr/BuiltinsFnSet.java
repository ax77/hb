package _st3_linearize_expr;

import java.util.HashMap;
import java.util.Map;

import _st3_linearize_expr.ir.VarCreator;
import _st3_linearize_expr.leaves.Var;
import ast_types.Type;

public abstract class BuiltinsFnSet {

  private static final Map<String, Var> stringsMap = new HashMap<>();
  private static final Map<String, Var> staticClasses = new HashMap<>();

  public static void registerStringLabel(String s, Var v) {
    if (stringsMap.containsKey(s)) {
      return;
    }
    stringsMap.put(s, v);
  }

  public static Map<String, Var> getStringsmap() {
    return stringsMap;
  }

  public static Var getVar(String s) {
    return stringsMap.get(s);
  }

  public static Var getNameFromStatics(String name, Type type) {
    if (staticClasses.containsKey(name)) {
      return staticClasses.get(name);
    }
    Var newvar = VarCreator.justNewVar(type);
    staticClasses.put(name, newvar);
    return newvar;
  }

  public static void clear() {
    stringsMap.clear();
    staticClasses.clear();
  }

}
