package _st3_linearize_expr;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.ir.FlatCodeItem;
import _st3_linearize_expr.items.AssignVarAllocObject;
import _st3_linearize_expr.items.AssignVarFlatCallClassCreationTmp;
import _st3_linearize_expr.items.AssignVarVar;
import ast_symtab.BuiltinNames;

public abstract class Peep {

  public static List<FlatCodeItem> tryPeep(final List<FlatCodeItem> rv) {

    // 1.
    List<FlatCodeItem> peepObjectAllocation = itIsPossibleToPeepObjectAllocation(rv);
    if (peepObjectAllocation != null) {
      return peepObjectAllocation;
    }

    // TODO: we have to track all of the arguments
    // if the last two items are assignVarClassCreation and assignVarVar,
    // and all of the imets before are arguments...
    // // 2.
    // List<FlatCodeItem> peepConstructorCall = itIsPossibleToPeepConstructorCall(rv);
    // if (peepConstructorCall != null) {
    //   return peepConstructorCall;
    // }

    return rv;
  }

  // 1) A wildly used object creation.
  // 
  // struct utest* t128 = (struct utest*) hb_alloc(sizeof(struct utest));
  // struct utest* __this = t128;
  //
  // items, assVarAlloc, assVarVar
  // !!! types are the same
  //
  // struct utest* __this = (struct utest*) hb_alloc(sizeof(struct utest));
  //

  private static List<FlatCodeItem> itIsPossibleToPeepObjectAllocation(List<FlatCodeItem> rv) {
    if (rv.size() != 2) {
      return null;
    }
    boolean flag = rv.get(0).isAssignVarAllocObject() && rv.get(1).isAssignVarVar();
    if (!flag) {
      return null;
    }

    AssignVarAllocObject origAssignVarAllocObject = rv.get(0).getAssignVarAllocObject();
    AssignVarVar origAssignVarVar = rv.get(1).getAssignVarVar();

    if (!origAssignVarAllocObject.getLvalue().getType().isEqualTo(origAssignVarVar.getLvalue().getType())) {
      return null;
    }
    if (!origAssignVarVar.getLvalue().getName().equals(BuiltinNames.__this_ident)) {
      return null;
    }

    /// OK:

    List<FlatCodeItem> result = new ArrayList<>();
    AssignVarAllocObject assignVarAllocObject = new AssignVarAllocObject(origAssignVarVar.getLvalue(),
        origAssignVarAllocObject.getTypename());

    result.add(new FlatCodeItem(assignVarAllocObject));

    return result;
  }

  // struct utest* t86 = utest_init_28(t85);
  // struct utest* a = t86;
  //
  // items: assignVarClassCreation, assignVarVar
  //
  // struct utest* a = utest_init_28(t85);

  private static List<FlatCodeItem> itIsPossibleToPeepConstructorCall(List<FlatCodeItem> rv) {
    if (rv.size() != 2) {
      return null;
    }
    boolean flag = rv.get(0).isAssignVarFlatCallClassCreationTmp() && rv.get(1).isAssignVarVar();
    if (!flag) {
      return null;
    }

    AssignVarFlatCallClassCreationTmp origAssignVarAllocObject = rv.get(0).getAssignVarFlatCallClassCreationTmp();
    AssignVarVar origAssignVarVar = rv.get(1).getAssignVarVar();

    if (!origAssignVarAllocObject.getLvalue().getType().isEqualTo(origAssignVarVar.getLvalue().getType())) {
      return null;
    }

    /// OK:

    List<FlatCodeItem> result = new ArrayList<>();
    AssignVarFlatCallClassCreationTmp assignVarFlatCallClassCreationTmp = new AssignVarFlatCallClassCreationTmp(
        origAssignVarVar.getLvalue(), origAssignVarAllocObject.getRvalue());

    result.add(new FlatCodeItem(assignVarFlatCallClassCreationTmp));

    return result;
  }

}
