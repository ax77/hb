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

  private final ClassDeclaration str;
  private final StringBuilder proto;
  private final StringBuilder impls;

  public GenBuiltinString(ClassDeclaration str) {
    if (!str.isNativeString()) {
      throw new AstParseException("expect str class, but was: " + str.toString());
    }

    this.str = str;
    this.proto = new StringBuilder();
    this.impls = new StringBuilder();
    gen();
  }

  private void gen() {

    genProtos(str);

    List<Function> functions = LinearizeMethods.flat(str);
    for (Function f : functions) {
      genMethod(f);
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

  private void genProtos(ClassDeclaration c) {

    lineP(GenCommentHeader.gen("str: " + ToStringsInternal.classHeaderToString(c)));
    lineP("struct " + ToStringsInternal.classHeaderToString(c) + "\n{");
    lineP("    char * buf;");
    lineP("    size_t len;");
    lineP("};\n");

  }

  private void genMethod(Function func) {

    final ClassMethodDeclaration method = func.getMethodSignature();
    final List<VarDeclarator> parameters = method.getParameters();

    final String methodType = ToStringsInternal.typeToString(method.getType());
    final String signToStringCall = ToStringsInternal.signToStringCall(method);
    final String methodCallsHeader = "static " + methodType + " " + signToStringCall
        + ToStringsInternal.parametersToString(method.getParameters()) + " {";

    lineP("static " + methodType + " " + signToStringCall + ToStringsInternal.parametersToString(method.getParameters())
        + ";");

    // TODO: optimize, because we calculate the length in strdup() and here too!
    if (signToStringCall.startsWith("str_init_")) {

      if (parameters.size() == 2) { // __this, buffer
        lineI(methodCallsHeader);
        lineI("    assert(__this);");
        lineI("    assert(buffer);");
        lineI("    assert(buffer->buf);");
        lineI("    __this->buf = hstrdup(buffer->buf);");
        lineI("    __this->len = strlen(buffer->buf);");
        lineI("}\n");
      }

      else {
        throw new AstParseException("unimplemented str constructor: " + method.getIdentifier().toString());
      }

    }

    else if (signToStringCall.startsWith("str_get_")) {
      lineI(methodCallsHeader);
      lineI("    assert(__this);");
      lineI("    assert(__this->buf);");
      lineI("    assert(index >= 0);");
      lineI("    assert(index < __this->len);");
      lineI("    return __this->buf[index];");
      lineI("}\n");
    }

    else if (signToStringCall.startsWith("str_len_")) {
      lineI(methodCallsHeader);
      lineI("    assert(__this);");
      lineI("    return __this->len;");
      lineI("}\n");
    }

    else if (signToStringCall.startsWith("str_deinit_")) {
      lineI(methodCallsHeader);
      lineI("    assert(__this);\n");

      lineI("    if(!is_alive(__this)) {                                    ");
      lineI("        return;                                                ");
      lineI("    }                                                          ");
      lineI("    set_deletion_bit(__this);                                  ");
      lineI("    if(is_alive(__this->buf)) {                                ");
      lineI("        set_deletion_bit(__this->buf);                         ");
      lineI("    }                                                          ");
      //lineI("    drop_ptr((void**)&__this->buf, char_default_empty_ptr);    ");

      lineI("}\n");
    }

    else if (signToStringCall.startsWith("str_equals_")) {
      lineI(methodCallsHeader);
      lineI("    assert(__this);");
      lineI("    assert(__this->buf);");
      lineI("    assert(another);");
      lineI("    assert(another->buf);");
      lineI("    if(__this->len != another->len) {");
      lineI("        return false;");
      lineI("    }");
      lineI("    return strcmp(__this->buf, another->buf) == 0;");
      lineI("}\n");
    }

    else {
      if (method.isTest()) {
        lineI(methodCallsHeader);
        lineI(func.getBlock().toString());
        lineI("}\n");
      }

      else {
        throw new AstParseException("unimplemented array method: " + method.getIdentifier().toString());
      }
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
