package _st2_annotate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
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
import ast_unit.InstantiationUnit;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import hashed.Hash_ident;
import tokenize.Token;

public abstract class BuildOpAssignMethod {

  public static ClassMethodDeclaration opAssign(InstantiationUnit instantiationUnit, ClassDeclaration object)
      throws IOException {

    final Token beginPos = object.getBeginPos();
    final Type typename = new Type(new ClassTypeRef(object, object.getTypeParametersT()), beginPos);

    final List<VarDeclarator> params = new ArrayList<>();
    params.add(new VarDeclarator(VarBase.METHOD_PARAMETER, new Modifiers(), typename,
        Hash_ident.getHashedIdent("lvalue"), beginPos));
    params.add(new VarDeclarator(VarBase.METHOD_PARAMETER, new Modifiers(), typename,
        Hash_ident.getHashedIdent("rvalue"), beginPos));

    final StmtBlock block = new StmtBlock();
    StmtReturn ret = new StmtReturn();
    ret.setExpression(new ExprExpression(new ExprIdent(Hash_ident.getHashedIdent("rvalue")), beginPos));
    block.pushItemBack(new StmtStatement(ret, beginPos));

    //@formatter:off
    ClassMethodDeclaration result = new ClassMethodDeclaration(ClassMethodBase.IS_FUNC
        , new Modifiers()
        , object
        , BuiltinNames.opAssign_ident
        , params
        , typename
        , block
        , beginPos
    );
    //@formatter:on

    result.setGeneratedByDefault();
    return result;
  }

}
