package ast_printers;

import java.util.List;

import ast_vars.VarBase;
import ast_vars.VarDeclarator;

public abstract class VarPrinters {

  public static String varsTos(List<VarDeclarator> variables) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < variables.size(); i++) {
      VarDeclarator var = variables.get(i);
      sb.append(varBaseSimple(var));
      sb.append(var.getIdentifier().getName());
      if (i + 1 < variables.size()) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }

  public static String bindedVarsComment(List<VarDeclarator> variables) {
    StringBuilder sb = new StringBuilder();

    if (!variables.isEmpty()) {
      sb.append(" // vars: [");
      sb.append(VarPrinters.varsTos(variables));
      sb.append("]");
    }

    return sb.toString();
  }

  private static String varBaseSimple(VarDeclarator var) {
    if (var.is(VarBase.CLASS_FIELD)) {
      return "F:";
    }
    if (var.is(VarBase.LOCAL_VAR)) {
      return "L:";
    }
    if (var.is(VarBase.METHOD_PARAMETER)) {
      return "P:";
    }
    if (var.is(VarBase.METHOD_VAR)) {
      return "M:";
    }
    return "?:";
  }

}
