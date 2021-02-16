package ast_st3_tac.vars.store;

import java.util.List;

import ast_types.Type;

public class Call {
  private final Type type;
  private final Var function;
  private final List<Value> args;

  public Call(Type type, Var function, List<Value> args) {
    this.type = type;
    this.function = function;
    this.args = args;
  }

  public Type getType() {
    return type;
  }

  public Var getFunction() {
    return function;
  }

  public List<Value> getArgs() {
    return args;
  }

  private String argsToString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");

    for (int i = 0; i < args.size(); i++) {
      Value param = args.get(i);
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
