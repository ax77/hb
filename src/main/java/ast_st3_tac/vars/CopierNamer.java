package ast_st3_tac.vars;

import ast_main.GlobalCounter;
import ast_method.ClassMethodDeclaration;
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
    return Hash_ident.getHashedIdent(String.format("__t%d", GlobalCounter.next()));
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

  public static String getMethodName(ClassMethodDeclaration m) {
    StringBuilder sb = new StringBuilder();
    sb.append(m.getClazz().getIdentifier().getName());
    sb.append("_");
    if (m.isFunction()) {
      sb.append(m.getIdentifier().getName());
      sb.append("_");
    }
    if (m.isConstructor()) {
      sb.append("init_");
    }
    if (m.isDestructor()) {
      sb.append("deinit_");
    }
    sb.append(m.getUniqueIdToString());
    return sb.toString();
  }

}
