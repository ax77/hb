package _st2_annotate;

import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprAssign;
import ast_expr.ExprDefaultValueForType;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_stmt.StmtBlock;
import ast_stmt.StmtStatement;
import ast_vars.VarDeclarator;
import tokenize.T;
import tokenize.Token;

public abstract class BuildDefaultInitializersBlockForAllFields {

  public static StmtBlock createEmptifiiers(ClassDeclaration object) {
    final List<VarDeclarator> fields = object.getFields();
    final StmtBlock block = new StmtBlock();
    if (!fields.isEmpty()) {
      for (VarDeclarator field : object.getFields()) {
        ExprExpression init = initForField(object, field);
        block.pushItemBack(new StmtStatement(init, object.getBeginPos()));
      }
    }
    return block;
  }

  private static ExprExpression initForField(ClassDeclaration object, VarDeclarator field) {

    /// {
    ///   this.fieldName1 = default(fieldType1)
    ///   this.fieldName2 = default(fieldType2)
    /// }

    ExprExpression thisExpression = new ExprExpression(object, object.getBeginPos());
    ExprFieldAccess eFieldAccess = new ExprFieldAccess(thisExpression, field.getIdentifier());
    ExprExpression lhs = new ExprExpression(eFieldAccess, object.getBeginPos());

    ExprDefaultValueForType eDefaultValueForType = new ExprDefaultValueForType(field.getType());
    ExprExpression rhs = new ExprExpression(eDefaultValueForType, object.getBeginPos());

    Token assignOp = new Token(object.getBeginPos(), "=", T.T_ASSIGN);
    ExprExpression result = new ExprExpression(new ExprAssign(assignOp, lhs, rhs), object.getBeginPos());

    return result;
  }

}
