package ast_stmt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ast_expr.ExprExpression;
import ast_printers.VarPrinters;
import ast_vars.VarDeclarator;

public class StmtReturn implements Serializable, VarRegistrator {
  private static final long serialVersionUID = -3087335776553034864L;

  private ExprExpression expression;

  ///@REFCOUNT
  private final List<VarDeclarator> variables;

  public StmtReturn() {
    this.variables = new ArrayList<>();
  }

  public ExprExpression getExpression() {
    return expression;
  }

  public void setExpression(ExprExpression expression) {
    this.expression = expression;
  }

  @Override
  public List<VarDeclarator> getRelatedVariables() {
    return variables;
  }

  @Override
  public void registerVariable(VarDeclarator sym) {
    variables.add(sym);
  }

  public boolean hasExpression() {
    return expression != null;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("return");
    if (hasExpression()) {
      sb.append(" ");
      sb.append(expression.toString());
    }
    sb.append(";");
    sb.append(VarPrinters.bindedVarsComment(variables));
    return sb.toString();
  }

}