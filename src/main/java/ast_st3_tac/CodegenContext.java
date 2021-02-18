package ast_st3_tac;

import tokenize.Ident;
import utils_oth.NullChecker;

public class CodegenContext {
  private Ident currentMethodName;

  public Ident getCurrentMethodName() {
    NullChecker.check(currentMethodName);
    return currentMethodName;
  }

  public void setCurrentMethodName(Ident currentMethodName) {
    this.currentMethodName = currentMethodName;
  }

}
