package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.Var;
import tokenize.Ident;
import utils_oth.NullChecker;

public class CallListenerResultMethod {

  private final Var dest;
  private final Ident listenerName; // before_call, after_call
  private final String callpath; // main_class.main()::another_class.f()
  private final int line;

  public CallListenerResultMethod(Var dest, Ident listenerName, String callpath, int line) {
    NullChecker.check(dest);

    this.dest = dest;
    this.listenerName = listenerName;
    this.callpath = callpath;
    this.line = line;
  }

  public String getCallpath() {
    return callpath;
  }

  public int getLine() {
    return line;
  }

  public Var getDest() {
    return dest;
  }

  public Ident getListenerName() {
    return listenerName;
  }

  @Override
  public String toString() {
    return listenerName.getName() + "(" + "\"" + callpath + "\"" + ", " + String.format("%d", line) + ")";
  }

}
