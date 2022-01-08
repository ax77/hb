package _st7_codeout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import _st7_codeout.parts.GenEnums;
import _st7_codeout.parts.GenBuiltinArray;
import _st7_codeout.parts.GenBuiltinString;
import _st7_codeout.parts.GenBuiltinStringStaticLabels;
import _st7_codeout.parts.GenCommentHeader;
import _st7_codeout.parts.GenEmpties;
import _st7_codeout.parts.GenFunctions;
import _st7_codeout.parts.GenInterfaces;
import _st7_codeout.parts.GenRuntimeHeader;
import _st7_codeout.parts.GenStaticFields;
import _st7_codeout.parts.GenStructsBodies;
import _st7_codeout.parts.GenStructsForwards;
import _st7_codeout.parts.GenUnittests;
import ast_class.ClassDeclaration;
import ast_main.ParserMainOptions;
import errors.AstParseException;
import utils_oth.NullChecker;

public class CgMain {

  private final List<ClassDeclaration> classes;
  private final StringBuilder resultBuffer;

  public CgMain(List<ClassDeclaration> classes) {
    this.classes = classes;
    this.resultBuffer = new StringBuilder();
    gen();
  }

  @Override
  public String toString() {
    return resultBuffer.toString();
  }

  private void gen() {

    // headers and runtime functions
    final String runtimeHeader = GenRuntimeHeader.genIncludesAndRuntimeFuncs();
    final String structsForwards = GenStructsForwards.gen(classes);

    // empties
    final GenEmpties empties = new GenEmpties(classes);
    final String emptiesProto = empties.getProto();
    final String emptiesImpls = empties.getImpls();

    // unittests
    final GenUnittests tests = new GenUnittests(classes);
    final String testsImpls = tests.getImpls();

    /// at this point we begin to cut the classes from the list
    /// if we want string, arrays, main class - we have to cut them all
    /// to be sure they are present, etc...

    // assert true function which depends on the string class
    final String assertTrueFunction = GenRuntimeHeader.genAssertTrueFunction();

    // arrays, must have
    final List<ClassDeclaration> arrays = cutArrays();
    final GenBuiltinArray arraysCg = new GenBuiltinArray(arrays);
    final String arraysProto = arraysCg.getProto();
    final String arraysImpls = arraysCg.getImpls();

    final ClassDeclaration strClazz = cutString();
    final GenBuiltinString stringCg = new GenBuiltinString(strClazz);
    final String strProto = stringCg.getProto();
    final String strImpls = stringCg.getImpls();

    final List<ClassDeclaration> interfaces = cutInterfaces();
    final GenInterfaces interfacesCg = new GenInterfaces(interfaces);
    final String interfacesProto = interfacesCg.getProto();
    final String interfacesImpls = interfacesCg.getImpls();

    final List<ClassDeclaration> enums = cutEnums();
    final GenEnums enumsCg = new GenEnums(enums);
    final String enumsProto = enumsCg.getProto();

    // structs, except natives that we've handled before
    final String structsBodies = GenStructsBodies.gen(classes);

    // main class
    final ClassDeclaration mainClass = cutMainClass();
    final List<Function> mainClassFuncs = LinearizeMethods.flat(mainClass);

    // static fields
    final String staticFields = GenStaticFields.gen(classes);

    // and all of the functions
    final GenFunctions funcs = new GenFunctions(classes);
    final String funcsProto = funcs.getProto();
    final String funcsImpls = funcs.getImpls();

    // and the result

    resultBuffer.append(GenCommentHeader.gen("the runtime part"));
    resultBuffer.append(runtimeHeader);

    resultBuffer.append(GenCommentHeader.gen("fields from static classes"));
    resultBuffer.append(staticFields);

    resultBuffer.append(GenCommentHeader.gen("struct forwards"));
    resultBuffer.append(structsForwards);

    resultBuffer.append(GenCommentHeader.gen("arrays proto"));
    resultBuffer.append(arraysProto);

    resultBuffer.append(GenCommentHeader.gen("string proto"));
    resultBuffer.append(strProto);

    resultBuffer.append(GenCommentHeader.gen("string impls"));
    resultBuffer.append(strImpls);

    resultBuffer.append(GenCommentHeader.gen("interfaces proto"));
    resultBuffer.append(interfacesProto);

    resultBuffer.append(GenCommentHeader.gen("enums proto"));
    resultBuffer.append(enumsProto);

    resultBuffer.append(GenCommentHeader.gen("struct bodies"));
    resultBuffer.append(structsBodies);

    String stringLabls = GenBuiltinStringStaticLabels.gen(); // XXX: we MUST gen labels after we traversed all of the classes
    resultBuffer.append(GenCommentHeader.gen("string labels"));
    resultBuffer.append(stringLabls);

    resultBuffer.append(GenCommentHeader.gen("functions proto"));
    resultBuffer.append(funcsProto);

    resultBuffer.append(GenCommentHeader.gen("empties proto"));
    resultBuffer.append(emptiesProto);

    resultBuffer.append(GenCommentHeader.gen("assert true impl"));
    resultBuffer.append(assertTrueFunction);

    resultBuffer.append(GenCommentHeader.gen("arrays methods impls"));
    resultBuffer.append(arraysImpls);

    resultBuffer.append(GenCommentHeader.gen("interfaces methods impls"));
    resultBuffer.append(interfacesImpls);

    resultBuffer.append(GenCommentHeader.gen("functions impls"));
    resultBuffer.append(funcsImpls);

    resultBuffer.append(GenCommentHeader.gen("tests impls"));
    resultBuffer.append(testsImpls);

    resultBuffer.append(GenCommentHeader.gen("empties impls"));
    resultBuffer.append(emptiesImpls);

    // main method
    if (mainClassFuncs.size() != 1) {
      throw new AstParseException("cannot find main function");
    }
    final Function mainFn = mainClassFuncs.get(0);
    String mainMethodImpl = mainFn.toString();
    String mainMethodCall = mainFn.signToStringCall();

    resultBuffer.append(mainMethodImpl);
    resultBuffer.append("int main(int argc, char** argv) \n{\n");
    resultBuffer.append("    MANAGED_HEAP = map_new(hashmap_hash_ptr, hashmap_equal_ptr);\n\n");

    if (ParserMainOptions.GENERATE_CALL_STACK) {
      resultBuffer.append("    push_function(__func__, -1);\n");
    }

    resultBuffer.append("    __init_empties_static_data__();   \n");
    resultBuffer.append("    __run_all_tests__();              \n");
    resultBuffer.append("    int result = " + mainMethodCall + ";\n\n");
    resultBuffer.append("    printf(\"%d\\n\", result);\n");
    resultBuffer.append("    printf(\"%s\\n\", \":ok:\");\n");

    if (ParserMainOptions.GENERATE_CALL_STACK) {
      resultBuffer.append("    pop_function(__func__, -1);\n");
    }

    resultBuffer.append("    return result;\n");
    resultBuffer.append("\n}\n");

  }

