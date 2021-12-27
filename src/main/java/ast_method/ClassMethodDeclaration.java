package ast_method;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import _st1_templates.TypeSetter;
import ast_class.ClassDeclaration;
import ast_main.GlobalCounter;
import ast_modifiers.Modifiers;
import ast_printers.TypeListToString;
import ast_sourceloc.Location;
import ast_sourceloc.SourceLocation;
import ast_stmt.StmtBlock;
import ast_symtab.Keywords;
import ast_types.Type;
import ast_vars.VarDeclarator;
import tokenize.Ident;
import tokenize.Token;
import utils_oth.NullChecker;

public class ClassMethodDeclaration implements Serializable, TypeSetter, Location {

  private static final long serialVersionUID = 2982374768194205119L;

  private final ClassMethodBase base;
  private final Modifiers mod;
  private final Token beginPos;
  private final ClassDeclaration clazz;
  private final Ident identifier;
  private final List<VarDeclarator> parameters;
  private Type returnType;
  private final StmtBlock block;
  private final String testName;

  /// for code-generation only,
  /// because we may have many overload methods
  /// and constructors, and it is much easy to 
  /// create an unique number neither than generate
  /// this number dynamically
  private final int uniqueId;

  /// auto-generated constructor/destructor/etc...
  /// at a code-generation stage it is possible
  /// to mark these methods as __always_inline__
  /// because of the simplicity 
  ///
  private boolean isGeneratedByDefault;

  // function/init
  public ClassMethodDeclaration(ClassMethodBase base, Modifiers mod, ClassDeclaration clazz, Ident identifier,
      List<VarDeclarator> parameters, Type returnType, StmtBlock block, Token beginPos) {

    NullChecker.check(base, clazz, mod, identifier, parameters, returnType, block, beginPos);

    this.base = base;
    this.mod = mod;
    this.clazz = clazz;
    this.identifier = identifier;
    this.parameters = parameters;
    this.returnType = returnType;
    this.block = block;
    this.beginPos = beginPos;

    this.testName = "";
    this.uniqueId = GlobalCounter.next();

  }

  // deinit
  public ClassMethodDeclaration(ClassDeclaration clazz, StmtBlock block, Token beginPos) {

    NullChecker.check(clazz, block, beginPos);

    this.base = ClassMethodBase.IS_DESTRUCTOR;
    this.mod = new Modifiers();
    this.clazz = clazz;
    this.identifier = Keywords.deinit_ident;
    this.parameters = new ArrayList<>();
    this.returnType = new Type(beginPos);
    this.block = block;
    this.beginPos = beginPos;

    this.testName = "";
    this.uniqueId = GlobalCounter.next();
  }

  // test "something" {}
  public ClassMethodDeclaration(ClassDeclaration clazz, String testName, StmtBlock block, Token beginPos) {
    NullChecker.check(clazz, testName, block, beginPos);

    this.base = ClassMethodBase.IS_TEST;
    this.mod = new Modifiers();
    this.clazz = clazz;
    this.identifier = Keywords.test_ident;
    this.parameters = new ArrayList<>();
    this.returnType = new Type(beginPos);
    this.block = block;
    this.beginPos = beginPos;

    this.testName = testName;
    this.uniqueId = GlobalCounter.next();
  }

  @Override
  public Type getType() {
    return returnType;
  }

  @Override
  public void setType(Type typeToSet) {
    this.returnType = typeToSet;
  }

  public int getUniqueId() {
    return uniqueId;
  }

  public String getUniqueIdToString() {
    return String.format("%d", uniqueId);
  }

  public ClassMethodBase getBase() {
    return base;
  }

  public ClassDeclaration getClazz() {
    return clazz;
  }

  public List<VarDeclarator> getParameters() {
    return parameters;
  }

  public boolean isVoid() {
    return returnType.isVoid();
  }

  public StmtBlock getBlock() {
    return block;
  }

  public boolean isGeneratedByDefault() {
    return isGeneratedByDefault;
  }

  public void setGeneratedByDefault() {
    this.isGeneratedByDefault = true;
  }

  public void pushParameterFront(VarDeclarator param) {
    NullChecker.check(param);
    this.parameters.add(0, param);
  }

  private String parametersToString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");

    for (int i = 0; i < parameters.size(); i++) {
      VarDeclarator param = parameters.get(i);
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

    sb.append(mod.toString());
    sb.append(" ");
    sb.append(returnType.toString());
    sb.append(" ");
    sb.append(identifier);

    if (isConstructor()) {
      if (!clazz.getTypeParametersT().isEmpty()) {
        sb.append("<");
        sb.append(TypeListToString.gen(clazz.getTypeParametersT()));
        sb.append(">");
      }
    }

    sb.append(parametersToString());
    sb.append(block.toString());
    return sb.toString();
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

  @Override
  public Token getBeginPos() {
    return beginPos;
  }

  public Modifiers getModifiers() {
    return mod;
  }

  public boolean isMain() {
    return identifier.getName().equals("main");
  }

  public boolean isDestructor() {
    return base == ClassMethodBase.IS_DESTRUCTOR;
  }

  public String getTestName() {
    return testName;
  }

  public boolean isTest() {
    return base == ClassMethodBase.IS_TEST;
  }

}
