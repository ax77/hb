package _st3_linearize_expr.items;

import tokenize.Ident;

public class CallListenerVoidMethod {

  private final Ident listenerName; // before_call, after_call
  private final String callpath; // main_class.main()::another_class.f()
  private final int line;

  public CallListenerVoidMethod(Ident listenerName, String callpath, int line) {
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

  @Override
  public String toString() {
    return listenerName.getName() + "(/*path=*/" + callpath + ", /*line=*/" + String.format("%d", line) + ")";
  }

}
