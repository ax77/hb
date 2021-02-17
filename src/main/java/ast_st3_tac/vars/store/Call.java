package ast_st3_tac.vars.store;

import java.util.List;

import ast_method.ClassMethodDeclaration;
import ast_types.Type;
import tokenize.Ident;

public class Call {
  private final Type type;
  private final Ident function;
  private final List<Var> args;

  /// for rewriting
  private final boolean isConstructor;

  public Call(Type type, Ident function, List<Var> args, boolean isConstructor) {
    this.type = type;
    this.function = function;
    this.args = args;
    this.isConstructor = isConstructor;
  }

  public Type getType() {
    return type;
  }

  public Ident getFunction() {
    return function;
  }

  public List<Var> getArgs() {
    return args;
  }

  public boolean isConstructor() {
    return isConstructor;
  }

  private String argsToString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");

    for (int i = 0; i < args.size(); i++) {
      Var param = args.get(i);
      sb.append(param.toString());

      if (i + 1 < args.size()) {
        sb.append(", ");
      }
    }

    sb.append(")");
    return sb.toString();
  }

  @Override
  public String toString() {
    return function.toString() + argsToString();
  }

}
