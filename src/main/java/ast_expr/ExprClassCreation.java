package ast_expr;

import java.io.Serializable;
import java.util.List;

import ast_st1_templates.TypeSetter;
import ast_types.Type;
import ast_vars.VarDeclarator;
import utils_oth.NullChecker;

public class ExprClassCreation implements Serializable, TypeSetter {
  private static final long serialVersionUID = -8666532744723689317L;

  // <class instance creation expression> ::= new <class type> < type-arguments > ( <argument list>? )

  private Type classtype;
  private final List<ExprExpression> arguments;

  //MIR:TREE
  private VarDeclarator var;

  public ExprClassCreation(Type classtype, List<ExprExpression> arguments) {
    NullChecker.check(classtype, arguments);

    this.classtype = classtype;
    this.arguments = arguments;
  }

  public List<ExprExpression> getArguments() {
    return arguments;
  }

  @Override
  public String toString() {
    return "new " + classtype.toString() + ExprUtil.funcArgsToString(arguments);
  }

  @Override
  public void setType(Type typeToSet) {
    this.classtype = typeToSet;
  }

  public VarDeclarator getVar() {
    return var;
  }

  public void setVar(VarDeclarator var) {
    this.var = var;
  }

  @Override
  public Type getType() {
    return classtype;
  }

}
