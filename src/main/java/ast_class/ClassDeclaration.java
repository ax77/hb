package ast_class;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import _st1_templates.TypeSetter;
import ast_expr.ExprExpression;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_printers.TypePrinters;
import ast_sourceloc.Location;
import ast_sourceloc.SourceLocation;
import ast_stmt.StmtBlock;
import ast_symtab.BuiltinNames;
import ast_symtab.Keywords;
import ast_types.Type;
import ast_types.TypeListsComparer;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import hashed.Hash_ident;
import tokenize.Ident;
import tokenize.Token;
import utils_oth.NullChecker;

public class ClassDeclaration implements Serializable, Location {

  private static final long serialVersionUID = 6225743252762855961L;

  /// class, interface, enum
  private final Ident keyword;
  private Modifiers modifiers;

  private Token beginPos;
  private final Ident identifier;
  private final List<ClassMethodDeclaration> constructors;
  private final List<VarDeclarator> fields;
  private final List<ClassMethodDeclaration> methods;
  private final List<StmtBlock> staticBlocks;
  private ClassMethodDeclaration destructor;

  /// if it is a class it may implements some interfaces
  ///
  private final List<InterfaceItem> interfaces;

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
  private List<Type> typeParametersT;
  private boolean typeParametersWasSet = false;

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

  public ClassDeclaration(Ident keyword, Ident identifier, List<Type> typeParametersT, Token beginPos) {
    NullChecker.check(keyword, identifier, typeParametersT, beginPos);
    checkTypeParameters(typeParametersT);

    this.keyword = keyword;
    this.modifiers = new Modifiers();
    this.identifier = identifier;
    this.typeParametersT = Collections.unmodifiableList(typeParametersT);
    this.beginPos = beginPos;

    // lists
    this.constructors = new ArrayList<>();
    this.fields = new ArrayList<>();
    this.methods = new ArrayList<>();
    this.staticBlocks = new ArrayList<>();
    this.typeSetters = new ArrayList<>();
    this.interfaces = new ArrayList<>();
  }

  private void checkTypeParameters(List<Type> typeParametersT) {
    for (Type tp : typeParametersT) {
      if (!tp.isTypenameID()) {
        throw new AstParseException("expect type-parameter, but was: " + tp.toString());
      }
    }
  }

  public void setTypeParametersT(List<Type> typeParametersT) {
    NullChecker.check(typeParametersT);

    for (Type tp : typeParametersT) {
      if (!tp.isTypenameID()) {
        throw new AstParseException("is not a type-parameter: " + tp.toString());
      }
    }

    // we may 'set' type-parameters if, and only if
    // this class was 'forward' defined, and when we begin
    // to parse the real class-declaration, and we found
    // the previously defined class, and set these type-parameters
    // to it.
    if (typeParametersWasSet) {
      throw new AstParseException("you cannot set type-parameters more than once.");
    }

    this.typeParametersT = typeParametersT;
    this.typeParametersWasSet = true;
  }

