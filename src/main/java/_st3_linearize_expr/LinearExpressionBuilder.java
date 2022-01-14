package _st3_linearize_expr;

import java.util.List;

import _st3_linearize_expr.ir.FlatCodeItem;
import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_method.ClassMethodDeclaration;
import ast_vars.VarDeclarator;

public abstract class LinearExpressionBuilder {

  public static LinearExpression build(ExprExpression from, ClassDeclaration clazz, ClassMethodDeclaration method) {
    if (from == null) {
      return new LinearExpression();
    }
    final RewriterExpr tcg = new RewriterExpr(from, method);
    final List<FlatCodeItem> result = tcg.getRv();
    final List<FlatCodeItem> tryPeep = Peep.tryPeep(result);
    return new LinearExpression(tryPeep);
  }

  public static LinearExpression build(VarDeclarator from, ClassDeclaration clazz, ClassMethodDeclaration method) {
    if (from == null) {
      return new LinearExpression();
    }
    final RewriterExpr tcg = new RewriterExpr(from, method);
    final List<FlatCodeItem> result = tcg.getRv();
    final List<FlatCodeItem> tryPeep = Peep.tryPeep(result);
    return new LinearExpression(tryPeep);
  }

}
