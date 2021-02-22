package ast_st3_tac;

import ast_builtins.BuiltinNames;
import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_method.ClassMethodDeclaration;
import ast_printers.GenericListPrinter;
import ast_st3_tac.ir.CopierNamer;
import ast_stmt.StatementBase;
import ast_stmt.StmtBlock;
import ast_stmt.StmtBlockItem;
import ast_stmt.StmtFor;
import ast_stmt.StmtReturn;
import ast_stmt.StmtSelect;
import ast_stmt.StmtStatement;
import ast_unit.InstantiationUnit;
import ast_vars.VarDeclarator;
import errors.AstParseException;

public class UnitToText {

  private final StringBuilder textout;

  @Override
  public String toString() {
    return textout.toString();
  }

  public UnitToText(InstantiationUnit unit) {
    this.textout = new StringBuilder();
    visit(unit);
  }

  private void g(String what) {
    textout.append(what);
    textout.append("\n");
  }

  private void visit(InstantiationUnit o) {

    genStructs(o);

    g("class main_class" + "\n{");
    for (ClassDeclaration c : o.getClasses()) {
      g("/// METHODS: " + c.getIdentifier().getName());
      genClazzMethods(c);
    }
    g("\n}");

  }

  private void genStructs(InstantiationUnit o) {

    for (ClassDeclaration c : o.getClasses()) {
      final String name = c.getIdentifier().getName();
      if (name.equals("main_class")) {
        continue;
      }

      g("class " + name + "\n{");

      for (VarDeclarator field : c.getFields()) {
        g(field.toString());
      }

      g("\n}");
    }

  }

  private void genClazzMethods(final ClassDeclaration object) {

    //constructors 
    for (ClassMethodDeclaration constructor : object.getConstructors()) {
      genMethod(object, constructor);
    }

    //destructor
    if (object.getDestructor() != null) {
      genMethod(object, object.getDestructor());
    }

    //methods
    for (ClassMethodDeclaration method : object.getMethods()) {
      genMethod(object, method);
    }

  }

  private void genMethod(final ClassDeclaration object, final ClassMethodDeclaration method) {

    if (method.getIdentifier().equals(BuiltinNames.opAssign_ident)) {
      g(method.getType().toString() + " " + CopierNamer.getMethodName(method)
          + GenericListPrinter.paramsToStringWithBraces(method.getParameters()));
      g(method.getBlock().toString());
      return;
    }

    if (method.getIdentifier().getName().equals("main")) {
      g("void main()");
    } else {
      g(method.getType().toString() + " " + CopierNamer.getMethodName(method)
          + GenericListPrinter.paramsToStringWithBraces(method.getParameters()));
    }

    genBlock(method.getBlock());

  }

  private void genStatement(StmtStatement s) {
    if (s == null) {
      return;
    }
    StatementBase base = s.getBase();
    if (base == StatementBase.SIF) {
      genSelection(s.getIfStmt());
    } else if (base == StatementBase.SEXPR) {
      genExprStmt(s);
    } else if (base == StatementBase.SBLOCK) {
      genBlock(s.getBlockStmt());
    } else if (base == StatementBase.SRETURN) {
      genReturn(s.getReturnStmt());
    } else if (base == StatementBase.SFOR) {
      genFor(s.getForStmt());
    } else if (base == StatementBase.SBREAK) {
      g("break;");
    } else if (base == StatementBase.SCONTINUE) {
      g("continue;");
    } else {
      throw new AstParseException("unimpl. stmt.:" + base.toString());
    }
  }

  private void genFor(StmtFor forStmt) {
    g("for(;;)");
    genBlock(forStmt.getBlock());
  }

  private void genSelection(StmtSelect ifStmt) {

    g("/////// " + ifStmt.getCondition().toString());
    g(ifStmt.getLinearCondition().toString());
    String last = ifStmt.getLinearCondition().getDestToString();

    g("if(" + last + ")");
    genBlock(ifStmt.getTrueStatement());

    if (ifStmt.hasElse()) {
      g("else ");
      genBlock(ifStmt.getOptionalElseStatement());
    }
  }

  private void genReturn(StmtReturn returnStmt) {

    if (returnStmt.hasExpression()) {
      g("/////// return " + returnStmt.getExpression().toString());
      g(returnStmt.getLinearExpression().toString());
      g("return " + returnStmt.getLinearExpression().getDestToString() + ";");
    } else {
      g("return;");
    }

  }

  private void genExprStmt(StmtStatement statement) {
    ExprExpression expr = statement.getExprStmt();
    g("/////// " + expr.toString());
    g(statement.getLinearExprStmt().toString());
  }

  private void genBlock(StmtBlock blockStmt) {
    g("\n{\n");
    for (StmtBlockItem item : blockStmt.getBlockItems()) {
      if (item.isVarDeclarationItem()) {
        g("/////// " + item.getLocalVariable().toString());
        g(item.getLinearLocalVariable().toString());
      } else {
        genStatement(item.getStatement());
      }
    }
    if (!blockStmt.getDestr().isEmpty()) {
      g("\n{\n");
      g(blockStmt.getDestr().toString());
      g("\n}\n");
    }
    g("\n}\n");
  }

}
