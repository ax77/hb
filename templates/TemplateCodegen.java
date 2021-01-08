package njast.templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jscan.hashed.Hash_ident;
import jscan.symtab.Ident;
import njast.ast_nodes.clazz.ClassConstructorDeclaration;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.top.InstantiationUnit;
import njast.ast_utils.SerializationUtils;
import njast.errors.EParseException;
import njast.types.Type;
import njast.types.TypeBindings;

public class TemplateCodegen {

  // temporaries
  private final List<Type> generatedClasses;
  private final HashMap<String, Dto> generatedClassesForReuse;

  // output for generated
  private final InstantiationUnit instantiationUnit;

  public TemplateCodegen() {
    this.generatedClasses = new ArrayList<>();
    this.generatedClassesForReuse = new HashMap<>();
    this.instantiationUnit = new InstantiationUnit();
  }

  public InstantiationUnit getInstantiationUnit() {
    return instantiationUnit;
  }

  public Type getTypeFromTemplate(final Type from) {
    if (!from.isClassTemplate()) {
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

    final String origName = from.getClassType().getIdentifier().getName();
    final String newName = buildNewName(origName, from.getClassType().getUniqueIdStr(), from.getTypeArguments());

    final ClassDeclaration templateClass = copyClazz(from.getClassType(), newName);
    final List<Type> typeArguments = from.getTypeArguments();
    final List<Type> typeParameters = templateClass.getTypeParametersT();

    if (typeArguments.size() != typeParameters.size()) {
      throw new EParseException("type parameters and type arguments are different by count.");
    }

    // I) fill all typename's with real types
    for (int i = 0; i < typeParameters.size(); i++) {
      Type ref = typeArguments.get(i);
      templateClass.getTypeParametersT().get(i).fillPropValues(ref);
    }

    // II) replace
    for (int i = 0; i < typeParameters.size(); i++) {
      Type ref = typeArguments.get(i);
      Ident typenameT = typeParameters.get(i).getTypeVariable();
      Type typeToSet = getTypeFromTemplate(ref);
      replaceOneTypeParam(templateClass, typenameT, typeToSet);
    }

    templateClass.setTypeParametersT(new ArrayList<Type>());
    final Type result = new Type(templateClass);

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

    generatedClassesForReuse.put(from.getClassType().getIdentifier().getName(), new Dto(from, typeArguments, result));
    generatedClasses.add(result);
    instantiationUnit.put(result.getClassType());

    return result;
  }

  private String buildNewName(String origNameOfTemplateClass, String classUniqueId, List<Type> typeArguments) {
    StringBuilder sb = new StringBuilder();
    sb.append(origNameOfTemplateClass);
    sb.append("_");
    sb.append(classUniqueId);
    sb.append("_");
    sb.append(typeArgumentsToStringForGeneratedName(typeArguments));
    return sb.toString();
  }

  // move toString() here, to guarantee that all names for generated templates will be unique
  // and not depends on general toString() which we may change one day or other.
  //
  private String typeToString(Type tp) {
    if (tp.isPrimitive()) {
      return TypeBindings.BIND_PRIMITIVE_TO_STRING.get(tp.getBase());
    }
    if (tp.isTypeVarRef()) {
      return tp.getTypeVariable().getName();
    }
    if (!tp.isClassRef()) {
      throw new EParseException("expect class-name");
    }
    return tp.getClassType().getIdentifier().getName();
  }

  private String typeArgumentsToStringForGeneratedName(List<Type> typeArguments) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < typeArguments.size(); i++) {
      Type tp = typeArguments.get(i);
      sb.append(typeToString(tp));
      if (i + 1 < typeArguments.size()) {
        sb.append("_");
      }
    }
    return sb.toString();
  }

  private Type presentedInGenerated2(Type result) {

    if (!result.isClassRef()) {
      throw new EParseException("expect class-type");
    }

    final ClassDeclaration classWeWantToFind = result.getClassType();
    final String name = classWeWantToFind.getIdentifier().getName();

    for (Type tp : generatedClasses) {
      if (tp.isClassRef()) {
        final ClassDeclaration classAlreadyGenerated = tp.getClassType();
        final boolean first = classAlreadyGenerated.getIdentifier().getName().equals(name);
        final boolean second = classAlreadyGenerated.getUniqueId() == classWeWantToFind.getUniqueId();
        if (first && second) {
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

    if (!from.isClassRef()) {
      throw new EParseException("expect class-type");
    }

    final String name = from.getClassType().getIdentifier().getName();
    final Dto storedResult = generatedClassesForReuse.get(name);

    if (storedResult == null) {
      return null;
    }

    final List<Type> previouslyExpandedArgs = storedResult.getTypeArguments();
    final List<Type> currentGivenAgrs = from.getTypeArguments();
    if (!typeArgumentListsAreEqualForTemplate(previouslyExpandedArgs, currentGivenAgrs)) {
      return null;
    }

    return storedResult.getResult();
  }

  private boolean typeArgumentListsAreEqualForTemplate(List<Type> first, List<Type> second) {

    if (first.size() != second.size()) {
      return false;
    }

    for (int i = 0; i < first.size(); i++) {
      Type tp1 = first.get(i);
      Type tp2 = second.get(i);
      if (!tp1.isEqualAsGeneric(tp2)) {
        return false;
      }
    }

    return true;
  }

  private ClassDeclaration copyClazz(ClassDeclaration given, String newname) {
    ClassDeclaration object = (ClassDeclaration) SerializationUtils.clone(given);
    object.setIdentifier(Hash_ident.getHashedIdent(newname));

    for (ClassConstructorDeclaration constructor : object.getConstructors()) {
      constructor.setIdentifier(Hash_ident.getHashedIdent(newname));
    }

    return object;
  }

  // is important to follow the order of expansion
  // 1) replace all plain type-names: [T item -> int item]
  // 2) replace all self-references: [ class Node<E> { Node<E> next; } ]
  // 3) expand all references with real type

  private void replaceOneTypeParam(ClassDeclaration object, Ident typenameT, Type typeToSet) {

    final List<TypeSetter> typeSetters = new GetAllTypeSetters(object).getTypeSetters();
    for (TypeSetter typeSetter : typeSetters) {
      fabric(typeSetter, object, typenameT, typeToSet);
    }

  }

  private void fabric(TypeSetter typeSetter, ClassDeclaration object, Ident typenameT, Type typeToSet) {

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

    if (!typeToCheck.isTypeVarRef()) {
      return false;
    }

    final Ident typeParameterName = typeToCheck.getTypeParameter();
    if (typeParameterName.equals(typenameT)) {
      return true;
    }

    return false;
  }

  private void maybeClearTypeParametersIfSelfReference(Type typeToCheck, ClassDeclaration object) {

    if (!typeToCheck.isClassRef()) {
      return;
    }

    final ClassDeclaration nested = typeToCheck.getClassType();

    if (nested.equals(object)) {
      // nested.getTypeParametersT().clear();
      nested.setTypeParametersT(new ArrayList<>());
    }

  }

  private Type maybeExpandTypeToNewType(Type typeToCheck) {

    if (!typeToCheck.isClassRef()) {
      return null;
    }

    final Type newtype = getTypeFromTemplate(typeToCheck);
    return newtype;

  }

}
