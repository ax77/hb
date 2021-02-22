package ast_st2_annotate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_st3_tac.FlatCode;
import ast_st3_tac.ir.CopierNamer;
import ast_st3_tac.ir.FlatCodeItem;
import ast_st3_tac.items.FlatCallVoid;
import ast_st3_tac.leaves.Var;
import ast_stmt.StmtBlock;
import ast_stmt.StmtBlockItem;
import ast_vars.VarBase;
import hashed.Hash_ident;
import tokenize.Ident;

public abstract class GetDestr {

  public static void semBlock(ClassDeclaration object, ClassMethodDeclaration method, StmtBlock block) {
    FlatCode fc = GetDestr.genDestr(block);
    block.setDestr(fc);
  }

  private static List<Var> revlist(List<Var> input) {
    List<Var> rv = new ArrayList<>();
    if (input.isEmpty()) {
      return rv;
    }
    for (int i = input.size() - 1; i >= 0; i -= 1) {
      rv.add(input.get(i));
    }
    return rv;
  }

  private static FlatCode genDestr(StmtBlock block) {
    List<FlatCodeItem> items = new ArrayList<>();
    List<Var> collected = new ArrayList<>();
    List<Ident> names = new ArrayList<>();

    for (StmtBlockItem blockItem : block.getBlockItems()) {
      if (!blockItem.isVarDeclarationItem()) {
        continue;
      }
      FlatCode fc = blockItem.getLinearLocalVariable();
      List<Var> allVars = revlist(fc.getAllVars());
      for (Var v : allVars) {
        if (!v.getType().is_class()) {
          continue;
        }
        if (v.is(VarBase.METHOD_PARAMETER)) {
          continue;
        }
        if (!names.contains(v.getName())) {
          collected.add(v);
          names.add(v.getName()); // TODO: rewrite!
        }
      }
    }

    Collections.sort(collected);

    for (Var v : collected) {
      ClassDeclaration clazz = v.getType().getClassTypeFromRef();
      Ident fn = Hash_ident.getHashedIdent(CopierNamer.getMethodName(clazz.getDestructor()));
      List<Var> args = new ArrayList<>();
      args.add(v);
      FlatCallVoid flatCallVoid = new FlatCallVoid(fn, args);
      items.add(new FlatCodeItem(flatCallVoid));
    }

    return new FlatCode(items);
  }

}
