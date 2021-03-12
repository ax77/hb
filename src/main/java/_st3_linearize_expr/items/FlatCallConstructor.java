package _st3_linearize_expr.items;

import java.util.List;

import _st3_linearize_expr.leaves.Var;
import ast_printers.GenericListPrinter;
import errors.AstParseException;
import utils_oth.NullChecker;

public class FlatCallConstructor {
  private final String fullname;
  private final Var thisVar; // must be also the first arg into args 
  private final List<Var> args;

  public FlatCallConstructor(String fullname, List<Var> args, Var thisVar) {
    NullChecker.check(fullname, args, thisVar);

    if (args.isEmpty() || !args.get(0).equals(thisVar)) {
      throw new AstParseException("constructor arguments are not correct");
    }

    this.fullname = fullname;
    this.args = args;
    this.thisVar = thisVar;
  }

  public List<Var> getArgs() {
    return args;
  }

  public Var getThisVar() {
    return thisVar;
  }

  public String getFullname() {
    return fullname;
  }

  @Override
  public String toString() {
    return fullname + GenericListPrinter.paramsToStringWithBraces(args);
  }

}