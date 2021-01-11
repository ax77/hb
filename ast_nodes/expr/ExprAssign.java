package njast.ast_nodes.expr;

import java.io.Serializable;

import jscan.tokenize.Token;

public class ExprAssign implements Serializable {
  private static final long serialVersionUID = 3976062078026787165L;
  private final Token operator;
  private final ExprExpression lvalue;
  private final ExprExpression rvalue;

  public ExprAssign(Token operator, ExprExpression lvalue, ExprExpression rvalue) {
    this.operator = operator;
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Token getOperator() {
    return operator;
  }

  public ExprExpression getLvalue() {
    return lvalue;
  }

  public ExprExpression getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(lvalue.toString());
    sb.append(" ");
    sb.append(operator.getValue());
    sb.append(" ");
    sb.append(rvalue.toString());
    return sb.toString();
  }

}
