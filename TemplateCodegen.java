package njast;

import java.util.List;

import org.apache.commons.lang3.SerializationUtils;

import jscan.hashed.Hash_ident;
import jscan.symtab.Ident;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.TypeParameters;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.types.ReferenceType;
import njast.types.Type;

public class TemplateCodegen {

  static int cnt = 0;

  //  // TODO: check this and that...
  //  private static Type get(Ident id, List<Ident> fp, List<ReferenceType> ap) {
  //    int index = -1;
  //    for (int i = 0; i < fp.size(); i++) {
  //      if (fp.get(i).equals(id)) {
  //        index = i;
  //        break;
  //      }
  //    }
  //    return new Type(ap.get(index));
  //  }

  public static void expandTemplate(ClassDeclaration given, Ident fp, ReferenceType ap,
      List<ClassDeclaration> generated) {

    final ClassDeclaration typeName = ap.getTypeName();
    if (typeName.isTemplate()) {
      ReferenceType reftype = ap.getTypeArguments().get(0);
      final Ident typenameT = typeName.getTypeParameters().getTypeParameters().get(0);
      expandTemplate(typeName, typenameT, reftype, generated);
    }

    ClassDeclaration object = (ClassDeclaration) SerializationUtils.clone(given);
    object.setTypeParameters(new TypeParameters());
    object.setIsTemplate(false);
    String newname = String.format("%s_%d", object.getIdentifier().getName(), cnt++);
    object.setIdentifier(Hash_ident.getHashedIdent(newname));

    //fields
    for (VarDeclarator field : object.getFields()) {
      if (field.getType().isTypeParameterStub()) {
        final Ident typeParameterName = field.getType().getTypeParameter();

        Type tp = new Type(ap);
        if (!generated.isEmpty()) {
          tp = new Type(new ReferenceType(generated.get(0)));
        }

        field.setType(tp);
      }
    }

    //    //methods
    //    for (ClassMethodDeclaration method : object.getMethods()) {
    //
    //      if (!method.isVoid()) {
    //        if (method.getResultType().isTypeParameterStub()) {
    //          final Ident typeParameterName = method.getResultType().getTypeParameter();
    //          method.setResultType(new Type(ap));
    //        }
    //      }
    //
    //      for (FormalParameter formal : method.getFormalParameterList().getParameters()) {
    //        if (formal.getType().isTypeParameterStub()) {
    //          final Ident typeParameterName = formal.getType().getTypeParameter();
    //          formal.setType(new Type(ap));
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
    //              var.setType(get(typeParameterName, fp, ap));
    //            }
    //          }
    //        }
    //
    //        // statements
    //        final StmtStatement statement = block.getStatement();
    //        if (statement != null) {
    //          boolean result = new ApplyStmt(this).applyStatement(object, statement);
    //          if (!result) {
    //            System.out.println("...??? stmt");
    //          }
    //        }
    //
    //      }
    //
    //    }

    //    //constructors (the last, it works with methods and fields)
    //    for (ClassConstructorDeclaration constructor : object.getConstructors()) {
    //    }

    generated.add(0, object);

  }
}
