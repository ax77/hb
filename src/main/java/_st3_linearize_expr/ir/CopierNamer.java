package _st3_linearize_expr.ir;

import ast_main.GlobalCounter;
import ast_method.ClassMethodDeclaration;
import hashed.Hash_ident;
import tokenize.Ident;

public abstract class CopierNamer {

  public static Ident tmpIdent() {
    return Hash_ident.getHashedIdent(String.format("t%d", GlobalCounter.next()));
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
    sb.append(m.getUniqueIdToString());
    sb.append("_");
    return sb.toString();
  }

}
