package ast_st3_tac.ir;

import java.util.ArrayList;
import java.util.List;

import ast_modifiers.Modifiers;
import ast_st2_annotate.Mods;
import ast_st3_tac.leaves.Var;
import ast_types.Type;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;

public class VarCreator {

  private final List<Var> allVars;

  public VarCreator() {
    // TODO: linked hash-set instead of array
    this.allVars = new ArrayList<>();
  }

  public List<Var> getAllVars() {
    return allVars;
  }

  public Var copyVarDecl(VarDeclarator src) {
    final Var result = new Var(src.getBase(), src.getMods(), src.getType(), src.getIdentifier());
    if (!allVars.contains(result)) {
      allVars.add(result);
    }
    return result;
  }

  public Var justNewVar(Type type) {
    final Var result = new Var(VarBase.LOCAL_VAR, new Modifiers(), type, CopierNamer.tmpIdent());
    if (!allVars.contains(result)) {
      allVars.add(result);
    }
    return result;
  }

  public Var justNewVarFromFieldNoBindings(Type type) {
    final Var result = new Var(VarBase.LOCAL_VAR, new Modifiers(), type, CopierNamer.tmpIdent());
    return result;
  }

  public Var just_this_(Type type) {
    final Var result = new Var(VarBase.METHOD_PARAMETER, Mods.letMods(), type, CopierNamer._this_());
    if (!allVars.contains(result)) {
      allVars.add(result);
    }
    return result;
  }

}
