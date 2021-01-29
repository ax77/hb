package ast_st2_annotate;

import static ast_st2_annotate.PredefinedNamesForRewriters.GET_ITERATOR_METHOD_NAME;
import static ast_st2_annotate.PredefinedNamesForRewriters.ITERATOR_GET_NEXT_METHOD_NAME;
import static ast_st2_annotate.PredefinedNamesForRewriters.ITERATOR_HAS_NEXT_METHOD_NAME;

import java.util.ArrayList;
import java.util.List;

import ast_checkers.IteratorChecker;
import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_expr.FuncArg;
import ast_stmt.Stmt_for;
import ast_types.Type;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import hashed.Hash_ident;
import tokenize.Ident;
import tokenize.Token;

public class ForLoopRewriter {

  private static int var_counter = 0;

  public static void rewriteForLoop(ClassDeclaration object, Stmt_for forloop) {

    // for item in args { ... }
    // =>
    // {
    //     let __it0__: list_iterator_2_string = args.get_iterator();
    //     for ( ; __it0__.has_next() ; ) 
    //     {
    //         let item = __it0__.get_next();
    //         {
    //             <loop-body>
    //         }
    //     }
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
    final Token beginPos = iterAux.getBeginPos();
    iterAux.setResultType(elemType);

    final Ident item = iterAux.getIdent().getIdentifier();

    // 1) let iter: iterator<TYPE> = collection.get_iterator();
    final Ident iteratorVarName = createNameForIteratorItself();

    final VarDeclarator iteratorVar = genMethodGetIterator(collectionObjectName, iteratorType, iteratorVarName,
        beginPos);

    final VarDeclarator itemVar = genItemVar(elemType, item, iteratorVarName, beginPos);

    final List<VarDeclarator> decl = new ArrayList<>();
    decl.add(iteratorVar);
    decl.add(itemVar); // TODO: not here.
    forloop.setDecl(decl);

    setTest(forloop, iteratorVarName, beginPos);

  }

  private static void setTest(Stmt_for forloop, final Ident iteratorVarName, Token beginPos) {
    // 3) iter.has_next();
    final ExprExpression testExpression = call(ITERATOR_HAS_NEXT_METHOD_NAME, iteratorVarName, beginPos);
    forloop.setTest(testExpression);
  }

  private static VarDeclarator genItemVar(final Type elemType, final Ident item, final Ident iteratorVarName,
      Token beginPos) {
    // 4) let item = iter.get_net() ;

    final ExprExpression rhs = call(ITERATOR_GET_NEXT_METHOD_NAME, iteratorVarName, beginPos);
    final VarDeclarator var = new VarDeclarator(VarBase.LOCAL_VAR, Mods.letModifiers(), elemType, item, beginPos);
    var.setSimpleInitializer(rhs);

    return var;
  }

  private static ExprExpression id(Ident iteratorVarName, Token beginPos) {
    return new ExprExpression(new ExprIdent(iteratorVarName), beginPos);
  }

  private static ExprExpression call(final Ident funcname, final Ident objectName, Token beginPos) {
    final ArrayList<FuncArg> emptyArgs = new ArrayList<>();
    return new ExprExpression(new ExprMethodInvocation(id(objectName, beginPos), funcname, emptyArgs), beginPos);
  }

  private static Ident createNameForIteratorItself() {
    final String nextCount = String.format("%d", var_counter++);
    return Hash_ident.getHashedIdent("__it" + nextCount + "__");
  }

  private static VarDeclarator genMethodGetIterator(final Ident collectionObjectName, final Type iteratorType,
      final Ident iteratorVarName, Token beginPos) {

    final VarDeclarator iteratorVar = new VarDeclarator(VarBase.LOCAL_VAR, Mods.letModifiers(), iteratorType,
        iteratorVarName, beginPos);
    final ExprExpression iteratorVarInitializer = call(GET_ITERATOR_METHOD_NAME, collectionObjectName, beginPos);

    iteratorVar.setSimpleInitializer(iteratorVarInitializer);
    return iteratorVar;

  }

}
