package njast.ast_class.methods;

public class MethodDeclaration {

  private MethodHeader methodHeader;
  private MethodBody methodBody;

  public MethodDeclaration() {
  }

  public MethodDeclaration(MethodHeader methodHeader, MethodBody methodBody) {
    this.methodHeader = methodHeader;
    this.methodBody = methodBody;
  }

  public MethodHeader getMethodHeader() {
    return methodHeader;
  }

  public void setMethodHeader(MethodHeader methodHeader) {
    this.methodHeader = methodHeader;
  }

  public MethodBody getMethodBody() {
    return methodBody;
  }

  public void setMethodBody(MethodBody methodBody) {
    this.methodBody = methodBody;
  }

}
