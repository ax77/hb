package ast_templates;

import java.util.List;

import ast_class.ClassDeclaration;
import ast_mir.ApplyInstantiationUnit;
import ast_unit.CompilationUnit;
import ast_unit.InstantiationUnit;
import ast_unit.TypeDeclaration;

public class InstatantiationUnitBuilder {

  // we hold instantiation-unit in template-codegen, because we
  // have to expand the whole unit, and we should know every template
  // we expanded until the end of the source, because we reuse previously 
  // expanded templates at new invocation places, and after expansion we
  // store expanded class in instantiation-unit.
  // why do we need this class?
  // we traverse the whole syntax-tree, ignoring templated-classes, and
  // for each class we collect its type-setters (places where type may will change)
  // then - we try to expand this type if it is a template invocation
  // if it doesn't - the type is returning the same as it was before
  // as a result we have the whole classes - which was generated from templates,
  // and the rest of classes - where each template was changed with its type at
  // instantiation point.
  // that is: we ignore here all templates from compilation-unit,
  // expand all templates in normal classes, and collect the result

  private final TemplateCodegen templateCodegen;

  public InstatantiationUnitBuilder(CompilationUnit compilationUnit) {
    this.templateCodegen = new TemplateCodegen();
    visit(compilationUnit);
  }

  public InstantiationUnit getInstantiationUnit() {
    final InstantiationUnit instantiationUnit = templateCodegen.getInstantiationUnit();

    final ApplyInstantiationUnit applier = new ApplyInstantiationUnit();
    applier.visit(instantiationUnit);

    return instantiationUnit;
  }

  private void visit(CompilationUnit compilationUnit) {
    for (TypeDeclaration td : compilationUnit.getTypeDeclarations()) {
      final ClassDeclaration classDeclaration = td.getClassDeclaration();
      if (classDeclaration.isTemplate()) {
        continue;
      }

      List<TypeSetter> typeSetters = classDeclaration.getTypeSetters();
      for (TypeSetter ts : typeSetters) {
        typeset(ts);
      }
      templateCodegen.getInstantiationUnit().put(classDeclaration);
    }
  }

  private void typeset(TypeSetter typeSetter) {
    typeSetter.setType(templateCodegen.getTypeFromTemplate(typeSetter.getType()));
  }

}