  public void setBeginPos(Token beginPos) {
    NullChecker.check(beginPos);
    this.beginPos = beginPos;
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

  public boolean isEqualTo(ClassDeclaration another) {
    if (this == another) {
      return true;
    }

    // class/interface/enum
    if (!keyword.equals(another.getKeyword())) {
      return false;
    }

    // type-name
    if (!identifier.equals(another.getIdentifier())) {
      return false;
    }

    if (!TypeListsComparer.typeListsAreEqual(typeParametersT, another.getTypeParametersT())) {
      return false;
    }

    // paranoia?
    if (fields.size() != another.getFields().size()) {
      System.out.println("not equal: fields");
      return false;
    }
    if (constructors.size() != another.getConstructors().size()) {
      System.out.println("not equal: constructors");
      return false;
    }
    if (methods.size() != another.getMethods().size()) {
      System.out.println("not equal: methods");
      return false;
    }
    if (staticBlocks.size() != another.getStaticBlocks().size()) {
      System.out.println("not equal: static {}");
      return false;
    }
    if (interfaces.size() != another.getInterfaces().size()) {
      System.out.println("not equal: interfaces");
      return false;
    }

    return true;
  }

  public boolean isClass() {
    return keyword.equals(Keywords.class_ident);
  }

  public boolean isInterface() {
    return keyword.equals(Keywords.interface_ident);
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

  public void addStaticBlock(StmtBlock block) {
    NullChecker.check(block);
    this.staticBlocks.add(block);
  }

  public void addInterfaceToImplements(InterfaceItem interfaceItem) {
    NullChecker.check(interfaceItem);

    registerTypeSetter(interfaceItem);
    this.interfaces.add(interfaceItem);
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

  public Modifiers getModifiers() {
    return modifiers;
  }

  public void setModifiers(Modifiers modifiers) {
    this.modifiers = modifiers;
  }

  public List<ClassMethodDeclaration> getConstructors() {
    return constructors;
  }

  public List<VarDeclarator> getFields() {
    return fields;
  }

  public Ident getKeyword() {
    return keyword;
  }

  public List<StmtBlock> getStaticBlocks() {
    return staticBlocks;
  }

  public List<InterfaceItem> getInterfaces() {
    return interfaces;
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
  private boolean isCompatibleByArguments(ClassMethodDeclaration method, List<ExprExpression> arguments) {
    List<VarDeclarator> formalParameters = method.getParameters();
    if (formalParameters.size() != arguments.size()) {
      return false;
    }
    for (int i = 0; i < formalParameters.size(); i++) {
      Type tp1 = formalParameters.get(i).getType();
      Type tp2 = arguments.get(i).getResultType();
      if (!tp1.isEqualTo(tp2)) {
        return false;
      }
    }
    return true;
  }

  public ClassMethodDeclaration getMethod(Ident name, List<ExprExpression> arguments) {
    for (ClassMethodDeclaration method : methods) {
      if (method.getIdentifier().equals(name)) {
        if (isCompatibleByArguments(method, arguments)) {
          return method;
        }
      }
    }
    return null;
  }

  public ClassMethodDeclaration getPredefinedMethod(Ident name) {
    for (ClassMethodDeclaration method : methods) {
      if (method.getIdentifier().equals(name)) {
        return method;
      }
    }
    throw new AstParseException("class has no method: " + name.toString());
  }

  public ClassMethodDeclaration getMethodForSure(String name) {
    ClassMethodDeclaration m = getPredefinedMethod(Hash_ident.getHashedIdent(name));
    if (m == null) {
      throw new AstParseException("class has no method: " + name.toString());
    }
    return m;
  }

  public boolean hasPredefinedMethod(Ident name) {
    for (ClassMethodDeclaration method : methods) {
      if (method.getIdentifier().equals(name)) {
        return true;
      }
    }
    return false;
  }

  public ClassMethodDeclaration getConstructor(List<ExprExpression> arguments) {
    for (ClassMethodDeclaration method : constructors) {
      if (isCompatibleByArguments(method, arguments)) {
        return method;
      }
    }
    return null;
  }

  private String interfacesToString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < interfaces.size(); i++) {
      InterfaceItem item = interfaces.get(i);
      sb.append(item.toString());
      if (i + 1 < interfaces.size()) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(keyword.toString() + " ");
    sb.append(identifier.getName());

    if (!typeParametersT.isEmpty()) {
      sb.append("_");
      sb.append(TypePrinters.typeArgumentsToString(typeParametersT));
    }

    if (!interfaces.isEmpty()) {
      sb.append(" implements ");
      sb.append(interfacesToString());
    }

    sb.append("\n{\n");

    for (VarDeclarator var : fields) {
      sb.append(var.toString() + "\n");
    }

    for (StmtBlock block : staticBlocks) {
      sb.append("static ");
      sb.append(block.toString());
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

  public boolean isMainClass() {
    return identifier.getName().equals("main_class");
  }

  public boolean isNativeArray() {
    return identifier.equals(BuiltinNames.array_ident);
  }

}
