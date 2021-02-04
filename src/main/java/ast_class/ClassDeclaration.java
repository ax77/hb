package ast_class;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast_expr.FuncArg;
import ast_method.ClassMethodDeclaration;
import ast_sourceloc.Location;
import ast_sourceloc.SourceLocation;
import ast_st1_templates.TypeSetter;
import ast_types.Type;
import ast_types.TypeListsComparer;
import ast_types.TypePrinters;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import tokenize.Ident;
import tokenize.Token;
import utils_oth.NullChecker;

public class ClassDeclaration implements Serializable, Location {

  private static final long serialVersionUID = 6225743252762855961L;

  private final Token beginPos;
  private final Ident identifier;
  private final List<ClassMethodDeclaration> constructors;
  private final List<VarDeclarator> fields;
  private final List<ClassMethodDeclaration> methods;
  private ClassMethodDeclaration destructor;

  /// the class is incomplete before '}'
  /// we will use this flag to be sure that
  /// we can bind all unresolved identifiers to
  /// fully-defined classes.
  ///
  private boolean isComplete;

  /// we store type-variables as original references to Type
  ///
  /// each type-variable is an identifier like: 
  /// class Pair<K, V> { K key; V value; Pair<K, V> hash; }
  /// where type-variables are: K and V in this example
  ///
  /// when we'll expand template, 
  /// we'll replace each at once in every places it used by its pointer
  /// it is important to not screw this reference up before
  ///
  private final List<Type> typeParametersT;

  /// we'll collect all type-setters here
  /// type-setter is an variable, new-expression, method-parameter, etc...
  /// each type-setter in a class is an unique pointer.
  /// when we'll expand templates - we can iterate over this list,
  /// instead of the iteration over the whole AST, 
  /// and paste instead of each type-parameter the real type which was given
  /// var flags: list<i32>;
  /// we'll set 'i32' for each type parameter
  /// we expand each type-parameter step by step, and compare the 
  /// names, if it is a simple type-parameter like 'T' -> class pair<K, V> { K key; V value; }
  /// there: K and V are 'plain' type-parameters, and for declaration like [var p: pair<i32, u64>;] 
  /// we'll compare that type-parameters.size() == type-arguments.size() and
  /// we'll iterate over each type-parameter, and set the real type (i32, u64) instead of
  /// its name ('T')
  ///
  private final List<TypeSetter> typeSetters;

  public ClassDeclaration(Ident identifier, List<Type> typeParametersT, Token beginPos) {
    NullChecker.check(identifier, typeParametersT, beginPos);
    checkTypeParameters(typeParametersT);

    this.identifier = identifier;
    this.typeParametersT = Collections.unmodifiableList(typeParametersT);
    this.beginPos = beginPos;

    // lists
    this.constructors = new ArrayList<>();
    this.fields = new ArrayList<>();
    this.methods = new ArrayList<>();
    this.typeSetters = new ArrayList<>();
  }

  private void checkTypeParameters(List<Type> typeParametersT) {
    for (Type tp : typeParametersT) {
      if (!tp.is_typename_id()) {
        throw new AstParseException("expect type-parameter, but was: " + tp.toString());
      }
    }
  }

  public ClassMethodDeclaration getDestructor() {
    return destructor;
  }

  public void setDestructor(ClassMethodDeclaration destructor) {
    NullChecker.check(destructor);
    this.destructor = destructor;
  }

  public boolean isComplete() {
    return isComplete;
  }

  public void setComplete(boolean isComplete) {
    this.isComplete = isComplete;
  }

  public boolean is_equal_to(ClassDeclaration another) {
    if (this == another) {
      return true;
    }

    final Ident anotherId = another.getIdentifier();
    if (!identifier.equals(anotherId)) {
      return false;
    }

    if (!TypeListsComparer.typeListsAreEqual(typeParametersT, another.getTypeParametersT())) {
      return false;
    }

    // paranoia?
    if (fields.size() != another.getFields().size()) {
      return false;
    }
    if (constructors.size() != another.getConstructors().size()) {
      return false;
    }
    if (methods.size() != another.getMethods().size()) {
      return false;
    }

    return true;
  }

