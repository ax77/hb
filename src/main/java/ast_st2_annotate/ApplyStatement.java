package ast_st2_annotate;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_expr.ExprUnary;
import ast_method.ClassMethodDeclaration;
import ast_st3_tac.FlatCode;
import ast_stmt.StatementBase;
import ast_stmt.StmtBlock;
import ast_stmt.StmtBlockItem;
import ast_stmt.StmtBreak;
import ast_stmt.StmtContinue;
import ast_stmt.StmtFor;
import ast_stmt.StmtReturn;
import ast_stmt.StmtSelect;
import ast_stmt.StmtStatement;
import ast_types.TypeBindings;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import errors.ErrorLocation;
import tokenize.T;
import tokenize.Token;

public class ApplyStatement {

  private final SymbolTable symtabApplier;

  public ApplyStatement(SymbolTable symtabApplier) {
    this.symtabApplier = symtabApplier;
  }

  public void applyStatement(final ClassDeclaration object, final ClassMethodDeclaration method,
      final StmtStatement s) {

    if (s == null) {
      return;
    }

    StatementBase base = s.getBase();
    if (base == StatementBase.SIF) {
      visitSelectionStmt(object, method, s.getIfStmt());
    } else if (base == StatementBase.SEXPR) {
      visitExprStmt(object, method, s);
    } else if (base == StatementBase.SBLOCK) {
      visitBlock(object, method, s.getBlockStmt());
    } else if (base == StatementBase.SRETURN) {
      visitReturn(object, method, s.getReturnStmt());
    } else if (base == StatementBase.SFOR) {
      visitFor(object, method, s);
    } else if (base == StatementBase.SBREAK) {
      //TODO:
    } else if (base == StatementBase.SCONTINUE) {
      visitContinue(object, method, s);
    } else {
      throw new AstParseException("unimpl. stmt.:" + base.toString());
    }

  }

  private void visitContinue(ClassDeclaration object, ClassMethodDeclaration method, StmtStatement s) {

    /// we should execute the step of the for-loop 
    /// before the continue

    final Token beginPos = s.getBeginPos();
    final StmtContinue con = s.getContinueStmt();

    /// ret-2
    final StmtFor forStmt = con.getLoop();
    if (forStmt.getStep() == null) {
      return;
    }

    final StmtBlock stepBlock = new StmtBlock();
    final ExprExpression step = forStmt.getStep();
    if (step.getResultType() == null) {
      throw new AstParseException("TODO: continue, result type of the for step.");
    }

    final StmtStatement statement = new StmtStatement(step, beginPos);
    statement.setLinearExprStmt(GetCodeItems.getFlatCode(step, method));

    stepBlock.pushItemBack(new StmtBlockItem(statement));
    stepBlock.pushItemBack(new StmtBlockItem(new StmtStatement(con, beginPos)));

    s.replaceContinueWithBlock(stepBlock);

  }

  private void visitExprStmt(final ClassDeclaration object, ClassMethodDeclaration method, final StmtStatement node) {
    applyExpression(object, node.getExprStmt());
    semExprStmt(object, method, node);
  }

  private void visitReturn(final ClassDeclaration object, ClassMethodDeclaration method, final StmtReturn node) {
    applyExpression(object, node.getExpression());
    semReturn(object, method, node);
  }

  private void visitFor(ClassDeclaration object, ClassMethodDeclaration method, StmtStatement s) {

    StmtFor node = s.getForStmt();

    symtabApplier.openBlockScope("block", node.getBlock());
    visitLocalVar(object, node.getDecl());
    applyExpression(object, node.getInit());
    applyExpression(object, node.getTest());
    applyExpression(object, node.getStep());
    visitBlock(object, method, node.getBlock());
    symtabApplier.closeBlockScope();

    semFor(object, method, s);
  }

  private void visitSelectionStmt(final ClassDeclaration object, final ClassMethodDeclaration method,
      final StmtSelect node) {
    applyExpression(object, node.getCondition());
    visitBlock(object, method, node.getTrueStatement());
    visitBlock(object, method, node.getOptionalElseStatement());
    semSelection(object, method, node);
  }

  private void visitBlock(final ClassDeclaration object, final ClassMethodDeclaration method, final StmtBlock block) {

    /// if without else, etc...
    if (block == null) {
      return;
    }

    symtabApplier.openBlockScope("block", block);

    for (StmtBlockItem item : block.getBlockItems()) {
      visitLocalVar(object, item.getLocalVariable());
      applyStatement(object, method, item.getStatement());
      semBlockItem(object, method, item);
    }

    symtabApplier.closeBlockScope();

    GetDestr.semBlock(object, method, block);
  }

