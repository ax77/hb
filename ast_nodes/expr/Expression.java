package njast.ast_nodes.expr;

import static njast.ast_kinds.CExpressionBase.ETERNARY;

import jscan.cstrtox.C_strtox;
import jscan.symtab.Ident;
import jscan.tokenize.Token;
import njast.ast_kinds.CExpressionBase;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class Expression implements AstTraverser {

  private final CExpressionBase base; // what union contains
  private Unary unary;
  private Binary binary;
  private Ternary eternary;
  private NumericConstant cnumber;
  private Ident symbol;
  private MethodInvocation methodInvocation;
  private FieldAccess fieldAccess;

  public Expression(Unary unary) {
    this.base = CExpressionBase.EUNARY;
    this.unary = unary;
  }

  public Expression(Binary binary) {
    this.base = CExpressionBase.EBINARY;
    this.binary = binary;
  }

  public Expression(Ternary eternary) {
    this.base = ETERNARY;
    this.eternary = eternary;
  }

  public Expression(C_strtox e, Token token) {
    e.ev(); // XXX:

    this.base = CExpressionBase.EPRIMARY_NUMBER;

    NumericConstant number = null;
    if (e.isIntegerKind()) {
      number = new NumericConstant(e.getClong(), e.getNumtype());
    } else {
      number = new NumericConstant(e.getCdouble(), e.getNumtype());
    }

    this.cnumber = number;
  }

  public Expression(MethodInvocation methodInvocation) {
    this.base = CExpressionBase.EMETHOD_INVOCATION;
    this.methodInvocation = methodInvocation;
  }

  public Expression(FieldAccess fieldAccess) {
    this.base = CExpressionBase.EFIELD_ACCESS;
    this.fieldAccess = fieldAccess;
  }

  public Expression(Ident symbol) {
    this.base = CExpressionBase.EPRIMARY_IDENT;
    this.symbol = symbol;
  }

  public CExpressionBase getBase() {
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
