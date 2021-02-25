package ast_st5_stmts.execs;

import java.util.ArrayList;
import java.util.List;

import ast_expr.ExprExpression;
import ast_expr.ExprUnary;
import ast_method.ClassMethodDeclaration;
import ast_st3_tac.LinearExpression;
import ast_st3_tac.LinearExpressionBuilder;
import ast_stmt.StatementBase;
import ast_stmt.StmtBlock;
import ast_stmt.StmtFor;
import ast_stmt.StmtStatement;
import ast_types.TypeBindings;
import errors.AstParseException;
import tokenize.T;
import tokenize.Token;

public class Rewriter {

  private final ClassMethodDeclaration method;
  private final LinearBlock result;
  private final List<LinearLoop> loops;

  public Rewriter(ClassMethodDeclaration method) {
    this.method = method;
    this.result = new LinearBlock();
    this.loops = new ArrayList<>();

    visitBlock(method.getBlock(), result);
  }

  public LinearBlock getResult() {
    return result;
  }

  private void visitBlock(StmtBlock block, LinearBlock result) {
    if (block == null) {
      throw new AstParseException("unexpected empty block");
    }
    for (StmtStatement item : block.getBlockItems()) {
      gen(item, result);
    }
  }

  private void gen(final StmtStatement s, LinearBlock result) {
    if (s == null) {
      throw new AstParseException("unexpected empty statement");
    }

    StatementBase base = s.getBase();

    if (base == StatementBase.SIF) {

      /// 1) linearize the if-expression, and put it into the block
      LinearExpression lexpr = LinearExpressionBuilder.build(s.getIfStmt().getCondition(), method.getClazz(), method);
      result.pushItemBack(new LinearStatement(StatementBase.SEXPR, lexpr));

      /// 2) create the if-statement with the result variable
      LinearSelection linearSelection = new LinearSelection(lexpr.getDest());
      result.pushItemBack(new LinearStatement(linearSelection));

      /// 3) visit the true block of the created if, with the true-statement 
      /// of the original block.
      visitBlock(s.getIfStmt().getTrueStatement(), linearSelection.getTrueBlock());

      if (s.getIfStmt().hasElse()) {

        linearSelection.setElseBlock(new LinearBlock());
        visitBlock(s.getIfStmt().getOptionalElseStatement(), linearSelection.getElseBlock());
      }

    }

    else if (base == StatementBase.SEXPR) {
      LinearExpression lexpr = LinearExpressionBuilder.build(s.getExprStmt(), method.getClazz(), method);
      result.pushItemBack(new LinearStatement(StatementBase.SEXPR, lexpr));
    }

    else if (base == StatementBase.SBLOCK) {
      final LinearStatement nestedStmt = new LinearStatement(new LinearBlock());
      result.pushItemBack(nestedStmt);

      visitBlock(s.getBlockStmt(), nestedStmt.getLinearBlock());
    }

    else if (base == StatementBase.SRETURN) {
    }

    else if (base == StatementBase.SFOR) {

      final StmtFor forStmt = s.getForStmt();
      final ExprExpression test = forStmt.getTest();
      final ExprExpression step = forStmt.getStep();

      final LinearLoop resultLoop = new LinearLoop();
      pushLoop(resultLoop);
      result.pushItemBack(new LinearStatement(resultLoop));

      if (test != null) {
        final Token beginPos = test.getBeginPos();
        final ExprUnary un = new ExprUnary(new Token(beginPos, "!", T.T_EXCLAMATION), test);
        final ExprExpression eNot = new ExprExpression(un, beginPos);
        eNot.setResultType(TypeBindings.make_boolean(beginPos));

        /// 1) create the first item of the for-block, and append this expression as
        /// an item of a for-loop-block
        /// if (!test) { break; }
        LinearExpression testExpr = LinearExpressionBuilder.build(eNot, method.getClazz(), method);
        resultLoop.setTest(testExpr);
        resultLoop.getBlock().pushItemBack(new LinearStatement(StatementBase.SEXPR, testExpr));

        /// NOTE: this statement guard has no destructors, bacause it is a
        /// first statement in a loop, and it know nothing about any variable
        /// which will be defined later.
        LinearSelection breakGuard = new LinearSelection(testExpr.getDest());
        breakGuard.getTrueBlock().pushItemBack(new LinearStatement(new LinearBreak(peekLoop())));

        /// 2) append the test-guard
        resultLoop.getBlock().pushItemBack(new LinearStatement(breakGuard));
      }

      /// the 'step' is a last statement of the for-loop-block
      /// but we should expand it later, if we will see any break
      /// or continue;
      if (step != null) {
        LinearExpression stepExpr = LinearExpressionBuilder.build(step, method.getClazz(), method);
        resultLoop.setStep(stepExpr);
      }

      /// visit the block, and we know the test, and the step
      /// for break and continue statements.
      visitBlock(forStmt.getBlock(), resultLoop.getBlock());

      if (resultLoop.hasStep()) {
        resultLoop.getBlock().pushItemBack(new LinearStatement(StatementBase.SEXPR, resultLoop.getStep()));
      }

      popLoop();
    }

    else if (base == StatementBase.SBREAK) {
      /// TODO: this statement has destructors from 
      /// the top of the loop to this particularly
      /// point... all variables defined below are ignored.
      ///
      LinearBreak label = new LinearBreak(peekLoop());
      result.pushItemBack(new LinearStatement(label));
    }

    else if (base == StatementBase.SCONTINUE) {
      /// TODO: this statement has destructors from 
      /// the top of the loop to this particularly
      /// point... all variables defined below are ignored.
      ///
      /// TODO: can we use the previously expanded expression,
      /// or generate the new one from the original for-loop ExprExpression-step?
      ///
      final LinearLoop theLoopOfTheContinue = peekLoop();
      if (theLoopOfTheContinue.hasStep()) {
        /// we should append the 'step' expression
        /// if it was a for-loop before 'continue'
        result.pushItemBack(new LinearStatement(StatementBase.SEXPR, theLoopOfTheContinue.getStep()));
      }

      LinearContinue label = new LinearContinue(theLoopOfTheContinue);
      result.pushItemBack(new LinearStatement(label));
    }

    else if (base == StatementBase.SVAR_DECLARATION) {

      LinearExpression lexpr = LinearExpressionBuilder.build(s.getLocalVariable(), method.getClazz(), method);
      result.pushItemBack(new LinearStatement(StatementBase.SVAR_DECLARATION, lexpr));

    }

    else {
      throw new AstParseException("unimpl. stmt.:" + base.toString());
    }
  }

  /// LOOPS stack

  private void pushLoop(LinearLoop s) {
    loops.add(0, s);
  }

  private void popLoop() {
    loops.remove(0);
  }

  private LinearLoop peekLoop() {
    return loops.get(0);
  }

}
