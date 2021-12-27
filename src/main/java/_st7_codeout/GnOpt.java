package _st7_codeout;

import java.util.List;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_types.Type;
import ast_vars.VarDeclarator;
import errors.AstParseException;

public abstract class GnOpt {

  private static StringBuilder sb = new StringBuilder();

  private static void line(String string) {
    sb.append(string);
    sb.append("\n");
  }

  public static String genOptStruct(ClassDeclaration c) {
    sb = new StringBuilder();

    Type arrayOf = c.getTypeParametersT().get(0);

    line("struct " + ToStringsInternal.classHeaderToString(c) + "\n{");
    line("    " + ToStringsInternal.typeToString(arrayOf) + " value;");
    line("};\n");

    return sb.toString();
  }

  public static String genOptMethod(Function func) {
    sb = new StringBuilder();

    final ClassMethodDeclaration method = func.getMethodSignature();
    final List<VarDeclarator> parameters = method.getParameters();
    final ClassDeclaration clazz = method.getClazz();

    Type arrayOf = clazz.getTypeParametersT().get(0);
    String arrayOfToString = ToStringsInternal.typeToString(arrayOf);

    final String methodType = ToStringsInternal.typeToString(method.getType());
    final String signToStringCall = ToStringsInternal.signToStringCall(method);
    final String methodCallsHeader = methodType + " " + signToStringCall
        + ToStringsInternal.parametersToString(method.getParameters()) + " {";
    final String castZero = "((" + arrayOfToString + ") 0)";

    /// native opt();
    /// native opt(T value);
    /// native boolean is_some();
    /// native boolean is_none();
    /// native T get();

    if (signToStringCall.startsWith("opt_init_")) {

      /// empty
      if (parameters.size() == 1) {
        line(methodCallsHeader);
        line("    assert(__this);");
        line("    __this->value = " + castZero + ";");
        line("}\n");
      }

      /// fixed
      else if (parameters.size() == 2) {
        line(methodCallsHeader);
        line("    assert(__this);");
        line("    assert(value);");
        line("    __this->value = value;");
        line("}\n");
      }

      else {
        throw new AstParseException("unimplemented opt constructor: " + method.getIdentifier().toString());
      }

    }

    else if (signToStringCall.startsWith("opt_is_some_")) {
      line(methodCallsHeader);
      line("    assert(__this);");
      line("    return __this->value != " + castZero + ";");
      line("}\n");
    }

    else if (signToStringCall.startsWith("opt_is_none_")) {
      line(methodCallsHeader);
      line("    assert(__this);");
      line("    return __this->value == " + castZero + ";");
      line("}\n");
    }

    else if (signToStringCall.startsWith("opt_get_")) {
      line(methodCallsHeader);
      line("    assert(__this);");
      line("    assert(__this->value);");
      line("    return __this->value;");
      line("}\n");
    }

    else if (signToStringCall.startsWith("opt_deinit_")) {
      line(methodCallsHeader);
      line("    assert(__this);");
      line("}\n");
    }

    else if (signToStringCall.startsWith("opt_equals_")) {
      line(methodCallsHeader);
      line("    assert(__this);");
      line("}\n");
    }

    else {
      throw new AstParseException("unimplemented opt method: " + method.getIdentifier().toString());
    }

    return sb.toString();

  }

}
