package ast_method;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ast.IUniqueId;
import ast.UniqueCounter;
import ast_class.ClassDeclaration;
import ast_sourceloc.ILocation;
import ast_sourceloc.SourceLocation;
import ast_st1_templates.TypeSetter;
import ast_stmt.StmtBlock;
import ast_symtab.IdentMap;
import ast_types.Type;
import tokenize.Ident;
import utils_oth.NullChecker;

public class ClassMethodDeclaration implements Serializable, TypeSetter, ILocation, IUniqueId {

  private static final long serialVersionUID = 2982374768194205119L;

  /// Definition: 
  /// Two of the components of a method declaration comprise the method signature—the method's name and the parameter types.

  // init: clazz, parameters, body
  // deinit: clazz, body
  // func: clazz, header, parameters, body

  private final ClassMethodBase base;
  private final ClassDeclaration clazz;
  private final MethodSignature signature;
  private /*final*/ Type returnType;
  private final StmtBlock block;
  private final SourceLocation location;
  private final int uniqueId;

  // function/init
  public ClassMethodDeclaration(ClassMethodBase base, ClassDeclaration clazz, MethodSignature signature,
      Type returnType, StmtBlock block, SourceLocation location) {

    NullChecker.check(clazz, signature, block, location);

    this.base = base;
    this.clazz = clazz;
    this.signature = signature;
    this.returnType = returnType;
    this.block = block;
    this.location = location;
    this.uniqueId = UniqueCounter.getUniqueId();

  }

  // deinit
  public ClassMethodDeclaration(ClassDeclaration clazz, StmtBlock block, SourceLocation location) {

    NullChecker.check(clazz, block, location);

    this.base = ClassMethodBase.IS_DESTRUCTOR;
    this.clazz = clazz;
    this.signature = new MethodSignature(IdentMap.deinit_ident, new ArrayList<>());
    this.returnType = new Type();
    this.block = block;
    this.location = location;
    this.uniqueId = UniqueCounter.getUniqueId();
  }

  @Override
  public Type getType() {
    return returnType;
  }

  @Override
  public void setType(Type typeToSet) {
    this.returnType = typeToSet;
  }

  public MethodSignature getSignature() {
    return signature;
  }

  public ClassMethodBase getBase() {
    return base;
  }

  public ClassDeclaration getClazz() {
    return clazz;
  }

  public List<MethodParameter> getParameters() {
    return signature.getParameters();
  }

  public boolean isVoid() {
    return isFunction() && getType().is_void_stub();
  }

  public StmtBlock getBlock() {
    return block;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    if (isFunction()) {
      sb.append("func ");
      sb.append(signature.toString());
    }

    if (isConstructor()) {
      sb.append(signature.toString());
    }

    if (isDestructor()) {
      sb.append("deinit");
    }

    if (isFunction() && !isVoid()) {
      sb.append(" -> ");
      sb.append(getType().toString());
    }

    sb.append("\n{\n");
    sb.append(block.toString());
    sb.append("\n}\n");

    return sb.toString();
  }

  public boolean isDestructor() {
    return base == ClassMethodBase.IS_DESTRUCTOR;
  }

  public boolean isFunction() {
    return base == ClassMethodBase.IS_FUNC;
  }

  public boolean isConstructor() {
    return base == ClassMethodBase.IS_CONSTRUCTOR;
  }

  public Ident getIdentifier() {
    return signature.getMethodName();
  }

  @Override
  public SourceLocation getLocation() {
    return location;
  }

  @Override
  public String getLocationToString() {
    return location.toString();
  }

  @Override
  public String getUniqueIdToString() {
    return String.format("%d", uniqueId);
  }

  @Override
  public int getUniqueId() {
    return uniqueId;
  }

}
