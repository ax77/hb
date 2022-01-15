package _st3_linearize_expr;

import java.util.List;

import _st3_linearize_expr.ir.FlatCodeItem;
import _st3_linearize_expr.items.AssignVarVar;
import _st3_linearize_expr.leaves.Var;
import errors.AstParseException;
import tokenize.Ident;

public abstract class Peep {

  public static List<FlatCodeItem> tryPeep(final List<FlatCodeItem> rv) {

    // lhs == temporary
    for (FlatCodeItem item : rv) {
      if (!item.isAssignVarVar()) {
        continue;
      }
      if (item.getAssignVarVar().getLvalue().isOriginalNoTempVar()) {
        continue;
      }

      final AssignVarVar node = item.getAssignVarVar();
      boolean wasReplaced = replaceNested(rv, item);
      if (wasReplaced) {
        item.setIgnore(node.getRvalue());
      }

    }

    // lhs == original variable
    for (FlatCodeItem item : rv) {
      if (!item.isAssignVarVar()) {
        continue;
      }
      if (!item.getAssignVarVar().getLvalue().isOriginalNoTempVar()) {
        continue;
      }

      final AssignVarVar node = item.getAssignVarVar();
    }

    // and here remove all unreachable

    return rv;
  }

  private static boolean replaceNested(final List<FlatCodeItem> rv, FlatCodeItem item) {

    final AssignVarVar node = item.getAssignVarVar();

    Var lvalue = node.getLvalue();
    Var rvalue = node.getRvalue();
    Ident lname = lvalue.getName();
    Ident rname = rvalue.getName();

    boolean wasReplaced = false;

    for (FlatCodeItem nested : rv) {
      if (nested.equals(item)) {
        continue;
      }
      List<Var> vars = nested.getAllVars();
      for (Var v : vars) {
        boolean f = replaceOne(v, lname, rname);
        if (f) {
          wasReplaced = true;
        }
      }
    }
    return wasReplaced;
  }

  private static boolean replaceOne(Var orig, Ident lname, Ident rname) {
    boolean res = false;
    if (orig.getName().equals(lname)) {
      orig.replaceName(rname);
      res = true;
    }
    return res;
  }

  private static void unimpl(FlatCodeItem item) {
    throw new AstParseException("unimpl. item: " + item.toString());
  }

}
