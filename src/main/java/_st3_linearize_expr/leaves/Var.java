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

  /// a bit which indicates that this is
  /// an original varialbe, i.e. no temporary
  /// generated, and we can ignore any other
  /// var when we will generate destructors.
  /// 
  private boolean isOriginalNoTempVar;

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
    if (isOriginalNoTempVar()) {
      return type.toString() + " " + name.getName();
    }
    return /*"const " + */ type.toString() + " " + name.getName();
  }

  public boolean isOriginalNoTempVar() {
    return isOriginalNoTempVar;
  }

  public void setOriginalNoTempVar(boolean isOriginalNoTempVar) {
    this.isOriginalNoTempVar = isOriginalNoTempVar;
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
