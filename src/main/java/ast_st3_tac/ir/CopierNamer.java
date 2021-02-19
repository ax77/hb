package ast_st3_tac.ir;

import ast_main.GlobalCounter;
import ast_method.ClassMethodDeclaration;
import ast_st3_tac.leaves.Var;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import hashed.Hash_ident;
import tokenize.Ident;

public abstract class CopierNamer {

  public static Ident tmpIdent() {
    return Hash_ident.getHashedIdent(String.format("__t%d", GlobalCounter.next()));
  }

  public static Ident _this_() {
    return Hash_ident.getHashedIdent("_this_");
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
