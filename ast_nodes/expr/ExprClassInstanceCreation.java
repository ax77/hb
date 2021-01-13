package njast.ast_nodes.expr;

import java.io.Serializable;
import java.util.List;

import njast.ast_nodes.FuncArg;
import njast.ast_utils.ExprUtil;
import njast.parse.NullChecker;
import njast.templates.TypeSetter;
import njast.types.Type;

public class ExprClassInstanceCreation implements Serializable, TypeSetter {
  private static final long serialVersionUID = -8666532744723689317L;

  // <class instance creation expression> ::= new <class type> < type-arguments > ( <argument list>? )

  private Type classtype;
  private final List<FuncArg> arguments;

  public ExprClassInstanceCreation(Type classtype, List<FuncArg> arguments) {
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
