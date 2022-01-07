package _st7_codeout.parts;

import java.util.ArrayList;
import java.util.List;

import _st7_codeout.Ccode;
import _st7_codeout.ToStringsInternal;
import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_vars.VarDeclarator;
import errors.AstParseException;

public class GenInterfaces implements Ccode {

  private final StringBuilder proto;
  private final StringBuilder impls;

  public GenInterfaces(List<ClassDeclaration> interfaces) {
    this.proto = new StringBuilder();
    this.impls = new StringBuilder();

    for (ClassDeclaration c : interfaces) {
      genInterfaceMethod(c);
      genConstructorForEachImplementor(c);
      proto.append(genInterfaceBody(c));
    }
  }

  // markable_init_for_token(mstruct markable *__this, void *object) {
  //   __this->object = object;
  //   __this->mark = &token_mark;
  //   ...
  // }
  // markable_init_for_literal(mstruct markable *__this, void *object) {
  //   __this->object = object;
  //   __this->mark = &literal_mark;
  //   ...
  // }

  private void genConstructorForEachImplementor(ClassDeclaration interfaceClazz) {
    List<ClassDeclaration> implementors = interfaceClazz.getImplementations();

    StringBuilder result = new StringBuilder();

    for (ClassDeclaration realClazz : implementors) {

      final String hdrI = hdr(interfaceClazz);
      result.append("static void " + interfaceClazz.getIdentifier() + "_init_for_" + realClazz.getIdentifier() + "("
          + hdrI + " *__this, void *object)" + " {\n");

      StringBuilder funcPtrs = new StringBuilder();
      funcPtrs.append("assert(__this);\n");
      funcPtrs.append("assert(object);\n");

      funcPtrs.append("__this->object = object;\n");

      //TODO: cast expression for function-pointer
      final List<ClassMethodDeclaration> interfaceMethods = interfaceClazz.getMethods();
      for (ClassMethodDeclaration m : interfaceMethods) {
        oneFuncPtrInitializer("__this", funcPtrs, realClazz, m);
      }
      if (interfaceClazz.getDestructor() != null) {
        oneFuncPtrInitializer("__this", funcPtrs, realClazz, interfaceClazz.getDestructor());
      }
      result.append(funcPtrs);
      result.append("}\n\n");
    }

    impls.append(result);
  }

  private String hdr(ClassDeclaration c) {
    StringBuilder sb = new StringBuilder();
    sb.append("struct ");
    sb.append(c.getIdentifier().getName());

    if (!c.getTypeParametersT().isEmpty()) {
      sb.append("_");
      sb.append(ToStringsInternal.typeArgumentsToString(c.getTypeParametersT()));
    }
    return sb.toString();
  }

  /// static void markable_init_for_token(struct markable *__this, void *object) {
  ///   assert(__this);
  ///   assert(object);
  ///   __this->object = object;
  ///   __this->mark = (void (*)(void*)) &token_mark_30;
  ///   __this->unmark = (void (*)(void*)) &token_unmark_33;
  ///   __this->equals = (int (*)(void*, struct markable*)) &token_equals_61;
  ///   __this->deinit = (void (*)(void*)) &token_deinit_63;
  /// }

  private void oneFuncPtrInitializer(final String lvalueVarName, final StringBuilder funcPtrs,
      final ClassDeclaration realClazz, ClassMethodDeclaration m) {
    final String methodName = m.getIdentifier().getName();

    ClassMethodDeclaration method = null;
    if (m.isDestructor()) {
      method = realClazz.getDestructor();
    } else {
      method = realClazz.getMethodForSure(methodName);
    }

    final String implMethodFullname = ToStringsInternal.getMethodName(method);

    List<VarDeclarator> params = new ArrayList<>(m.getParameters());
    params.remove(0);
    String paramsToStr = parametersToString(params);
    if (paramsToStr.isEmpty()) {
      paramsToStr = "(void*)";
    } else {
      paramsToStr = "(void*, " + paramsToStr + ")";
    }
    paramsToStr = "(" + ToStringsInternal.typeToString(m.getType()) + " (*) " + paramsToStr + ")";

    funcPtrs.append(lvalueVarName + "->" + methodName + " = " + paramsToStr + " &" + implMethodFullname + ";\n");
  }

