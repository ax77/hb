package ast_expr;

import java.io.Serializable;
import java.util.List;

import ast_st2_annotate.Symbol;
import tokenize.Ident;
import utils_oth.NullChecker;

public class ExprMethodInvocation implements Serializable, MirSymbol {
  private static final long serialVersionUID = -6288485017757935715L;

  // object.funcname(arguments)
  private final ExprExpression object;
  private final Ident funcname;
  private final List<ExprExpression> arguments;

  //MIR:TREE
  private Symbol sym;

  // a.b()
  // self.b()
  public ExprMethodInvocation(ExprExpression object, Ident funcname, List<ExprExpression> arguments) {
    NullChecker.check(funcname, object, arguments);

    this.funcname = funcname;
    this.object = object;
    this.arguments = arguments;
  }

  public ExprExpression getObject() {
    return object;
  }

  public List<ExprExpression> getArguments() {
    return arguments;
  }

  public Ident getFuncname() {
    return funcname;
  }

  @Override
  public Symbol getSym() {
    return sym;
  }

  @Override
  public void setSym(Symbol sym) {
    this.sym = sym;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append(object.toString());
    sb.append(".");

    sb.append(funcname.getName());
    sb.append(ExprUtil.funcArgsToString(arguments));

    return sb.toString();
  }

}
