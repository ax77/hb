package ast_st1_templates;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import ast_class.ClassDeclaration;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeListsComparer;
import ast_unit.InstantiationUnit;
import errors.AstParseException;
import tokenize.Ident;
import utils_oth.NullChecker;
import utils_ser.SerializationUtils;

public class TemplateCodegen {

  // hashed results, already expanded classes
  private final HashMap<Type, List<Type>> generatedClassesResult;
  private final HashMap<Type, List<Type>> generatedClassesTemporary;

  // output for generated
  private final InstantiationUnit instantiationUnit;

  public TemplateCodegen() {
    this.generatedClassesResult = new HashMap<>();
    this.instantiationUnit = new InstantiationUnit();
    this.generatedClassesTemporary = new HashMap<>();
  }

  public InstantiationUnit getInstantiationUnit() {
    return instantiationUnit;
  }

  public Type getTypeFromTemplate(final Type from) {

    if (!from.is_class_template()) {
      return from;
    }

    // if deep-nested templates generated many times, and they are the same
    // we can find them by their names and previously given type-arguments.
    //
    final Type alreadyGenerated1 = presentedInGenerated(from, from.getTypeArgumentsFromRef(),
        generatedClassesTemporary);
    if (alreadyGenerated1 != null) {
      return alreadyGenerated1;
    }
    generatedClassesTemporary.put(from, from.getTypeArgumentsFromRef());

    final ClassDeclaration template = copyClazz(from.getClassTypeFromRef());
    final List<Type> typeArguments = Collections.unmodifiableList(from.getTypeArgumentsFromRef());

    if (typeArguments.size() != template.getTypeParametersT().size()) {
      throw new AstParseException("type parameters and type arguments are different by count.");
    }

    for (int i = 0; i < typeArguments.size(); i++) {
      // replace 'T' with given type like i32, [i32], list<i32>, etc...
      template.getTypeParametersT().get(i).fillPropValues(typeArguments.get(i));
    }

    // expand the whole template type-setters recursively
    final List<TypeSetter> typeSetters = template.getTypeSetters();
    for (final TypeSetter ts : typeSetters) {
      final Type maybeShouldExpandIt = ts.getType();
      ts.setType(getTypeFromTemplate(maybeShouldExpandIt));
    }

    // if deep-nested templates generated many times, and they are the same
    // we can find them by their names and previously given type-arguments.
    //
    final Type alreadyGenerated2 = presentedInGenerated(from, typeArguments, generatedClassesResult);
    if (alreadyGenerated2 != null) {
      return alreadyGenerated2;
    }

    // template.setTypeParametersT(new ArrayList<>());
    final Type result = new Type(new ClassTypeRef(template, typeArguments));

    generatedClassesResult.put(result, typeArguments);
    instantiationUnit.put(result.getClassTypeFromRef());

    return result;
  }

  private Type presentedInGenerated(Type result, List<Type> typeArguments, HashMap<Type, List<Type>> generatedClasses) {
    checkIsClass(result);

    final ClassDeclaration classWeWantToFind = result.getClassTypeFromRef();
    final Ident classWeWantToFindId = classWeWantToFind.getIdentifier();

    for (Entry<Type, List<Type>> hashed : generatedClasses.entrySet()) {
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

  private Type copyType(Type given) {
    return (Type) SerializationUtils.clone(given);
  }

  private ClassDeclaration copyClazz(ClassDeclaration given) {
    return (ClassDeclaration) SerializationUtils.clone(given);
  }

}
