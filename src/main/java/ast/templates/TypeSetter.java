package ast.templates;

import ast.types.Type;

public interface TypeSetter {
  void setType(Type typeToSet);

  Type getType();
}
