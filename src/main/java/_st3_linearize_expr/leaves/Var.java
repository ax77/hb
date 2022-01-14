package _st3_linearize_expr.leaves;

import _st7_codeout.ToStringsInternal;
import ast_modifiers.Modifiers;
import ast_symtab.Keywords;
import ast_types.Type;
import ast_vars.VarBase;
import tokenize.Ident;

public class Var implements Comparable<Var> {
  private static int oCounter = 0;

  private final VarBase base;
  private final Modifiers mods;
  private final Type type;
  private Ident name;
  private final int theOrderOfAppearance;

  /// a bit which indicates that this is
  /// an original varialbe, i.e. no temporary
  /// generated, and we can ignore any other
  /// var when we will generate destructors.
  /// 
  private boolean isOriginalNoTempVar;

  public void replaceName(Ident with) {
    this.name = with;
  }

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

  //TODO: we have to handle this using a special modifiers promotion.
  //it is impossible to use these modifiers EVERYWHERE.
  //Because: we have a LOT of temporary variables, BUT: we may use
  //these modifiers ONLY with original one.
  //AND: 3AC does not know about our intention, and it will generate
  //a straightforward output, and it will be right.
  //SO:TODO
  @SuppressWarnings("unused")
  private String modsToString() {
    StringBuilder sb = new StringBuilder();
    for (Ident id : mods.getModifiers()) {
      sb.append(id.getName());
      sb.append(" ");
    }
    if (mods.getModifiers().isEmpty()) {
      sb.append("const ");
    }
    return sb.toString();
  }

  public String typeNameToString() {
    if (isOriginalNoTempVar()) {
      return ToStringsInternal.typeToString(type) + " " + name.getName();
    }
    return /*modsToString() +*/ ToStringsInternal.typeToString(type) + " " + name.getName();
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
