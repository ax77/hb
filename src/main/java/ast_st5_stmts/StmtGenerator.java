package ast_st5_stmts;

import java.util.ArrayList;
import java.util.List;

import ast_stmt.StatementBase;
import ast_stmt.StmtBlock;
import ast_stmt.StmtBlockItem;
import ast_stmt.StmtBreak;
import ast_stmt.StmtContinue;
import ast_stmt.StmtFor;
import ast_stmt.StmtReturn;
import ast_stmt.StmtSelect;
import ast_stmt.StmtStatement;
import ast_vars.VarDeclarator;
import errors.AstParseException;

public class StmtGenerator {

  /// sometimes we have to walk the special kind of
  /// the statements, without the order of appearance.
  /// to add some semantic information into each of
  /// them, or to remove some things, etc...

  private final List<StmtBlockItem> allBlockItems;
  private final List<VarDeclarator> allVarDecls;
  private final List<StmtSelect> allStmtSelect;
  private final List<StmtStatement> allStmtExpression;
  private final List<StmtBlock> allStmtBlock;
  private final List<StmtReturn> allStmtReturn;
  private final List<StmtFor> allStmtFor;
  private final List<StmtBreak> allStmtBreak;
  private final List<StmtContinue> allStmtContinue;

  public StmtGenerator(final StmtBlock block) {
    this.allBlockItems = new ArrayList<>();
    this.allVarDecls = new ArrayList<>();
    this.allStmtSelect = new ArrayList<>();
    this.allStmtExpression = new ArrayList<>();
    this.allStmtBlock = new ArrayList<>();
    this.allStmtReturn = new ArrayList<>();
    this.allStmtFor = new ArrayList<>();
    this.allStmtBreak = new ArrayList<>();
    this.allStmtContinue = new ArrayList<>();
    visitBlock(block);
  }

  private void visitBlock(StmtBlock block) {
    if (block == null) {
      return;
    }
    allStmtBlock.add(block);
    for (StmtBlockItem item : block.getBlockItems()) {
      allBlockItems.add(item);

      if (item.isStatementItem()) {
        gen(item.getStatement());
      }
      if (item.isVarDeclarationItem()) {
        allVarDecls.add(item.getLocalVariable());
      }
    }
  }

  private void gen(final StmtStatement s) {
    if (s == null) {
      return;
    }
    StatementBase base = s.getBase();
    if (base == StatementBase.SIF) {
      allStmtSelect.add(s.getIfStmt());
      visitBlock(s.getIfStmt().getTrueStatement());
      visitBlock(s.getIfStmt().getOptionalElseStatement());
    } else if (base == StatementBase.SEXPR) {
      allStmtExpression.add(s);
    } else if (base == StatementBase.SBLOCK) {
      visitBlock(s.getBlockStmt());
    } else if (base == StatementBase.SRETURN) {
      allStmtReturn.add(s.getReturnStmt());
    } else if (base == StatementBase.SFOR) {
      allStmtFor.add(s.getForStmt());
      visitBlock(s.getForStmt().getBlock());
    } else if (base == StatementBase.SBREAK) {
      allStmtBreak.add(s.getBreakStmt());
    } else if (base == StatementBase.SCONTINUE) {
      allStmtContinue.add(s.getContinueStmt());
    } else {
      throw new AstParseException("unimpl. stmt.:" + base.toString());
    }
  }

  public List<StmtBlockItem> getAllBlockItems() {
    return allBlockItems;
  }

  public List<VarDeclarator> getAllVarDecls() {
    return allVarDecls;
  }

  public List<StmtSelect> getAllStmtSelect() {
    return allStmtSelect;
  }

  public List<StmtStatement> getAllStmtExpression() {
    return allStmtExpression;
  }

  public List<StmtBlock> getAllStmtBlock() {
    return allStmtBlock;
  }

  public List<StmtReturn> getAllStmtReturn() {
    return allStmtReturn;
  }

  public List<StmtFor> getAllStmtFor() {
    return allStmtFor;
  }

  public List<StmtBreak> getAllStmtBreak() {
    return allStmtBreak;
  }

  public List<StmtContinue> getAllStmtContinue() {
    return allStmtContinue;
  }

}
