package njast.ast_nodes.clazz;

import java.io.Serializable;

import njast.ast_nodes.clazz.methods.FormalParameterList;
import njast.ast_nodes.stmt.StmtBlock;

public class ClassConstructorDeclaration implements Serializable {

  private static final long serialVersionUID = 921504026387262618L;

  private final FormalParameterList formalParameterList;
  private final StmtBlock block;

  public ClassConstructorDeclaration(FormalParameterList formalParameterList, StmtBlock block) {
    this.formalParameterList = formalParameterList;
    this.block = block;
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

    sb.append("init");
    sb.append(formalParameterList.toString());
    sb.append("\n{\n");
    sb.append(block.toString());
    sb.append("\n}\n");

    return sb.toString();
  }

}
