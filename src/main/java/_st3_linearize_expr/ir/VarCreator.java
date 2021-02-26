package _st3_linearize_expr.ir;

import _st2_annotate.Mods;
import _st3_linearize_expr.leaves.Var;
import ast_modifiers.Modifiers;
import ast_symtab.BuiltinNames;
import ast_types.Type;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;

public abstract class VarCreator {

  public static Var copyVarDecl(VarDeclarator src) {
    final Var result = new Var(src.getBase(), src.getMods(), src.getType(), src.getIdentifier());
    return result;
  }

  public static Var justNewVar(Type type) {
    final Var result = new Var(VarBase.LOCAL_VAR, new Modifiers(), type, CopierNamer.tmpIdent());
    return result;
  }

  public static Var just_this_(Type type) {
    final Var result = new Var(VarBase.METHOD_PARAMETER, Mods.letMods(), type, BuiltinNames.__this_ident);
    return result;
  }

}
