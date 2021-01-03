package njast;

import java.util.List;

import org.apache.commons.lang3.SerializationUtils;

import jscan.hashed.Hash_ident;
import jscan.symtab.Ident;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.TypeParameters;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.clazz.methods.FormalParameter;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.stmt.StmtBlock;
import njast.ast_nodes.stmt.StmtBlockItem;
import njast.types.ReferenceType;
import njast.types.Type;

public class TemplateCodegen {

  public static ReferenceType getType(ReferenceType from, List<ReferenceType> togen) {
    if (!from.isClassTemplate()) {
      return from;
    }

    final ClassDeclaration templateClass = copyClazz(from.getTypeName(), from.toString().trim());
    final List<ReferenceType> typeArguments = from.getTypeArguments();
    final List<Ident> typeParameters = from.getTypeParameters();

    for (int i = 0; i < typeParameters.size(); i++) {
      ReferenceType ref = typeArguments.get(i);
      Ident typenameT = typeParameters.get(i);
      Type typeToSet = new Type(getType(ref, togen));
      replaceOneTypeParam(templateClass, typenameT, typeToSet);
    }

    final ReferenceType result = new ReferenceType(templateClass);
    togen.add(result);
    return result;
  }

  private static ClassDeclaration copyClazz(ClassDeclaration given, String newname) {
    ClassDeclaration object = (ClassDeclaration) SerializationUtils.clone(given);
    object.setTypeParameters(new TypeParameters());
    object.setIsTemplate(false);
    object.setIdentifier(Hash_ident.getHashedIdent(newname));
    return object;
  }

  private static void replaceOneTypeParam(ClassDeclaration object, Ident typenameT, Type typeToSet) {

    //fields
    for (VarDeclarator field : object.getFields()) {
      if (field.getType().isTypeParameterStub()) {
        final Ident typeParameterName = field.getType().getTypeParameter();
        if (typeParameterName.equals(typenameT)) {
          field.setType(typeToSet);
        }
      }
    }

    //methods
    for (ClassMethodDeclaration method : object.getMethods()) {

      if (!method.isVoid()) {
        if (method.getResultType().isTypeParameterStub()) {
          final Ident typeParameterName = method.getResultType().getTypeParameter();
          if (typeParameterName.equals(typenameT)) {
            method.setResultType(typeToSet);
          }
        }
      }

      for (FormalParameter formal : method.getFormalParameterList().getParameters()) {
        if (formal.getType().isTypeParameterStub()) {
          final Ident typeParameterName = formal.getType().getTypeParameter();
          if (typeParameterName.equals(typenameT)) {
            formal.setType(typeToSet);
          }
        }
      }

      //body
      final StmtBlock body = method.getBody();
      final List<StmtBlockItem> blocks = body.getBlockStatements();

      for (StmtBlockItem block : blocks) {

        // declarations
        final List<VarDeclarator> localVars = block.getLocalVars();
        if (localVars != null) {
          for (VarDeclarator var : localVars) {
            if (var.getType().isTypeParameterStub()) {
              final Ident typeParameterName = var.getType().getTypeParameter();
              if (typeParameterName.equals(typenameT)) {
                var.setType(typeToSet);
              }
            }
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

}