  private static String parametersToString(List<VarDeclarator> parameters) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < parameters.size(); i++) {
      VarDeclarator param = parameters.get(i);
      sb.append(ToStringsInternal.typeToString(param.getType()));

      if (i + 1 < parameters.size()) {
        sb.append(", ");
      }
    }

    return sb.toString();
  }

  //xxxxx
  /// interface
  /// struct markable {
  ///     /// Is an actual pointer to an implementor.
  ///     /// I.e. a real class, which implements this interface,
  ///     /// and 'hidden' under this skin right now.
  ///     ///
  ///     void *object;
  /// 
  ///     /// This is an interface method.
  ///     /// Note: the parameter '__this' is a pointer to an
  ///     /// implementor, and NOT a pointer to this very structure.
  ///     /// Here's a difference you should know.
  ///     /// It's just a pointer to a real method from a real class.
  ///     /// The method from an interface is defined below,
  ///     /// and its signature is different.
  ///     ///
  ///     int (*mark)(void *__this);
  ///     void (*flush)(void *__this, int newvalue);
  /// };
  ///
  /// NOTE: it's quite possible to add a union
  /// with all the structures that implemented this interface.
  /// And then by using a flag choose the right method to invoke.
  /// But: it will be 'if' statement then, and a hidden control flow,
  /// and it's not a good idea.
  /// The second thought: it's to use a _Generic from C11, but:
  /// it works statically, think about this.
  ///
  /// So: we have to use a pointer to void in this case,
  /// while I'm not prepared to implement a better design.
  /// It's not so bad, actually.
  /// It's a first place when we really need a void*.
  ///
  /// int markable_mark(struct markable *__this) {
  ///     assert(__this);
  ///     assert(__this->object);
  ///     assert(__this->mark);
  ///     return __this->mark(__this->object);
  /// }
  /// 
  /// void markable_flush(struct markable *__this, int newvalue) {
  ///     assert(__this);
  ///     assert(__this->object);
  ///     assert(__this->flush);
  ///     __this->flush(__this->object, newvalue);
  /// }

  public static String genInterfaceBody(ClassDeclaration c) {
    StringBuilder sb = new StringBuilder();
    sb.append("struct ");
    sb.append(c.getIdentifier().getName());

    if (!c.getTypeParametersT().isEmpty()) {
      sb.append("_");
      sb.append(ToStringsInternal.typeArgumentsToString(c.getTypeParametersT()));
    }

    sb.append("\n{\n");
    sb.append("  void *object;\n");

    for (ClassMethodDeclaration method : c.getMethods()) {
      oneFuncPtrToString(c, sb, method);
    }

    if (c.getDestructor() != null) {
      oneFuncPtrToString(c, sb, c.getDestructor());
    }

    sb.append("\n};\n");
    return sb.toString();
  }

  private static void oneFuncPtrToString(ClassDeclaration c, StringBuilder sb, ClassMethodDeclaration method) {
    String type = ToStringsInternal.typeToString(method.getType());
    String name = "(*" + method.getIdentifier().getName() + ")";

    final List<VarDeclarator> parameters = new ArrayList<>(method.getParameters()); //XXX: copy, do not modify original!
    if (parameters.isEmpty()) {
      throw new AstParseException("empty parameters for interface: " + c.toString());
    }
    parameters.remove(0); // __this

    String params = ToStringsInternal.parametersToString(parameters);
    if (params.startsWith("(") && params.endsWith(")")) {
      String sub = params.substring(1, params.length() - 1);
      String comma = sub.trim().isEmpty() ? "" : ", ";
      params = "(void *__this" + comma + sub + ")";
    } else {
      throw new AstParseException("empty parameters for interface: " + params);
    }

    sb.append(type + " " + name + params + ";\n");
  }

  //xxxxx
  /// interface
  /// struct markable {
  ///     /// Is an actual pointer to an implementor.
  ///     /// I.e. a real class, which implements this interface,
  ///     /// and 'hidden' under this skin right now.
  ///     ///
  ///     void *object;
  /// 
  ///     /// This is an interface method.
  ///     /// Note: the parameter '__this' is a pointer to an
  ///     /// implementor, and NOT a pointer to this very structure.
  ///     /// Here's a difference you should know.
  ///     /// It's just a pointer to a real method from a real class.
  ///     /// The method from an interface is defined below,
  ///     /// and its signature is different.
  ///     ///
  ///     int (*mark)(void *__this);
  ///     void (*flush)(void *__this, int newvalue);
  /// };
  ///
  /// NOTE: it's quite possible to add a union
  /// with all the structures that implemented this interface.
  /// And then by using a flag choose the right method to invoke.
  /// But: it will be 'if' statement then, and a hidden control flow,
  /// and it's not a good idea.
  /// The second thought: it's to use a _Generic from C11, but:
  /// it works statically, think about this.
  ///
  /// So: we have to use a pointer to void in this case,
  /// while I'm not prepared to implement a better design.
  /// It's not so bad, actually.
  /// It's a first place when we really need a void*.
  ///
  /// int markable_mark(struct markable *__this) {
  ///     assert(__this);
  ///     assert(__this->object);
  ///     assert(__this->mark);
  ///     return __this->mark(__this->object);
  /// }
  /// 
  /// void markable_flush(struct markable *__this, int newvalue) {
  ///     assert(__this);
  ///     assert(__this->object);
  ///     assert(__this->flush);
  ///     __this->flush(__this->object, newvalue);
  /// }

  private void genInterfaceMethod(ClassDeclaration c) {

    for (ClassMethodDeclaration method : c.getMethods()) {
      final String methodType = ToStringsInternal.typeToString(method.getType());
      final String signToStringCall = ToStringsInternal.signToStringCall(method);
      final List<VarDeclarator> parameters = method.getParameters();
      final String parametersToString = ToStringsInternal.parametersToString(parameters);
      final String methodCallsHeader = "static " + methodType + " " + signToStringCall + parametersToString;
      final String methodCleanName = method.getIdentifier().getName();

      List<VarDeclarator> paramsCopy = new ArrayList<>(parameters);
      paramsCopy.remove(0); // __this

      StringBuilder paramNames = new StringBuilder();
      for (int i = 0; i < paramsCopy.size(); i += 1) {
        VarDeclarator var = paramsCopy.get(i);
        String comma = (i + 1 < paramsCopy.size()) ? ", " : "";
        paramNames.append(var.getIdentifier().getName() + comma);
      }

      StringBuilder func = new StringBuilder();
      func.append(methodCallsHeader + " {\n");
      func.append("assert(__this);\n");
      func.append("assert(__this->object);\n");

      func.append("assert(__this->" + methodCleanName + ");\n");

      if (!method.getType().isVoid()) {
        func.append("return ");
      }

      String comma = (paramNames.toString().trim().isEmpty()) ? "" : ", ";
      func.append("__this->" + methodCleanName + "(__this->object" + comma + paramNames + ");\n");
      func.append("}\n\n");

      impls.append(func.toString());
      proto.append(methodCallsHeader + ";\n");
    }

    if (c.getDestructor() != null) {
      ClassMethodDeclaration method = c.getDestructor();
      final String methodType = ToStringsInternal.typeToString(method.getType());
      final String signToStringCall = ToStringsInternal.signToStringCall(method);
      final List<VarDeclarator> parameters = method.getParameters();
      final String parametersToString = ToStringsInternal.parametersToString(parameters);
      final String methodCallsHeader = "static " + methodType + " " + signToStringCall + parametersToString;
      final String methodCleanName = method.getIdentifier().getName();

      impls.append(methodCallsHeader + " {  }\n");
      proto.append(methodCallsHeader + ";\n");
    }
  }

  @Override
  public String getProto() {
    return proto.toString();
  }

  @Override
  public String getImpls() {
    return impls.toString();
  }

}
