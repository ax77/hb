package ast_st3_tac.vars.store;

import ast_types.Type;

public class AllocObject {
  // type x2 = new type();
  // 
  // size, align, class-name -> from the class-type-ref
  private final Type typename;

  public AllocObject(Type typename) {
    this.typename = typename;
  }

  public Type getTypename() {
    return typename;
  }

  @Override
  public String toString() {
    return "new " + typename.getClassTypeFromRef().getIdentifier().toString() + "()";
  }

}
