package _st7_codeout.parts;

import java.util.List;

import _st7_codeout.Ccode;
import _st7_codeout.Function;
import _st7_codeout.LinearizeMethods;
import _st7_codeout.ToStringsInternal;
import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_vars.VarDeclarator;
import errors.AstParseException;

public class GenBuiltinString implements Ccode {

  private final ClassDeclaration stringClazz;
  private final StringBuilder proto;
  private final StringBuilder impls;

  public GenBuiltinString(ClassDeclaration stringClazz) {
    if (!stringClazz.isNativeString()) {
      throw new AstParseException("expect string class, but was: " + stringClazz.toString());
    }
    this.stringClazz = stringClazz;
    this.proto = new StringBuilder();
    this.impls = new StringBuilder();
    gen();
  }

  private void gen() {
    List<Function> functions = LinearizeMethods.flat(stringClazz);

    genProtos();

    for (Function f : functions) {

      final ClassMethodDeclaration method = f.getMethodSignature();
      final String methodType = ToStringsInternal.typeToString(method.getType());
      final String signToStringCall = ToStringsInternal.signToStringCall(method);
      final String methodCallsHeader = "static " + methodType + " " + signToStringCall
          + ToStringsInternal.parametersToString(method.getParameters()) + " {";

      //@formatter:off
      boolean isNativeMethod = 
             signToStringCall.startsWith("string_init_") 
          || signToStringCall.startsWith("string_length_")
          || signToStringCall.startsWith("string_get_")
          || signToStringCall.startsWith("string_deinit_")
          || signToStringCall.startsWith("string_equals_")
          ;
      //@formatter:on

      if (isNativeMethod) {
        genMethod(f);
      }

      else if (method.isTest()) {
        lineI(methodCallsHeader);
        lineI(f.getBlock().toString());
        lineI("}\n");
      }

      else {
        impls.append(f.toString());
      }

      if (method.isConstructor()) {
        lineP("static " + ToStringsInternal.typeToString(method.getType()) + " "
            + ToStringsInternal.signToStringCall(method) + ToStringsInternal.parametersToString(method.getParameters())
            + ";");
      } else {
        lineP("static " + methodType + " " + signToStringCall
            + ToStringsInternal.parametersToString(method.getParameters()) + ";");
      }
    }
  }

  private void lineI(String string) {
    impls.append(string);
    impls.append("\n");
  }

  private void lineP(String string) {
    proto.append(string);
    proto.append("\n");
  }

  private void genProtos() {

    lineP("struct " + ToStringsInternal.classHeaderToString(stringClazz) + "\n{");
    lineP("    char * buffer;");
    lineP("    size_t length;");
    lineP("};\n");

  }

  private void genMethod(Function func) {

    final ClassMethodDeclaration method = func.getMethodSignature();

    final String methodType = ToStringsInternal.typeToString(method.getType());
    final String signToStringCall = ToStringsInternal.signToStringCall(method);
    final String methodCallsHeader = "static " + methodType + " " + signToStringCall
        + ToStringsInternal.parametersToString(method.getParameters()) + " {";

    /// native string(string buffer);
    /// native int length();
    /// native char get(int index);

    if (signToStringCall.startsWith("string_init_")) {
      // void string_init_20_(struct string* __this, struct string* buffer)
      // void string_init_20_(struct string* __this, char         * buffer)

      final List<VarDeclarator> parameters = method.getParameters();

      if (parameters.get(1).getType().isString()) {
        lineI("static " + methodType + " " + signToStringCall
            + ToStringsInternal.parametersToString(method.getParameters()) + " {");
        lineI("    assert(__this);");
        lineI("    assert(buffer);");
        lineI("    assert(buffer->buffer);\n");
        lineI("    __this->buffer = hstrdup(buffer->buffer);");
        lineI("    __this->length = buffer->length;");
        lineI("}\n");
      }

      else if (parameters.get(1).getType().isCharArray()) {
        lineI("static " + methodType + " " + signToStringCall
            + ToStringsInternal.parametersToString(method.getParameters()) + " {");
        lineI("    assert(__this);");
        lineI("    assert(buffer);");
        lineI("    assert(buffer->data);\n");
        lineI("    __this->buffer = hstrdup(buffer->data);");
        lineI("    __this->length = buffer->size;");
        lineI("}\n");
      }

      else {
        throw new AstParseException("unimplemented string constructor: " + method.getIdentifier().toString());
      }
    }

    else if (signToStringCall.startsWith("string_length_")) {
      lineI(methodCallsHeader);
      lineI("    assert(__this);");
      lineI("    assert(__this->buffer);\n");
      lineI("    return __this->length;");
      lineI("}\n");
    }

    else if (signToStringCall.startsWith("string_get_")) {
      lineI(methodCallsHeader);
      lineI("    assert(__this);");
      lineI("    assert(__this->buffer);");
      lineI("    assert(__this->length > 0);");
      lineI("    assert(index >= 0);");
      lineI("    assert(index < __this->length);\n");
      lineI("    return __this->buffer[index];");
      lineI("}\n");
    }

    else if (signToStringCall.startsWith("string_deinit_")) {
      lineI(methodCallsHeader);
      lineI("    assert(__this);");
      lineI("}\n");
    }

    else if (signToStringCall.startsWith("string_equals_")) {
      lineI(methodCallsHeader);
      lineI("    assert(__this);");
      lineI("    assert(__this->buffer);");
      lineI("    assert(another);");
      lineI("    assert(another->buffer);\n");
      lineI("    if(__this->length != another->length) {");
      lineI("        return 0;");
      lineI("    }");
      lineI("    return strcmp(__this->buffer, another->buffer) == 0;");
      lineI("}\n");
    }

    else {
      throw new AstParseException("unimplemented string method: " + method.getIdentifier().toString());
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
