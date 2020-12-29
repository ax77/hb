package njast.ast_nodes.expr;

import static njast.ast_kinds.ExpressionBase.ETERNARY;

import jscan.cstrtox.C_strtox;
import jscan.symtab.Ident;
import jscan.tokenize.Token;
import njast.ast_kinds.ExpressionBase;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class ExpressionNode implements AstTraverser {

  private final ExpressionBase base; // what union contains
  private Unary unary;
  private Binary binary;
  private Ternary eternary;
  private NumericConstant cnumber;
  private Ident symbol;
  private MethodInvocation methodInvocation;
  private FieldAccess fieldAccess;

  public ExpressionNode(Unary unary) {
    this.base = ExpressionBase.EUNARY;
    this.unary = unary;
  }

  public ExpressionNode(Binary binary) {
    this.base = ExpressionBase.EBINARY;
    this.binary = binary;
  }

  public ExpressionNode(Ternary eternary) {
    this.base = ETERNARY;
    this.eternary = eternary;
  }

  public ExpressionNode(C_strtox e, Token token) {
    e.ev(); // XXX:

    this.base = ExpressionBase.EPRIMARY_NUMBER;

    NumericConstant number = null;
    if (e.isIntegerKind()) {
      number = new NumericConstant(e.getClong(), e.getNumtype());
    } else {
      number = new NumericConstant(e.getCdouble(), e.getNumtype());
    }

    this.cnumber = number;
  }

  public ExpressionNode(MethodInvocation methodInvocation) {
    this.base = ExpressionBase.EMETHOD_INVOCATION;
    this.methodInvocation = methodInvocation;
  }

  public ExpressionNode(FieldAccess fieldAccess) {
    this.base = ExpressionBase.EFIELD_ACCESS;
    this.fieldAccess = fieldAccess;
  }

  public ExpressionNode(Ident symbol) {
    this.base = ExpressionBase.EPRIMARY_IDENT;
    this.symbol = symbol;
  }

  public ExpressionBase getBase() {
    return base;
  }

  public Unary getUnary() {
    return unary;
  }

  public Binary getBinary() {
    return binary;
  }

  public Ternary getEternary() {
    return eternary;
  }

  public NumericConstant getCnumber() {
    return cnumber;
  }

  public Ident getSymbol() {
    return symbol;
  }

  public MethodInvocation getMethodInvocation() {
    return methodInvocation;
  }

  public FieldAccess getFieldAccess() {
    return fieldAccess;
  }

  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

}
