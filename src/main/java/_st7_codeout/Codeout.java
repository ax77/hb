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
import _st3_linearize_expr.ir.FlatCodeItem;
import _st3_linearize_expr.items.FlatCallVoid;
import _st3_linearize_expr.leaves.Var;
import ast_class.ClassDeclaration;
import ast_printers.TypePrinters;
import ast_types.Type;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import tokenize.Ident;

public class Codeout {
  private final List<ClassDeclaration> pods;
  private final List<Function> functions;

  private final Set<String> printfNames;
  private final StringBuilder builtinsFn;
  private final StringBuilder stringsLabels;

  public Codeout() {
    this.pods = new ArrayList<>();
    this.functions = new ArrayList<>();
    this.printfNames = new HashSet<>();
    this.builtinsFn = new StringBuilder();
    this.stringsLabels = new StringBuilder();
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

  public String classHeaderToString(ClassDeclaration c, boolean withKw) {
    StringBuilder sb = new StringBuilder();
    if (withKw) {
      sb.append("struct ");
    }
    sb.append(c.getIdentifier().getName());

    if (!c.getTypeParametersT().isEmpty()) {
      sb.append("_");
      sb.append(TypePrinters.typeArgumentsToString(c.getTypeParametersT()));
    }

    return sb.toString();
  }

  private String includes() {
    StringBuilder sb = new StringBuilder();

    //sb.append("#include <assert.h>  \n");
    //sb.append("#include <ctype.h>   \n");
    //sb.append("#include <limits.h>  \n");
    //sb.append("#include <stdarg.h>  \n");
    //sb.append("#include <stdbool.h> \n");
    //sb.append("#include <stddef.h>  \n");
    //sb.append("#include <stdint.h>  \n");
    //sb.append("#include <stdio.h>   \n");
    //sb.append("#include <stdlib.h>  \n");
    //sb.append("#include <string.h>  \n");

    sb.append("#include \"generated_types.h\" \n");
    sb.append("#include \"hrt/heap.h\"        \n");
    sb.append("\n\n");

    return sb.toString();
  }

  private void line(String s) {
    builtinsFn.append(s);
    builtinsFn.append("\n");
  }

  private void genBuiltins() {
    /// we know these functions AFTER we process the whole unit
    List<FlatCodeItem> builtins = BuiltinsFnSet.getBuiltins();
    Map<String, Var> strings = BuiltinsFnSet.getStringsmap();

    for (FlatCodeItem item : builtins) {
      if (item.isFlatCallVoid()) {
        FlatCallVoid fc = item.getFlatCallVoid();
        String fullname = fc.getFullname();
        if (fullname.startsWith("std_print_")) {
          if (!printfNames.contains(fullname)) {
            genPrintf(fc, fullname);
            printfNames.add(fullname);
          }
        }
      }

      else if (item.isAssignVarFlatCallResult()) {

      }

      else {
        throw new AstParseException("unr.");
      }
    }

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

  private void genPrintf(FlatCallVoid fc, String fullname) {
    final StringBuilder printfArguments = new StringBuilder();
    final List<Type> types = new ArrayList<>();
    final List<Var> names = new ArrayList<>();
    final List<String> toAssert = new ArrayList<>();

    final List<Var> fcArgs = fc.getArgs();
    for (int i = 0; i < fcArgs.size(); i += 1) {
      final Var arg = fcArgs.get(i);
      printfArguments.append(arg.typeNameToString());

      types.add(arg.getType());
      names.add(arg);

      if (arg.getType().isClass()) {
        toAssert.add(arg.getName().getName());
      }

      if (i + 1 < fcArgs.size()) {
        printfArguments.append(", ");
      }
    }

    StringBuilder printfBody = new StringBuilder();
    for (int i = 0; i < types.size(); i += 1) {
      final Type tp = types.get(i);
      final Ident name = names.get(i).getName();

      if (tp.isClass() && tp.getClassTypeFromRef().isNativeString()) {
        printfBody.append(name.getName() + "->buffer");
      } else {
        if (!tp.isPrimitive()) {
          throw new AstParseException("unimpl.: print compound type");
        }
        printfBody.append(name.getName());
      }

      if (i + 1 < types.size()) {
        printfBody.append(", ");
      }
    }

    StringBuilder printfAsserts = new StringBuilder();
    for (String s : toAssert) {
      printfAsserts.append("assert(" + s + ");\n");
    }

    line("static void " + fullname + "(" + printfArguments.toString() + ")");
    line("{");
    line(printfAsserts.toString());
    line("    printf(" + printfBody.toString() + ");");
    line("}\n");
  }

  private void genMainFile(StringBuilder sb) throws IOException {
    final String fileName = "main.c";
    FileWriter fw = new FileWriter(fileName);
    fw.write(sb.toString());
    fw.close();
  }

  @Override
  public String toString() {

    try {
      GenAppendDepsFile.g(pods);
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      GenIsCompoundFile.g(pods);
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      GenDeinitsFile.g(pods);
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    Set<ClassDeclaration> arrays = new HashSet<>();
    Set<ClassDeclaration> strings = new HashSet<>();
    Set<Function> arrayMethods = new HashSet<>();
    Set<Function> stringMethods = new HashSet<>();
    List<Function> mainMethodOut = new ArrayList<>();

    String structTypedefs = genStructsTypedefs();
    String funcProtos = genFuncProtos();
    String structsImpls = genStructs(arrays, strings);
    String functions = genFunctions(arrayMethods, stringMethods, mainMethodOut);

    StringBuilder genTypesFile = new StringBuilder();
    genTypesFile.append(GenTypeDescriptions.genTypeDescrsExtern(pods));
    genTypesFile.append("\n");
    genTypesFile.append(structTypedefs);
    genTypesFile.append("\n");
    genTypesFile.append(funcProtos);
    genTypesFile.append("\n");
    genTypesFile.append(structsImpls);
    genTypesFile.append("\n");
    try {
      GenGeneratedTypesFile.genGeneratedTypesFile(genTypesFile.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }

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
    sb.append(CCMacro.genMacro());
    sb.append(GenTypeDescriptions.genTypeDescrsImpl(pods));
    sb.append(GenArrays.buildArraysProtos(arrays));
    sb.append(stringsLabels.toString());
    sb.append("\n");
    sb.append(CCString.genString());
    sb.append("\n");

    // impls
    sb.append(GenArrays.buildArraysImplsStructs(arrays));
    sb.append(GenArrays.buildArraysImplsMethods(arrayMethods));
    sb.append(builtinsFn.toString());
    sb.append(functions);
    sb.append("\n");

    // main
    sb.append(mainMethodImpl);
    sb.append("int main(int args, char** argv) \n{\n");
    sb.append("    initHeap();   \n");
    sb.append("    init_frames();\n");
    sb.append("    open_frame(); \n\n");
    sb.append("    int result = " + mainMethodCall + ";\n\n");
    sb.append("    dump_heap();   \n");
    sb.append("    close_frame(); \n");
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

  private String genFunctions(Set<Function> arrayMethods, Set<Function> stringMethods, List<Function> mainMethod) {
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
      if (c.isNativeArray()) {
        arrayMethods.add(f);
        continue;
      }
      if (c.isNativeString()) {
        stringMethods.add(f);
        continue;
      }
      sb.append(f.toString());
      sb.append("\n");
    }

    return sb.toString();
  }

  private String genStructs(Set<ClassDeclaration> arrays, Set<ClassDeclaration> strings) {
    StringBuilder sb = new StringBuilder();

    for (ClassDeclaration c : pods) {
      if (c.isMainClass()) {
        continue;
      }
      if (c.isNativeArray()) {
        arrays.add(c);
        continue;
      }
      if (c.isNativeString()) {
        strings.add(c);
        continue;
      }
      sb.append(classToString(c));
      sb.append("\n");
    }

    return sb.toString();
  }

  private String genStructsTypedefs() {
    StringBuilder sb = new StringBuilder();

    for (ClassDeclaration c : pods) {
      if (c.isMainClass()) {
        continue;
      }
      if (c.isNativeArray()) {
        continue;
      }
      if (c.isNativeString()) {
        continue;
      }
      sb.append("typedef " + classHeaderToString(c, true) + " * " + classHeaderToString(c, false) + ";\n");
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
      if (c.isNativeArray()) {
        continue;
      }
      if (c.isNativeString()) {
        continue;
      }
      sb.append(f.signToString() + ";\n");
    }

    return sb.toString();
  }

}
