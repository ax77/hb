package ast_expr;

import java.io.Serializable;
import java.util.List;

import ast_printers.GenericListPrinter;
import tokenize.Ident;
import utils_oth.NullChecker;

public class ExprBuiltinFunc implements Serializable {
  private static final long serialVersionUID = 6082427317484452898L;

  private final Ident name;
  private final List<ExprExpression> args;

  /// for assert-true intrinsic
  private String fileToString;
  private String lineToString;
  private String exprToString;

  public ExprBuiltinFunc(Ident name, List<ExprExpression> args) {
    this.name = name;
    this.args = args;
  }

  public ExprBuiltinFunc(Ident name, List<ExprExpression> args, String fileToString, String lineToString,
      String exprToString) {

    NullChecker.check(name, args, fileToString, lineToString, exprToString);

    this.name = name;
    this.args = args;
    this.fileToString = fileToString;
    this.lineToString = lineToString;
    this.exprToString = exprToString;
  }

  public Ident getName() {
    return name;
  }

  public List<ExprExpression> getArgs() {
    return args;
  }

  public String getFileToString() {
    return fileToString;
  }

  public String getLineToString() {
    return lineToString;
  }

  public String getExprToString() {
    return exprToString;
  }

  @Override
  public String toString() {
    return name.toString() + GenericListPrinter.paramsToStringWithBraces(args);
  }

}
