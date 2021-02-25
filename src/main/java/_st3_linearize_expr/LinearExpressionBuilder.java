package _st3_linearize_expr;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_method.ClassMethodDeclaration;
import ast_vars.VarDeclarator;

public abstract class LinearExpressionBuilder {

  public static LinearExpression build(ExprExpression from, ClassDeclaration clazz, ClassMethodDeclaration method) {
    if (from == null) {
      return new LinearExpression();
    }
    RewriterExpr tcg = new RewriterExpr(from, method);
    return new LinearExpression(tcg.getRv(), tcg.getVarCreator().getAllVars());
  }

  public static LinearExpression build(VarDeclarator from, ClassDeclaration clazz, ClassMethodDeclaration method) {
    if (from == null) {
      return new LinearExpression();
    }
    RewriterExpr tcg = new RewriterExpr(from, method);
    return new LinearExpression(tcg.getRv(), tcg.getVarCreator().getAllVars());
  }

}
