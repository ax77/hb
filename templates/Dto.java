package njast.templates;

import java.util.List;

import njast.parse.AstParseException;
import njast.parse.NullChecker;
import njast.types.Type;

public class Dto {
  private final Type templateGiven;
  private final List<Type> typeArguments;
  private final Type result;

  public Dto(Type templateGiven, List<Type> typeArguments, Type result) {
    NullChecker.check(templateGiven);
    NullChecker.check(typeArguments);
    NullChecker.check(result);

    if (!templateGiven.is_class_template()) {
      throw new AstParseException("expect class-template");
    }

    this.templateGiven = templateGiven;
    this.typeArguments = typeArguments;
    this.result = result;
  }

  public List<Type> getTypeArguments() {
    return typeArguments;
  }

  public Type getResult() {
    return result;
  }

  public Type getTemplateGiven() {
    return templateGiven;
  }

}
