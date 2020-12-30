package njast.ast_nodes.clazz.methods;

import jscan.symtab.Ident;
import njast.ast_nodes.stmt.StmtBlock;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;
import njast.types.Type;

public class ClassMethodDeclaration implements AstTraverser {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

  // header
  private Type resultType;
  private final Ident identifier;
  private FormalParameterList formalParameterList;
  private boolean isVoid;

  // body
  private StmtBlock body;

  public ClassMethodDeclaration(Type resultType, Ident identifier, FormalParameterList formalParameterList,
      StmtBlock body) {

    this.resultType = resultType;
    this.identifier = identifier;
    this.formalParameterList = formalParameterList;
    this.isVoid = (resultType == null);
    this.body = body;

  }

  public FormalParameterList getFormalParameterList() {
    return formalParameterList;
  }

  public void setFormalParameterList(FormalParameterList formalParameterList) {
    this.formalParameterList = formalParameterList;
  }

  public Type getResultType() {
    return resultType;
  }

  public void setResultType(Type resultType) {
    this.resultType = resultType;
  }

  public boolean isVoid() {
    return isVoid;
  }

  public void setVoid(boolean isVoid) {
    this.isVoid = isVoid;
  }

  public StmtBlock getBody() {
    return body;
  }

  public void setBody(StmtBlock body) {
    this.body = body;
  }

  public Ident getIdentifier() {
    return identifier;
  }

  public boolean isCorrectToOverloadWith(ClassMethodDeclaration another) {
    if (formalParameterList.isEqualTo(another.getFormalParameterList())) {
      return false;
    }
    return true;
  }

}
