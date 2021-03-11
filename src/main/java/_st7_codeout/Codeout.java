package _st7_codeout;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_printers.TypePrinters;
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
    
    sb.append("void* hmalloc(size_t size)       \n");
    sb.append("{                                \n");
    sb.append("    if (size == 0) {             \n");
    sb.append("        size = 1;                \n");
    sb.append("    }                            \n");
    sb.append("    assert(size < INT_MAX);      \n");
    sb.append("                                 \n");
    sb.append("    void *ptr = NULL;            \n");
    sb.append("    ptr = malloc(size);          \n");
    sb.append("    if (ptr == NULL) {           \n");
    sb.append("        ptr = malloc(size);      \n");
    sb.append("        if (ptr == NULL) {       \n");
    sb.append("            ptr = malloc(size);  \n");
    sb.append("        }                        \n");
    sb.append("    }                            \n");
    sb.append("    assert(ptr);                 \n");
    sb.append("    return ptr;                  \n");
    sb.append("}                                \n");

    return sb.toString();
  }
  //@formatter:on

  private String proto(Function f) {
    return f.signToString();
  }

  private boolean isMainClass(ClassDeclaration c) {
    return (c.getIdentifier().getName().equals("main_class"));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(headers());

    String mainMethod = "";
    String mainMethodCall = "";

    for (ClassDeclaration c : pods) {
      if (isMainClass(c)) {
        continue;
      }
      sb.append("typedef " + classHeaderToString(c, true) + " * " + classHeaderToString(c, false) + ";\n");
    }

    for (Function f : functions) {
      if (isMainClass(f.getMethodSignature().getClazz())) {
        if (!f.getMethodSignature().isMain()) {
          continue;
        }
      }
      sb.append(proto(f) + ";\n");
    }

    for (ClassDeclaration c : pods) {
      if (isMainClass(c)) {
        continue;
      }
      sb.append(classToString(c));
    }

    for (Function f : functions) {
      if (isMainClass(f.getMethodSignature().getClazz())) {
        if (!f.getMethodSignature().isMain()) {
          continue;
        }
      }
      if (f.getMethodSignature().isMain()) {
        mainMethod = f.toString();
        mainMethodCall = f.signToStringCall();
        continue;
      }
      sb.append(f.toString());
    }

    if (mainMethod.isEmpty() || mainMethodCall.isEmpty()) {
      throw new AstParseException("there is no main...");
    }

    sb.append(mainMethod);
    sb.append("int main(int args, char** argv) \n{\n");
    sb.append("    int result = " + mainMethodCall + ";\n");
    sb.append("    printf(\"%d\\n\", result);\n");
    sb.append("    return result;\n");
    sb.append("\n}\n");

    return sb.toString();
  }

}
