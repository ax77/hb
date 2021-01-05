package njast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jscan.hashed.Hash_ident;
import jscan.symtab.Ident;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.clazz.methods.FormalParameter;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.stmt.StmtBlock;
import njast.ast_nodes.stmt.StmtBlockItem;
import njast.errors.EParseException;
import njast.types.Type;

public class TemplateCodegen {

  private static int temps_name = 0;

  private final List<Type> generatedClasses;
  private final HashMap<String, Dto> generatedClassesForReuse;
  private final Type result;

  public TemplateCodegen(Type from) {
    this.generatedClasses = new ArrayList<>();
    this.generatedClassesForReuse = new HashMap<>();
    this.result = getTypeFromTemplate(from);
  }

  public Type getResult() {
    return result;
  }

  public List<Type> getGeneratedClasses() {
    return generatedClasses;
  }

  private Type getTypeFromTemplate(Type from) {
    if (!from.isClassTemplate()) {
      return from;
    }
    Type ready = generated(from);
    if (ready != null) {
      return ready;
    }

    final String origName = from.getClassType().getIdentifier().getName();

    final ClassDeclaration templateClass = copyClazz(from.getClassType(), origName);
    final List<Type> typeArguments = from.getTypeArguments();
    final List<Type> typeParameters = templateClass.getTypeParametersT();

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

    generatedClassesForReuse.put(from.getClassType().getIdentifier().getName(), new Dto(typeArguments, result));
    generatedClasses.add(result);

    return result;
  }

  private Type generated(Type from) {
    if (generatedClassesForReuse.isEmpty()) {
      return null;
    }

    if (!from.isClassRef()) {
      throw new EParseException("expect class-type");
    }

    final String name = from.getClassType().getIdentifier().getName();

    final Dto dto = generatedClassesForReuse.get(name);
    if (dto == null) {
      return null;
    }

    final Type result = dto.getResult();
    if (result == null) {
      throw new EParseException("empty result");
    }

    final List<Type> args = dto.getTypeArguments();
    if (args == null) {
      return null;
    }

    final List<Type> typeArgumentsFrom = from.getTypeArguments();
    if (typeArgumentsFrom == null) {
      return null;
    }

    if (typeArgumentsFrom.size() != args.size()) {
      return null;
    }

    for (int i = 0; i < args.size(); i++) {
      Type tp1 = typeArgumentsFrom.get(i);
      Type tp2 = args.get(i);
      if (!tp1.isEqualAsGeneric(tp2)) {
        return null;
      }
    }

    return result;
  }

  private ClassDeclaration copyClazz(ClassDeclaration given, String newname) {
    ClassDeclaration object = (ClassDeclaration) SerializationUtils.clone(given);
    object.setIdentifier(Hash_ident.getHashedIdent(String.format("t_%s_%d", newname, temps_name++)));
    return object;
  }

  // is important to follow the order of expansion
  // 1) replace all plain type-names: [T item -> int item]
  // 2) replace all self-references: [ class Node<E> { Node<E> next; } ]
  // 3) expand all references with real type

  private void replaceOneTypeParam(ClassDeclaration object, Ident typenameT, Type typeToSet) {

    //fields
    for (VarDeclarator field : object.getFields()) {
      fabric(field, object, typenameT, typeToSet);
    }

    //methods
    for (ClassMethodDeclaration method : object.getMethods()) {

      if (!method.isVoid()) {
        fabric(method, object, typenameT, typeToSet);
      }

      for (FormalParameter formal : method.getFormalParameterList().getParameters()) {
        fabric(formal, object, typenameT, typeToSet);
      }

      //body
      final StmtBlock body = method.getBody();
      final List<StmtBlockItem> blocks = body.getBlockStatements();

      for (StmtBlockItem block : blocks) {

        // declarations
        final List<VarDeclarator> localVars = block.getLocalVars();
        if (localVars != null) {
          for (VarDeclarator var : localVars) {
            fabric(var, object, typenameT, typeToSet);
          }
        }

        // // statements
        // final StmtStatement statement = block.getStatement();
        // if (statement != null) {
        //   boolean result = new ApplyStmt(this).applyStatement(object, statement);
        //   if (!result) {
        //     System.out.println("...??? stmt");
        //   }
        // }

      }

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
      nested.getTypeParametersT().clear();
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
