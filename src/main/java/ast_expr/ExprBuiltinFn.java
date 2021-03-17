package ast_expr;

import java.io.Serializable;
import java.util.List;

import _st1_templates.TypeSetter;
import ast_printers.GenericListPrinter;
import ast_printers.TypePrinters;
import ast_types.Type;
import tokenize.Ident;
import utils_oth.NullChecker;

public class ExprBuiltinFn implements Serializable, TypeSetter {
  private static final long serialVersionUID = 166218239967767110L;

  private final Ident function;
  private final List<ExprExpression> callArguments;
  private final List<Type> typeArguments;
  private Type returnType;

  public ExprBuiltinFn(Ident function, List<Type> typeArguments, List<ExprExpression> callArguments, Type returnType) {
    NullChecker.check(function, typeArguments, callArguments, returnType);

    this.function = function;
    this.callArguments = callArguments;
    this.typeArguments = typeArguments;
    this.returnType = returnType;
  }

  public Ident getFunction() {
    return function;
  }

  public List<ExprExpression> getCallArguments() {
    return callArguments;
  }

  @Override
  public Type getType() {
    return returnType;
  }

  @Override
  public void setType(Type typeToSet) {
    this.returnType = typeToSet;
  }

  public List<Type> getTypeArguments() {
    return typeArguments;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("std.");
    sb.append(function.getName());
    if (!typeArguments.isEmpty()) {
      sb.append("_");
      sb.append(TypePrinters.typeArgumentsToString(typeArguments));
    }
    sb.append(GenericListPrinter.paramsToStringWithBraces(callArguments));
    return sb.toString();
  }

}
