package njast;

import java.util.List;

import org.apache.commons.lang3.SerializationUtils;

import jscan.symtab.Ident;
import njast.ast_nodes.clazz.ClassConstructorDeclaration;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.clazz.methods.FormalParameter;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.stmt.StmtBlock;
import njast.ast_nodes.stmt.StmtBlockItem;
import njast.types.Type;

public class TemplateCodegen {

  // TODO: check this and that...
  private Type get(Ident id, List<Ident> fp, List<Type> ap) {
    int index = -1;
    for (int i = 0; i < fp.size(); i++) {
      if (fp.get(i).equals(id)) {
        index = i;
        break;
      }
    }
    return ap.get(index);
  }

  public ClassDeclaration expandTemplate(ClassDeclaration given, List<Ident> fp, List<Type> ap,
      List<ClassDeclaration> generated) {

    for (Type tp : ap) {
      if (tp.isReference() && tp.getReferenceType().getTypeName().isTemplate()) {
        expandTemplate(tp.getReferenceType().getTypeName(), fp, ap, generated);
      }
    }

    ClassDeclaration object = (ClassDeclaration) SerializationUtils.clone(given);
    object.setIsTemplate(false);

    //fields
    for (VarDeclarator field : object.getFields()) {
      if (field.getType().isTypeParameterStub()) {
        final Ident typeParameterName = field.getType().getTypeParameter();
        field.setType(get(typeParameterName, fp, ap));
      }
    }

    //methods
    for (ClassMethodDeclaration method : object.getMethods()) {

      if (!method.isVoid()) {
        if (method.getResultType().isTypeParameterStub()) {
          final Ident typeParameterName = method.getResultType().getTypeParameter();
          method.setResultType(get(typeParameterName, fp, ap));
        }
      }

      for (FormalParameter formal : method.getFormalParameterList().getParameters()) {
        if (formal.getType().isTypeParameterStub()) {
          final Ident typeParameterName = formal.getType().getTypeParameter();
          formal.setType(get(typeParameterName, fp, ap));
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
              var.setType(get(typeParameterName, fp, ap));
            }
          }
        }

        //        // statements
        //        final StmtStatement statement = block.getStatement();
        //        if (statement != null) {
        //          boolean result = new ApplyStmt(this).applyStatement(object, statement);
        //          if (!result) {
        //            System.out.println("...??? stmt");
        //          }
        //        }

      }

    }

    //constructors (the last, it works with methods and fields)
    for (ClassConstructorDeclaration constructor : object.getConstructors()) {
    }

    generated.add(object);
    return object;

  }
}
