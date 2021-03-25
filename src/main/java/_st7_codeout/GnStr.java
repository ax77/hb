package _st7_codeout;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import errors.AstParseException;

public abstract class GnStr {

  private static StringBuilder sb = new StringBuilder();

  private static void line(String string) {
    sb.append(string);
    sb.append("\n");
  }

  public static String genStringStruct(ClassDeclaration c) {
    sb = new StringBuilder();

    line("struct " + c.headerToString() + "\n{");
    line("    char * buffer;");
    line("    size_t length;");
    line("};\n");

    return sb.toString();
  }

  public static String genStringMethod(Function func) {
    sb = new StringBuilder();

    final ClassMethodDeclaration method = func.getMethodSignature();

    final String methodType = method.getType().toString();
    final String signToStringCall = method.signToStringCall();
    final String methodCallsHeader = methodType + " " + signToStringCall + method.parametersToString() + " {";

    /// native string(string buffer);
    /// native int length();
    /// native char get(int index);

    if (signToStringCall.startsWith("string_init_")) {
      // void string_init_20_(struct string* __this, struct string* buffer)
      // void string_init_20_(struct string* __this, char         * buffer)
      line(methodType + " " + signToStringCall + "(struct string* __this, const char * const buffer)" + " {");
      line("    assert(__this);");
      line("    assert(buffer);");
      line("    __this->buffer = hstrdup(buffer);");
      line("    __this->length = strlen(buffer);");
      line("}\n");
    }

    else if (signToStringCall.startsWith("string_length_")) {
      line(methodCallsHeader);
      line("    assert(__this);");
      line("    assert(__this->buffer);");
      line("    return __this->length;");
      line("}\n");
    }

    else if (signToStringCall.startsWith("string_get_")) {
      line(methodCallsHeader);
      line("    assert(__this);");
      line("    assert(__this->buffer);");
      line("    assert(__this->length > 0);");
      line("    assert(index >= 0);");
      line("    assert(index < __this->length);");
      line("    return __this->buffer[index];");
      line("}\n");
    }

    else if (signToStringCall.startsWith("string_deinit_")) {
      line(methodCallsHeader);
      line("    assert(__this);");
      line("}\n");
    }

    else {
      throw new AstParseException("unimplemented string method: " + method.getIdentifier().toString());
    }

    return sb.toString();

  }

}
