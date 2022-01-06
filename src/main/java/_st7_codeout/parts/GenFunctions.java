package _st7_codeout.parts;

import java.util.ArrayList;
import java.util.List;

import _st7_codeout.Ccode;
import _st7_codeout.Function;
import _st7_codeout.LinearizeMethods;
import _st7_codeout.ToStringsInternal;
import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_vars.VarDeclarator;

public class GenFunctions implements Ccode {

  // Gen all of the functions implementations
  // excluding string and arrays.

  private final List<ClassDeclaration> classes;
  private final StringBuilder proto;
  private final StringBuilder impls;

  public GenFunctions(List<ClassDeclaration> classes) {
    this.classes = classes;
    this.proto = new StringBuilder();
    this.impls = new StringBuilder();
    gen();
  }

  private void gen() {
    for (ClassDeclaration c : classes) {
      genProtos(c);

      if (c.isInterface()) {
        genInterfaceMethod(c);
      }

      else {

        List<Function> functions = LinearizeMethods.flat(c);
        for (Function f : functions) {
          genMethod(f);
        }
      }
    }

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

  private void genMethod(Function f) {

    final ClassMethodDeclaration method = f.getMethodSignature();
    final String methodType = ToStringsInternal.typeToString(method.getType());
    final String signToStringCall = ToStringsInternal.signToStringCall(method);
    final String methodCallsHeader = "static " + methodType + " " + signToStringCall
        + ToStringsInternal.parametersToString(method.getParameters()); //+ " {"

    if (f.getMethodSignature().getModifiers().isNative()) {
      String nativeMethod = GenNativeStdFunction.gen(f);
      impls.append(nativeMethod);
    }

    else {
      impls.append(f.toString());
    }

    proto.append(methodCallsHeader + ";\n");
  }

  private void genProtos(ClassDeclaration c) {
    // TODO Auto-generated method stub

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
