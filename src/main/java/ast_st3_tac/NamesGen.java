package ast_st3_tac;

import ast_method.ClassMethodDeclaration;

public abstract class NamesGen {

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
