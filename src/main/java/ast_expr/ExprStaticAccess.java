package ast_expr;

import java.util.List;

import _st1_templates.TypeSetter;
import ast_class.ClassDeclaration;
import ast_printers.ArgsListToString;
import ast_types.Type;
import errors.AstParseException;

public class ExprStaticAccess implements TypeSetter {

  private Type type;

  public ExprStaticAccess(Type type) {
    if (!type.isClassTemplate()) {
      throw new AstParseException("expect template class, but was: " + type.toString());
    }
    this.type = type;
  }

  @Override
  public String toString() {
    ClassDeclaration classname = type.getClassTypeFromRef();
    List<Type> typeargs = type.getTypeArgumentsFromRef();
    return classname.getIdentifier() + ArgsListToString.paramsToStringWithBraces(typeargs, '<');
  }

  @Override
  public void setType(Type typeToSet) {
    this.type = typeToSet;
  }

  @Override
  public Type getType() {
    return type;
  }
}
