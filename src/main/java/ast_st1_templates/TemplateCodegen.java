package ast_st1_templates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeListsComparer;
import errors.AstParseException;
import tokenize.Ident;
import utils_oth.NullChecker;
import utils_ser.SerializationUtils;

public class TemplateCodegen {

  private final List<Type> generatedClasses;

  public TemplateCodegen() {
    this.generatedClasses = new ArrayList<>();
  }

  public List<Type> getGeneratedClasses() {
    return generatedClasses;
  }

  public Type getTypeFromTemplate(final Type from) {

    if (!from.is_class_template()) {
      return from;
    }

    /// if deep-nested templates generated many times, and they are the same
    /// we can find them by their names and previously given type-arguments.
    ///
    final Type alreadyGenerated = presentedInGenerated(from);
    if (alreadyGenerated != null) {
      return alreadyGenerated;
    }

    /// we store the 'result' by its 'ref', and then we will expand the rest of
    /// the template recursively, at each recursive call we'll check
    /// that expanded class may be had been expanded previously,
    /// by its name and given type-arguments, if we found it - 
    /// we can return it, and prevent the stack-overflow.
    /// it works also with self-referenced classes like list/tree/node/entry/etc.
    /// where class has fields with the type of itself:
    /// class node<T> { var prev: node<T>; var next: node<T>; }
    /// when we will begin with expansion of that class, we will
    /// paste type-arguments instead of its type-parameters and then
    /// recursively expand the body of the template 
    /// (fields, method-return, method-params, etc...)
    /// all its type-setters... and if some type-setter has the same name
    /// and its type-parameters are set by type-arguments that has this 'type'
    /// at this stage - it means that we should not expand this class 
    /// again and again, and can just return the previously saved ref.
    ///
    final ClassDeclaration template = copyClazz(from.getClassTypeFromRef());
    final List<Type> typeArguments = Collections.unmodifiableList(from.getTypeArgumentsFromRef());
    final Type result = new Type(new ClassTypeRef(template, typeArguments), template.getBeginPos());
    generatedClasses.add(result);

    if (typeArguments.size() != template.getTypeParametersT().size()) {
      throw new AstParseException("type parameters and type arguments are different by count.");
    }

    /// replace each type-parameter 'T' 
    /// with given type like i32, [i32], list<i32>, etc...
    for (int i = 0; i < typeArguments.size(); i++) {
      template.getTypeParametersT().get(i).fillPropValues(typeArguments.get(i));
    }

    /// expand the whole template type-setters recursively
    final List<TypeSetter> typeSetters = template.getTypeSetters();
    for (final TypeSetter ts : typeSetters) {
      final Type maybeShouldExpandIt = ts.getType();
      ts.setType(getTypeFromTemplate(maybeShouldExpandIt));
    }

    return result;
  }

  private Type presentedInGenerated(final Type result) {
    checkIsClass(result);

    final ClassDeclaration classWeWantToFind = result.getClassTypeFromRef();
    final Ident classWeWantToFindId = classWeWantToFind.getIdentifier();
    final List<Type> typeArgumentsWeWantToFind = result.getTypeArgumentsFromRef();

    for (Type prevType : generatedClasses) {
      checkIsClass(prevType);

      final ClassDeclaration prevClazz = prevType.getClassTypeFromRef();
      final Ident prevIdent = prevClazz.getIdentifier();
      if (prevIdent.equals(classWeWantToFindId)) {
        /// if this class had been expanded previously - it means
        /// that ist type-parameters was replaced with the 
        /// type-arguments, i.e. given-types, and we can compare
        /// these lists, and be sure that if these lists are the same
        /// that means that this class was expanded previously, or will be 
        /// expanded later, we can return this ref.
        final List<Type> typeParametersPrevClazz = prevClazz.getTypeParametersT();
        if (TypeListsComparer.typeListsAreEqual(typeParametersPrevClazz, typeArgumentsWeWantToFind)) {
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
