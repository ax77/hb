package ast_expr;

import java.io.Serializable;

import ast_sourceloc.ILocation;
import ast_sourceloc.SourceLocation;
import ast_types.Type;
import literals.IntLiteral;
import tokenize.Token;

public class ExprExpression implements Serializable, ILocation {
  private static final long serialVersionUID = -2905880039842730533L;

  // main
  private final ExpressionBase base; // what union contains
  private final SourceLocation location;
  private Type resultType;

  // nodes
  private ExprUnary unary;
  private ExprBinary binary;
  private IntLiteral number;
  private ExprIdent ident;
  private ExprMethodInvocation methodInvocation;
  private ExprFieldAccess fieldAccess;
  private ExprClassCreation classCreation;
  private ExprAssign assign;
  private ExprArrayCreation arrayCreation;
  private ExprSelf selfExpression;
  private String stringConst;
  private ExprArrayAccess arrayAccess;

  public ExprExpression(ExprArrayAccess arrayAccess, Token beginPos) {
    this.base = ExpressionBase.EARRAY_ACCESS;
    this.location = new SourceLocation(beginPos);
    this.arrayAccess = arrayAccess;
  }

  public ExprExpression(ExprSelf selfExpression, Token beginPos) {
    this.base = ExpressionBase.ESELF;
    this.location = new SourceLocation(beginPos);
    this.selfExpression = selfExpression;
  }

  public ExprExpression(String stringConst, Token beginPos) {
    this.base = ExpressionBase.ESTRING_CONST;
    this.location = new SourceLocation(beginPos);
    this.stringConst = stringConst;
  }

  public ExprExpression(ExprAssign assign, Token beginPos) {
    this.base = ExpressionBase.EASSIGN;
    this.location = new SourceLocation(beginPos);
    this.assign = assign;
  }

  public ExprExpression(ExprArrayCreation arrayCreation, Token beginPos) {
    this.base = ExpressionBase.EARRAY_INSTANCE_CREATION;
    this.location = new SourceLocation(beginPos);
    this.arrayCreation = arrayCreation;
  }

  public ExprExpression(ExpressionBase base, Token beginPos) {
    this.base = base;
    this.location = new SourceLocation(beginPos);
  }

  public ExprExpression(ExprClassCreation classCreation, Token beginPos) {
    this.base = ExpressionBase.ECLASS_INSTANCE_CREATION;
    this.location = new SourceLocation(beginPos);
    this.classCreation = classCreation;
  }

  public ExprExpression(ExprUnary unary, Token beginPos) {
    this.base = ExpressionBase.EUNARY;
    this.location = new SourceLocation(beginPos);
    this.unary = unary;
  }

  public ExprExpression(ExprBinary binary, Token beginPos) {
    this.base = ExpressionBase.EBINARY;
    this.location = new SourceLocation(beginPos);
    this.binary = binary;
  }

  public ExprExpression(IntLiteral e, Token token) {
    this.base = ExpressionBase.EPRIMARY_NUMBER;
    this.location = new SourceLocation(token);
    this.number = e;
  }

  public ExprExpression(ExprMethodInvocation methodInvocation, Token beginPos) {
    this.base = ExpressionBase.EMETHOD_INVOCATION;
    this.location = new SourceLocation(beginPos);
    this.methodInvocation = methodInvocation;
  }

  public ExprExpression(ExprFieldAccess fieldAccess, Token beginPos) {
    this.base = ExpressionBase.EFIELD_ACCESS;
    this.location = new SourceLocation(beginPos);
    this.fieldAccess = fieldAccess;
  }

  public ExprExpression(ExprIdent symbol, Token beginPos) {
    this.base = ExpressionBase.EPRIMARY_IDENT;
    this.location = new SourceLocation(beginPos);
    this.ident = symbol;
  }

  public ExprArrayAccess getArrayAccess() {
    return arrayAccess;
  }

  public String getStringConst() {
    return stringConst;
  }

  public ExprArrayCreation getArrayInstanceCreation() {
    return arrayCreation;
  }

  public ExprAssign getAssign() {
    return assign;
  }

  public Type getResultType() {
    return resultType;
  }

  public ExprSelf getSelfExpression() {
    return selfExpression;
  }

  public void setResultType(Type resultType) {
    this.resultType = resultType;
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

  public ExprIdent getIdent() {
    return ident;
  }

  public IntLiteral getNumber() {
    return number;
  }

  public ExprMethodInvocation getMethodInvocation() {
    return methodInvocation;
  }

  public ExprFieldAccess getFieldAccess() {
    return fieldAccess;
  }

  public ExprClassCreation getClassCreation() {
    return classCreation;
  }

  public ExprArrayCreation getArrayCreation() {
    return arrayCreation;
  }

  public boolean is(ExpressionBase what) {
    return base.equals(what);
  }

  @Override
  public String toString() {
    if (base == ExpressionBase.EBINARY) {
      return binary.toString();
    }
    if (base == ExpressionBase.EASSIGN) {
      return assign.toString();
    }
    if (base == ExpressionBase.EFIELD_ACCESS) {
      return fieldAccess.toString();
    }
    if (base == ExpressionBase.EPRIMARY_IDENT) {
      return ident.toString();
    }
    if (base == ExpressionBase.ESELF) {
      return selfExpression.toString();
    }
    if (base == ExpressionBase.EMETHOD_INVOCATION) {
      return methodInvocation.toString();
    }
    if (base == ExpressionBase.EPRIMARY_NUMBER) {
      return number.toString();
    }
    if (base == ExpressionBase.EPRIMARY_NULL_LITERAL) {
      return "null";
    }
    if (base == ExpressionBase.ECLASS_INSTANCE_CREATION) {
      return classCreation.toString();
    }
    if (base == ExpressionBase.EARRAY_INSTANCE_CREATION) {
      return arrayCreation.toString();
    }
    if (base == ExpressionBase.ESTRING_CONST) {
      return stringConst;
    }
    if (base == ExpressionBase.EARRAY_ACCESS) {
      return arrayAccess.toString();
    }
    if (base == ExpressionBase.EUNARY) {
      return unary.toString();
    }
    return base.toString();
  }

  @Override
  public SourceLocation getLocation() {
    return location;
  }

  @Override
  public String getLocationToString() {
    return location.toString();
  }

}