  private ClassDeclaration cutMainClass() {
    Iterator<ClassDeclaration> iter = classes.iterator();

    while (iter.hasNext()) {
      ClassDeclaration c = iter.next();
      if (c.isMainClass()) {
        ClassDeclaration rv = c;
        iter.remove();
        return rv;
      }
    }

    throw new AstParseException("cannot find main class");
  }

  private List<ClassDeclaration> cutInterfaces() {

    Iterator<ClassDeclaration> iter = classes.iterator();
    List<ClassDeclaration> rv = new ArrayList<ClassDeclaration>();

    while (iter.hasNext()) {
      ClassDeclaration c = iter.next();
      if (c.isInterface()) {
        rv.add(c);
        iter.remove();
      }
    }

    return rv;
  }

  private List<ClassDeclaration> cutEnums() {

    Iterator<ClassDeclaration> iter = classes.iterator();
    List<ClassDeclaration> rv = new ArrayList<ClassDeclaration>();

    while (iter.hasNext()) {
      ClassDeclaration c = iter.next();
      if (c.isEnum()) {
        rv.add(c);
        iter.remove();
      }
    }

    return rv;
  }

  private ClassDeclaration cutString() {

    ClassDeclaration str = null;
    Iterator<ClassDeclaration> iter = classes.iterator();

    while (iter.hasNext()) {
      ClassDeclaration c = iter.next();
      if (c.isNativeString()) {
        str = c;
        iter.remove();
        break;
      }
    }

    NullChecker.check(str);
    return str;
  }

  private List<ClassDeclaration> cutArrays() {

    Iterator<ClassDeclaration> iter = classes.iterator();
    List<ClassDeclaration> rv = new ArrayList<ClassDeclaration>();

    while (iter.hasNext()) {
      ClassDeclaration c = iter.next();
      if (c.isNativeArray()) {
        rv.add(c);
        iter.remove();
      }
    }

    return rv;
  }

}
