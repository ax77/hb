package errors;

import ast_expr.ExprExpression;
import ast_vars.VarDeclarator;

public abstract class ErrorLocation {

  public static void errorExpression(String message, ExprExpression expression) {
    String tail = "";
    if (expression != null) {
      tail = expression.toString();
    }
    throw new AstParseException("\n| " + expression.getLocationToString() + "\n| error[" + message + "]\n| " + tail);
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

}
