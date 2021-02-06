package ast_expr;

import java.io.Serializable;

import ast_sourceloc.Location;
import ast_sourceloc.SourceLocation;
import ast_types.Type;
import literals.IntLiteral;
import tokenize.Token;
import utils_oth.NullChecker;

public class ExprExpression implements Serializable, Location {
  private static final long serialVersionUID = -2905880039842730533L;

  // main
  private /*final*/ ExpressionBase base; // what union contains
  private final Token beginPos;
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
  private ExprThis selfExpression;
  private boolean booleanLiteral;
  private ExprCast castExpression;
  private ExprBuiltinFn builtinFn;

  //MIR:TREE:rewriter
  public void replaceIdentWithFieldAccess(ExprFieldAccess fieldAccess) {
    this.base = ExpressionBase.EFIELD_ACCESS;
    this.fieldAccess = fieldAccess;
    this.ident = null;
  }

  public ExprExpression(ExprBuiltinFn builtinFn, Token beginPos) {
    this.base = ExpressionBase.EBUILTIN_FN;
    this.beginPos = beginPos;
    this.builtinFn = builtinFn;
  }

  public ExprExpression(ExprCast castExpression, Token beginPos) {
    this.base = ExpressionBase.ECAST;
    this.castExpression = castExpression;
    this.beginPos = beginPos;
  }

  public ExprExpression(boolean value, Token beginPos) {
    this.base = ExpressionBase.EBOOLEAN_LITERAL;
    this.beginPos = beginPos;
    this.booleanLiteral = value;
  }

  public ExprExpression(ExprThis selfExpression, Token beginPos) {
    this.base = ExpressionBase.ETHIS;

    this.beginPos = beginPos;
    this.selfExpression = selfExpression;
  }

  public ExprExpression(ExprAssign assign, Token beginPos) {
    this.base = ExpressionBase.EASSIGN;

    this.beginPos = beginPos;
    this.assign = assign;
  }

  // string-literal, char-literal, null-literal
  //
  public ExprExpression(ExpressionBase base, Token beginPos) {
    NullChecker.check(beginPos);
    this.base = base;

    this.beginPos = beginPos;
  }

  public ExprExpression(ExprClassCreation classCreation, Token beginPos) {
    this.base = ExpressionBase.ECLASS_INSTANCE_CREATION;

    this.beginPos = beginPos;
    this.classCreation = classCreation;
  }

  public ExprExpression(ExprUnary unary, Token beginPos) {
    this.base = ExpressionBase.EUNARY;

    this.beginPos = beginPos;
    this.unary = unary;
  }

  public ExprExpression(ExprBinary binary, Token beginPos) {
    this.base = ExpressionBase.EBINARY;

    this.beginPos = beginPos;
    this.binary = binary;
  }

  public ExprExpression(IntLiteral e, Token token) {
    this.base = ExpressionBase.EPRIMARY_NUMBER;
    this.beginPos = token;
    this.number = e;
  }

  public ExprExpression(ExprMethodInvocation methodInvocation, Token beginPos) {
    this.base = ExpressionBase.EMETHOD_INVOCATION;

    this.beginPos = beginPos;
    this.methodInvocation = methodInvocation;
  }

  public ExprExpression(ExprFieldAccess fieldAccess, Token beginPos) {
    this.base = ExpressionBase.EFIELD_ACCESS;

    this.beginPos = beginPos;
    this.fieldAccess = fieldAccess;
  }

  public ExprExpression(ExprIdent symbol, Token beginPos) {
    this.base = ExpressionBase.EPRIMARY_IDENT;

    this.beginPos = beginPos;
    this.ident = symbol;
  }

  public ExprBuiltinFn getBuiltinFn() {
    return builtinFn;
  }

  public ExprAssign getAssign() {
    return assign;
  }

  public Type getResultType() {
    return resultType;
  }

  public ExprThis getSelfExpression() {
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

  public boolean is(ExpressionBase what) {
    return base.equals(what);
  }

  public boolean getBooleanLiteral() {
    return booleanLiteral;
  }

  public ExprCast getCastExpression() {
    return castExpression;
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
    if (base == ExpressionBase.ETHIS) {
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
    if (base == ExpressionBase.ESTRING_CONST) {
      return beginPos.getValue(); // because of the bootstrap: '\0' etc...
    }
    if (base == ExpressionBase.EUNARY) {
      return unary.toString();
    }
    if (base == ExpressionBase.ECHAR_CONST) {
      return beginPos.getValue(); // because of the bootstrap: '\0' etc...
    }
    if (base == ExpressionBase.EBOOLEAN_LITERAL) {
      if (booleanLiteral) {
        return "true";
      }
      return "false";
    }
    if (base == ExpressionBase.ECAST) {
      return castExpression.toString();
    }
    if (base == ExpressionBase.EBUILTIN_FN) {
      return builtinFn.toString();
    }
    return base.toString();
  }

  @Override
  public SourceLocation getLocation() {
    return beginPos.getLocation();
  }

  @Override
  public String getLocationToString() {
    return beginPos.getLocationToString();
  }

  public Token getBeginPos() {
    return beginPos;
  }

}
