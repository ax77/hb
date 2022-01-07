package ast_expr;

import java.io.Serializable;

import ast_class.ClassDeclaration;
import ast_sourceloc.Location;
import ast_sourceloc.SourceLocation;
import ast_types.Type;
import literals.IntLiteral;
import tokenize.Token;
import utils_oth.NullChecker;

public class ExprExpression implements Serializable, Location {
  private static final long serialVersionUID = -2905880039842730533L;

  // main
  private final ExpressionBase base; // what union contains
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
  private ClassDeclaration selfExpression;
  private Boolean booleanLiteral;
  private ExprCast castExpression;
  private ExprTernaryOperator ternaryOperator;
  private ExprSizeof exprSizeof;
  private ExprTypeof exprTypeof;
  private ExprBuiltinFunc exprBuiltinFunc;
  private ExprForLoopStepComma exprForLoopStepComma;
  private ExprStaticAccess exprStaticAccess;
  private ExprDefaultValueForType exprDefaultValueForType;
  private ExprDelete exprDelete;

  public ExprExpression(ExprDelete exprDelete, Token beginPos) {
    this.base = ExpressionBase.EDELETE;
    this.exprDelete = exprDelete;
    this.beginPos = beginPos;
  }

  public ExprExpression(ExprDefaultValueForType exprDefaultValueForType, Token beginPos) {
    this.base = ExpressionBase.EDEFAULT_VALUE_FOR_TYPE;
    this.exprDefaultValueForType = exprDefaultValueForType;
    this.beginPos = beginPos;
  }

  public ExprExpression(ExprStaticAccess exprStaticAccess, Token beginPos) {
    this.base = ExpressionBase.ESTATIC_ACCESS;
    this.exprStaticAccess = exprStaticAccess;
    this.beginPos = beginPos;
  }

  public ExprExpression(Token beginPos) {
    this.base = ExpressionBase.EDEFAULT_VALUE_FOR_TYPE;
    this.beginPos = beginPos;
  }

  public ExprExpression(ExprForLoopStepComma exprForLoopStepComma, Token beginPos) {
    this.base = ExpressionBase.EFOR_LOOP_STEP_COMMA;
    this.beginPos = beginPos;
    this.exprForLoopStepComma = exprForLoopStepComma;
  }

  public ExprExpression(ExprBuiltinFunc exprBuiltinFunc, Token beginPos) {
    this.base = ExpressionBase.EBUILTIN_FUNC;
    this.beginPos = beginPos;
    this.exprBuiltinFunc = exprBuiltinFunc;
  }

  public ExprExpression(ExprTypeof exprTypeof, Token beginPos) {
    this.base = ExpressionBase.ETYPEOF;
    this.beginPos = beginPos;
    this.exprTypeof = exprTypeof;
  }

  public ExprExpression(ExprSizeof exprSizeof, Token beginPos) {
    this.base = ExpressionBase.ESIZEOF;
    this.exprSizeof = exprSizeof;
    this.beginPos = beginPos;
  }

  public ExprExpression(ExprTernaryOperator ternaryOperator, Token beginPos) {
    this.base = ExpressionBase.ETERNARY_OPERATOR;
    this.beginPos = beginPos;
    this.ternaryOperator = ternaryOperator;
  }

  public ExprExpression(ExprCast castExpression, Token beginPos) {
    this.base = ExpressionBase.ECAST;
    this.castExpression = castExpression;
    this.beginPos = beginPos;
  }

  public ExprExpression(Boolean value, Token beginPos) {
    this.base = ExpressionBase.EBOOLEAN_LITERAL;
    this.beginPos = beginPos;
    this.booleanLiteral = value;
  }

  public ExprExpression(ClassDeclaration selfExpression, Token beginPos) {
    this.base = ExpressionBase.ETHIS;
    this.beginPos = beginPos;
    this.selfExpression = selfExpression;
  }

  public ExprExpression(ExprAssign assign, Token beginPos) {
    this.base = ExpressionBase.EASSIGN;
    this.beginPos = beginPos;
    this.assign = assign;
  }

  // string-literal, char-literal
  //
  public ExprExpression(ExpressionBase base, Token beginPos) {
    NullChecker.check(beginPos);
    this.base = base;
    this.beginPos = beginPos;
  }

  public ExprExpression(ExprClassCreation classCreation, Token beginPos) {
    this.base = ExpressionBase.ECLASS_CREATION;
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

  public ExprAssign getAssign() {
    return assign;
  }

  public Type getResultType() {
    return resultType;
  }

  public ClassDeclaration getSelfExpression() {
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
      return "this";
    }
    if (base == ExpressionBase.EMETHOD_INVOCATION) {
      return methodInvocation.toString();
    }
    if (base == ExpressionBase.EPRIMARY_NUMBER) {
      return number.toString();
    }
    if (base == ExpressionBase.ECLASS_CREATION) {
      return classCreation.toString();
    }
    if (base == ExpressionBase.EPRIMARY_STRING) {
      return beginPos.getValue(); // because of the bootstrap: '\0' etc...
    }
    if (base == ExpressionBase.EUNARY) {
      return unary.toString();
    }
    if (base == ExpressionBase.EPRIMARY_CHAR) {
      return beginPos.getValue(); // because of the bootstrap: '\0' etc...
    }
    if (base == ExpressionBase.EBOOLEAN_LITERAL) {
      return booleanLiteral.toString();
    }
    if (base == ExpressionBase.ECAST) {
      return castExpression.toString();
    }
    if (base == ExpressionBase.ETERNARY_OPERATOR) {
      return ternaryOperator.toString();
    }
    if (base == ExpressionBase.ESIZEOF) {
      return exprSizeof.toString();
    }
    if (base == ExpressionBase.ETYPEOF) {
      return exprTypeof.toString();
    }
    if (base == ExpressionBase.EBUILTIN_FUNC) {
      return exprBuiltinFunc.toString();
    }
    if (base == ExpressionBase.EFOR_LOOP_STEP_COMMA) {
      return exprForLoopStepComma.toString();
    }
    if (base == ExpressionBase.EDEFAULT_VALUE_FOR_TYPE) {
      return exprDefaultValueForType.toString();
    }
    if (base == ExpressionBase.ESTATIC_ACCESS) {
      return exprStaticAccess.toString();
    }
    if (base == ExpressionBase.EDELETE) {
      return exprDelete.toString();
    }
    return base.toString();
  }

  public ExprForLoopStepComma getExprForLoopStepComma() {
    return exprForLoopStepComma;
  }

  public ExprBuiltinFunc getExprBuiltinFunc() {
    return exprBuiltinFunc;
  }

  public ExprSizeof getExprSizeof() {
    return exprSizeof;
  }

  public ExprTernaryOperator getTernaryOperator() {
    return ternaryOperator;
  }

  @Override
  public SourceLocation getLocation() {
    return beginPos.getLocation();
  }

  @Override
  public String getLocationToString() {
    return beginPos.getLocationToString();
  }

  @Override
  public Token getBeginPos() {
    return beginPos;
  }

  public ExprTypeof getExprTypeof() {
    return exprTypeof;
  }

  public ExprStaticAccess getExprStaticAccess() {
    return exprStaticAccess;
  }

  public ExprDefaultValueForType getExprDefaultValueForType() {
    return exprDefaultValueForType;
  }

  public ExprDelete getExprDelete() {
    return exprDelete;
  }

}
