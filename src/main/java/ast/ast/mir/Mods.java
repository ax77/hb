package ast.ast.mir;

import ast.ast.modifiers.Modifiers;
import ast.symtab.IdentMap;
import jscan.tokenize.Ident;

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
