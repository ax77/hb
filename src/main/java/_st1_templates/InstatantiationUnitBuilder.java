package _st1_templates;

import java.io.IOException;
import java.util.List;

import _st2_annotate.ApplyUnit;
import ast_class.ClassDeclaration;
import ast_types.Type;
import ast_unit.CompilationUnit;
import ast_unit.InstantiationUnit;
import utils_oth.NullChecker;

public class InstatantiationUnitBuilder {

  private final TemplateCodegen templateCodegen;
  private final InstantiationUnit instantiationUnit;
  private final CompilationUnit compilationUnit;

  public InstatantiationUnitBuilder(CompilationUnit compilationUnit) throws IOException {
    NullChecker.check(compilationUnit);

    this.templateCodegen = new TemplateCodegen();
    this.instantiationUnit = new InstantiationUnit();
    this.compilationUnit = compilationUnit;

    expandTemplates();
    addGeneratedClasses();
    applyInstantiationUnit();
  }

  private void expandTemplates() {
    for (final ClassDeclaration c : compilationUnit.getClasses()) {
      final List<TypeSetter> typeSetters = c.getTypeSetters();
      for (TypeSetter ts : typeSetters) {
        final Type givenType = ts.getType();
        final Type typeFromTemplate = templateCodegen.getTypeFromTemplate(givenType);
        ts.setType(typeFromTemplate);
      }
      instantiationUnit.put(c);
    }
  }

  private void addGeneratedClasses() {
    for (Type tp : templateCodegen.getGeneratedClasses()) {
      final ClassDeclaration c = tp.getClassTypeFromRef();
      instantiationUnit.put(c);
    }
  }

  private void applyInstantiationUnit() throws IOException {

    // resolve all symbols, identifiers, with scope rules,
    // and add result-type to each expression.
    final ApplyUnit applier = new ApplyUnit(instantiationUnit);
    applier.visit();

  }

  public InstantiationUnit getInstantiationUnit() {
    return instantiationUnit;
  }

}
