package ast_st1_templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeListsComparer;
import ast_unit.InstantiationUnit;
import errors.AstParseException;
import hashed.Hash_ident;
import tokenize.Ident;
import utils_ser.SerializationUtils;

public class TemplateCodegen {

  // temporaries
  private final List<Type> generatedClasses;
  private final HashMap<String, Dto> generatedClassesForReuse;

  // output for generated
  private final InstantiationUnit instantiationUnit;

  //anti-recursion
  private List<ClassDeclaration> expansionState = new ArrayList<>();

  public TemplateCodegen() {
    this.generatedClasses = new ArrayList<>();
    this.generatedClassesForReuse = new HashMap<>();
    this.instantiationUnit = new InstantiationUnit();
  }

  public InstantiationUnit getInstantiationUnit() {
    return instantiationUnit;
  }

  public Type getTypeFromTemplate(final Type from) {
    if (!from.is_class_template()) {
      return from;
    }

    //anti-recursion
    if (from.getClassTypeFromRef().isNoexpand()) {
      return from;
    }

    // this means that we already generated class from given template with this name
    // and these type-parameters list.
    //
    Type ready = presentedInGeneratedByOriginalClassName(from);
    if (ready != null) {
      // System.out.println("hashed_1: " + from.toString());
      return ready;
    }

    final String newName = NameBuilder.buildNewName(from);

    final ClassDeclaration templateClass = copyClazz(from.getClassTypeFromRef(), newName);
    final List<Type> typeArguments = from.getTypeArgumentsFromRef();
    final List<Type> typeParameters = templateClass.getTypeParametersT();

    // we should store the original names of type-parameters:
    // class pair<K, V> {  } -> the K and V as identifiers.
    // bacause the typeParameters will be replaced, and we'll lose
    // the information
    final List<Ident> typeParametersNames = new ArrayList<>();
    for (Type typeparm : typeParameters) {
      typeParametersNames.add(typeparm.getTypenameId());
    }

    //anti-recursion
    expansionState.add(0, templateClass);

    if (typeArguments.size() != typeParameters.size()) {
      throw new AstParseException("type parameters and type arguments are different by count.");
    }

    // I) fill all typename's with real types
    for (int i = 0; i < typeParameters.size(); i++) {
      Type ref = typeArguments.get(i);
      templateClass.getTypeParametersT().get(i).fillPropValues(ref);
    }

    // II) replace
    for (int i = 0; i < typeParameters.size(); i++) {
      Type ref = typeArguments.get(i);
      Ident typenameT = typeParametersNames.get(i);
      Type typeToSet = getTypeFromTemplate(ref);
      replaceOneTypeParam(templateClass, typenameT, typeToSet);
    }

    templateClass.setTypeParametersT(new ArrayList<Type>());
    final Type result = new Type(new ClassTypeRef(templateClass, new ArrayList<>()));

    // TODO: how to do this more clean and precise?
    // if deep-nested templates generated many times, and they are the same
    // we can find them by their names only.
    // can we guarantee that it is a proper way to do this?
    // need more tests with code-review.
    //
    Type ready2 = presentedInGenerated2(result);
    if (ready2 != null) {
      // System.out.println("hashed_2: " + result.toString());
      return ready2;
    }

    generatedClassesForReuse.put(from.getClassTypeFromRef().getIdentifier().getName(),
        new Dto(from, typeArguments, result));
    generatedClasses.add(result);
    instantiationUnit.put(result.getClassTypeFromRef());

    return result;
  }

  private Type presentedInGenerated2(Type result) {

    if (!result.is_class()) {
      throw new AstParseException("expect class-type");
    }

    final ClassDeclaration classWeWantToFind = result.getClassTypeFromRef();
    final Ident classWeWantToFindId = classWeWantToFind.getIdentifier();

    for (Type tp : generatedClasses) {
      if (tp.is_class()) {
        final ClassDeclaration classAlreadyGenerated = tp.getClassTypeFromRef();
        final Ident classAlreadyGeneratedId = classAlreadyGenerated.getIdentifier();
        if (classAlreadyGeneratedId.equals(classWeWantToFindId)) {
          return tp;
        }
      }
    }

    return null;
  }

  private Type presentedInGeneratedByOriginalClassName(Type from) {

    if (generatedClassesForReuse.isEmpty()) {
      return null;
    }

    if (!from.is_class()) {
      throw new AstParseException("expect class-type");
    }

    final String name = from.getClassTypeFromRef().getIdentifier().getName();
    final Dto storedResult = generatedClassesForReuse.get(name);

    if (storedResult == null) {
      return null;
    }

    final List<Type> previouslyExpandedArgs = storedResult.getTypeArguments();
    final List<Type> currentGivenAgrs = from.getTypeArgumentsFromRef();
    if (!TypeListsComparer.typeListsAreEqual(previouslyExpandedArgs, currentGivenAgrs)) {
      return null;
    }

    return storedResult.getResult();
  }

  private ClassDeclaration copyClazz(ClassDeclaration given, String newname) {
    final Ident newIdentifier = Hash_ident.getHashedIdent(newname);

    final ClassDeclaration object = (ClassDeclaration) SerializationUtils.clone(given);
    object.setIdentifier(newIdentifier);

    return object;
  }

  // is important to follow the order of expansion
  // 1) replace all plain type-names: [T item -> int item]
  // 2) replace all self-references: [ class Node<E> { Node<E> next; } ]
  // 3) expand all references with real type

  private void replaceOneTypeParam(ClassDeclaration object, Ident typenameT, Type typeToSet) {

    final List<TypeSetter> typeSetters = object.getTypeSetters();
    for (TypeSetter typeSetter : typeSetters) {
      fabric(typeSetter, object, typenameT, typeToSet);
    }

  }

  private void fabric(TypeSetter typeSetter, ClassDeclaration object, Ident typenameT, Type typeToSet) {

    //anti-recursion
    for (ClassDeclaration cd : expansionState) {
      if (cd.equals(object)) {
        // System.out.println("mark noexpand: " + cd.getIdentifier().getName());
        cd.setNoexpand(true);
      }
    }

    Type typeToCheck = typeSetter.getType();

    // 1)
    if (maybeReplaceTypenameWithType(typeToCheck, typenameT)) {
      typeSetter.setType(typeToSet);
    }

    // 2)
    maybeClearTypeParametersIfSelfReference(typeToCheck, object);

    // 3)
    Type newTypeToSet = maybeExpandTypeToNewType(typeToCheck);
    if (newTypeToSet != null) {
      typeSetter.setType(newTypeToSet);
    }

  }

  private boolean maybeReplaceTypenameWithType(Type typeToCheck, Ident typenameT) {

    if (!typeToCheck.is_typename_id()) {
      return false;
    }

    final Ident typeParameterName = typeToCheck.getTypenameId();
    if (typeParameterName.equals(typenameT)) {
      return true;
    }

    return false;
  }

  private void maybeClearTypeParametersIfSelfReference(Type typeToCheck, ClassDeclaration object) {

    if (!typeToCheck.is_class()) {
      return;
    }

    final ClassDeclaration nested = typeToCheck.getClassTypeFromRef();

    if (nested.equals(object)) {
      nested.setTypeParametersT(new ArrayList<>());
    }

  }

  private Type maybeExpandTypeToNewType(Type typeToCheck) {

    if (!typeToCheck.is_class()) {
      return null;
    }

    final Type newtype = getTypeFromTemplate(typeToCheck);
    return newtype;

  }

}
