package _st2_annotate;

import ast_expr.ExprAssign;
import ast_expr.ExprBinary;
import ast_expr.ExprDefaultValueForType;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprIdent;
import ast_modifiers.Modifiers;
import ast_stmt.StmtBlock;
import ast_stmt.StmtFor;
import ast_stmt.StmtStatement;
import ast_types.Type;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import hashed.Hash_ident;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public abstract class ExpandRecursiveCallIntoLoop {

  public static StmtStatement expandField(VarDeclarator field) {

    final Token beginPos = field.getBeginPos();
    final Type tp = field.getType();
    Ident name = field.getIdentifier();

    /// An example:
    ///
    /// class node { node prev; node next; }
    ///
    /// We cannot generate direct calls, because it will cause a recursion.
    /// And it depends on how the size of the object;
    /// If it is a linked-list with 1_000_000 of nodes - we do not have enough stack space
    /// for invoke all these calls.
    /// So - we have to linearize a recursion into a simple loop.
    /// for(FieldType tmp = FieldName; tmp != default(FieldType); tmp = tmp.FieldName) {
    ///     mark_ptr(tmp);
    /// }
    ///
    ///
    /// {
    ///   node iterator = this.next;
    ///   for(; iterator != default(type); ) 
    ///   {
    ///     node saved_ref = iterator.next;
    ///     iterator.next = default(type);
    ///     {
    ///       // destroy the object here
    ///     }
    ///     iterator = saved_ref;
    ///   }
    /// }

    Ident iterVarName = Hash_ident.getHashedIdent("iterator");
    VarDeclarator iteratorDecl = new VarDeclarator(VarBase.LOCAL_VAR, new Modifiers(), tp, iterVarName, beginPos);
    ExprExpression iteratorInit = new ExprExpression(new ExprIdent(name), beginPos);
    iteratorDecl.setSimpleInitializer(iteratorInit);

    // outer block with a declaration
    StmtBlock outerBlockNamespace = new StmtBlock();
    outerBlockNamespace.pushItemBack(new StmtStatement(iteratorDecl, beginPos));

    // loop test expression
    //
    // iterator != default(FieldType)
    //
    Token notEqualOp = new Token(beginPos, "!=", T.T_NE);
    ExprExpression lhs = new ExprExpression(new ExprIdent(iterVarName), beginPos);
    ExprExpression rhs = new ExprExpression(new ExprDefaultValueForType(tp), beginPos);
    ExprExpression test = new ExprExpression(new ExprBinary(notEqualOp, lhs, rhs), beginPos);

    // loop step expression
    //
    // saved_ref = iterator.next
    //

    Ident tmpVarName = Hash_ident.getHashedIdent("saved_ref");
    VarDeclarator tmpVarDecl = new VarDeclarator(VarBase.LOCAL_VAR, new Modifiers(), tp, tmpVarName, beginPos);

    final ExprExpression iteratorDotNext = new ExprExpression(new ExprIdent(iterVarName), beginPos);
    final ExprFieldAccess fieldAccessTmp = new ExprFieldAccess(iteratorDotNext, name);
    final ExprExpression tmpVarInitializer = new ExprExpression(fieldAccessTmp, beginPos);
    tmpVarDecl.setSimpleInitializer(tmpVarInitializer);

    // iterator = saved_ref;
    //
    Token tokEqualOp = new Token(beginPos, "=", T.T_ASSIGN);
    ExprExpression lvalue = new ExprExpression(new ExprIdent(iterVarName), beginPos);
    ExprExpression rvalue = new ExprExpression(new ExprIdent(tmpVarName), beginPos);
    ExprExpression assign = new ExprExpression(new ExprAssign(tokEqualOp, lvalue, rvalue), beginPos);

    // the loop itself

    // for loop with test and step expressions
    StmtFor forLoop = new StmtFor();
    StmtBlock forLoopBody = new StmtBlock();
    forLoopBody.pushItemBack(new StmtStatement(tmpVarDecl, beginPos));
    forLoopBody.pushItemBack(new StmtStatement(new StmtBlock(), beginPos)); // and empty block, where the value will be deleted...
    forLoopBody.pushItemBack(new StmtStatement(assign, beginPos));

    forLoop.setTest(test);
    forLoop.setBlock(forLoopBody); // the block of the FOR-LOOP, here will be our marking function.

    // the block itself as a result
    outerBlockNamespace.pushItemBack(new StmtStatement(forLoop, beginPos));
    return new StmtStatement(outerBlockNamespace, beginPos);
  }

}
