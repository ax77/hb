package ast_st1_templates;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_unit.InstantiationUnit;
import errors.AstParseException;
import hashed.Hash_ident;
import tokenize.Ident;
import utils_ser.SerializationUtils;

public class TemplateCodegen {

  private final List<Type> generatedClasses;

  // output for generated
  private final InstantiationUnit instantiationUnit;

  public TemplateCodegen() {
    this.generatedClasses = new ArrayList<>();
    this.instantiationUnit = new InstantiationUnit();
  }

  public InstantiationUnit getInstantiationUnit() {
    return instantiationUnit;
  }

  public Type getTypeFromTemplate(final Type from) {

    if (!from.is_class_template()) {
      return from;
    }

    if (from.isPainted()) {
      return from;
    }

    if (from.getClassTypeFromRef().isHidden()) {
      Type copyof = copyType(from);
      copyof.paint();
      return copyof;
    }

    final String newName = NameBuilder.buildNewName(from);
    final ClassDeclaration template = copyClazz(from.getClassTypeFromRef(), newName);
    final List<Type> givenTypes = from.getTypeArgumentsFromRef();

    if (givenTypes.size() != template.getTypeParametersT().size()) {
      throw new AstParseException("type parameters and type arguments are different by count.");
    }

    for (int i = 0; i < givenTypes.size(); i++) {
      // replace 'T' with given type like i32, [i32], list<i32>, etc...
      template.getTypeParametersT().get(i).fillPropValues(givenTypes.get(i));
    }

    // we should hide template before substitution
    // now we have all promoted typenames
    // 
    template.hide();

    // expand the whole template type-setters recursively
    final List<TypeSetter> typeSetters = template.getTypeSetters();
    for (final TypeSetter ts : typeSetters) {
      if (ts instanceof TypeSpecialUnhide) {
        TypeSpecialUnhide specUnhide = (TypeSpecialUnhide) ts;
        specUnhide.unhide();
      }
      final Type maybeShouldExpandIt = ts.getType();
      ts.setType(getTypeFromTemplate(maybeShouldExpandIt));
    }

    template.setTypeParametersT(new ArrayList<>());
    final Type result = new Type(new ClassTypeRef(template, new ArrayList<>()));

    // if deep-nested templates generated many times, and they are the same
    // we can find them by their names only.
    // can we guarantee that it is a proper way to do this?
    // need more tests with code-review.
    //
    final Type alreadyGenerated = presentedInGenerated(result);
    if (alreadyGenerated != null) {
      return alreadyGenerated;
    }

    generatedClasses.add(result);
    instantiationUnit.put(result.getClassTypeFromRef());

    return result;
  }

  private Type presentedInGenerated(Type result) {

    if (!result.is_class()) {
      throw new AstParseException("expect class-type");
    }

    final ClassDeclaration classWeWantToFind = result.getClassTypeFromRef();
    final Ident classWeWatnToFindId = classWeWantToFind.getIdentifier();

    for (Type tp : generatedClasses) {
      if (!tp.is_class()) {
        continue;
      }
      final ClassDeclaration classAlreadyGenerated = tp.getClassTypeFromRef();
      final Ident alreadyGeneratedId = classAlreadyGenerated.getIdentifier();
      if (alreadyGeneratedId.equals(classWeWatnToFindId)) {
        return tp;
      }
    }

    return null;
  }

  private Type copyType(Type given) {
    return (Type) SerializationUtils.clone(given);
  }

  private ClassDeclaration copyClazz(ClassDeclaration given, String newName) {
    final ClassDeclaration object = (ClassDeclaration) SerializationUtils.clone(given);
    object.setIdentifier(Hash_ident.getHashedIdent(newName));
    return object;
  }

}
