package _st7_codeout;

import java.beans.MethodDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import _st3_linearize_expr.BuiltinsFnSet;
import _st3_linearize_expr.CEscaper;
import _st3_linearize_expr.leaves.Var;
import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_printers.TypePrinters;
import ast_symtab.BuiltinNames;
import ast_types.Type;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import tokenize.Ident;

public class Codeout {
  private final List<ClassDeclaration> pods;
  private final List<Function> functions;

  private final Set<String> generatedBuiltinNames;
  private final StringBuilder builtinsFn;
  private final StringBuilder builtinsArrays;

  private final StringBuilder builtinsTypedefs;
  private final StringBuilder stringsLabels;

  public Codeout() {
    this.pods = new ArrayList<>();
    this.functions = new ArrayList<>();
    this.generatedBuiltinNames = new HashSet<>();
    this.builtinsFn = new StringBuilder();
    this.builtinsArrays = new StringBuilder();
    this.stringsLabels = new StringBuilder();
    this.builtinsTypedefs = new StringBuilder();
  }

  public void add(ClassDeclaration e) {
    this.pods.add(e);
  }

  public void add(Function e) {
    this.functions.add(e);
  }

  public List<ClassDeclaration> getPods() {
    return pods;
  }

  public List<Function> getFunctions() {
    return functions;
  }

  public String classToString(ClassDeclaration c) {
    StringBuilder sb = new StringBuilder();
    sb.append("struct ");
    sb.append(c.getIdentifier().getName());

    if (!c.getTypeParametersT().isEmpty()) {
      sb.append("_");
      sb.append(TypePrinters.typeArgumentsToString(c.getTypeParametersT()));
    }

    sb.append("\n{\n");

    for (VarDeclarator var : c.getFields()) {
      sb.append(var.toString() + "\n");
    }

    sb.append("\n};\n");
    return sb.toString();
  }

  private String includes() {
    return GenRT.prebuf();
  }

  private void lineBuiltin(String s) {
    builtinsFn.append(s);
    builtinsFn.append("\n");
  }

  private void lineBuiltinArr(String s) {
    builtinsArrays.append(s);
    builtinsArrays.append("\n");
  }

  private void genBuiltins() {

    Map<String, Var> strings = BuiltinsFnSet.getStringsmap();

    for (Entry<String, Var> ent : strings.entrySet()) {
      String s = ent.getKey();
      Var v = ent.getValue();
      int[] esc = CEscaper.escape(s);
      StringBuilder content = new StringBuilder();
      for (int j = 0; j < esc.length; j += 1) {
        content.append("'" + CEscaper.unesc((char) esc[j]) + "'");
        if (j + 1 < esc.length) {
          content.append(", ");
        }
      }
      String initBuffer = content.toString();
      stringsLabels.append("static const char " + v.getName().getName() + "[] = { " + initBuffer + "};\n");
    }
  }

  private void genMainFile(StringBuilder sb) throws IOException {
    final String fileName = "main.c";
    FileWriter fw = new FileWriter(fileName);
    fw.write(sb.toString());
    fw.close();
  }

