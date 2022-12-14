package ast_expr;

import java.io.Serializable;
import java.util.List;

import _st1_templates.TypeSetter;
import ast_method.ClassMethodDeclaration;
import ast_printers.ArgsListToString;
import ast_types.Type;
import errors.ErrorLocation;
import utils_oth.NullChecker;

public class ExprClassCreation implements Serializable, TypeSetter {
  private static final long serialVersionUID = -8666532744723689317L;

  // <class instance creation expression> ::= new <class type> < type-arguments > ( <argument list>? )

  private Type classtype;
  private final List<ExprExpression> arguments;

  //MIR:TREE
  private ClassMethodDeclaration constructor;

  public ExprClassCreation(Type classtype, List<ExprExpression> arguments) {
    NullChecker.check(classtype, arguments);

    if (!classtype.isClass()) {
      ErrorLocation.errorType("expect class type, but was: ", classtype);

    }

    this.classtype = classtype;
    this.arguments = arguments;
  }

  public List<ExprExpression> getArguments() {
    return arguments;
  }

  @Override
  public String toString() {
    return "new " + classtype.toString() + ArgsListToString.paramsToStringWithBraces(arguments, '(');
  }

  @Override
  public void setType(Type typeToSet) {
    this.classtype = typeToSet;
  }

  @Override
  public Type getType() {
    return classtype;
  }

  public ClassMethodDeclaration getConstructor() {
    return constructor;
  }

  public void setConstructor(ClassMethodDeclaration constructor) {
    this.constructor = constructor;
  }

}
