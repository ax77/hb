package _st7_codeout.parts;

import java.util.List;

import _st3_linearize_expr.CEscaper;
import _st7_codeout.Ccode;
import _st7_codeout.ToStringsInternal;
import ast_class.ClassDeclaration;
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

    for (ClassDeclaration c : classes) {

      for (ClassMethodDeclaration m : c.getTests()) {
        final String className = m.getClazz().getIdentifier().toString();
        final String testName = CEscaper.unquote(m.getTestName());

        String name = "\"" + className + " :: " + testName + "\"";
        String sign = ToStringsInternal.signToStringCall(m);

        impls.append("\nprintf(\"test: %s\\n\", " + name + ");\n");
        impls.append(sign + "();\n");
      }
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
