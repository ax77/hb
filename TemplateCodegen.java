package njast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;

import jscan.hashed.Hash_ident;
import jscan.symtab.Ident;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.clazz.methods.FormalParameter;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.stmt.StmtBlock;
import njast.ast_nodes.stmt.StmtBlockItem;
import njast.types.ReferenceType;
import njast.types.ReferenceTypeBase;
import njast.types.Type;

public class TemplateCodegen {

  private static int temps = 0;

  public static ReferenceType getType(ReferenceType from, List<ReferenceType> togen, HashMap<String, Dto> temps) {
    if (!from.isClassTemplate()) {
      return from;
    }
    ReferenceType ready = generated(from, temps);
    if (ready != null) {
      return ready;
    }

    final ClassDeclaration templateClass = copyClazz(from.getClassType(), from.toString().trim());
    final List<ReferenceType> typeArguments = from.getTypeArguments();
    final List<ReferenceType> typeParameters = templateClass.getTypeParametersT();

    for (int i = 0; i < typeParameters.size(); i++) {
      ReferenceType ref = typeArguments.get(i);
      Ident typenameT = typeParameters.get(i).getTypeVariable();
      Type typeToSet = new Type(getType(ref, togen, temps));

      templateClass.getTypeParametersT().get(i).fillPropValues(ref);
      replaceOneTypeParam(templateClass, typenameT, typeToSet, togen, temps);
    }

    templateClass.setTypeParametersT(new ArrayList<ReferenceType>());
    final ReferenceType result = new ReferenceType(templateClass);

    temps.put(from.getClassType().getIdentifier().getName(), new Dto(typeArguments, result));

    togen.add(result);
    return result;
  }

  private static ReferenceType generated(ReferenceType from, HashMap<String, Dto> temps) {
    if (temps.isEmpty()) {
      return null;
    }
    String name = from.getClassType().getIdentifier().getName();
    Dto dto = temps.get(name);
    List<ReferenceType> args = dto.getTypeArguments();
    if (args == null) {
      return null;
    }
    if (from.getTypeArguments().size() != args.size()) {
      return null;
    }
    for (int i = 0; i < args.size(); i++) {
      ReferenceType tp1 = from.getTypeArguments().get(i);
      ReferenceType tp2 = args.get(i);
      if (tp1.equals(tp2)) {
        return dto.getResult();
      }
    }
    return null;
  }

  private static ClassDeclaration copyClazz(ClassDeclaration given, String newname) {
    ClassDeclaration object = (ClassDeclaration) SerializationUtils.clone(given);
    object.setIdentifier(Hash_ident.getHashedIdent(String.format("t%d", temps++)));
    return object;
  }

  private static void replaceOneTypeParam(ClassDeclaration object, Ident typenameT, Type typeToSet,
      List<ReferenceType> togen, HashMap<String, Dto> temps) {

    //fields
    for (VarDeclarator field : object.getFields()) {
      if (field.getType().isTypeParameterStub()) {
        final Ident typeParameterName = field.getType().getTypeParameter();
        if (typeParameterName.equals(typenameT)) {
          field.setType(typeToSet);
        }
      } else {
        if (field.getType().isReference()) {
          ReferenceType oldtype = field.getType().getReferenceType();
          if (oldtype.getBase() == ReferenceTypeBase.CLASS_REF) {
            ClassDeclaration nested = oldtype.getClassType();
            if (nested.equals(object)) {
              nested.getTypeParametersT().clear();
            }
          }
          ReferenceType newtype = getType(oldtype, togen, temps);
          field.setType(new Type(newtype));
        }
      }
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

}
