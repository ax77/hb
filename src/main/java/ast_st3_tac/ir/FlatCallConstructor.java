package ast_st3_tac.ir;

import java.util.List;

import ast_printers.GenericListPrinter;
import ast_st3_tac.vars.store.Var;
import errors.AstParseException;
import tokenize.Ident;
import utils_oth.NullChecker;

public class FlatCallConstructor {
  private final Ident function;
  private final Var thisVar; // must be also the first arg into args 
  private final List<Var> args;

  public FlatCallConstructor(Ident function, List<Var> args, Var thisVar) {
    NullChecker.check(function, args, thisVar);

    if (args.isEmpty() || !args.get(0).equals(thisVar)) {
      throw new AstParseException("constructor arguments are not correct");
    }

    this.function = function;
    this.args = args;
    this.thisVar = thisVar;
  }

  public Ident getFunction() {
    return function;
  }

  public List<Var> getArgs() {
    return args;
  }

  public Var getThisVar() {
    return thisVar;
  }

  @Override
  public String toString() {
    return function.toString() + GenericListPrinter.paramsToStringWithBraces(args);
  }

}