  @Override
  public String toString() {

    List<Function> mainMethodOut = new ArrayList<>();

    String staticFields = genStaticFields();
    String structTypedefs = genStructsForwards();
    String funcProtos = genFuncProtos();
    String structsImpls = genStructs();
    String functions = genFunctions(mainMethodOut);

    StringBuilder genTypesFile = new StringBuilder();
    genTypesFile.append(structTypedefs);
    genTypesFile.append("\n");
    genTypesFile.append(funcProtos);
    genTypesFile.append("\n");
    genTypesFile.append(structsImpls);
    genTypesFile.append("\n");

    if (mainMethodOut.size() != 1) {
      throw new AstParseException("there is no main...");
    }

    final Function mainFn = mainMethodOut.get(0);
    String mainMethodImpl = mainFn.toString();
    String mainMethodCall = mainFn.signToStringCall();

    genBuiltins();

    StringBuilder sb = new StringBuilder();

    // protos
    sb.append(includes());
    sb.append(staticFields.toString());
    sb.append(builtinsTypedefs.toString());
    sb.append(genTypesFile.toString());
    sb.append(CCMacro.genMacro());
    sb.append(stringsLabels.toString());
    sb.append(builtinsArrays.toString());
    sb.append("\n");
    sb.append("\n");

    // impls
    sb.append(builtinsFn.toString());
    sb.append(functions);
    sb.append("\n");

    // main
    sb.append(mainMethodImpl);
    sb.append("int main(int argc, char** argv) \n{\n");
    sb.append("    int result = " + mainMethodCall + ";\n\n");
    sb.append("    printf(\"%d\\n\", result);\n");
    sb.append("    return result;\n");
    sb.append("\n}\n");

    try {
      genMainFile(sb);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return sb.toString();
  }

  ///TODO:static_semantic
  private String genStaticFields() {
    StringBuilder sb = new StringBuilder();

    for (ClassDeclaration c : pods) {
      if (c.isMainClass()) {
        continue;
      }
      if (!c.isStaticClass()) {
        continue;
      }
      if (c.getFields().isEmpty()) {
        continue;
      }
      for (VarDeclarator field : c.getFields()) {
        StringBuilder varname = new StringBuilder();
        varname.append(c.getIdentifier().toString());
        varname.append("_");
        varname.append(field.getIdentifier().toString());

        sb.append("static const " + field.getType().toString() + " " + varname + " = "
            + field.getSimpleInitializer().toString() + ";\n");
      }
    }

    return sb.toString();
  }

  private String genFunctions(List<Function> mainMethod) {
    StringBuilder sb = new StringBuilder();

    for (Function f : functions) {
      final ClassDeclaration c = f.getMethodSignature().getClazz();
      if (c.isMainClass()) {
        if (!f.getMethodSignature().isMain()) {
          continue;
        }
      }
      if (f.getMethodSignature().isMain()) {
        mainMethod.add(f);
        continue;
      }

      if (f.getMethodSignature().getClazz().isNativeArray()) {
        genArrayMethod(f);
        continue;
      }
      if (f.getMethodSignature().getClazz().isNativeOpt()) {
        genOptMethod(f);
        continue;
      }

      if (f.getMethodSignature().getModifiers().isNative()) {
        genNativeMethod(f);
        continue;
      }

      sb.append(f.toString());
      sb.append("\n");
    }

    return sb.toString();
  }

  /// opt

  private void genOptStruct(ClassDeclaration c) {
    Type arrayOf = c.getTypeParametersT().get(0);

    lineBuiltinArr("struct " + c.headerToString() + "\n{");
    lineBuiltinArr("    " + arrayOf.toString() + " value;");
    lineBuiltinArr("};\n");

  }

  private void genOptMethod(Function func) {
    final ClassMethodDeclaration method = func.getMethodSignature();
    final Ident name = method.getIdentifier();
    final List<VarDeclarator> parameters = method.getParameters();
    final List<VarDeclarator> params = parameters;
    final ClassDeclaration clazz = method.getClazz();

    Type arrayOf = clazz.getTypeParametersT().get(0);
    String arrayOfToString = arrayOf.toString();

    //
    final String methodType = method.getType().toString();
    final String signToStringCall = method.signToStringCall();
    final String methodCallsHeader = methodType + " " + signToStringCall + method.parametersToString() + " {";
    final String sizeofElem = "sizeof(" + arrayOfToString + ")";
    final String sizeofMulAlloc = "(" + arrayOfToString + "*) hcalloc( 1u, (" + sizeofElem + " * __this->alloc) );";
    final String castZero = "((" + arrayOfToString + ") 0)";

    /// native opt();
    /// native opt(T value);
    /// native boolean is_some();
    /// native boolean is_none();
    /// native T get();

    if (signToStringCall.startsWith("opt_init_")) {

      /// empty
      if (parameters.size() == 1) {
        lineBuiltinArr(methodCallsHeader);
        lineBuiltinArr("    assert(__this);");
        lineBuiltinArr("    __this->value = " + castZero + ";");
        lineBuiltinArr("}\n");
      }

      /// fixed
      else if (parameters.size() == 2) {
        lineBuiltinArr(methodCallsHeader);
        lineBuiltinArr("    assert(__this);");
        lineBuiltinArr("    assert(value);");
        lineBuiltinArr("    __this->value = value;");
        lineBuiltinArr("}\n");
      }

      else {
        throw new AstParseException("unimplemented opt constructor: " + method.getIdentifier().toString());
      }

    }

    else if (signToStringCall.startsWith("opt_is_some_")) {
      lineBuiltinArr(methodCallsHeader);
      lineBuiltinArr("    assert(__this);");
      lineBuiltinArr("    return __this->value != " + castZero + ";");
      lineBuiltinArr("}\n");
    }

    else if (signToStringCall.startsWith("opt_is_none_")) {
      lineBuiltinArr(methodCallsHeader);
      lineBuiltinArr("    assert(__this);");
      lineBuiltinArr("    return __this->value == " + castZero + ";");
      lineBuiltinArr("}\n");
    }

    else if (signToStringCall.startsWith("opt_get_")) {
      lineBuiltinArr(methodCallsHeader);
      lineBuiltinArr("    assert(__this);");
      lineBuiltinArr("    assert(__this->value);");
      lineBuiltinArr("    return __this->value;");
      lineBuiltinArr("}\n");
    }

    else {
      throw new AstParseException("unimplemented array method: " + method.getIdentifier().toString());
    }

  }

  /// arrays

  private void genArrayStruct(ClassDeclaration c) {
    Type arrayOf = c.getTypeParametersT().get(0);

    lineBuiltinArr("struct " + c.headerToString() + "\n{");
    lineBuiltinArr("    " + arrayOf.toString() + "* data;");
    lineBuiltinArr("    size_t size;");
    lineBuiltinArr("    size_t alloc;");
    lineBuiltinArr("    int is_fixed;");
    lineBuiltinArr("};\n");
  }

  private void genArrayMethod(Function func) {
    final ClassMethodDeclaration method = func.getMethodSignature();
    final Ident name = method.getIdentifier();
    final List<VarDeclarator> parameters = method.getParameters();
    final List<VarDeclarator> params = parameters;
    final ClassDeclaration clazz = method.getClazz();

    Type arrayOf = clazz.getTypeParametersT().get(0);
    String arrayOfToString = arrayOf.toString();

    //
    final String methodType = method.getType().toString();
    final String signToStringCall = method.signToStringCall();
    final String methodCallsHeader = methodType + " " + signToStringCall + method.parametersToString() + " {";
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
        lineBuiltinArr(methodCallsHeader);
        lineBuiltinArr("    assert(__this);");
        lineBuiltinArr("    __this->size = 0;");
        lineBuiltinArr("    __this->alloc = 2;");
        lineBuiltinArr("    __this->is_fixed = 0;");
        lineBuiltinArr("    __this->data = " + sizeofMulAlloc);
        lineBuiltinArr("}\n");
      }

      /// fixed
      else if (parameters.size() == 2 && parameters.get(1).getType().isInt()) {
        lineBuiltinArr(methodCallsHeader);
        lineBuiltinArr("    assert(__this);");
        lineBuiltinArr("    assert(size > 0);");
        lineBuiltinArr("    assert(size < INT_MAX);");

        lineBuiltinArr("    __this->size = 0;");
        lineBuiltinArr("    __this->alloc = size;");
        lineBuiltinArr("    __this->is_fixed = 1;");
        lineBuiltinArr("    __this->data = " + sizeofMulAlloc);
        lineBuiltinArr("}\n");
      }

      else {
        throw new AstParseException("unimplemented array constructor: " + method.getIdentifier().toString());
      }

    }

