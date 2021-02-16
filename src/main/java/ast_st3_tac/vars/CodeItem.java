package ast_st3_tac.vars;

import ast_st3_tac.vars.store.Call;

public class CodeItem {

  /// a.b = x1;
  /// etc...
  private StoreLeaf store;

  /// type_init_5(x2, _t_5);
  private Call voidCall;

  /// strtemp _t_5 = x1;
  private TempVarAssign varAssign;

  public CodeItem(StoreLeaf assign) {
    this.store = assign;
  }

  public CodeItem(Call voidCall) {
    this.voidCall = voidCall;
  }

  public CodeItem(TempVarAssign varAssign) {
    this.varAssign = varAssign;
  }

  public boolean isStore() {
    return store != null;
  }

  public boolean isVoidCall() {
    return voidCall != null;
  }

  public boolean isVarAssign() {
    return varAssign != null;
  }

  public StoreLeaf getStore() {
    return store;
  }

  public Call getVoidCall() {
    return voidCall;
  }

  public TempVarAssign getVarAssign() {
    return varAssign;
  }

  @Override
  public String toString() {
    if (isStore()) {
      return store.toString();
    }
    if (isVoidCall()) {
      return voidCall.toString();
    }
    if (isVarAssign()) {
      return varAssign.toString();
    }
    return "???-null-code-item>";
  }

}
