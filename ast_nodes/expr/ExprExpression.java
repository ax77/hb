package njast.ast_nodes.expr;

import static njast.ast_kinds.ExpressionBase.ETERNARY;

import jscan.cstrtox.C_strtox;
import jscan.symtab.Ident;
import jscan.tokenize.Token;
import njast.ast_kinds.ExpressionBase;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class ExprExpression implements AstTraverser {

  private final ExpressionBase base; // what union contains
  private ExprUnary unary;
  private ExprBinary binary;
  private ExprTernary eternary;
  private ExprNumericConstant literalNumber;
  private Ident literalIdentifier;
  private ExprMethodInvocation methodInvocation;
  private ExprFieldAccess fieldAccess;

  public ExprExpression(ExprUnary unary) {
    this.base = ExpressionBase.EUNARY;
    this.unary = unary;
  }

  public ExprExpression(ExprBinary binary) {
    this.base = ExpressionBase.EBINARY;
    this.binary = binary;
  }

  public ExprExpression(ExprTernary eternary) {
    this.base = ETERNARY;
    this.eternary = eternary;
  }

  public ExprExpression(C_strtox e, Token token) {
    e.ev(); // XXX:

    this.base = ExpressionBase.EPRIMARY_NUMBER;

    ExprNumericConstant number = null;
    if (e.isIntegerKind()) {
      number = new ExprNumericConstant(e.getClong(), e.getNumtype());
    } else {
      number = new ExprNumericConstant(e.getCdouble(), e.getNumtype());
    }

    this.literalNumber = number;
  }

  public ExprExpression(ExprMethodInvocation methodInvocation) {
    this.base = ExpressionBase.EMETHOD_INVOCATION;
    this.methodInvocation = methodInvocation;
  }

  public ExprExpression(ExprFieldAccess fieldAccess) {
    this.base = ExpressionBase.EFIELD_ACCESS;
    this.fieldAccess = fieldAccess;
  }

  public ExprExpression(Ident symbol) {
    this.base = ExpressionBase.EPRIMARY_IDENT;
    this.literalIdentifier = symbol;
  }

  public ExpressionBase getBase() {
    return base;
  }

  public ExprUnary getUnary() {
    return unary;
  }

  public ExprBinary getBinary() {
    return binary;
  }

  public ExprTernary getEternary() {
    return eternary;
  }

  public ExprNumericConstant getCnumber() {
    return literalNumber;
  }

  public Ident getSymbol() {
    return literalIdentifier;
  }

  public ExprMethodInvocation getMethodInvocation() {
    return methodInvocation;
  }

  public ExprFieldAccess getFieldAccess() {
    return fieldAccess;
  }

  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

}
