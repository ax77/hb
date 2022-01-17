package _st3_linearize_expr;

import java.util.HashMap;
import java.util.Map;

import _st3_linearize_expr.ir.VarCreator;
import _st3_linearize_expr.leaves.Var;
import ast_class.ClassDeclaration;
import ast_main.imports.GlobalSymtab;
import ast_symtab.BuiltinNames;
import ast_types.ClassTypeRef;
import ast_types.Type;

public abstract class BuiltinsFnSet {

  private static final Map<String, Var> stringsMap = new HashMap<>();
  private static final Map<String, Var> staticClasses = new HashMap<>();

  public static Map<String, Var> getStringsmap() {
    return stringsMap;
  }

  public static Var getLabel(String s) {
    if (stringsMap.containsKey(s)) {
      return stringsMap.get(s);
    }
    final ClassDeclaration str = GlobalSymtab.getClassByName(BuiltinNames.str_ident, true);
    final Var v = VarCreator.justNewVar(new Type(new ClassTypeRef(str, str.getTypeParametersT())));
    stringsMap.put(s, v);
    return v;
  }

  public static Var getNameFromStatics(String name, Type type) {
    if (staticClasses.containsKey(name)) {
      return staticClasses.get(name);
    }
    final Var newvar = VarCreator.justNewVar(type);
    staticClasses.put(name, newvar);
    return newvar;
  }

  public static void clear() {
    stringsMap.clear();
    staticClasses.clear();
  }

}
