package _st7_codeout;

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
import ast_vars.VarDeclarator;
import errors.AstParseException;
import tokenize.Ident;

public class Codeout {
  private final List<ClassDeclaration> pods;
  private final List<Function> functions;

  private final Set<String> generatedBuiltinNames;
  private final StringBuilder builtinsFn;

  private final StringBuilder builtinsTypedefs;
  private final StringBuilder stringsLabels;

  public Codeout() {
    this.pods = new ArrayList<>();
    this.functions = new ArrayList<>();
    this.generatedBuiltinNames = new HashSet<>();
    this.builtinsFn = new StringBuilder();
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
      stringsLabels.append("char " + v.getName().getName() + "[] = { " + initBuffer + "};\n");
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

      if (f.getMethodSignature().getModifiers().isNative()) {
        genNativeMethod(f);
        continue;
      }

      sb.append(f.toString());
      sb.append("\n");
    }

    return sb.toString();
  }

  private void genNativeMethod(Function func) {
    final ClassMethodDeclaration method = func.getMethodSignature();
    final Ident name = method.getIdentifier();
    final List<VarDeclarator> params = method.getParameters();
    final ClassDeclaration clazz = method.getClazz();

    //
    final String methodType = method.getType().toString();
    final String methodCallsHeared = methodType + " " + method.signToStringCall() + method.parametersToString() + " {";

    if (BuiltinNames.isMemClassNativeMethodName(name)) {
      if (!clazz.isNativePtr()) {
        throw new AstParseException("unexpected uses of native mem.func");
      }
    }

    /// native_malloc_ident = g("native_malloc");
    /// native_calloc_ident = g("native_calloc");
    /// native_free_ident = g("native_free");
    /// native_ptr_access_at_ident = g("native_ptr_access_at");
    /// native_ptr_set_at_ident = g("native_ptr_set_at");
    /// native_memcpy_ident = g("native_memcpy");
    ///
    /// native *T native_malloc(*T ptr, int size);
    /// native *T native_calloc(*T ptr, int count, int size);
    /// native void native_free(*T ptr);
    /// native T native_ptr_access_at(*T ptr, int offset);       // char c = p[i]
    /// native T native_ptr_set_at(*T ptr, int offset, T value); // p[i] = c
    /// native *T native_memcpy(*T dst, *T src, int count);

    /// type* malloc(box_str, ptr**, size)
    //

    if (name.equals(BuiltinNames.native_malloc_ident)) {
      lineBuiltin(methodCallsHeared);
      lineBuiltin("    return (" + methodType + ") hmalloc(size);");
      lineBuiltin("}");
    }
    if (name.equals(BuiltinNames.native_calloc_ident)) {
      lineBuiltin(methodCallsHeared);
      lineBuiltin("    return (" + methodType + ") hcalloc(count, size);");
      lineBuiltin("}");
    }
    if (name.equals(BuiltinNames.native_free_ident)) {
      lineBuiltin(methodCallsHeared);
      lineBuiltin("    free(ptr);");
      lineBuiltin("}");
    }
    if (name.equals(BuiltinNames.native_ptr_access_at_ident)) {
      lineBuiltin(methodCallsHeared);
      lineBuiltin("    return ptr[offset];");
      lineBuiltin("}");
    }
    if (name.equals(BuiltinNames.native_ptr_set_at_ident)) {
      lineBuiltin(methodCallsHeared);
      lineBuiltin("    " + methodType + " old = ptr[offset];");
      lineBuiltin("    ptr[offset] = value;");
      lineBuiltin("    return old;");
      lineBuiltin("}");
    }
    if (name.equals(BuiltinNames.native_memcpy_ident)) {
      lineBuiltin(methodCallsHeared);
      lineBuiltin("    return (" + methodType + ") memcpy(dst, src, count);");
      lineBuiltin("}");
    }

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
      lineBuiltin(methodCallsHeared);
      lineBuiltin("    assert(because);");
      lineBuiltin("}");
    }
    if (name.equals(BuiltinNames.native_assert_true_ident)) {
      lineBuiltin(methodCallsHeared);
      lineBuiltin("    assert_true(condition);");
      lineBuiltin("}");
    }

    ///
    if (name.equals(BuiltinNames.native_open_ident)) {
      lineBuiltin(methodCallsHeared);
      lineBuiltin("    return open(filename, O_RDONLY);");
      lineBuiltin("}");
    }
    if (name.equals(BuiltinNames.native_close_ident)) {
      lineBuiltin(methodCallsHeared);
      lineBuiltin("    return close(fd);");
      lineBuiltin("}");
    }
    if (name.equals(BuiltinNames.native_read_ident)) {
      lineBuiltin(methodCallsHeared);
      lineBuiltin("    assert(fd != -1);");
      lineBuiltin("    assert(buffer);");
      lineBuiltin("    assert(size > 0);");
      lineBuiltin("    return read(fd, buffer, size);");
      lineBuiltin("}");
    }

    ///
    if (name.equals(BuiltinNames.native_printf_ident)) {
      lineBuiltin(methodCallsHeared);

      StringBuilder printfArgNames = new StringBuilder();
      for (int i = 0; i < params.size(); i += 1) {
        VarDeclarator param = params.get(i);
        printfArgNames.append(param.getIdentifier().getName());
        if (i + 1 < params.size()) {
          printfArgNames.append(", ");
        }
      }

      lineBuiltin("    printf(" + printfArgNames.toString() + ");");
      lineBuiltin("}");
    }

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
