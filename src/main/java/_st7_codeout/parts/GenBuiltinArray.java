package _st7_codeout.parts;

import java.util.List;

import _st7_codeout.Ccode;
import _st7_codeout.Function;
import _st7_codeout.LinearizeMethods;
import _st7_codeout.ToStringsInternal;
import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_types.Type;
import ast_vars.VarDeclarator;
import errors.AstParseException;

public class GenBuiltinArray implements Ccode {

  private final List<ClassDeclaration> arrays;
  private final StringBuilder proto;
  private final StringBuilder impls;

  public GenBuiltinArray(List<ClassDeclaration> arrays) {
    for (ClassDeclaration c : arrays) {
      if (!c.isNativeArray()) {
        throw new AstParseException("expect array class, but was: " + c.toString());
      }
    }
    this.arrays = arrays;
    this.proto = new StringBuilder();
    this.impls = new StringBuilder();
    gen();
  }

  private void gen() {

    for (ClassDeclaration c : arrays) {
      genProtos(c);

      List<Function> functions = LinearizeMethods.flat(c);
      for (Function f : functions) {
        genMethod(f);
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

  private void genProtos(ClassDeclaration c) {

    final Type arrayOf = c.getTypeParametersT().get(0);
    final String arrayOfStr = ToStringsInternal.typeToString(arrayOf);

    lineP(GenCommentHeader.gen("array: " + ToStringsInternal.classHeaderToString(c)));
    lineP("struct " + ToStringsInternal.classHeaderToString(c) + "\n{");
    lineP("    " + arrayOfStr + "* data;");
    lineP("    size_t size;");
    lineP("    size_t alloc;");
    lineP("};\n");

  }

  private String genTableEmptifier(Type tp) {

    String varname = ToStringsInternal.defaultVarNameForType(tp);

    StringBuilder sb = new StringBuilder();
    sb.append("for (size_t i = 0; i < __this->alloc; i += 1) {\n");
    sb.append("  __this->data[i] = " + varname + ";\n");
    sb.append("}\n");

    return sb.toString();
  }

  private void genMethod(Function func) {

    final ClassMethodDeclaration method = func.getMethodSignature();
    final List<VarDeclarator> parameters = method.getParameters();
    final ClassDeclaration clazz = method.getClazz();

    Type arrayOf = clazz.getTypeParametersT().get(0);
    String arrayOfToString = ToStringsInternal.typeToString(arrayOf);

    final String methodType = ToStringsInternal.typeToString(method.getType());
    final String signToStringCall = ToStringsInternal.signToStringCall(method);
    final String methodCallsHeader = "static " + methodType + " " + signToStringCall
        + ToStringsInternal.parametersToString(method.getParameters()) + " {";
    final String sizeofElem = "sizeof(" + arrayOfToString + ")";
    final String sizeofMulAlloc = "(" + arrayOfToString + "*) hcalloc( 1u, (" + sizeofElem + " * __this->alloc) );";

    lineP("static " + methodType + " " + signToStringCall + ToStringsInternal.parametersToString(method.getParameters())
        + ";");

    if (signToStringCall.startsWith("arr_init_")) {

      if (parameters.size() == 1) {
        lineI(methodCallsHeader);
        lineI("    assert(__this);");
        lineI("    __this->size = 0;");
        lineI("    __this->alloc = 2;");
        lineI("    __this->data = " + sizeofMulAlloc);
        lineI(genTableEmptifier(arrayOf));
        lineI("}\n");
      }

      else {
        throw new AstParseException("unimplemented array constructor: " + method.getIdentifier().toString());
      }

    }

    else if (signToStringCall.startsWith("arr_add_")) {
      lineI(methodCallsHeader);

      lineI("    if(__this->size >= __this->alloc) {                ");
      lineI("        __this->alloc += 1;                            ");
      lineI("        __this->alloc *= 2;                            ");
      lineI("        " + arrayOfToString + "* ndata = " + sizeofMulAlloc);
      lineI("        for(size_t i = 0; i < __this->size; i += 1) {  ");
      lineI("          ndata[i] = __this->data[i];                  ");
      lineI("        }                                              ");
      lineI("        free(__this->data);                            ");
      lineI("        __this->data = ndata;                          ");
      lineI("    }                                                  ");
      lineI("    __this->data[__this->size] = element;              ");
      lineI("    __this->size += 1;                                 ");

      lineI("}\n");
    }

    else if (signToStringCall.startsWith("arr_get_")) {
      lineI(methodCallsHeader);
      lineI("    assert(__this);");
      lineI("    assert(__this->data);");
      lineI("    assert(__this->size > 0);");
      lineI("    assert(index >= 0);");
      lineI("    assert(index < __this->size);");
      lineI("    return __this->data[index];");
      lineI("}\n");
    }

    else if (signToStringCall.startsWith("arr_set_")) {
      lineI(methodCallsHeader);
      lineI("    assert(__this);");
      lineI("    assert(__this->data);");
      lineI("    assert(__this->size > 0);");
      lineI("    assert(index >= 0);");
      lineI("    assert(index < __this->size);");
      lineI("    " + arrayOfToString + " old =  __this->data[index];");
      lineI("    __this->data[index] = element;");
      lineI("    return old;");
      lineI("}\n");
    }

    else if (signToStringCall.startsWith("arr_size_")) {
      lineI(methodCallsHeader);
      lineI("    assert(__this);");
      lineI("    return __this->size;");
      lineI("}\n");
    }

    else if (signToStringCall.startsWith("arr_is_empty_")) {
      lineI(methodCallsHeader);
      lineI("    assert(__this);");
      lineI("    return (__this->size == 0);");
      lineI("}\n");
    }

    else if (signToStringCall.startsWith("arr_deinit_")) {
      lineI(methodCallsHeader);
      lineI("    assert(__this);\n");

      if (arrayOf.isClass()) {
        ClassDeclaration cd = arrayOf.getClassTypeFromRef();
        ClassMethodDeclaration meth = cd.getDestructor();
        String subTypeName = ToStringsInternal.typeToString(arrayOf);
        lineI("for( size_t i = 0; i < __this->size; i += 1 ) {");
        lineI("    " + subTypeName + " __element = __this->data[i];");
        lineI("    " + ToStringsInternal.signToStringCall(meth) + "(__element);");
        lineI("}");
      }

      lineI("    mark_ptr(__this);");
      lineI("    mark_ptr(__this->data);\n");

      lineI("}\n");
    }

    else if (signToStringCall.startsWith("arr_equals_")) {
      lineI(methodCallsHeader);
      lineI("    assert(__this);");
      lineI("    assert(__this->data);");
      lineI("    assert(another);");
      lineI("    assert(another->data);");

      lineI("    if(__this->size != another->size) {");
      lineI("        return false;");
      lineI("    }");

      if (arrayOf.isClass()) {
        ClassDeclaration cd = arrayOf.getClassTypeFromRef();
        ClassMethodDeclaration meth = cd.getMethodForSure("equals");
        String subTypeName = ToStringsInternal.typeToString(arrayOf);
        lineI("  for( size_t i = 0; i < __this->size; i += 1 ) {");
        lineI("      " + subTypeName + " __element_1 = __this->data[i];");
        lineI("      " + subTypeName + " __element_2 = another->data[i];");
        lineI("      if(!" + ToStringsInternal.signToStringCall(meth) + "(__element_1, __element_2)) {");
        lineI("          return false;");
        lineI("      }");
        lineI("  }");
      }

      else {
        if (arrayOf.isChar()) {
          lineI("if(strcmp(__this->data, another->data) != 0) {");
          lineI("    return false;");
          lineI("}");
        }

        else {
          String subTypeName = ToStringsInternal.typeToString(arrayOf);
          lineI("      " + subTypeName + " __element_1 = *(__this->data);");
          lineI("      " + subTypeName + " __element_2 = *(another->data);");

          lineI("if(__element_1 != __element_2) {");
          lineI("    return false;");
          lineI("}");
        }
      }

      lineI("    return true;");
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
