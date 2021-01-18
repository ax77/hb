package njast.ast.mir;

import jscan.symtab.Ident;
import njast.ast.modifiers.Modifiers;
import njast.symtab.IdentMap;

public abstract class Mods {

  public static Modifiers varModifiers() {
    Ident[] mods = { IdentMap.var_ident };
    return new Modifiers(mods);
  }

  public static Modifiers letModifiers() {
    Ident[] mods = { IdentMap.let_ident };
    return new Modifiers(mods);
  }
}
