package ast_st3_tac.vars.store;

import ast_modifiers.Modifiers;
import ast_types.Type;
import ast_vars.VarBase;
import tokenize.Ident;

public class Var {
  private final VarBase base;
  private final Modifiers mods;
  private final Type type;
  private final Ident name;

  public Var(VarBase base, Modifiers mods, Type type, Ident name) {
    this.base = base;
    this.mods = mods;
    this.type = type;
    this.name = name;
  }

  public boolean is(VarBase want) {
    return base.equals(want);
  }

  @Override
  public String toString() {
    return name.getName();
  }

  public VarBase getBase() {
    return base;
  }

  public Modifiers getMods() {
    return mods;
  }

  public Type getType() {
    return type;
  }

  public Ident getName() {
    return name;
  }

  public String typeNameToString() {
    return type.toString() + " " + name.getName();
  }

}
