package njast.ast_nodes.clazz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jscan.symtab.Ident;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.clazz.methods.FormalParameter;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.expr.ExprExpression;
import njast.ast_nodes.stmt.StmtBlock;
import njast.types.Type;

public class ClassDeclaration implements Serializable {

  private static final long serialVersionUID = 6225743252762855961L;

  //  NormalClassDeclaration:
  //    class Identifier [TypeParameters]
  //    [extends Type] [implements TypeList] ClassBody

  private /*final*/ Ident identifier;
  private TypeParameters typeParameters;
  private List<ClassConstructorDeclaration> constructors;
  private List<StmtBlock> staticInitializers;
  private List<VarDeclarator> fields;
  private List<ClassMethodDeclaration> methods;
  private boolean isTemplate;

  public ClassDeclaration(Ident identifier) {
    this.identifier = identifier;
    initLists();
  }

  private void initLists() {
    this.constructors = new ArrayList<ClassConstructorDeclaration>();
    this.fields = new ArrayList<VarDeclarator>();
    this.methods = new ArrayList<ClassMethodDeclaration>();
    this.staticInitializers = new ArrayList<StmtBlock>();
  }

  public Ident getIdentifier() {
    return identifier;
  }

  public void put(ClassConstructorDeclaration e) {
    this.constructors.add(e);
  }

  public void put(VarDeclarator e) {
    this.fields.add(e);
  }

  public void put(ClassMethodDeclaration e) {
    this.methods.add(e);
  }

  public void put(StmtBlock e) {
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

  public TypeParameters getTypeParameters() {
    return typeParameters;
  }

  public void setTypeParameters(TypeParameters typeParameters) {
    this.typeParameters = typeParameters;
    this.isTemplate = !typeParameters.isEmpty();
  }

  public boolean isTemplate() {
    return isTemplate;
  }

  public void setIsTemplate(boolean isTemplate) {
    this.isTemplate = isTemplate;
  }

  public void setIdentifier(Ident identifier) {
    this.identifier = identifier;
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
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (VarDeclarator var : fields) {
      sb.append("  " + var.toString() + "\n");
    }
    return "class " + identifier.getName() + " {\n" + sb.toString() + "}\n";
  }

}