  public void registerTypeSetter(TypeSetter ts) {
    NullChecker.check(ts);
    this.typeSetters.add(ts);
  }

  public List<TypeSetter> getTypeSetters() {
    return typeSetters;
  }

  public Ident getIdentifier() {
    return identifier;
  }

  public void addConstructor(ClassMethodDeclaration e) {
    NullChecker.check(e);

    registerMethodParameters(e);
    this.constructors.add(e);
  }

  public void addField(VarDeclarator e) {
    NullChecker.check(e);

    registerTypeSetter(e);
    this.fields.add(e);
  }

  public void addMethod(ClassMethodDeclaration e) {
    NullChecker.check(e);

    if (!e.isVoid()) {
      registerTypeSetter(e);
    }

    registerMethodParameters(e);
    this.methods.add(e);
  }

  public void registerMethodParameters(ClassMethodDeclaration e) {
    for (VarDeclarator param : e.getParameters()) {
      registerTypeSetter(param);
    }
  }

  public List<ClassMethodDeclaration> getConstructors() {
    return constructors;
  }

  public List<VarDeclarator> getFields() {
    return fields;
  }

  public List<ClassMethodDeclaration> getMethods() {
    return methods;
  }

  public List<Type> getTypeParametersT() {
    return typeParametersT;
  }

  public boolean hasTypeParameter(Ident typenameT) {
    for (Type ref : typeParametersT) {
      if (ref.getTypenameId().equals(typenameT)) {
        return true;
      }
    }
    return false;
  }

  public boolean isTemplate() {
    return !typeParametersT.isEmpty();
  }

  public VarDeclarator getField(Ident name) {
    for (VarDeclarator field : fields) {
      if (field.getIdentifier().equals(name)) {
        return field;
      }
    }
    return null;
  }

  // TODO:
  private boolean isCompatibleByArguments(ClassMethodDeclaration method, List<FuncArg> arguments) {
    List<VarDeclarator> formalParameters = method.getParameters();
    if (formalParameters.size() != arguments.size()) {
      return false;
    }
    for (int i = 0; i < formalParameters.size(); i++) {
      Type tp1 = formalParameters.get(i).getType();
      Type tp2 = arguments.get(i).getExpression().getResultType();
      if (!tp1.is_equal_to(tp2)) {
        return false;
      }
    }
    return true;
  }

  public ClassMethodDeclaration getMethod(Ident name, List<FuncArg> arguments) {
    for (ClassMethodDeclaration method : methods) {
      if (method.getIdentifier().equals(name)) {
        if (isCompatibleByArguments(method, arguments)) {
          return method;
        }
      }
    }
    return null;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ");
    sb.append(identifier.getName());

    if (!typeParametersT.isEmpty()) {
      sb.append(TypePrinters.typeArgumentsToString(typeParametersT));
    }

    sb.append("\n{\n");

    for (VarDeclarator var : fields) {
      sb.append(var.toString() + "\n");
    }

    for (ClassMethodDeclaration constructor : constructors) {
      sb.append(constructor.toString() + "\n");
    }

    if (destructor != null) {
      sb.append(destructor.toString() + "\n");
    }

    for (ClassMethodDeclaration method : methods) {
      sb.append(method.toString() + "\n");
    }

    sb.append("\n}\n");

    return sb.toString();
  }

  public Type getTypeParameter(Ident ident) {
    for (Type ref : typeParametersT) {
      if (ref.getTypenameId().equals(ident)) {
        return ref;
      }
    }
    throw new AstParseException("unknown typename: " + ident.getName());
  }

  @Override
  public SourceLocation getLocation() {
    return beginPos.getLocation();
  }

  @Override
  public String getLocationToString() {
    return beginPos.getLocationToString();
  }

  @Override
  public Token getBeginPos() {
    return beginPos;
  }

}
