package ast_st2_annotate;

import ast_modifiers.Modifiers;
import ast_symtab.Keywords;
import tokenize.Ident;

public abstract class Mods {

  public static Modifiers varModifiers() {
    Ident[] mods = { Keywords.var_ident };
    return new Modifiers(mods);
  }

  public static Modifiers letModifiers() {
    Ident[] mods = { Keywords.let_ident };
    return new Modifiers(mods);
  }
}
