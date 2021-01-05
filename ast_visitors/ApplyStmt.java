package njast.ast_visitors;

import java.util.List;

import njast.ast_kinds.StatementBase;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.expr.ExprExpression;
import njast.ast_nodes.stmt.StmtBlock;
import njast.ast_nodes.stmt.StmtBlockItem;
import njast.ast_nodes.stmt.StmtFor;
import njast.ast_nodes.stmt.StmtStatement;
import njast.ast_nodes.stmt.Stmt_if;
import njast.errors.EParseException;

public class ApplyStmt {
  private final ApplyCompilationUnit typeApplier;

  public ApplyStmt(ApplyCompilationUnit typeApplier) {
    this.typeApplier = typeApplier;
  }

  public boolean applyStatement(final ClassDeclaration object, final StmtStatement statement) {

    StatementBase base = statement.getBase();
    boolean hasItsOwnScope = (base == StatementBase.SBLOCK) || (base == StatementBase.SFOR);

    if (hasItsOwnScope) {
      typeApplier.openBlockScope(base.toString());
    }

    // process-statement

    if (base == StatementBase.SFOR) {
      StmtFor forloop = statement.getSfor();
      List<VarDeclarator> vars = forloop.getDecl();
      if (vars != null) {
        for (VarDeclarator var : vars) {
          typeApplier.initVarZero(var);
          typeApplier.defineBlockVar(var);
        }
      }
      StmtStatement loop = forloop.getLoop();
      if (loop != null) {
        applyStatement(object, loop);
      }
    }

    else if (base == StatementBase.SIF) {
      Stmt_if sif = statement.getSif();

      boolean result = new ApplyExpr(typeApplier).applyExpr(object, sif.getIfexpr());
      if (!result) {
        System.out.println("...??? expr");
      }

      final StmtStatement thenBlock = sif.getIfstmt();
      if (thenBlock != null) {
        applyStatement(object, thenBlock);
      }

      final StmtStatement elseBlock = sif.getIfelse();
      if (elseBlock != null) {
        applyStatement(object, elseBlock);
      }
    }

    else if (base == StatementBase.SEXPR) {
      boolean result = new ApplyExpr(typeApplier).applyExpr(object, statement.getSexpression());
      if (!result) {
        System.out.println("...??? expr");
      }
    }

    else if (base == StatementBase.SBLOCK) {

      final StmtBlock body = statement.getCompound();
      final List<StmtBlockItem> blocks = body.getBlockStatements();

      for (StmtBlockItem block : blocks) {

        // declarations
        final List<VarDeclarator> localVars = block.getLocalVars();
        if (localVars != null) {
          for (VarDeclarator var : localVars) {
            typeApplier.initVarZero(var);
            typeApplier.defineBlockVar(var);
          }
        }

        // statements
        final StmtStatement statementRest = block.getStatement();
        if (statementRest != null) {
          applyStatement(object, statementRest);
        }

      }

    }

    else if (base == StatementBase.SRETURN) {
      final ExprExpression retExpr = statement.getSexpression();
      if (retExpr != null) {
        boolean result = new ApplyExpr(typeApplier).applyExpr(object, retExpr);
        if (!result) {
          System.out.println("...??? expr");
        }
      }
      // TODO:here
      System.out.println(retExpr.toString());
    }

    else {
      throw new EParseException("unimpl. stmt.:" + base.toString());
    }

    if (hasItsOwnScope) {
      typeApplier.closeBlockScope();
    }

    return true;

  }

}
