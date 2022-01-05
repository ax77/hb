package _st3_linearize_expr.ir;

import _st3_linearize_expr.leaves.Var;
import ast_main.GlobalCounter;
import ast_modifiers.Modifiers;
import ast_modifiers.ModifiersChecker;
import ast_symtab.BuiltinNames;
import ast_types.Type;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import hashed.Hash_ident;
import tokenize.Ident;

public abstract class VarCreator {

  private static Ident tmpIdent() {
    return Hash_ident.getHashedIdent(String.format("t%d", GlobalCounter.next()));
  }

  public static Var copyVarDecl(VarDeclarator src) {
    final Var result = new Var(src.getBase(), src.getMods(), src.getType(), src.getIdentifier());
    result.setOriginalNoTempVar(true);
    return result;
  }

  public static Var justNewVar(Type type) {
    final Var result = new Var(VarBase.LOCAL_VAR, new Modifiers(), type, tmpIdent());
    return result;
  }
  
  public static Var varWithName(Type type, String name) {
    final Var result = new Var(VarBase.LOCAL_VAR, new Modifiers(), type, Hash_ident.getHashedIdent(name));
    return result;
  }

  public static Var just_this_(Type type) {
    final Var result = new Var(VarBase.METHOD_PARAMETER, new Modifiers(), type, BuiltinNames.__this_ident);
    return result;
  }

}
