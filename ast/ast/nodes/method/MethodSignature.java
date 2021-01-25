package ast.ast.nodes.method;

import java.io.Serializable;
import java.util.List;

import ast.types.Type;
import jscan.tokenize.Ident;

public class MethodSignature implements Serializable {
  private static final long serialVersionUID = 4778343178556621253L;

  private final Ident methodName;
  private final List<MethodParameter> parameters;

  public MethodSignature(Ident methodName, List<MethodParameter> parameters) {
    this.methodName = methodName;
    this.parameters = parameters;
  }

  public Ident getMethodName() {
    return methodName;
  }

  public List<MethodParameter> getParameters() {
    return parameters;
  }

  private String parametersToString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");

    for (int i = 0; i < parameters.size(); i++) {
      MethodParameter param = parameters.get(i);
      sb.append(param.toString());

      if (i + 1 < parameters.size()) {
        sb.append(", ");
      }
    }

    sb.append(")");
    return sb.toString();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(methodName.getName());
    sb.append(parametersToString());
    return sb.toString();
  }

  public boolean isEqualTo(MethodSignature another) {
    if (!methodName.equals(another.getMethodName())) {
      return false;
    }

    int bound = parameters.size();
    List<MethodParameter> anotherParameters = another.getParameters();

    if (bound != anotherParameters.size()) {
      return false;
    }

    for (int i = 0; i < bound; i++) {
      Type tp1 = parameters.get(i).getType();
      Type tp2 = anotherParameters.get(i).getType();
      if (!tp1.is_equal_to(tp2)) {
        return false;
      }
    }

    return true;
  }
}
