package _st2_annotate;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_method.ClassMethodDeclaration;
import ast_stmt.StmtBlock;
import ast_stmt.StmtStatement;
import ast_symtab.Keywords;
import ast_vars.VarDeclarator;
import tokenize.Token;

public abstract class BuildDefaultDestructor {

  public static ClassMethodDeclaration build(ClassDeclaration object) {

    final StmtBlock block = new StmtBlock();

    final List<VarDeclarator> fields = object.getFields();
    if (!fields.isEmpty()) {
      for (int i = fields.size() - 1; i >= 0; i -= 1) {
        VarDeclarator field = fields.get(i);
        if (field.getType().is_class()) {
          ExprExpression deinit = deinitForField(object, field);
          block.pushItemBack(new StmtStatement(deinit, object.getBeginPos()));
        }
      }
    }

    final Token beginPos = object.getBeginPos();

    final ClassMethodDeclaration destructor = new ClassMethodDeclaration(object, block, beginPos);

    destructor.setGeneratedByDefault();
    return destructor;

  }

  private static ExprExpression deinitForField(ClassDeclaration object, VarDeclarator field) {
    List<ExprExpression> arguments = new ArrayList<>();
    ExprIdent id = new ExprIdent(field.getIdentifier());
    final Token beginPos = object.getBeginPos();
    final ExprExpression idExpr = new ExprExpression(id, beginPos);
    final ExprMethodInvocation exprMethodInvocation = new ExprMethodInvocation(idExpr, Keywords.deinit_ident,
        arguments);
    return new ExprExpression(exprMethodInvocation, beginPos);
  }

}