  private void visitLocalVar(final ClassDeclaration object, final VarDeclarator var) {
    if (var == null) {
      return;
    }

    symtabApplier.defineBlockVar(var);
    applyInitializer(object, var);
  }

  private void applyExpression(ClassDeclaration object, ExprExpression e) {
    ApplyExpression applier = new ApplyExpression(symtabApplier);
    applier.applyExpression(object, e);
  }

  private void applyInitializer(ClassDeclaration object, VarDeclarator var) {
    ApplyInitializer applier = new ApplyInitializer(symtabApplier);
    applier.applyInitializer(object, var);

  }

  /// 3ac-semantic

  private void semExprStmt(ClassDeclaration object, ClassMethodDeclaration method, final StmtStatement node) {
    node.setLinearExprStmt(GetCodeItems.getFlatCode(node.getExprStmt(), method));
  }

  private void semBlockItem(ClassDeclaration object, ClassMethodDeclaration method, StmtBlockItem item) {
    item.setLinearLocalVariable(GetCodeItems.getFlatCode(item.getLocalVariable(), method));
  }

  private void semReturn(ClassDeclaration object, ClassMethodDeclaration method, final StmtReturn node) {
    node.setLinearExpression(GetCodeItems.getFlatCode(node.getExpression(), method));
    symtabApplier.peekBlock().addReturn(node);
  }

  private void semFor(ClassDeclaration object, ClassMethodDeclaration method, StmtStatement s) {
    StmtFor node = s.getForStmt();

    checkIsBoolean(node.getTest());

    node.setLinearDecl(GetCodeItems.getFlatCode(node.getDecl(), method));
    node.setLinearInit(GetCodeItems.getFlatCode(node.getInit(), method));
    node.setLinearTest(GetCodeItems.getFlatCode(node.getTest(), method));
    node.setLinearStep(GetCodeItems.getFlatCode(node.getStep(), method));

    /// rewrite for-loop

    /// for(int i = 0; i < 8; i += 1) {
    ///   ...
    /// }
    /// ::
    /// {
    ///   int i = 0;
    ///   for(;;) {
    ///     if(!(i < 8)) {
    ///       break;
    ///     }
    ///     ...
    ///     {
    ///       i += 1;
    ///     }
    ///   }
    /// }

    final Token beginPos = s.getBeginPos();
    final StmtBlock outerBlock = new StmtBlock();

    if (node.getDecl() != null) {
      final StmtBlockItem item = new StmtBlockItem(node.getDecl());
      item.setLinearLocalVariable(node.getLinearDecl());
      outerBlock.pushItemBack(item);
      //node.setDecl(null);
    }
    if (node.getTest() != null) {

      /// if(!test)
      final ExprUnary un = new ExprUnary(new Token(beginPos, "!", T.T_EXCLAMATION), node.getTest());
      final ExprExpression eNot = new ExprExpression(un, beginPos);
      eNot.setResultType(TypeBindings.make_boolean(beginPos));

      /// { break; }
      StmtBlock ifBlock = new StmtBlock();
      ifBlock.pushItemBack(new StmtBlockItem(new StmtStatement(new StmtBreak(node), beginPos)));

      final StmtSelect selection = new StmtSelect(eNot, ifBlock, null);
      selection.setLinearCondition(GetCodeItems.getFlatCode(eNot, method));

      final StmtBlockItem breakTheLoop = new StmtBlockItem(new StmtStatement(selection, beginPos));
      node.getBlock().pushItemFront(breakTheLoop);
      //node.setTest(null);
    }

    // { j += 1; }
    if (node.getStep() != null) {
      final StmtStatement statement = new StmtStatement(node.getStep(), beginPos);
      statement.setLinearExprStmt(node.getLinearStep());

      final StmtBlock stepBlock = new StmtBlock();
      final StmtBlockItem item = new StmtBlockItem(statement);
      stepBlock.pushItemBack(item);

      final StmtBlockItem stepBlockItem = new StmtBlockItem(new StmtStatement(stepBlock, beginPos));
      node.getBlock().pushItemBack(stepBlockItem);
      //node.setStep(null);
    }

    outerBlock.pushItemBack(new StmtBlockItem(new StmtStatement(node, beginPos)));
    s.replaceForWithBlock(outerBlock);
  }

  private void semSelection(ClassDeclaration object, ClassMethodDeclaration method, final StmtSelect node) {
    checkIsBoolean(node.getCondition());
    node.setLinearCondition(GetCodeItems.getFlatCode(node.getCondition(), method));
  }

  private void checkIsBoolean(ExprExpression e) {
    // that's ok - the test expression of the for-loop must ne null
    // for example.
    if (e == null) {
      return;
    }
    if (!e.getResultType().is_boolean()) {
      ErrorLocation.errorExpression("expected boolean type: ", e);
    }
  }

}
