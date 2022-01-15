package _st3_linearize_expr.items;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.Leaf;
import _st3_linearize_expr.leaves.Var;

public class StoreVarLiteral implements VarCollector {
  private final Var dst;
  private final Leaf src;

  public StoreVarLiteral(Var dst, Leaf src) {
    this.dst = dst;
    this.src = src;
  }

  public Var getDst() {
    return dst;
  }

  public Leaf getSrc() {
    return src;
  }

  @Override
  public String toString() {
    return dst.typeNameToString() + " = " + src.toString();
  }

  @Override
  public List<Var> getAllVars() {
    List<Var> vars = new ArrayList<>();
    vars.add(dst);
    if (src.isVar()) {
      vars.add(src.getVar());
    }
    return vars;
  }

}
