package _st3_linearize_expr;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.ir.FlatCodeItem;
import _st3_linearize_expr.items.AssignVarAllocObject;
import _st3_linearize_expr.items.AssignVarBinop;
import _st3_linearize_expr.items.AssignVarBool;
import _st3_linearize_expr.items.AssignVarDefaultValueForType;
import _st3_linearize_expr.items.AssignVarFieldAccess;
import _st3_linearize_expr.items.AssignVarFlatCallClassCreationTmp;
import _st3_linearize_expr.items.AssignVarFlatCallResult;
import _st3_linearize_expr.items.AssignVarUnop;
import _st3_linearize_expr.items.AssignVarVar;
import _st3_linearize_expr.items.FlatCallVoid;
import _st3_linearize_expr.items.FlatCallVoidStatic;
import _st3_linearize_expr.items.StoreFieldVar;
import _st3_linearize_expr.items.StoreVarVar;
import _st3_linearize_expr.leaves.Binop;
import _st3_linearize_expr.leaves.FieldAccess;
import _st3_linearize_expr.leaves.FunctionCallWithResult;
import _st3_linearize_expr.leaves.FunctionCallWithResultStatic;
import _st3_linearize_expr.leaves.Unop;
import _st3_linearize_expr.leaves.Var;
import _st7_codeout.ToStringsInternal;
import ast_symtab.BuiltinNames;
import ast_types.Type;
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

  private static void propagateName(AssignVarVar assignVarVar, List<FlatCodeItem> rv) {
    Var lvalue = assignVarVar.getLvalue();
    Var rvalue = assignVarVar.getRvalue();
    Ident lname = lvalue.getName();
    Ident rname = rvalue.getName();

    for (final FlatCodeItem item : rv) {

      if (item.isAssignVarAllocObject()) {
        unimpl(item);
      }

      else if (item.isAssignVarBinop()) {
        AssignVarBinop node = item.getAssignVarBinop();
        replaceOne(node.getLvalue(), lname, rname);

        Binop op = node.getRvalue();
        replaceOne(op.getLhs(), lname, rname);
        replaceOne(op.getRhs(), lname, rname);

      }

      else if (item.isAssignVarBool()) {
        unimpl(item);
      }

      else if (item.isAssignVarFieldAccess()) {

        AssignVarFieldAccess node = item.getAssignVarFieldAccess();
        replaceOne(node.getLvalue(), lname, rname);

        FieldAccess access = node.getRvalue();
        replaceOne(access.getObject(), lname, rname);
        replaceOne(access.getField(), lname, rname);

      }

      else if (item.isAssignVarFlatCallStringCreationTmp()) {
        unimpl(item);
      }

      else if (item.isAssignVarFlatCallClassCreationTmp()) {
        AssignVarFlatCallClassCreationTmp node = item.getAssignVarFlatCallClassCreationTmp();
        replaceOne(node.getLvalue(), lname, rname);

        for (Var arg : node.getRvalue().getArgs()) {
          replaceOne(arg, lname, rname);
        }
      }

      else if (item.isAssignVarFlatCallResult()) {

        FunctionCallWithResult call = item.getAssignVarFlatCallResult().getRvalue();
        for (Var arg : call.getArgs()) {
          replaceOne(arg, lname, rname);
        }

      }

      else if (item.isAssignVarNum()) {
        //unimpl(item);
      }

      else if (item.isAssignVarUnop()) {

        AssignVarUnop node = item.getAssignVarUnop();
        replaceOne(node.getLvalue(), lname, rname);

        Unop op = node.getRvalue();
        replaceOne(op.getOperand(), lname, rname);

      }

      else if (item.isAssignVarVar()) {

        AssignVarVar node = item.getAssignVarVar();
        if (node.getLvalue().isOriginalNoTempVar()) {
          replaceOne(node.getRvalue(), lname, rname);
        }

      }

      else if (item.isFlatCallVoid()) {

        FlatCallVoid call = item.getFlatCallVoid();
        for (Var arg : call.getArgs()) {
          replaceOne(arg, lname, rname);
        }

      }

      else if (item.isStoreFieldVar()) {
        StoreFieldVar node = item.getStoreFieldVar();
        replaceOne(node.getSrc(), lname, rname);

        FieldAccess access = node.getDst();
        replaceOne(access.getObject(), lname, rname);
        replaceOne(access.getField(), lname, rname);
      }

      else if (item.isStoreVarVar()) {
        StoreVarVar node = item.getStoreVarVar();
        replaceOne(node.getSrc(), lname, rname);
        replaceOne(node.getDst(), lname, rname);

      }

      else if (item.isAssignVarSizeof()) {
        unimpl(item);
      }

      else if (item.isAssignVarFieldAccessStatic()) {
        unimpl(item);
      }

      else if (item.isFlatCallVoidStatic()) {

        FlatCallVoidStatic call = item.getFlatCallVoidStatic();
        for (Var arg : call.getArgs()) {
          replaceOne(arg, lname, rname);
        }

      }

      else if (item.isAssignVarFlatCallResultStatic()) {

        FunctionCallWithResultStatic call = item.getAssignVarFlatCallResultStatic().getRvalue();
        for (Var arg : call.getArgs()) {
          replaceOne(arg, lname, rname);
        }

      }

      else if (item.isAssignVarCastExpression()) {
        unimpl(item);
      }

      else if (item.isSelectionShortCircuit()) {
        unimpl(item);
      }

      else if (item.isAssignVarDefaultValueForType()) {

        AssignVarDefaultValueForType node = item.getAssignVarDefaultValueForType();
        replaceOne(node.getLvalue(), lname, rname);
        replaceOne(node.getRvalue(), lname, rname);

      }

      else if (item.isBuiltinFuncAssertTrue()) {
        unimpl(item);
      }

      else {
        throw new AstParseException("unknown item: " + item.toString());
      }

    }
  }

  private static void unimpl(FlatCodeItem item) {
    throw new AstParseException("unimpl. item: " + item.toString());
  }

}
