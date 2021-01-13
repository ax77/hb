package njast.ast_nodes.clazz.methods;

import java.io.Serializable;
import java.util.List;

import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import njast.ast_nodes.IModTypeNameHeader;
import njast.ast_nodes.ModTypeNameHeader;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.IUniqueId;
import njast.ast_nodes.stmt.StmtBlock;
import njast.ast_utils.UniqueCounter;
import njast.errors.EParseException;
import njast.parse.ILocation;
import njast.parse.NullChecker;
import njast.types.Type;

public class ClassMethodDeclaration implements Serializable, IModTypeNameHeader, ILocation, IUniqueId {

  private static final long serialVersionUID = 2982374768194205119L;

  // init: clazz, parameters, body
  // deinit: clazz, body
  // func: clazz, header, parameters, body

  private final ClassMethodBase base;
  private final ClassDeclaration clazz;
  private final ModTypeNameHeader header;
  private final List<ModTypeNameHeader> parameters;
  private final StmtBlock block;
  private final SourceLocation location;
  private final int uniqueId;

  // function
  public ClassMethodDeclaration(ClassDeclaration clazz, ModTypeNameHeader header, List<ModTypeNameHeader> parameters,
      StmtBlock block, SourceLocation location) {

    NullChecker.check(clazz, header, parameters, block, location);

    this.base = ClassMethodBase.IS_FUNC;
    this.clazz = clazz;
    this.header = header;
    this.parameters = parameters;
    this.block = block;
    this.location = location;
    this.uniqueId = UniqueCounter.getUniqueId();
  }

  // init
  public ClassMethodDeclaration(ClassDeclaration clazz, List<ModTypeNameHeader> parameters, StmtBlock block,
      SourceLocation location) {

    NullChecker.check(clazz, parameters, block, location);

    this.base = ClassMethodBase.IS_CONSTRUCTOR;
    this.clazz = clazz;
    this.header = null;
    this.parameters = parameters;
    this.block = block;
    this.location = location;
    this.uniqueId = UniqueCounter.getUniqueId();
  }

  // deinit
  public ClassMethodDeclaration(ClassDeclaration clazz, StmtBlock block, SourceLocation location) {

    NullChecker.check(clazz, block, location);

    this.base = ClassMethodBase.IS_DESTRUCTOR;
    this.clazz = clazz;
    this.header = null;
    this.parameters = null;
    this.block = block;
    this.location = location;
    this.uniqueId = UniqueCounter.getUniqueId();
  }

  public ClassDeclaration getClazz() {
    return clazz;
  }

  public List<ModTypeNameHeader> getParameters() {
    return parameters;
  }

  public boolean isVoid() {
    return isFunction() && getType().isVoidStub();
  }

  public StmtBlock getBlock() {
    return block;
  }

  public String parametersToString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");

    for (int i = 0; i < parameters.size(); i++) {
      ModTypeNameHeader param = parameters.get(i);
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
      sb.append(getIdentifier().getName());
      sb.append(parametersToString());
    }

    if (isConstructor()) {
      sb.append("init");
      sb.append(parametersToString());
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

  @Override
  public Type getType() {
    if (!isFunction()) {
      throw new EParseException("constructor/destructor have no types");
    }
    return header.getType();
  }

  @Override
  public Ident getIdentifier() {
    if (!isFunction()) {
      throw new EParseException("constructor/destructor have no identifiers");
    }
    return header.getIdentifier();
  }

  public ModTypeNameHeader getHeader() {
    if (!isFunction()) {
      throw new EParseException("constructor/destructor have no headers");
    }
    return header;
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
