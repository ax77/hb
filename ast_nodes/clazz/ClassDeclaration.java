package njast.ast_nodes.clazz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jscan.symtab.Ident;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.clazz.methods.FormalParameter;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.expr.ExprExpression;
import njast.ast_nodes.stmt.StmtBlock;
import njast.errors.EParseException;
import njast.parse.NullChecker;
import njast.types.Type;

public class ClassDeclaration implements Serializable {

  private static final long serialVersionUID = 6225743252762855961L;

  //  NormalClassDeclaration:
  //    class Identifier [TypeParameters]
  //    [extends Type] [implements TypeList] ClassBody

  private static int classIdCounter = 0;

  private Ident identifier;
  private List<ClassConstructorDeclaration> constructors;
  private List<StmtBlock> staticInitializers;
  private List<VarDeclarator> fields;
  private List<ClassMethodDeclaration> methods;
  private final int uniqueId;

  // we store type-variables as original references to Type
  //
  // each type-variable is an identifier like: class Pair<K, V> { K key; V value; Pair<K, V> hash; }
  // where type-variables are: K and V in this example
  //
  // when we'll expand template, we'll replace each at once in every places it used by its pointer
  // it is important to not screw this reference up before
  //
  private List<Type> typeParametersT;

  public ClassDeclaration(Ident identifier) {
    this.uniqueId = classIdCounter++;
    this.identifier = identifier;
    initLists();
  }

  public boolean isEqualAsGeneric(ClassDeclaration another) {
    if (this == another) {
      return true;
    }
    if (!identifier.equals(another.getIdentifier())) {
      return false;
    }
    final List<Type> typeParametersAnother = another.getTypeParametersT();
    if (typeParametersT.size() != typeParametersAnother.size()) {
      return false;
    }
    for (int i = 0; i < typeParametersT.size(); i++) {
      Type tp1 = typeParametersT.get(i);
      Type tp2 = typeParametersAnother.get(i);
      if (!tp1.isEqualAsGeneric(tp2)) {
        return false;
      }
    }
    return true;
  }

  private void initLists() {
    this.constructors = new ArrayList<ClassConstructorDeclaration>();
    this.fields = new ArrayList<VarDeclarator>();
    this.methods = new ArrayList<ClassMethodDeclaration>();
    this.staticInitializers = new ArrayList<StmtBlock>();
    this.typeParametersT = new ArrayList<>();
  }

  public Ident getIdentifier() {
    return identifier;
  }

  public void put(ClassConstructorDeclaration e) {
    NullChecker.check(e);
    this.constructors.add(e);
  }

  public void put(VarDeclarator e) {
    NullChecker.check(e);
    this.fields.add(e);
  }

  public void put(ClassMethodDeclaration e) {
    NullChecker.check(e);
    this.methods.add(e);
  }

  public void put(StmtBlock e) {
    NullChecker.check(e);
    this.staticInitializers.add(e);
  }

  public List<ClassConstructorDeclaration> getConstructors() {
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
      if (ref.getTypeVariable().equals(typenameT)) {
        return true;
      }
    }
    return false;
  }

  public void setTypeParametersT(List<Type> typeParametersT) {
    for (Type tp : typeParametersT) {
      if (!tp.isTypeVarRef()) {
        throw new EParseException("expect type-parameter, but was: " + tp.toString());
      }
    }
    this.typeParametersT = Collections.unmodifiableList(typeParametersT);
  }

  public void setIdentifier(Ident identifier) {
    NullChecker.check(identifier);
    this.identifier = identifier;
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

  private boolean isCompatibleByArguments(ClassMethodDeclaration method, List<ExprExpression> arguments) {
    List<FormalParameter> formalParameters = method.getFormalParameterList().getParameters();
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

  public String getUniqueIdStr() {
    return String.format("%d", uniqueId);
  }

  public int getUniqueId() {
    return uniqueId;
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fields == null) ? 0 : fields.hashCode());
    result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
    result = prime * result + ((typeParametersT == null) ? 0 : typeParametersT.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ClassDeclaration other = (ClassDeclaration) obj;
    if (fields == null) {
      if (other.fields != null)
        return false;
    } else if (!fields.equals(other.fields))
      return false;
    if (identifier == null) {
      if (other.identifier != null)
        return false;
    } else if (!identifier.equals(other.identifier))
      return false;
    if (typeParametersT == null) {
      if (other.typeParametersT != null)
        return false;
    } else if (!typeParametersT.equals(other.typeParametersT))
      return false;
    return true;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ");
    sb.append(identifier.getName());

    sb.append("\n{\n");

    for (VarDeclarator var : fields) {
      sb.append(var.toString() + "\n");
    }

    for (ClassConstructorDeclaration constructor : constructors) {
      sb.append(constructor.toString() + "\n");
    }

    for (ClassMethodDeclaration method : methods) {
      sb.append(method.toString() + "\n");
    }

    sb.append("\n}\n");

    return sb.toString();
  }

  public Type getTypeParameter(Ident ident) {
    for (Type ref : typeParametersT) {
      if (ref.getTypeVariable().equals(ident)) {
        return ref;
      }
    }
    return null;
  }

}
