package _st2_annotate;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprAssign;
import ast_expr.ExprDefaultValueForType;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_method.ClassMethodBase;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_stmt.StmtBlock;
import ast_stmt.StmtStatement;
import ast_symtab.Keywords;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_vars.VarDeclarator;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public abstract class BuildDefaultConstructor {

  public static ClassMethodDeclaration build(ClassDeclaration object) {

    final StmtBlock block = genBlockForWithAllFieldsAreInitialized(object);
    final List<VarDeclarator> parameters = new ArrayList<>();

    final Token beginPos = object.getBeginPos();

    //final Type returnType = new Type(new ClassTypeRef(object, object.getTypeParametersT()));
    final Type returnType = new Type(beginPos); // void

    final ClassMethodDeclaration constructor = new ClassMethodDeclaration(ClassMethodBase.IS_CONSTRUCTOR,
        new Modifiers(), object, object.getIdentifier(), parameters, returnType, block, beginPos);

    constructor.setGeneratedByDefault();
    return constructor;

  }

  public static StmtBlock genBlockForWithAllFieldsAreInitialized(ClassDeclaration object) {
    final StmtBlock block = new StmtBlock();
    for (StmtStatement s : inits(object)) {
      block.pushItemBack(s);
    }
    return block;
  }

  private static List<StmtStatement> inits(ClassDeclaration object) {
    final List<VarDeclarator> fields = object.getFields();
    final List<StmtStatement> rv = new ArrayList<>();
    if (!fields.isEmpty()) {
      for (VarDeclarator field : object.getFields()) {
        ExprExpression init = initForField(object, field);
        rv.add(new StmtStatement(init, object.getBeginPos()));
      }
    }
    return rv;
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
