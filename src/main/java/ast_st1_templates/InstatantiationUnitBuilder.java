package ast_st1_templates;

import java.util.List;

import ast_class.ClassDeclaration;
import ast_st2_annotate.SymInstantiationUnitApplier;
import ast_types.Type;
import ast_unit.CompilationUnit;
import ast_unit.InstantiationUnit;
import utils_oth.NullChecker;

public class InstatantiationUnitBuilder {

  private final TemplateCodegen templateCodegen;
  private final InstantiationUnit instantiationUnit;
  private final CompilationUnit compilationUnit;

  public InstatantiationUnitBuilder(CompilationUnit compilationUnit) {
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
    for (ClassDeclaration c : templateCodegen.getGeneratedClasses()) {
      instantiationUnit.put(c);
    }
  }

  private void applyInstantiationUnit() {

    // resolve all symbols, identifiers, with scope rules,
    // and add result-type to each expression.
    final SymInstantiationUnitApplier applier = new SymInstantiationUnitApplier();
    applier.visit(instantiationUnit);

  }

  public InstantiationUnit getInstantiationUnit() {
    return instantiationUnit;
  }

}
