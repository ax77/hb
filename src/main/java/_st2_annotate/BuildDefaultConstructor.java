package _st2_annotate;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprAlloc;
import ast_expr.ExprExpression;
import ast_expr.ExprIdent;
import ast_method.ClassMethodBase;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_stmt.StmtBlock;
import ast_stmt.StmtReturn;
import ast_stmt.StmtStatement;
import ast_symtab.BuiltinNames;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import tokenize.Token;

public abstract class BuildDefaultConstructor {

  public static StmtStatement createThisAssignExprAlloc(ClassDeclaration object) {
    // struct object *__this = alloc(object)

    Token beginPos = object.getBeginPos();
    Type type = new Type(new ClassTypeRef(object, object.getTypeParametersT()));
    VarDeclarator varDeclarator = new VarDeclarator(VarBase.METHOD_VAR, new Modifiers(), type,
        BuiltinNames.__this_ident, beginPos);

    ExprAlloc alloc = new ExprAlloc(object);
    ExprExpression initializer = new ExprExpression(alloc, beginPos);
    varDeclarator.setSimpleInitializer(initializer);

    StmtStatement statement = new StmtStatement(varDeclarator, beginPos);
    return statement;
  }

  public static StmtStatement createReturnThis(ClassDeclaration object) {
    ExprIdent id = new ExprIdent(BuiltinNames.__this_ident);
    StmtReturn ret = new StmtReturn();
    ret.setExpression(new ExprExpression(id, object.getBeginPos()));
    return new StmtStatement(ret, object.getBeginPos());
  }

  public static ClassMethodDeclaration build(ClassDeclaration object) {

    final StmtBlock block = BuildDefaultInitializersBlockForAllFields.createEmptifiiers(object);
    block.pushItemFront(createThisAssignExprAlloc(object));
    block.pushItemBack(createReturnThis(object));

    final List<VarDeclarator> parameters = new ArrayList<>();

    final Token beginPos = object.getBeginPos();
    final Type returnType = new Type(new ClassTypeRef(object, object.getTypeParametersT()));

    final ClassMethodDeclaration constructor = new ClassMethodDeclaration(ClassMethodBase.IS_CONSTRUCTOR,
        new Modifiers(), object, object.getIdentifier(), parameters, returnType, block, beginPos);

    constructor.setGeneratedByDefault();
    return constructor;

  }

}
