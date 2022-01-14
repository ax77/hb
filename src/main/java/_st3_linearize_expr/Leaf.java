package _st3_linearize_expr;

import _st3_linearize_expr.leaves.Var;
import _st7_codeout.ToStringsInternal;
import ast_types.Type;
import literals.IntLiteral;

public class Leaf {
  private Var var;
  private IntLiteral intLiteral;
  private Boolean boolLiteral;
  private Type defaultValueForType;

  public Leaf(Var var) {
    this.var = var;
  }

  public Leaf(IntLiteral intLiteral) {
    this.intLiteral = intLiteral;
  }

  public Leaf(Boolean boolLiteral) {
    this.boolLiteral = boolLiteral;
  }

  public Leaf(Type deffaultValueForType) {
    this.defaultValueForType = deffaultValueForType;
  }

  public boolean isVar() {
    return var != null;
  }

  public boolean isBool() {
    return boolLiteral != null;
  }

  public boolean isDefault() {
    return defaultValueForType != null;
  }

  public boolean isIntLiteral() {
    return intLiteral != null;
  }

  public Var getVar() {
    return var;
  }

  public IntLiteral getIntLiteral() {
    return intLiteral;
  }

  public Boolean getBoolLiteral() {
    return boolLiteral;
  }

  public Type getDefaultValueForType() {
    return defaultValueForType;
  }

  @Override
  public String toString() {
    if (isVar()) {
      return var.toString();
    }
    if (isBool()) {
      return boolLiteral.toString();
    }
    if (isIntLiteral()) {
      return intLiteral.toString();
    }
    if (isDefault()) {
      return ToStringsInternal.defaultVarNameForType(defaultValueForType);
    }
    return "Leaf ?[ 0";
  }

}
