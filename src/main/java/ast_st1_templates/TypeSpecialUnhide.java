package ast_st1_templates;

import java.io.Serializable;

import ast_class.ClassDeclaration;
import ast_types.Type;

public class TypeSpecialUnhide implements Serializable, TypeSetter {
  private static final long serialVersionUID = 2755214492462995292L;

  private final ClassDeclaration clazz;

  public TypeSpecialUnhide(ClassDeclaration clazz) {
    this.clazz = clazz;
  }

  public void unhide() {
    clazz.unhide();
  }

  @Override
  public void setType(Type typeToSet) {
  }

  @Override
  public Type getType() {
    return new Type();
  }
}
