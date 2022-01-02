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
    Ident iterVarName = Hash_ident.getHashedIdent("__iterator");
    VarDeclarator iteratorDecl = new VarDeclarator(VarBase.LOCAL_VAR, new Modifiers(), tp, iterVarName, beginPos);
    ExprExpression iteratorInit = new ExprExpression(new ExprIdent(name), beginPos);
    iteratorDecl.setSimpleInitializer(iteratorInit);

    // loop test expression
    //
    // tmp != default(FieldType)
    //
    Token notEqualOp = new Token(beginPos, "!=", T.T_NE);
    ExprExpression lhs = new ExprExpression(new ExprIdent(iterVarName), beginPos);
    ExprExpression rhs = new ExprExpression(new ExprDefaultValueForType(tp), beginPos);
    ExprExpression test = new ExprExpression(new ExprBinary(notEqualOp, lhs, rhs), beginPos);

    // loop step expression
    //
    // tmp = tmp.FieldName
    //
    Token assignOp = new Token(beginPos, "=", T.T_ASSIGN);
    ExprExpression lvalue = new ExprExpression(new ExprIdent(iterVarName), beginPos);
    ExprExpression rvalue = new ExprExpression(new ExprFieldAccess(lvalue, name), beginPos);
    ExprExpression step = new ExprExpression(new ExprAssign(assignOp, lvalue, rvalue), beginPos);

    // the loop itself

    // outer block with a declaration
    StmtBlock outerBlockNamespace = new StmtBlock();
    outerBlockNamespace.pushItemBack(new StmtStatement(iteratorDecl, beginPos));

    // for loop with test and step expressions
    StmtFor forLoop = new StmtFor();
    forLoop.setTest(test);
    forLoop.setStep(step);
    forLoop.setBlock(new StmtBlock()); // the block of the FOR-LOOP, here will be our marking function.
    outerBlockNamespace.pushItemBack(new StmtStatement(forLoop, beginPos));

    // the block itself as a result
    StmtStatement item = new StmtStatement(outerBlockNamespace, beginPos);
    return item;
  }

}
