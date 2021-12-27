package _st7_codeout;

import java.util.List;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_types.Type;
import ast_vars.VarDeclarator;
import errors.AstParseException;

public abstract class GnArray {

  private static StringBuilder sb = new StringBuilder();

  private static void line(String string) {
    sb.append(string);
    sb.append("\n");
  }

  public static String genArrayStruct(ClassDeclaration c) {
    sb = new StringBuilder();

    Type arrayOf = c.getTypeParametersT().get(0);

    line("struct " + ToStringsInternal.classHeaderToString(c) + "\n{");
    line("    " + ToStringsInternal.typeToString(arrayOf) + "* data;");
    line("    size_t size;");
    line("    size_t alloc;");
    line("};\n");

    return sb.toString();
  }

  public static String genArrayMethod(Function func) {
    sb = new StringBuilder();

    final ClassMethodDeclaration method = func.getMethodSignature();
    final List<VarDeclarator> parameters = method.getParameters();
    final ClassDeclaration clazz = method.getClazz();

    Type arrayOf = clazz.getTypeParametersT().get(0);
    String arrayOfToString = ToStringsInternal.typeToString(arrayOf);

    //
    final String methodType = ToStringsInternal.typeToString(method.getType());
    final String signToStringCall = ToStringsInternal.signToStringCall(method);
    final String methodCallsHeader = methodType + " " + signToStringCall
        + ToStringsInternal.parametersToString(method.getParameters()) + " {";
    final String sizeofElem = "sizeof(" + arrayOfToString + ")";
    final String sizeofMulAlloc = "(" + arrayOfToString + "*) hcalloc( 1u, (" + sizeofElem + " * __this->alloc) );";

    /// native array(int size);
    /// native T get(int index);
    /// native T set(int index, T element);
    /// native int size();

    if (signToStringCall.startsWith("array_init_")) {

      /// native array();         /// resizable
      /// native array(int size); /// fixed

      /// dynamic
      if (parameters.size() == 1) {
        line(methodCallsHeader);
        line("    assert(__this);");
        line("    __this->size = 0;");
        line("    __this->alloc = 2;");
        line("    __this->data = " + sizeofMulAlloc);
        line("}\n");
      }

      /// fixed
      else if (parameters.size() == 2 && parameters.get(1).getType().isInt()) {
        line(methodCallsHeader);
        line("    assert(__this);");
        line("    assert(size > 0);");
        line("    assert(size < INT_MAX);");

        line("    __this->size = 0;");
        line("    __this->alloc = size;");
        line("    __this->data = " + sizeofMulAlloc);
        line("}\n");
      }

      else {
        throw new AstParseException("unimplemented array constructor: " + method.getIdentifier().toString());
      }

    }

    else if (signToStringCall.startsWith("array_add_")) {
      line(methodCallsHeader);

      line("    if(__this->size >= __this->alloc) {                ");
      line("        __this->alloc += 1;                            ");
      line("        __this->alloc *= 2;                            ");
      line("        " + arrayOfToString + "* ndata = " + sizeofMulAlloc);
      line("        for(size_t i = 0; i < __this->size; i += 1) {  ");
      line("          ndata[i] = __this->data[i];                  ");
      line("        }                                              ");
      line("        free(__this->data);                            ");
      line("        __this->data = ndata;                          ");
      line("    }                                                  ");
      line("    __this->data[__this->size] = element;              ");
      line("    __this->size += 1;                                 ");

      line("}\n");
    }

    else if (signToStringCall.startsWith("array_get_")) {
      line(methodCallsHeader);
      line("    assert(__this);");
      line("    assert(__this->data);");
      line("    assert(__this->size > 0);");
      line("    assert(index >= 0);");
      line("    assert(index < __this->size);");
      line("    return __this->data[index];");
      line("}\n");
    }

    else if (signToStringCall.startsWith("array_set_")) {
      line(methodCallsHeader);
      line("    assert(__this);");
      line("    assert(__this->data);");
      line("    assert(index >= 0);");
      line("    assert(index < __this->size);");
      line("    " + arrayOfToString + " old =  __this->data[index];");
      line("    __this->data[index] = element;");
      line("    return old;");
      line("}\n");
    }

    else if (signToStringCall.startsWith("array_size_")) {
      line(methodCallsHeader);
      line("    assert(__this);");
      line("    return __this->size;");
      line("}\n");
    }

    else if (signToStringCall.startsWith("array_is_empty_")) {
      line(methodCallsHeader);
      line("    assert(__this);");
      line("    return (__this->size == 0);");
      line("}\n");
    }

    else if (signToStringCall.startsWith("array_deinit_")) {
      line(methodCallsHeader);
      line("    assert(__this);");
      line("}\n");
    }

    else if (signToStringCall.startsWith("array_equals_")) {
      line(methodCallsHeader);
      line("    assert(__this);");
      line("    return false;");
      line("}\n");
    }

    else {
      if (method.isTest()) {
        line(methodCallsHeader);
        line(func.getBlock().toString());
        line("}\n");
      }

      else {
        throw new AstParseException("unimplemented array method: " + method.getIdentifier().toString());
      }
    }

    return sb.toString();

  }

}
