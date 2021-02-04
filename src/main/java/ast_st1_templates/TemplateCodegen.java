package ast_st1_templates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import ast_class.ClassDeclaration;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeListsComparer;
import errors.AstParseException;
import tokenize.Ident;
import utils_oth.NullChecker;
import utils_ser.SerializationUtils;

public class TemplateCodegen {

  // classes, that was created from templates
  private final List<ClassDeclaration> generatedClasses;

  // hashed results, already expanded classes
  private final HashMap<Type, List<Type>> generatedClassesTemporary;

  public TemplateCodegen() {
    this.generatedClasses = new ArrayList<>();
    this.generatedClassesTemporary = new HashMap<>();
  }

  public List<ClassDeclaration> getGeneratedClasses() {
    return generatedClasses;
  }

  public Type getTypeFromTemplate(final Type from) {

    if (!from.is_class_template()) {
      return from;
    }

    // if deep-nested templates generated many times, and they are the same
    // we can find them by their names and previously given type-arguments.
    //
    final Type alreadyGenerated = presentedInGenerated(from, from.getTypeArgumentsFromRef());
    if (alreadyGenerated != null) {
      return alreadyGenerated;
    }
    generatedClassesTemporary.put(from, Collections.unmodifiableList(from.getTypeArgumentsFromRef()));

    final ClassDeclaration template = copyClazz(from.getClassTypeFromRef());
    final List<Type> typeArguments = Collections.unmodifiableList(from.getTypeArgumentsFromRef());

    if (typeArguments.size() != template.getTypeParametersT().size()) {
      throw new AstParseException("type parameters and type arguments are different by count.");
    }

    // replace each type-parameter 'T' 
    // with given type like i32, [i32], list<i32>, etc...
    for (int i = 0; i < typeArguments.size(); i++) {
      template.getTypeParametersT().get(i).fillPropValues(typeArguments.get(i));
    }

    // expand the whole template type-setters recursively
    final List<TypeSetter> typeSetters = template.getTypeSetters();
    for (final TypeSetter ts : typeSetters) {
      final Type maybeShouldExpandIt = ts.getType();
      ts.setType(getTypeFromTemplate(maybeShouldExpandIt));
    }

    final Type result = new Type(new ClassTypeRef(template, typeArguments), template.getBeginPos());
    generatedClasses.add(template);

    return result;
  }

  private Type presentedInGenerated(final Type result, final List<Type> typeArguments) {
    checkIsClass(result);

    final ClassDeclaration classWeWantToFind = result.getClassTypeFromRef();
    final Ident classWeWantToFindId = classWeWantToFind.getIdentifier();

    for (Entry<Type, List<Type>> hashed : generatedClassesTemporary.entrySet()) {
      final Type prevType = hashed.getKey();
      checkIsClass(prevType);

      final ClassDeclaration prevClazz = prevType.getClassTypeFromRef();
      final Ident prevIdent = prevClazz.getIdentifier();
      if (prevIdent.equals(classWeWantToFindId)) {
        if (TypeListsComparer.typeListsAreEqual(hashed.getValue(), typeArguments)) {
          return prevType;
        }
      }
    }

    return null;
  }

  private void checkIsClass(Type result) {
    NullChecker.check(result);
    if (!result.is_class()) {
      throw new AstParseException("expect class-type, but was: " + result.toString());
    }
  }

  private ClassDeclaration copyClazz(ClassDeclaration given) {
    return (ClassDeclaration) SerializationUtils.clone(given);
  }

}
