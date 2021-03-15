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

  //@formatter:off
  private String headers() {
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
    sb.append("#include \"hrt/heap.h\"        \n\n");
    //sb.append("#include \"hrt/mem.h\"         \n\n");
    
    sb.append(CCMacro.genMacro());
    sb.append("\n\n");
    
    return sb.toString();
  }
  //@formatter:on

  private void line(String s) {
    builtinsFn.append(s);
    builtinsFn.append("\n");
  }

  private String genFileIsCompound() throws IOException {
    // if (datatype == TD_STR || datatype == TD_TYPE || datatype == TD_TOKEN || datatype == TD_FILE_READER) {
    //   return 1;
    // }

    StringBuilder sb = new StringBuilder();

    for (ClassDeclaration c : pods) {
      String tdName = "TD_" + c.getIdentifier().getName().toUpperCase();
      sb.append("if (datatype == " + tdName + ")\n{\n");
      sb.append("    return 1;\n}\n");
    }

    final String fileName = "generated_is_compound.txt";
    FileWriter fw = new FileWriter(fileName);
    fw.write(sb.toString());
    fw.close();

    return sb.toString();
  }

  private void getDeinitsFile() throws IOException {
    // if (m->datatype == TD_STRING) {
    //   string e = (string) m->ptr;
    //   string_deinit(e);
    // }

    StringBuilder sb = new StringBuilder();

    for (ClassDeclaration c : pods) {
      if (c.isMainClass()) {
        continue;
      }

      String tdName = "TD_" + c.getIdentifier().getName().toUpperCase();
      String deinitCall = c.getDestructor().signToStringCall();
      String cName = c.getIdentifier().toString();

      sb.append("if (m->datatype == " + tdName + ")\n{\n");
      sb.append("    struct " + cName + " *e = (struct " + cName + "*) m->ptr;\n");
      sb.append("    " + deinitCall + "(e);\n");
      sb.append("\n}\n");
    }

    final String fileName = "generated_deinits.txt";
    FileWriter fw = new FileWriter(fileName);
    fw.write(sb.toString());
    fw.close();

  }

  private void genGeneratedTypesFile(String src) throws IOException {
    // generated_types.h

    StringBuilder prebuf = new StringBuilder();
    prebuf.append("#ifndef GENERATED_TYPES_H_                  \n");
    prebuf.append("#define GENERATED_TYPES_H_                  \n");
    prebuf.append("#include \"hrt/headers.h\"                  \n\n");
    prebuf.append("typedef int boolean;                        \n");
    prebuf.append("typedef struct string * string;             \n\n");
    prebuf.append("struct string                               \n");
    prebuf.append("{                                           \n");
    prebuf.append("    char *buffer;                           \n");
    prebuf.append("    size_t len;                             \n");
    prebuf.append("};                                          \n\n");
    prebuf.append("void string_init(string __this, char *buf); \n");
    prebuf.append("void string_deinit(string __this);          \n");
    prebuf.append("void string_destroy(string __this);         \n\n");
    prebuf.append("struct type_descr;                          \n");
    prebuf.append("extern struct type_descr *TD_CHAR_PTR;      \n");
    prebuf.append("extern struct type_descr *TD_ARRAY;         \n");
    prebuf.append("extern struct type_descr *TD_ARRAY_TABLE;   \n\n");

    final String fileName = "generated_types.h";
    FileWriter fw = new FileWriter(fileName);
    fw.write(prebuf.toString());
    fw.write(src);
    fw.write("\n#endif\n");
    fw.close();
  }

  private String genFileAppendDeps() throws IOException {

    /// if (datatype == TD_STR) {
    ///     struct string *e = (struct string*) ptr;
    ///     vec_add_unique_ignore_null(inProcessing, try_to_find_markable_by_ptr(e->buffer));
    /// 
    ///     return;
    /// }
    /// 
    /// if (datatype == TD_TOKEN) {
    ///     struct token *e = (struct token*) ptr;
    ///     vec_add_unique_ignore_null(inProcessing, try_to_find_markable_by_ptr(e->value));
    /// 
    ///     return;
    /// }

    // generated_append_deps.txt

    StringBuilder sb = new StringBuilder();

    for (ClassDeclaration c : pods) {
      String tdName = "TD_" + c.getIdentifier().getName().toUpperCase();
      String cName = c.getIdentifier().toString();

      sb.append("if (datatype == " + tdName + ")\n{\n");
      sb.append("    struct " + cName + " *e = (struct " + cName + "*) ptr;\n");
      for (VarDeclarator f : c.getFields()) {
        if (!f.getType().isClass()) {
          continue;
        }
        String fName = f.getIdentifier().toString();
        sb.append("    vec_add_unique_ignore_null(inProcessing, try_to_find_markable_by_ptr(e->" + fName + "));\n");
      }
      if (c.getIdentifier().equals(BuiltinNames.string_ident)) {
        sb.append("    vec_add_unique_ignore_null(inProcessing, try_to_find_markable_by_ptr(e->buffer));\n");
      }
      sb.append("    return;\n}\n");
    }

    final String fileName = "generated_append_deps.txt";
    FileWriter fw = new FileWriter(fileName);
    fw.write(sb.toString());
    fw.close();

    return sb.toString();

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

  private String proto(Function f) {
    return f.signToString();
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
      genFileAppendDeps();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      genFileIsCompound();
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      getDeinitsFile();
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    Set<ClassDeclaration> arrays = new HashSet<>();
    Set<ClassDeclaration> strings = new HashSet<>();
    Set<Function> arrayMethods = new HashSet<>();
    Set<Function> stringMethods = new HashSet<>();
    List<Function> mainMethodOut = new ArrayList<>();

    // struct type_descr *TD_STR = &(struct type_descr ) { .description = "TD_STR", };
    String typeDescrsImpl = genTypeDescrsImpl();
    String typeDescrsExtern = genTypeDescrsExtern();

    String tpdef = genStructsTypedefs();

    String protos = genFuncProtos();

    String structs = genStructs(arrays, strings);

    String funcs = genFunctions(arrayMethods, stringMethods, mainMethodOut);

    StringBuilder genTypesFile = new StringBuilder();
    genTypesFile.append(typeDescrsExtern);
    genTypesFile.append("\n");
    genTypesFile.append(tpdef);
    genTypesFile.append("\n");
    genTypesFile.append(protos);
    genTypesFile.append("\n");
    genTypesFile.append(structs);
    genTypesFile.append("\n");
    try {
      genGeneratedTypesFile(genTypesFile.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (mainMethodOut.size() != 1) {
      throw new AstParseException("there is no main...");
    }

    final Function mainFn = mainMethodOut.get(0);
    String mainMethodSign = mainFn.signToString();
    String mainMethodImpl = mainFn.toString();
    String mainMethodCall = mainFn.signToStringCall();

    genBuiltins();

    StringBuilder sb = new StringBuilder();

    // protos
    sb.append(headers());
    sb.append(typeDescrsImpl);
    sb.append(buildArraysProtos(arrays));
    //sb.append(tpdef);
    //sb.append(protos);
    sb.append(stringsLabels.toString());
    sb.append("\n");
    sb.append(CCString.genString());
    sb.append("\n");

    // impls
    sb.append(buildArraysImplsStructs(arrays));
    sb.append(buildArraysImplsMethods(arrayMethods));
    sb.append(builtinsFn.toString());
    //sb.append(structs);
    sb.append(funcs);
    //sb.append(builtinsFn.toString());
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

  private String genTypeDescrsImpl() {
    StringBuilder sb = new StringBuilder();

    // extern struct type_descr *TD_ARRAY_TABLE; 
    // struct type_descr *TD_STR = &(struct type_descr ) { .description = "TD_STR", };

    sb.append("struct type_descr *TD_CHAR_PTR = &(struct type_descr ) { .description = \"TD_CHAR_PTR\", };       \n");
    sb.append("struct type_descr *TD_ARRAY = &(struct type_descr ) { .description = \"TD_ARRAY\", };             \n");
    sb.append("struct type_descr *TD_ARRAY_TABLE = &(struct type_descr ) { .description = \"TD_ARRAY_TABLE\", }; \n");

    for (ClassDeclaration c : pods) {
      String tdName = "TD_" + c.getIdentifier().getName().toUpperCase();
      String cName = "\"" + c.getIdentifier().toString() + "\"";

      sb.append("struct type_descr *" + tdName + " = &(struct type_descr ) { .description = " + cName + ", };\n");
    }

    sb.append("\n");
    return sb.toString();
  }

  private String genTypeDescrsExtern() {
    StringBuilder sb = new StringBuilder();

    // extern struct type_descr *TD_ARRAY_TABLE; 
    // struct type_descr *TD_STR = &(struct type_descr ) { .description = "TD_STR", };

    for (ClassDeclaration c : pods) {
      String tdName = "TD_" + c.getIdentifier().getName().toUpperCase();
      String cName = "\"" + c.getIdentifier().toString() + "\"";

      sb.append("extern struct type_descr *" + tdName + ";\n");
    }

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
      sb.append(proto(f) + ";\n");
    }

    return sb.toString();
  }

}
