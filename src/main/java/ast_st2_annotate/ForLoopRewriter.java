package ast_st2_annotate;

import static ast_st2_annotate.PredefinedNamesForRewriters.GET_ITERATOR_METHOD_NAME;
import static ast_st2_annotate.PredefinedNamesForRewriters.ITERATOR_GET_CURRENT_METHOD_NAME;
import static ast_st2_annotate.PredefinedNamesForRewriters.ITERATOR_GET_NEXT_METHOD_NAME;
import static ast_st2_annotate.PredefinedNamesForRewriters.ITERATOR_HAS_NEXT_METHOD_NAME;

import java.util.ArrayList;
import java.util.List;

import ast_checkers.IteratorChecker;
import ast_class.ClassDeclaration;
import ast_expr.ExprAssign;
import ast_expr.ExprExpression;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_expr.FuncArg;
import ast_sourceloc.SourceLocation;
import ast_stmt.Stmt_for;
import ast_types.Type;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import ast_vars.VarInitializer;
import hashed.Hash_ident;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public class ForLoopRewriter {

  private static int var_counter = 0;

  public static void rewriteForLoop(ClassDeclaration object, Stmt_for forloop) {

    // for item in args { ... }
    // =>
    // {
    //   var __it0__: list_iterator_2_string = args.get_iterator();
    //   var item: string = __it0__.get_current();
    //   
    //   for( ; __it0__.has_next(); item = __it0__.get_next() )
    //   {
    //   }
    // }

    // collection
    final ExprExpression collectionAux = forloop.getAuxCollection();
    final VarDeclarator collectionObjectVar = collectionAux.getIdent().getVariable();
    final Ident collectionObjectName = collectionObjectVar.getIdentifier();
    final IteratorChecker checker = new IteratorChecker(collectionObjectVar.getType());
    final Type elemType = checker.getElemType(); // we checked that collection is 'iterable' here.
    final Type iteratorType = checker.getIteratorType();

    // iter
    final ExprExpression iterAux = forloop.getAuxIter();
    iterAux.setResultType(elemType);

    final Ident item = iterAux.getIdent().getIdentifier();

    // 1) let iter: iterator<TYPE> = collection.get_iterator();
    final Ident iteratorVarName = createNameForIteratorItself();
    final VarDeclarator iteratorVar = genMethodGetIterator(collectionObjectName, iteratorType, iteratorVarName);

    final List<VarDeclarator> decl = new ArrayList<>();
    decl.add(iteratorVar);

    //2 ) TYPE item = iter.current()
    final VarDeclarator itemVar = genMethodInitItemVar(elemType, item, iteratorVarName);
    decl.add(itemVar);
    forloop.setDecl(decl);

    setTest(forloop, iteratorVarName);
    setStep(forloop, item, iteratorVarName);
  }

  private static void setTest(Stmt_for forloop, final Ident iteratorVarName) {
    // 3) iter.has_next();
    final ExprExpression testExpression = call(ITERATOR_HAS_NEXT_METHOD_NAME, iteratorVarName);
    forloop.setTest(testExpression);
  }

  private static Token todoLoc() {
    Token tok = new Token();
    tok.setValue("???");
    tok.setType(T.TOKEN_ERROR);
    return tok;
  }

  private static void setStep(Stmt_for forloop, final Ident item, final Ident iteratorVarName) {
    // 4) item = iter.get_net()
    final ExprExpression lhs = id(item);
    final ExprExpression rhs = call(ITERATOR_GET_NEXT_METHOD_NAME, iteratorVarName);

    final ExprExpression stepExpression = new ExprExpression(new ExprAssign(getAssignTok(), lhs, rhs), todoLoc());
    forloop.setStep(stepExpression);
  }

  private static ExprExpression id(Ident iteratorVarName) {
    return new ExprExpression(new ExprIdent(iteratorVarName), todoLoc());
  }

  private static ExprExpression call(final Ident funcname, final Ident objectName) {
    final ArrayList<FuncArg> emptyArgs = new ArrayList<>();
    return new ExprExpression(new ExprMethodInvocation(funcname, id(objectName), emptyArgs), todoLoc());
  }

  private static Ident createNameForIteratorItself() {
    final String nextCount = String.format("%d", var_counter++);
    return Hash_ident.getHashedIdent("__it" + nextCount + "__");
  }

  private static Token getAssignTok() {
    Token tok = new Token();
    tok.setType(T.T_ASSIGN);
    tok.setValue("=");
    return tok;
  }

  private static VarDeclarator genMethodInitItemVar(Type elemType, Ident declIdent, final Ident iteratorVarName) {

    final SourceLocation loc = new SourceLocation("for...", -1, -1);

    final VarDeclarator itemVar = new VarDeclarator(VarBase.LOCAL_VAR, Mods.varModifiers(), elemType, declIdent, loc);

    final List<VarInitializer> inits = new ArrayList<>();
    inits.add(new VarInitializer(call(ITERATOR_GET_CURRENT_METHOD_NAME, iteratorVarName), 0));

    itemVar.setInitializer(inits);
    return itemVar;

  }

  private static VarDeclarator genMethodGetIterator(final Ident collectionObjectName, final Type iteratorType,
      final Ident iteratorVarName) {

    final SourceLocation loc = new SourceLocation("for...", -1, -1);

    final VarDeclarator iteratorVar = new VarDeclarator(VarBase.LOCAL_VAR, Mods.varModifiers(), iteratorType,
        iteratorVarName, loc);
    final ExprExpression iteratorVarInitializer = call(GET_ITERATOR_METHOD_NAME, collectionObjectName);

    final List<VarInitializer> inits = new ArrayList<>();
    inits.add(new VarInitializer(iteratorVarInitializer, 0));

    iteratorVar.setInitializer(inits);
    return iteratorVar;

  }

}
