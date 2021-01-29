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
import ast_symtab.Keywords;
import ast_types.Type;
import tokenize.Ident;
import tokenize.Token;
import utils_oth.NullChecker;

public class ClassMethodDeclaration implements Serializable, TypeSetter, ILocation, IUniqueId {

  private static final long serialVersionUID = 2982374768194205119L;

  /// Definition: 
  /// Two of the components of a method declaration comprise the method signature—the method's name and the parameter types.

  // init: clazz, parameters, body
  // deinit: clazz, body
  // func: clazz, header, parameters, body

  private final ClassMethodBase base;
  private final Token beginPos;
  private final ClassDeclaration clazz;
  private final Ident identifier;
  private final List<MethodParameter> parameters;
  private /*final*/ Type returnType;
  private final StmtBlock block;
  private final int uniqueId;

  // function/init
  public ClassMethodDeclaration(ClassMethodBase base, ClassDeclaration clazz, Ident identifier,
      List<MethodParameter> parameters, Type returnType, StmtBlock block, Token beginPos) {

    NullChecker.check(base, clazz, identifier, parameters, returnType, block, beginPos);

    this.base = base;
    this.clazz = clazz;
    this.identifier = identifier;
    this.parameters = parameters;
    this.returnType = returnType;
    this.block = block;
    this.beginPos = beginPos;
    this.uniqueId = UniqueCounter.getUniqueId();

  }

  // deinit
  public ClassMethodDeclaration(ClassDeclaration clazz, StmtBlock block, Token beginPos) {

    NullChecker.check(clazz, block, beginPos);

    this.base = ClassMethodBase.IS_DESTRUCTOR;
    this.clazz = clazz;
    this.identifier = Keywords.deinit_ident;
    this.parameters = new ArrayList<>();
    this.returnType = new Type();
    this.block = block;
    this.beginPos = beginPos;
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

  public ClassMethodBase getBase() {
    return base;
  }

  public ClassDeclaration getClazz() {
    return clazz;
  }

  public List<MethodParameter> getParameters() {
    return parameters;
  }

  public boolean isVoid() {
    return isFunction() && getType().is_void_stub();
  }

  public StmtBlock getBlock() {
    return block;
  }

  private String parametersToString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");

    for (int i = 0; i < parameters.size(); i++) {
      MethodParameter param = parameters.get(i);
      sb.append(param.toString());

      if (i + 1 < parameters.size()) {
        sb.append(", ");
      }
    }

    sb.append(")");
    return sb.toString();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    if (isFunction()) {
      sb.append("func ");
      sb.append(identifier);
      sb.append(parametersToString());
    }

    if (isConstructor()) {
      sb.append(identifier);
      sb.append(parametersToString());
    }

    if (isDestructor()) {
      sb.append("deinit");
    }

    if (isFunction() && !isVoid()) {
      sb.append(" -> ");
      sb.append(getType().toString());
    }

    sb.append(block.toString());
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
    return identifier;
  }

  @Override
  public SourceLocation getLocation() {
    return beginPos.getLocation();
  }

  @Override
  public String getLocationToString() {
    return beginPos.getLocationToString();
  }

  public Token getBeginPos() {
    return beginPos;
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
