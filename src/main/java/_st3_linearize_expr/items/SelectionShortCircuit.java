package _st3_linearize_expr.items;

import java.util.List;

import _st3_linearize_expr.ir.FlatCodeItem;
import _st3_linearize_expr.leaves.Var;

public class SelectionShortCircuit {

  private final Var dest;

  private final Var condVar;
  private final Var trueVar;
  private final Var elseVar;

  private final List<FlatCodeItem> condblock;
  private final List<FlatCodeItem> trueblock;
  private final List<FlatCodeItem> elseblock;

  public SelectionShortCircuit(Var dest, Var condVar, Var trueVar, Var elseVar, List<FlatCodeItem> condblock,
      List<FlatCodeItem> trueblock, List<FlatCodeItem> elseblock) {
    this.dest = dest;
    this.condVar = condVar;
    this.trueVar = trueVar;
    this.elseVar = elseVar;
    this.condblock = condblock;
    this.trueblock = trueblock;
    this.elseblock = elseblock;
  }

  public Var getDest() {
    return dest;
  }

  public List<FlatCodeItem> getCondblock() {
    return condblock;
  }

  public List<FlatCodeItem> getTrueblock() {
    return trueblock;
  }

  public List<FlatCodeItem> getElseblock() {
    return elseblock;
  }

  private String optSemiconon(FlatCodeItem item) {
    if (item.isSelectionShortCircuit()) {
      return "\n";
    }
    return ";\n";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(dest.typeNameToString() + " = 0;\n");

    sb.append("\n{\n");
    for (FlatCodeItem item : condblock) {
      sb.append(item.toString() + optSemiconon(item));
    }

    sb.append("if(" + condVar.getName().getName() + ") {\n");
    for (FlatCodeItem item : trueblock) {
      sb.append(item.toString() + optSemiconon(item));
    }
    sb.append("} else {\n");
    for (FlatCodeItem item : elseblock) {
      sb.append(item.toString() + optSemiconon(item));
    }
    sb.append("}\n");
    sb.append("\n}\n");

    return sb.toString();
  }

}
