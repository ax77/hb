package njast.ast_class.methods;

public class MethodHeader {

  //  <method header> ::= <method modifiers>? <result type> <method declarator> <throws>?
  //  
  //  <result type> ::= <type> | void
  //  
  //  <method modifiers> ::= <method modifier> | <method modifiers> <method modifier>
  //  
  //  <method modifier> ::= public | protected | private | static | abstract | final | synchronized | native
  //  
  //  <method declarator> ::= <identifier> ( <formal parameter list>? )
  //
  //  <formal parameter list> ::= <formal parameter> | <formal parameter list> , <formal parameter>
  //
  //  <formal parameter> ::= <type> <variable declarator id>

  private ResultType resultType;
  private MethodDeclarator methodDeclarator;

  public MethodHeader() {
  }

  public MethodHeader(ResultType resultType, MethodDeclarator methodDeclarator) {
    this.resultType = resultType;
    this.methodDeclarator = methodDeclarator;
  }

  public ResultType getResultType() {
    return resultType;
  }

  public void setResultType(ResultType resultType) {
    this.resultType = resultType;
  }

  public MethodDeclarator getMethodDeclarator() {
    return methodDeclarator;
  }

  public void setMethodDeclarator(MethodDeclarator methodDeclarator) {
    this.methodDeclarator = methodDeclarator;
  }

}