    else if (signToStringCall.startsWith("array_add_")) {
      lineBuiltinArr(methodCallsHeader);

      lineBuiltinArr("    if(__this->size >= __this->alloc) {                ");
      lineBuiltinArr("        __this->alloc += 1;                            ");
      lineBuiltinArr("        __this->alloc *= 2;                            ");
      lineBuiltinArr("        " + arrayOfToString + "* ndata = " + sizeofMulAlloc);
      lineBuiltinArr("        for(size_t i = 0; i < __this->size; i += 1) {  ");
      lineBuiltinArr("          ndata[i] = __this->data[i];                  ");
      lineBuiltinArr("        }                                              ");
      lineBuiltinArr("        free(__this->data);                            ");
      lineBuiltinArr("        __this->data = ndata;                          ");
      lineBuiltinArr("    }                                                  ");
      lineBuiltinArr("    __this->data[__this->size] = element;              ");
      lineBuiltinArr("    __this->size += 1;                                 ");

      lineBuiltinArr("}\n");
    }

    else if (signToStringCall.startsWith("array_get_")) {
      lineBuiltinArr(methodCallsHeader);
      lineBuiltinArr("    assert(__this);");
      lineBuiltinArr("    assert(__this->data);");
      lineBuiltinArr("    assert(__this->size > 0);");
      lineBuiltinArr("    assert(index >= 0);");
      lineBuiltinArr("    assert(index < __this->size);");
      lineBuiltinArr("    return __this->data[index];");
      lineBuiltinArr("}\n");
    }

