package njast.ast.mir;

import java.util.ArrayList;
import java.util.List;

import jscan.hashed.Hash_ident;
import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast.checkers.IteratorChecker;
import njast.ast.nodes.ClassDeclaration;
import njast.ast.nodes.expr.ExprAssign;
import njast.ast.nodes.expr.ExprExpression;
import njast.ast.nodes.expr.ExprIdent;
import njast.ast.nodes.expr.ExprMethodInvocation;
import njast.ast.nodes.expr.FuncArg;
import njast.ast.nodes.stmt.StmtFor;
import njast.ast.nodes.vars.VarBase;
import njast.ast.nodes.vars.VarDeclarator;
import njast.ast.nodes.vars.VarInitializer;
import njast.parse.AstParseException;
import njast.types.Type;

public class ForLoopRewriter {

  private static int var_counter = 0;

  public static void rewriteForLoop(ClassDeclaration object, StmtFor forloop) {
    // we'll rewrite short form to its full form
    // for item in collection {} 
    // ->
    // let iter: iterator<TYPE> = collection.get_iterator();
    // for(TYPE item = iter.current(); iter.has_next(); item = iter.get_next()) {}
    //

    // collection
    final ExprExpression collection = forloop.getAuxCollection();

    IteratorChecker checker = new IteratorChecker(collection.getIdent().getVariable());
    if (!checker.isIterable()) {
      throw new AstParseException("collection is not iterable: " + collection.toString());
    }
    Type elemType = checker.getElemType();
    Type iteratorType = checker.getIteratorType();

    // iter
    final ExprExpression iter = forloop.getAuxIter();
    iter.setResultType(elemType);

    final Ident declIdent = iter.getIdent().getIdentifier();

    // 1) init iterator: 
    //    let iter: iterator<TYPE> = collection.get_iterator();
    final Ident iteratorVarName = Hash_ident.getHashedIdent("__iterator__" + String.format("%d", var_counter++));

    final List<VarDeclarator> decl = new ArrayList<>();

    final VarDeclarator iteratorVar = getIteratorVar(collection, iteratorType, iteratorVarName);
    decl.add(iteratorVar);

    //2 ) for(TYPE item = iter.current(); iter.has_next(); item = iter.get_next()) {}
    //    rewrite loop header
    final VarDeclarator itemVar = getItemVar(elemType, declIdent, iteratorVarName);
    decl.add(itemVar);

    forloop.setDecl(decl);

    // 3) iter.has_next();
    forloop.setTest(getTestExpression(iteratorVarName));

    // 4) item = iter.get_net()
    final ExprExpression lhs = new ExprExpression(new ExprIdent(declIdent));
    final ExprExpression rhs = new ExprExpression(new ExprMethodInvocation(Hash_ident.getHashedIdent("get_next"),
        new ExprExpression(new ExprIdent(iteratorVarName)), new ArrayList<>()));

    final ExprExpression stepExpression = new ExprExpression(new ExprAssign(getAssignTok(), lhs, rhs));

    forloop.setStep(stepExpression);

  }

  private static Token getAssignTok() {
    Token tok = new Token();
    tok.setType(T.T_ASSIGN);
    tok.setValue("=");
    return tok;
  }

  private static ExprExpression getTestExpression(Ident iteratorVarName) {
    final Ident hasNextIdent = Hash_ident.getHashedIdent("has_next");
    final ExprMethodInvocation invocation = new ExprMethodInvocation(hasNextIdent,
        new ExprExpression(new ExprIdent(iteratorVarName)), new ArrayList<>());
    final ExprExpression testExpression = new ExprExpression(invocation);
    return testExpression;
  }

  private static VarDeclarator getItemVar(Type elemType, Ident declIdent, final Ident iteratorVarName) {
    final SourceLocation loc = new SourceLocation("for...", -1, -1);
    final ArrayList<FuncArg> emptyArgs = new ArrayList<>();

    final Ident currIdent = Hash_ident.getHashedIdent("current");

    final ExprMethodInvocation iterCurrent = new ExprMethodInvocation(currIdent,
        new ExprExpression(new ExprIdent(iteratorVarName)), emptyArgs);

    final VarDeclarator itemVar = new VarDeclarator(VarBase.LOCAL_VAR, elemType, declIdent, loc);
    final List<VarInitializer> inits = new ArrayList<>();

    inits.add(new VarInitializer(new ExprExpression(iterCurrent), 0));
    itemVar.setInitializer(inits);

    return itemVar;
  }

  private static VarDeclarator getIteratorVar(final ExprExpression collection, final Type iteratorType,
      final Ident iteratorVarName) {

    final ArrayList<FuncArg> emptyArgs = new ArrayList<>();
    final SourceLocation loc = new SourceLocation("for...", -1, -1);

    final Ident getIteratorIdent = Hash_ident.getHashedIdent("get_iterator");
    final Ident collectionNameIdent = collection.getIdent().getIdentifier();
    final ExprMethodInvocation methodInvocation = new ExprMethodInvocation(getIteratorIdent,
        new ExprExpression(new ExprIdent(collectionNameIdent)), emptyArgs);
    final ExprExpression iteratorVarInitializer = new ExprExpression(methodInvocation);

    final VarDeclarator iteratorVar = new VarDeclarator(VarBase.LOCAL_VAR, iteratorType, iteratorVarName, loc);
    final List<VarInitializer> inits = new ArrayList<>();

    inits.add(new VarInitializer(iteratorVarInitializer, 0));
    iteratorVar.setInitializer(inits);

    return iteratorVar;

  }

}
