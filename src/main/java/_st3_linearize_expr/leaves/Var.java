package _st3_linearize_expr.leaves;

import ast_modifiers.Modifiers;
import ast_types.Type;
import ast_vars.VarBase;
import tokenize.Ident;

public class Var implements Comparable<Var> {
  private static int oCounter = 0;

  private final VarBase base;
  private final Modifiers mods;
  private final Type type;
  private final Ident name;
  private final int theOrderOfAppearance;

  public Var(VarBase base, Modifiers mods, Type type, Ident name) {
    this.base = base;
    this.mods = mods;
    this.type = type;
    this.name = name;

    this.theOrderOfAppearance = oCounter;
    oCounter += 1;
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

  public int getTheOrderOfAppearance() {
    return theOrderOfAppearance;
  }

  public String typeNameToString() {
    return type.toString() + " " + name.getName();
  }

  @Override
  public int compareTo(Var o) {
    if (theOrderOfAppearance < o.getTheOrderOfAppearance()) {
      return 1;
    }
    if (theOrderOfAppearance > o.getTheOrderOfAppearance()) {
      return -1;
    }
    return 0;
  }

}
