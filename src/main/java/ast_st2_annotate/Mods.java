package ast_st2_annotate;

import ast_modifiers.Modifiers;
import ast_symtab.IdentMap;
import tokenize.Ident;

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
