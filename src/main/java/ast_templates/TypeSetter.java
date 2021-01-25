package ast_templates;

import ast_types.Type;

public interface TypeSetter {
  void setType(Type typeToSet);

  Type getType();
}
