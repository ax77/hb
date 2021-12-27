package errors;

import ast_expr.ExprExpression;
import ast_types.Type;
import ast_vars.VarDeclarator;

public abstract class ErrorLocation {

  public static void errorExpression(String message, final ExprExpression expression) {
    String tail = "";
    if (expression != null) {
      tail = expression.toString();
    }
    throw new AstParseException("\n| " + expression.getLocationToString() + "\n| error[" + message + "]\n| " + tail);
  }

  public static void errorInitializer(String message, VarDeclarator var, final ExprExpression init) {
    final String varStr = var.toString();
    throw new AstParseException("\n| " + init.getLocationToString() + "\n| error[" + message + "]\n| " + varStr);
  }

  public static void errorVarRedefinition(VarDeclarator varYouWantToDefine, VarDeclarator varDefinedPreviously) {

    final String name = varYouWantToDefine.getIdentifier().getName();
    final String location = varYouWantToDefine.getLocationToString();

    StringBuilder sb = new StringBuilder();
    sb.append("\nError: ");
    sb.append(location);
    sb.append("\n");
    sb.append("duplicate variable: ");
    sb.append("[" + name + "]");
    sb.append(" : " + varYouWantToDefine.getBase().toString());
    sb.append("\n");
    sb.append("previously defined here: ");
    sb.append(varDefinedPreviously.getLocationToString());
    sb.append(" : " + varDefinedPreviously.getBase().toString());
    sb.append("\n");

    throw new AstParseException(sb.toString());
  }

  public static void errorType(String message, final Type type) {
    String tail = "";
    if (type != null) {
      tail = type.toString();
    }
    throw new AstParseException("\n| " + type.toString() + "\n| error[" + message + "]\n| " + tail);
  }

}
