package ast_st3_tac.vars;

import ast_method.MethodIdCounter;
import ast_st3_tac.vars.store.Var;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import hashed.Hash_ident;
import tokenize.Ident;

public abstract class CopierNamer {

  public static Var copyVarAddNewName(Var src) {
    return new Var(checkBase(src.getBase()), src.getMods(), src.getType(), tmpIdent());
  }

  public static Var copyVarDecl(VarDeclarator src) {
    return new Var(checkBase(src.getBase()), src.getMods(), src.getType(), src.getIdentifier());
  }

  public static Var copyVarDeclAddNewName(VarDeclarator var) {
    return new Var(checkBase(var.getBase()), var.getMods(), var.getType(), tmpIdent());
  }

  public static Ident tmpIdent() {
    return Hash_ident.getHashedIdent(String.format("__t%d", MethodIdCounter.next()));
  }

  public static Ident _this_() {
    return Hash_ident.getHashedIdent("_this_");
  }

  private static VarBase checkBase(VarBase base) {
    if (base != VarBase.METHOD_PARAMETER) {
      return VarBase.LOCAL_VAR;
    }
    return VarBase.METHOD_PARAMETER;
  }

}
