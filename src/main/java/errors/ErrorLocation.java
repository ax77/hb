package errors;

import ast_expr.ExprExpression;

public abstract class ErrorLocation {
  
  public static void errorExpression(String message, ExprExpression expression) {
    String tail = "";
    if (expression != null) {
      tail = expression.toString();
    }
    throw new AstParseException(expression.getLocationToString() + "error[" + message + "] " + tail);
  }
  
}
