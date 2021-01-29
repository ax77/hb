package ast_st2_annotate;

import static ast_st2_annotate.PredefinedNamesForRewriters.GET_ITERATOR_METHOD_NAME;
import static ast_st2_annotate.PredefinedNamesForRewriters.ITERATOR_GET_NEXT_METHOD_NAME;
import static ast_st2_annotate.PredefinedNamesForRewriters.ITERATOR_HAS_NEXT_METHOD_NAME;

import java.util.ArrayList;

import ast_checkers.IteratorChecker;
import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_expr.FuncArg;
import ast_stmt.StmtBlock;
import ast_stmt.StmtBlockItem;
import ast_stmt.StmtForeach;
import ast_stmt.StmtStatement;
import ast_stmt.StmtWhile;
import ast_types.Type;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import hashed.Hash_ident;
import tokenize.Ident;
import tokenize.Token;

public class ForeachToWhileRewriter {

  private static int var_counter = 0;

  private final SymbolTable symtabApplier;
  private final ClassDeclaration object;
  private final StmtForeach forloop;

  public ForeachToWhileRewriter(SymbolTable symtabApplier, ClassDeclaration object, StmtForeach forloop) {
    this.symtabApplier = symtabApplier;
    this.object = object;
    this.forloop = forloop;
  }

  public StmtBlock genBlock() {

    // // short form of the for-loop
    // //
    // list<string> args = new list<string>();
    // for item in args {
    //     print(item);
    // }
    // 
    // {
    //     let it: string_iter = args.get_iterator();
    //     while(it.has_next()) {
    //         let item: string = it.get_next();
    //         {
    //             print(item);
    //         }
    //     }
    // }
    // 
    // * create the block_I
    // * put in block_I iterator-var
    // * create the block_II
    // * put in block_II item-var
    // * put in block_II the block of the original loop
    // * create the while-loop with a test-expression and block_II
    // * put this while-loop in the block_I
    // * return block_I

    // we should know the type of this collection
    final SymExpressionApplier applier = new SymExpressionApplier(symtabApplier);
    applier.applyExpression(object, forloop.getAuxCollection());

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

    //  let iter: type = collection.get_iterator();
    final Ident iteratorVarName = createNameForIteratorItself();
    final VarDeclarator iteratorVar = genMethodGetIterator(collectionObjectName, iteratorType, iteratorVarName,
        beginPos);

    // iter.has_next()
    final ExprExpression condition = genTest(iteratorVarName, beginPos);

    // let item = iter.get_next() ;
    final Ident itemVarName = iterAux.getIdent().getIdentifier();
    final VarDeclarator itemVar = genItemVar(elemType, itemVarName, iteratorVarName, beginPos);

    // blocks generations
    final StmtBlock block_I = new StmtBlock();
    block_I.put(new StmtBlockItem(iteratorVar));

    final StmtBlock block_II = new StmtBlock();
    block_II.put(new StmtBlockItem(itemVar));
    block_II.put(new StmtBlockItem(new StmtStatement(forloop.getLoop(), beginPos)));

    final StmtWhile whileLoop = new StmtWhile(condition, block_II);
    block_I.put(new StmtBlockItem(new StmtStatement(whileLoop, beginPos)));

    return block_I;

  }

  private ExprExpression genTest(final Ident iteratorVarName, Token beginPos) {
    // iter.has_next();
    return call(ITERATOR_HAS_NEXT_METHOD_NAME, iteratorVarName, beginPos);
  }

  private VarDeclarator genItemVar(final Type elemType, final Ident item, final Ident iteratorVarName, Token beginPos) {
    // let item = iter.get_next() ;

    final ExprExpression rhs = call(ITERATOR_GET_NEXT_METHOD_NAME, iteratorVarName, beginPos);
    final VarDeclarator var = new VarDeclarator(VarBase.LOCAL_VAR, Mods.letModifiers(), elemType, item, beginPos);
    var.setSimpleInitializer(rhs);

    return var;
  }

  private ExprExpression id(Ident iteratorVarName, Token beginPos) {
    return new ExprExpression(new ExprIdent(iteratorVarName), beginPos);
  }

  private ExprExpression call(final Ident funcname, final Ident objectName, Token beginPos) {
    final ArrayList<FuncArg> emptyArgs = new ArrayList<>();
    return new ExprExpression(new ExprMethodInvocation(id(objectName, beginPos), funcname, emptyArgs), beginPos);
  }

  private Ident createNameForIteratorItself() {
    final String nextCount = String.format("%d", var_counter++);
    return Hash_ident.getHashedIdent("it_" + nextCount);
  }

  private VarDeclarator genMethodGetIterator(final Ident collectionObjectName, final Type iteratorType,
      final Ident iteratorVarName, Token beginPos) {

    final VarDeclarator iteratorVar = new VarDeclarator(VarBase.LOCAL_VAR, Mods.letModifiers(), iteratorType,
        iteratorVarName, beginPos);
    final ExprExpression iteratorVarInitializer = call(GET_ITERATOR_METHOD_NAME, collectionObjectName, beginPos);

    iteratorVar.setSimpleInitializer(iteratorVarInitializer);
    return iteratorVar;

  }

}
