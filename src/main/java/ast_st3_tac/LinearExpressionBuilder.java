package ast_st3_tac;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_method.ClassMethodDeclaration;
import ast_vars.VarDeclarator;

public abstract class LinearExpressionBuilder {

  public static LinearExpression build(ExprExpression from, ClassDeclaration clazz, ClassMethodDeclaration method) {
    if (from == null) {
      return new LinearExpression();
    }
    TacGenerator tcg = new TacGenerator(from, method);
    return new LinearExpression(tcg.getRv(), tcg.getVarCreator().getAllVars());
  }

  public static LinearExpression build(VarDeclarator from, ClassDeclaration clazz, ClassMethodDeclaration method) {
    if (from == null) {
      return new LinearExpression();
    }
    TacGenerator tcg = new TacGenerator(from, method);
    return new LinearExpression(tcg.getRv(), tcg.getVarCreator().getAllVars());
  }

}
