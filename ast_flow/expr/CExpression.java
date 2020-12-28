package njast.ast_flow.expr;

import static njast.ast_flow.expr.CExpressionBase.ETERNARY;

import jscan.cstrtox.C_strtox;
import jscan.symtab.Ident;
import jscan.tokenize.Token;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class CExpression implements AstTraverser {

  private final CExpressionBase base; // what union contains
  private Eunary unary;
  private Ebinary binary;
  private Eternary eternary;
  private NumericConstant cnumber;
  private Ident symbol;
  private MethodInvocation methodInvocation;
  private FieldAccess fieldAccess;
  private GetPointerToClass getPointerToClass;

  public CExpression(GetPointerToClass getPointerToClass) {
    this.base = CExpressionBase.EGET_POINTER_TO_CLASS;
    this.getPointerToClass = getPointerToClass;
  }

  public CExpression(Eunary unary) {
    this.base = CExpressionBase.EUNARY;
    this.unary = unary;
  }

  public CExpression(Ebinary binary) {
    this.base = CExpressionBase.EBINARY;
    this.binary = binary;
  }

  public CExpression(Eternary eternary) {
    this.base = ETERNARY;
    this.eternary = eternary;
  }

  public CExpression(C_strtox e, Token token) {
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

  public CExpression(MethodInvocation methodInvocation) {
    this.base = CExpressionBase.EMETHOD_INVOCATION;
    this.methodInvocation = methodInvocation;
  }

  public CExpression(FieldAccess fieldAccess) {
    this.base = CExpressionBase.EFIELD_ACCESS;
    this.fieldAccess = fieldAccess;
  }

  public CExpression(Ident symbol) {
    this.base = CExpressionBase.EPRIMARY_IDENT;
    this.symbol = symbol;
  }

  public CExpressionBase getBase() {
    return base;
  }

  public Eunary getUnary() {
    return unary;
  }

  public Ebinary getBinary() {
    return binary;
  }

  public Eternary getEternary() {
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

  public GetPointerToClass getGetPointerToClass() {
    return getPointerToClass;
  }

  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

}
