package _st7_codeout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_printers.TypePrinters;
import ast_types.Type;
import ast_vars.VarDeclarator;
import errors.AstParseException;

public class Codeout {
  private final List<ClassDeclaration> pods;
  private final List<Function> functions;

  public Codeout() {
    this.pods = new ArrayList<>();
    this.functions = new ArrayList<>();
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

  //@formatter:off
  private String headers() {
    StringBuilder sb = new StringBuilder();

    sb.append("#include <assert.h>  \n");
    sb.append("#include <ctype.h>   \n");
    sb.append("#include <limits.h>  \n");
    sb.append("#include <stdarg.h>  \n");
    sb.append("#include <stdbool.h> \n");
    sb.append("#include <stddef.h>  \n");
    sb.append("#include <stdint.h>  \n");
    sb.append("#include <stdio.h>   \n");
    sb.append("#include <stdlib.h>  \n");
    sb.append("#include <string.h>  \n");
    sb.append("#include \"mem.h\"   \n");
    
    sb.append("typedef int boolean; \n"); // TODO:
    
    return sb.toString();
  }
  //@formatter:on

  private String proto(Function f) {
    return f.signToString();
  }

  @Override
  public String toString() {

    Set<ClassDeclaration> arrays = new HashSet<>();
    Set<Function> arrayMethods = new HashSet<>();
    List<Function> mainMethodOut = new ArrayList<>();

    String tpdef = genStructsTypedefs();

    String protos = genFuncProtos();

    String structs = genStructs(arrays);

    String funcs = genFunctions(arrayMethods, mainMethodOut);

    if (mainMethodOut.size() != 1) {
      throw new AstParseException("there is no main...");
    }

    final Function mainFn = mainMethodOut.get(0);
    String mainMethodSign = mainFn.signToString();
    String mainMethodImpl = mainFn.toString();
    String mainMethodCall = mainFn.signToStringCall();

    StringBuilder sb = new StringBuilder();

    // protos
    sb.append(headers());
    sb.append(mainMethodSign + ";\n");
    sb.append(buildArraysProtos(arrays));
    sb.append(tpdef);
    sb.append(protos);

    // impls
    sb.append(buildArraysImplsStructs(arrays));
    sb.append(buildArraysImplsMethods(arrayMethods));
    sb.append(structs);
    sb.append(funcs);

    // main
    sb.append(mainMethodImpl);
    sb.append("int main(int args, char** argv) \n{\n");
    sb.append("    int result = " + mainMethodCall + ";\n");
    sb.append("    printf(\"%d\\n\", result);\n");
    sb.append("    return result;\n");
    sb.append("\n}\n");

    return sb.toString();
  }

  private String buildArraysProtos(Set<ClassDeclaration> arrays) {
    StringBuilder sb = new StringBuilder();
    for (ClassDeclaration c : arrays) {
      String datatype = c.getTypeParametersT().get(0).toString();
      String tpdef = CCArrays.genArrayStructTypedef(datatype);
      sb.append(tpdef);
    }
    return sb.toString();
  }

  private String buildArraysImplsStructs(Set<ClassDeclaration> arrays) {
    StringBuilder sb = new StringBuilder();
    for (ClassDeclaration c : arrays) {
      String datatype = c.getTypeParametersT().get(0).toString();
      String tpdef = CCArrays.genArrayStructImpl(datatype);
      sb.append(tpdef);
    }
    return sb.toString();
  }

  private String buildArraysImplsMethods(Set<Function> arrayMethods) {
    StringBuilder sb = new StringBuilder();
    for (Function f : arrayMethods) {
      final ClassMethodDeclaration method = f.getMethodSignature();
      final ClassDeclaration clazz = method.getClazz();
      final Type type = clazz.getTypeParametersT().get(0);
      final String datatype = type.toString();
      final boolean terminated = type.isChar();

      if (method.getIdentifier().getName().equals("add")) {
        String block = CCArrays.genArrayAddBlock(datatype, terminated);
        sb.append(f.signToString());
        sb.append(block);
      }

      else if (method.getIdentifier().getName().equals("get")) {
        String block = CCArrays.genArrayGetBlock(datatype);
        sb.append(f.signToString());
        sb.append(block);
      }

      else if (method.getIdentifier().getName().equals("set")) {
        String block = CCArrays.genArraySetBlock(datatype);
        sb.append(f.signToString());
        sb.append(block);
      }

      else if (method.getIdentifier().getName().equals("opAssign")) {
        String block = " \n{\n return rvalue; \n}\n ";
        sb.append(f.signToString());
        sb.append(block);
      }

      else if (method.getIdentifier().getName().equals("size")) {
        String block = CCArrays.genArraySizeBlock(datatype);
        sb.append(f.signToString());
        sb.append(block);
      }

      else {

        if (method.isConstructor()) {
          String block = CCArrays.genArrayAllocBlock(datatype);
          sb.append(f.signToString());
          sb.append(block);
        } else if (method.isDestructor()) {
          String block = " \n{\n  \n}\n ";
          sb.append(f.signToString());
          sb.append(block);
        }

        else {
          throw new AstParseException("unimpl. array method: " + method.getIdentifier().getName());
        }

      }
    }
    return sb.toString();
  }

  private String genFunctions(Set<Function> arrayMethods, List<Function> mainMethod) {
    StringBuilder sb = new StringBuilder();

    for (Function f : functions) {
      final ClassDeclaration clazz = f.getMethodSignature().getClazz();
      if (clazz.isMainClass()) {
        if (!f.getMethodSignature().isMain()) {
          continue;
        }
      }
      if (f.getMethodSignature().isMain()) {
        mainMethod.add(f);
        continue;
      }
      if (clazz.isNativeArray()) {
        arrayMethods.add(f);
        continue;
      }
      sb.append(f.toString());
    }

    return sb.toString();
  }

  private String genStructs(Set<ClassDeclaration> arrays) {
    StringBuilder sb = new StringBuilder();

    for (ClassDeclaration c : pods) {
      if (c.isMainClass()) {
        continue;
      }
      if (c.isNativeArray()) {
        arrays.add(c);
        continue;
      }
      sb.append(classToString(c));
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
      sb.append("typedef " + classHeaderToString(c, true) + " * " + classHeaderToString(c, false) + ";\n");
    }

    return sb.toString();
  }

  private String genFuncProtos() {
    StringBuilder sb = new StringBuilder();

    for (Function f : functions) {
      final ClassDeclaration clazz = f.getMethodSignature().getClazz();
      if (clazz.isMainClass()) {
        if (!f.getMethodSignature().isMain()) {
          continue;
        }
      }
      if (clazz.isNativeArray()) {
        continue;
      }
      sb.append(proto(f) + ";\n");
    }

    return sb.toString();
  }

}
