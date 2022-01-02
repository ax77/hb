package _st7_codeout.parts;

import java.util.List;

import _st3_linearize_expr.CEscaper;
import _st7_codeout.Ccode;
import _st7_codeout.ToStringsInternal;
import ast_class.ClassDeclaration;
import ast_main.ParserMainOptions;
import ast_method.ClassMethodDeclaration;

public class GenUnittests implements Ccode {

  private final List<ClassDeclaration> classes;
  private final StringBuilder proto;
  private final StringBuilder impls;

  private static final String RUN_ALL_TESTS_METHOD_NAME = "__run_all_tests__";

  public GenUnittests(List<ClassDeclaration> classes) {
    this.classes = classes;
    this.proto = new StringBuilder();
    this.impls = new StringBuilder();
    gen();
  }

  private void gen() {
    genMainTestMethodImpl();
  }

  private String genMainTestMethodImpl() {
    impls.append("static void " + RUN_ALL_TESTS_METHOD_NAME + "() \n{\n");

    if (ParserMainOptions.GENERATE_CALL_STACK) {
      impls.append("push_function(\"test_runner\", -1);\n");
    }

    //printf("test: %s\n", "test_string_class :: test string right");
    //test_string_class_test_54();

    int tot = 0;
    for (ClassDeclaration c : classes) {
      tot += c.getTests().size();
    }

    final String testTotal = String.format("%d", tot);
    int testNum = 1;

    for (ClassDeclaration c : classes) {

      final List<ClassMethodDeclaration> tests = c.getTests();

      for (ClassMethodDeclaration m : tests) {
        final String className = m.getClazz().getIdentifier().toString();
        final String testName = CEscaper.unquote(m.getTestName());

        String name = "\"" + className + " :: " + testName + "\"";
        String sign = ToStringsInternal.signToStringCall(m);

        String pushFuncSignature = ToStringsInternal.signToStringCallPushF(m);
        String location = String.format("%d", m.getLocation().getLine());

        impls.append("\nprintf(\"%d/%d test: %s\", " + String.format("%d", testNum) + ", " + testTotal + ", " + name
            + ");\n");

        if (ParserMainOptions.GENERATE_CALL_STACK) {
          impls.append("push_function(" + "\"" + "test_runner::" + pushFuncSignature + "\"" + ", " + location + ");\n");
        }

        impls.append(sign + "();\n");
        impls.append("printf(\"%s\\n\", \" OK\");");

        if (ParserMainOptions.GENERATE_CALL_STACK) {
          impls.append("pop_function(" + "\"" + "test_runner::" + pushFuncSignature + "\"" + ", " + location + ");\n");
        }

        testNum += 1;
      }
    }

    if (ParserMainOptions.GENERATE_CALL_STACK) {
      impls.append("pop_function(\"test_runner\", -1);\n");
    }

    impls.append("\n}\n");
    return impls.toString();
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
