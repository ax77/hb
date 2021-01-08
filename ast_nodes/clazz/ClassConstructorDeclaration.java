package njast.ast_nodes.clazz;

import java.io.Serializable;

import jscan.symtab.Ident;
import njast.ast_nodes.clazz.methods.FormalParameterList;
import njast.ast_nodes.stmt.StmtBlock;

public class ClassConstructorDeclaration implements Serializable {

  private static final long serialVersionUID = 921504026387262618L;

  private final Ident identifier;
  private final FormalParameterList formalParameterList;
  private final StmtBlock block;

  public ClassConstructorDeclaration(Ident identifier, FormalParameterList formalParameterList, StmtBlock block) {
    this.identifier = identifier;
    this.formalParameterList = formalParameterList;
    this.block = block;
  }

  public Ident getIdentifier() {
    return identifier;
  }

  public FormalParameterList getFormalParameterList() {
    return formalParameterList;
  }

  public StmtBlock getBlock() {
    return block;
  }

  public boolean isCompatibleRedefinition(ClassConstructorDeclaration another) {
    if (formalParameterList.isEqualTo(another.getFormalParameterList())) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append(identifier.getName());
    sb.append(formalParameterList.toString());
    sb.append("\n{\n");
    sb.append(block.toString());
    sb.append("\n}\n");

    return sb.toString();
  }

}