    else if (signToStringCall.startsWith("array_set_")) {
      lineBuiltinArr(methodCallsHeader);
      lineBuiltinArr("    assert(__this);");
      lineBuiltinArr("    assert(__this->data);");
      lineBuiltinArr("    assert(index >= 0);");
      lineBuiltinArr("    assert(index < __this->size);");
      lineBuiltinArr("    " + arrayOfToString + " old =  __this->data[index];");
      lineBuiltinArr("    __this->data[index] = element;");
      lineBuiltinArr("    return old;");
      lineBuiltinArr("}\n");
    }

    else if (signToStringCall.startsWith("array_size_")) {
      lineBuiltinArr(methodCallsHeader);
      lineBuiltinArr("    assert(__this);");
      lineBuiltinArr("    return __this->size;");
      lineBuiltinArr("}\n");
    }

    else if (signToStringCall.startsWith("array_is_empty_")) {
      lineBuiltinArr(methodCallsHeader);
      lineBuiltinArr("    assert(__this);");
      lineBuiltinArr("    return (__this->size == 0);");
      lineBuiltinArr("}\n");
    }

    else if (signToStringCall.startsWith("array_is_fixed_")) {
      lineBuiltinArr(methodCallsHeader);
      lineBuiltinArr("    assert(__this);");
      lineBuiltinArr("    return (__this->is_fixed);");
      lineBuiltinArr("}\n");
    }

