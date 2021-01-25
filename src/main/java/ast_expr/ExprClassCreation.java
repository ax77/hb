package ast_expr;

import java.io.Serializable;
import java.util.List;

import ast_templates.TypeSetter;
import ast_types.Type;
import utils_oth.NullChecker;

public class ExprClassCreation implements Serializable, TypeSetter {
  private static final long serialVersionUID = -8666532744723689317L;

  // <class instance creation expression> ::= new <class type> < type-arguments > ( <argument list>? )

  private Type classtype;
  private final List<FuncArg> arguments;

  public ExprClassCreation(Type classtype, List<FuncArg> arguments) {
    NullChecker.check(classtype, arguments);

    this.classtype = classtype;
    this.arguments = arguments;
  }

  public List<FuncArg> getArguments() {
    return arguments;
  }

  @Override
  public String toString() {
    return "new " + classtype.toString() + "(" + ExprUtil.exprListCommaToString1(arguments) + ")";
  }

  @Override
  public void setType(Type typeToSet) {
    this.classtype = typeToSet;
  }

  @Override
  public Type getType() {
    return classtype;
  }

}
