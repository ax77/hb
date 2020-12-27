package njast.ast_flow.expr;

import java.util.List;

public class Ecall {
  private final CExpression method;
  private final List<CExpression> arguments;

  public Ecall(CExpression method, List<CExpression> arguments) {
    this.method = method;
    this.arguments = arguments;
  }

  public CExpression getMethod() {
    return method;
  }

  public List<CExpression> getArguments() {
    return arguments;
  }

}