    else {
      throw new AstParseException("unimplemented array method: " + method.getIdentifier().toString());
    }

  }

  /// arrays

  private void genNativeMethod(Function func) {
    final ClassMethodDeclaration method = func.getMethodSignature();
    final Ident name = method.getIdentifier();
    final List<VarDeclarator> params = method.getParameters();
    final ClassDeclaration clazz = method.getClazz();

    //
    final String methodType = method.getType().toString();
    final String methodCallsHeader = methodType + " " + method.signToStringCall() + method.parametersToString() + " {";

    /// native_panic_ident = g("native_panic");
    /// native_assert_true_ident = g("native_assert_true");
    /// native_open_ident = g("native_open");
    /// native_close_ident = g("native_close");
    /// native_read_ident = g("native_read");
    /// 
    /// native void native_panic(*char because);
    /// native void native_assert_true(boolean condition);
    /// native int native_open(*char filename, int mode);
    /// native int native_close(int fd);
    /// native int native_read(int fd, *char buffer, int size);

    if (name.equals(BuiltinNames.native_panic_ident)) {
      lineBuiltin(methodCallsHeader);
      lineBuiltin("    assert(because);");
      lineBuiltin("}");
    }
    if (name.equals(BuiltinNames.native_assert_true_ident)) {
      lineBuiltin(methodCallsHeader);
      lineBuiltin("    assert_true(condition);");
      lineBuiltin("}");
    }

    ///
    if (name.equals(BuiltinNames.native_open_ident)) {
      lineBuiltin(methodCallsHeader);
      lineBuiltin("    return open(filename, O_RDONLY);");
      lineBuiltin("}");
    }
    if (name.equals(BuiltinNames.native_close_ident)) {
      lineBuiltin(methodCallsHeader);
      lineBuiltin("    return close(fd);");
      lineBuiltin("}");
    }
    if (name.equals(BuiltinNames.native_read_ident)) {
      lineBuiltin(methodCallsHeader);
      lineBuiltin("    assert(fd != -1);");
      lineBuiltin("    assert(buffer);");
      lineBuiltin("    assert(size > 0);");
      lineBuiltin("    return read(fd, buffer, size);");
      lineBuiltin("}");
    }

    ///
    if (name.equals(BuiltinNames.print_ident)) {
      lineBuiltin(methodCallsHeader);

      StringBuilder printfFmtNames = new StringBuilder();
      StringBuilder printfArgNames = new StringBuilder();
      StringBuilder asserts = new StringBuilder();

      for (int i = 0; i < params.size(); i += 1) {
        VarDeclarator param = params.get(i);

        PrintfPair printfPair = printfArgExpand(param);
        printfFmtNames.append(printfPair.getFmt());
        asserts.append(printfPair.assertsToStr());

        printfArgNames.append(printfPair.getArg());
        if (i + 1 < params.size()) {
          printfArgNames.append(", ");
        }
      }

      String quotedFmt = "\"" + printfFmtNames.toString() + "\\n\"";

      if (asserts.length() > 0) {
        lineBuiltin(asserts.toString());
      }

      lineBuiltin("    printf(" + quotedFmt + ", " + printfArgNames.toString() + ");");
      lineBuiltin("}\n");
    }

  }

  static class PrintfPair {
    private final String fmt;
    private final String arg;
    private final List<String> asserts;

    public PrintfPair(String fmt, String arg, List<String> asserts) {
      this.fmt = fmt;
      this.arg = arg;
      this.asserts = asserts;
    }

    public String getFmt() {
      return fmt;
    }

    public String getArg() {
      return arg;
    }

    public String assertsToStr() {
      StringBuilder sb = new StringBuilder();

      for (int i = 0; i < asserts.size(); i += 1) {
        String param = asserts.get(i);
        sb.append("    assert(" + param + ");\n");
      }
      return sb.toString();
    }

    @Override
    public String toString() {
      return "printf(\"" + fmt + "\", " + arg + ")";
    }

  }

  private String fmtStr(Type tp) {
    if (tp.isChar()) {
      return "%c";
    }
    if (tp.isInt() || tp.isShort() || tp.isBoolean() || tp.isLong()) {
      return "%d";
    }
    if (tp.isClass()) {
      if (tp.isCharArray() || tp.isString()) {
        return "%s";
      }
      return "%p";
    }
    if (tp.isFloat() || tp.isDouble()) {
      return "%f";
    }
    throw new AstParseException("unimplemented type to printf: " + tp.toString());
  }

  private PrintfPair printfArgExpand(VarDeclarator param) {
    Type tp = param.getType();

    String fmt = fmtStr(tp);
    String arg = param.getIdentifier().getName();
    List<String> asserts = new ArrayList<>();

    if (tp.isClass()) {
      asserts.add(param.getIdentifier().getName());

      if (tp.isCharArray()) {
        asserts.add(param.getIdentifier().getName() + "->data");
        arg = param.getIdentifier().getName() + "->data";
      }

      if (tp.isString()) {
        asserts.add(param.getIdentifier().getName() + "->buffer");
        asserts.add(param.getIdentifier().getName() + "->buffer->data");
        arg = param.getIdentifier().getName() + "->buffer->data";
      }
    }

    return new PrintfPair(fmt, arg, asserts);
  }

  private String genStructs() {
    StringBuilder sb = new StringBuilder();

    for (ClassDeclaration c : pods) {
      if (c.isMainClass()) {
        continue;
      }
      if (c.isStaticClass()) {
        continue;
      }
      if (c.isNativeArray()) {
        genArrayStruct(c);
        continue;
      }
      if (c.isNativeOpt()) {
        genOptStruct(c);
        continue;
      }

      sb.append(classToString(c));
      sb.append("\n");
    }

    for (ClassDeclaration c : pods) {
      if (c.isMainClass()) {
        continue;
      }
      if (c.isStaticClass()) {
        continue;
      }
      if (c.isNativeArray()) {
        continue;
      }

      sb.append("struct " + c.headerToString() + " " + c.headerToString() + "_zero;\n");
    }

    // struct list_iter_1024 list_iter_1024_zero;

    return sb.toString();
  }

  private String genStructsForwards() {
    StringBuilder sb = new StringBuilder();

    for (ClassDeclaration c : pods) {
      if (c.isMainClass()) {
        continue;
      }
      if (c.isStaticClass()) {
        continue;
      }
      if (c.isNativeArray()) {
        //continue;
      }
      sb.append("struct " + c.headerToString() + ";\n");
    }

    return sb.toString();
  }

  private String genFuncProtos() {
    StringBuilder sb = new StringBuilder();

    for (Function f : functions) {
      final ClassDeclaration c = f.getMethodSignature().getClazz();
      if (c.isMainClass()) {
        if (!f.getMethodSignature().isMain()) {
          continue;
        }
      }
      sb.append(f.signToString() + ";\n");
    }

    return sb.toString();
  }

}
