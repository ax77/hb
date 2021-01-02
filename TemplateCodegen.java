package njast;

import java.util.List;
import java.util.Map;

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

  public ClassDeclaration expandTemplate(ClassDeclaration given, Map<Ident, Type> bindings)
      throws CloneNotSupportedException {

    ClassDeclaration object = (ClassDeclaration) SerializationUtils.clone(given);

    //fields
    for (VarDeclarator field : object.getFields()) {
      if (field.getType().isTypeParameterStub()) {
        final Ident typeParameterName = field.getType().getTypeParameter();
        field.setType(bindings.get(typeParameterName));
      }
    }

    //methods
    for (ClassMethodDeclaration method : object.getMethods()) {

      if (!method.isVoid()) {
        if (method.getResultType().isTypeParameterStub()) {
          final Ident typeParameterName = method.getResultType().getTypeParameter();
          method.setResultType(bindings.get(typeParameterName));
        }
      }

      for (FormalParameter fp : method.getFormalParameterList().getParameters()) {
        if (fp.getType().isTypeParameterStub()) {
          final Ident typeParameterName = fp.getType().getTypeParameter();
          fp.setType(bindings.get(typeParameterName));
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
              var.setType(bindings.get(typeParameterName));
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

    return object;

  }
}
