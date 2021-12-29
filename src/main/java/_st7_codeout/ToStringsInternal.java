package _st7_codeout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeBase;
import ast_types.TypeBindings;
import ast_vars.VarDeclarator;

public abstract class ToStringsInternal {

  private static int counter = 1024;
  private static final Map<String, String> pairs = new HashMap<String, String>();

  public static String signToStringCall(ClassMethodDeclaration meth) {
    final String rest = typeArgumentsToString(meth.getClazz().getTypeParametersT());
    return getMethodName(meth) + rest;
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
    if (m.isTest()) {
      sb.append("test_");
    }
    sb.append(m.getUniqueIdToString());
    if (!m.getClazz().getTypeParametersT().isEmpty()) {
      sb.append("_");
    }
    return sb.toString();
  }

  public static String signToStringCallPushF(ClassMethodDeclaration m) {
    StringBuilder sb = new StringBuilder();
    sb.append(m.getClazz().getIdentifier().getName());
    sb.append(".");
    sb.append(m.getIdentifier().getName());
    return sb.toString();
  }

  public static String getPairsToString() {
    StringBuilder sb = new StringBuilder();
    for (Entry<String, String> e : pairs.entrySet()) {
      sb.append(e.getKey() + " :: " + e.getValue() + "\n");
    }
    return sb.toString();
  }

  private static String genName(Type forType) {
    if (forType.is(TypeBase.TP_TYPENAME_ID)) {
      System.out.println("type-printer found the typename-id in the type-arguments, the class is not expanded...");
      return "";
    }
    if (pairs.containsKey(typeToString(forType))) {
      return pairs.get(typeToString(forType));
    }
    String newname = String.format("%d", counter++);
    pairs.put(typeToString(forType), newname);
    return newname;
  }

  public static String typeArgumentsToString(List<Type> typeArguments) {
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < typeArguments.size(); i++) {
      Type tp = typeArguments.get(i);
      sb.append(genName(tp));
      if (i + 1 < typeArguments.size()) {
        sb.append("_");
      }
    }
    return sb.toString();
  }

  public static String classHeaderToString(ClassDeclaration c) {
    StringBuilder sb = new StringBuilder();
    sb.append(c.getIdentifier().getName());

    if (!c.getTypeParametersT().isEmpty()) {
      sb.append("_");
      sb.append(typeArgumentsToString(c.getTypeParametersT()));
    }

    return sb.toString();
  }

  public static String varToString(VarDeclarator var) {
    StringBuilder sb = new StringBuilder();
    sb.append(typeToString(var.getType()));
    sb.append(" ");
    sb.append(var.getIdentifier().getName());

    return sb.toString();
  }

  public static String parametersToString(List<VarDeclarator> parameters) {
    StringBuilder sb = new StringBuilder();
    sb.append("(");

    for (int i = 0; i < parameters.size(); i++) {
      VarDeclarator param = parameters.get(i);
      sb.append(varToString(param));

      if (i + 1 < parameters.size()) {
        sb.append(", ");
      }
    }

    sb.append(")");
    return sb.toString();
  }

  private static String classTypeRefString(ClassTypeRef ref) {
    StringBuilder sb = new StringBuilder();
    sb.append("struct ");
    sb.append(ref.getClazz().getIdentifier().getName());
    if (!ref.getTypeArguments().isEmpty()) {
      sb.append("_");
      sb.append(typeArgumentsToString(ref.getTypeArguments()));
    }
    sb.append("*");
    return sb.toString();
  }

  public static String typeToString(Type tp) {
    if (tp.isPrimitive()) {
      return TypeBindings.BIND_PRIMITIVE_TO_STRING.get(tp.getBase());
    }
    if (tp.isTypenameID()) {
      return tp.getTypenameId().getName();
    }
    if (tp.isVoid()) {
      return "void";
    }
    if (tp.isNullNoNameType()) {
      return "void *";
    }
    if (tp.isClass()) {
      return classTypeRefString(tp.getClassTypeRef());
    }
    return tp.getBase().toString();
  }

}
