package ast_class;

import java.io.Serializable;

import ast_st1_templates.TypeSetter;
import ast_types.Type;

/// class may implements the list of interfaces
/// each interface may also contains type-parameters
/// we should registed the interface as a type-setter
/// into the class which is implements it.
///
public class InterfaceItem implements Serializable, TypeSetter {
  private static final long serialVersionUID = 8716480829036761301L;

  private Type interface_;

  public InterfaceItem(Type interface_) {
    InterfaceChecker.checkItIsAnInterfaceClass(interface_);
    this.interface_ = interface_;
  }

  @Override
  public void setType(Type typeToSet) {
    InterfaceChecker.checkItIsAnInterfaceClass(typeToSet);
    this.interface_ = typeToSet;
  }

  @Override
  public Type getType() {
    return interface_;
  }

  @Override
  public String toString() {
    return interface_.toString();
  }

}
