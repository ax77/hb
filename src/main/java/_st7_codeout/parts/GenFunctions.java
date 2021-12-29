package _st7_codeout.parts;

import java.util.List;

import _st7_codeout.Ccode;
import _st7_codeout.Function;
import _st7_codeout.LinearizeMethods;
import _st7_codeout.ToStringsInternal;
import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;

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

      List<Function> functions = LinearizeMethods.flat(c);
      for (Function f : functions) {
        genMethod(f);
      }
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
