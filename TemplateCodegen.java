package njast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jscan.hashed.Hash_ident;
import jscan.symtab.Ident;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.errors.EParseException;
import njast.types.Type;

public class TemplateCodegen {

  private static int temps = 0;

  public static Type getType(Type from, List<Type> togen, HashMap<String, Dto> temps) {
    if (!from.isClassTemplate()) {
      return from;
    }
    Type ready = generated(from, temps);
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
      Type typeToSet = getType(ref, togen, temps);
      replaceOneTypeParam(templateClass, typenameT, typeToSet, togen, temps);
    }

    templateClass.setTypeParametersT(new ArrayList<Type>());
    final Type result = new Type(templateClass);

    temps.put(from.getClassType().getIdentifier().getName(), new Dto(typeArguments, result));
    togen.add(result);

    return result;
  }

  private static Type generated(Type from, HashMap<String, Dto> temps) {
    if (temps.isEmpty()) {
      return null;
    }

    if (!from.isClassRef()) {
      throw new EParseException("expect class-type");
    }

    final String name = from.getClassType().getIdentifier().getName();

    final Dto dto = temps.get(name);
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

  private static ClassDeclaration copyClazz(ClassDeclaration given, String newname) {
    ClassDeclaration object = (ClassDeclaration) SerializationUtils.clone(given);
    object.setIdentifier(Hash_ident.getHashedIdent(String.format("t_%s_%d", newname, temps++)));
    return object;
  }

  // is important to follow the order of expansion
  // 1) replace all plain type-names: [T item -> int item]
  // 2) replace all self-references: [ class Node<E> { Node<E> next; } ]
  // 3) expand all references with real type

  private static void replaceOneTypeParam(ClassDeclaration object, Ident typenameT, Type typeToSet, List<Type> togen,
      HashMap<String, Dto> temps) {

    //fields
    for (VarDeclarator field : object.getFields()) {

      // TODO: do this on types, and does not matter where (field, param, var, etc...)
      maybeReplaceTypenameWithType(field, typenameT, typeToSet);
      maybeClearTypeParametersIfSelfReference(field, object);
      maybeSetNewType(field, togen, temps);
    }

    //    //methods
    //    for (ClassMethodDeclaration method : object.getMethods()) {
    //
    //      if (!method.isVoid()) {
    //        if (method.getResultType().isTypeParameterStub()) {
    //          final Ident typeParameterName = method.getResultType().getTypeParameter();
    //          if (typeParameterName.equals(typenameT)) {
    //            method.setResultType(typeToSet);
    //          }
    //        } 
    //      }
    //
    //      for (FormalParameter formal : method.getFormalParameterList().getParameters()) {
    //        if (formal.getType().isTypeParameterStub()) {
    //          final Ident typeParameterName = formal.getType().getTypeParameter();
    //          if (typeParameterName.equals(typenameT)) {
    //            formal.setType(typeToSet);
    //          }
    //        }
    //      }
    //
    //      //body
    //      final StmtBlock body = method.getBody();
    //      final List<StmtBlockItem> blocks = body.getBlockStatements();
    //
    //      for (StmtBlockItem block : blocks) {
    //
    //        // declarations
    //        final List<VarDeclarator> localVars = block.getLocalVars();
    //        if (localVars != null) {
    //          for (VarDeclarator var : localVars) {
    //            if (var.getType().isTypeParameterStub()) {
    //              final Ident typeParameterName = var.getType().getTypeParameter();
    //              if (typeParameterName.equals(typenameT)) {
    //                var.setType(typeToSet);
    //              }
    //            }
    //          }
    //        }
    //
    //        // // statements
    //        // final StmtStatement statement = block.getStatement();
    //        // if (statement != null) {
    //        //   boolean result = new ApplyStmt(this).applyStatement(object, statement);
    //        //   if (!result) {
    //        //     System.out.println("...??? stmt");
    //        //   }
    //        // }
    //
    //      }
    //
    //    }
  }

  private static void maybeReplaceTypenameWithType(VarDeclarator field, Ident typenameT, Type typeToSet) {

    if (!field.getType().isTypeVarRef()) {
      return;
    }

    final Ident typeParameterName = field.getType().getTypeParameter();
    if (typeParameterName.equals(typenameT)) {
      field.setType(typeToSet);
    }

  }

  private static void maybeClearTypeParametersIfSelfReference(VarDeclarator field, ClassDeclaration object) {

    if (!field.getType().isClassRef()) {
      return;
    }

    final Type oldtype = field.getType();
    final ClassDeclaration nested = oldtype.getClassType();

    if (nested.equals(object)) {
      nested.getTypeParametersT().clear();
    }

  }

  private static void maybeSetNewType(VarDeclarator field, List<Type> togen, HashMap<String, Dto> temps) {

    if (!field.getType().isClassRef()) {
      return;
    }

    final Type oldtype = field.getType();
    final Type newtype = getType(oldtype, togen, temps);

    field.setType(newtype);
  }

}
