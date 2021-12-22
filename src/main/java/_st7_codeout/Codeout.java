package _st7_codeout;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import _st3_linearize_expr.BuiltinsFnSet;
import _st3_linearize_expr.CEscaper;
import _st3_linearize_expr.leaves.Var;
import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_printers.TypePrinters;
import ast_vars.VarDeclarator;
import errors.AstParseException;

public class Codeout {
  private final List<ClassDeclaration> pods;
  private final List<Function> functions;

  private final StringBuilder builtinsFn;
  private final StringBuilder builtinsArrays;

  private final StringBuilder builtinsTypedefs;
  private final StringBuilder stringsLabels;

  public Codeout() {
    this.pods = new ArrayList<>();
    this.functions = new ArrayList<>();
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
    return GnRuntime.prebuf();
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
    sb.append(stringsLabels.toString());
    sb.append(builtinsArrays.toString());
    sb.append("\n");
    sb.append("\n");

    // impls
    sb.append(builtinsFn.toString());
    sb.append(functions);
    sb.append("\n");

    // tests
    sb.append(genTests());

    // main
    sb.append(mainMethodImpl);
    sb.append("int main(int argc, char** argv) \n{\n");
    sb.append("    int result = " + mainMethodCall + ";\n\n");
    sb.append("    printf(\"%d\\n\", result);\n");
    sb.append("    return result;\n");
    sb.append("\n}\n");

    //    try {
    //      genMainFile(sb);
    //    } catch (IOException e) {
    //      e.printStackTrace();
    //    }

    return sb.toString();
  }

  private String genTests() {
    StringBuilder sb = new StringBuilder();
    sb.append("void __run_all_tests__() \n{\n");
    for (ClassDeclaration c : pods) {
      for (ClassMethodDeclaration m : c.getTests()) {
        final String className = m.getClazz().getIdentifier().toString();
        final String testName = CEscaper.unquote(m.getTestName());

        String name = "\"" + className + " :: " + testName + "\"";
        String sign = m.signToStringCall();

        sb.append("printf(\"test: %s\\n\", " + name + ");\n");
        sb.append(sign + "();\n");
      }
    }
    sb.append("\n}\n");
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
        lineBuiltinArr(GnArray.genArrayMethod(f));
        continue;
      }
      if (f.getMethodSignature().getClazz().isNativeOpt()) {
        lineBuiltinArr(GnOpt.genOptMethod(f));
        continue;
      }
      if (f.getMethodSignature().getClazz().isNativeString()) {
        lineBuiltinArr(GnStr.genStringMethod(f));
        continue;
      }

      if (f.getMethodSignature().getModifiers().isNative()) {
        lineBuiltin(GnNative.genNativeMethod(f));
        continue;
      }

      sb.append(f.toString());
      sb.append("\n");
    }

    return sb.toString();
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
        lineBuiltinArr(GnArray.genArrayStruct(c));
        continue;
      }
      if (c.isNativeOpt()) {
        lineBuiltinArr(GnOpt.genOptStruct(c));
        continue;
      }
      if (c.isNativeString()) {
        lineBuiltinArr(GnStr.genStringStruct(c));
        continue;
      }
      if (c.isInterface()) {
        sb.append(genInterfaceStruct(c));
        continue;
      }

      sb.append(classToString(c));
      sb.append("\n");
    }

    return sb.toString();
  }

  private String genInterfaceStruct(ClassDeclaration c) {
    StringBuilder sb = new StringBuilder();
    sb.append("struct " + c.headerToString() + "\n{\n");
    for (ClassMethodDeclaration m : c.getMethods()) {
      // void (*name) (args);
      sb.append("    " + m.getType().toString() + "(*");
      sb.append(m.getIdentifier().toString() + ")");
      sb.append(m.parametersToString());
      sb.append(";");
    }
    sb.append("\n};\n");
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
      ClassMethodDeclaration meth = f.getMethodSignature();
      final ClassDeclaration c = f.getMethodSignature().getClazz();

      if (c.isMainClass()) {
        if (!f.getMethodSignature().isMain()) {
          continue;
        }
      }
      if (c.isNativeString() && meth.isConstructor()) {
        sb.append(meth.getType().toString() + " " + meth.signToStringCall()
            + "(struct string* __this, const char * const buffer);\n");
        continue;
      }
      sb.append(f.signToString() + ";\n");
    }

    return sb.toString();
  }

}
