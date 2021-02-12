package ast_st3_tac;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_method.ClassMethodDeclaration;
import ast_stmt.StatementBase;
import ast_stmt.StmtBlock;
import ast_stmt.StmtBlockItem;
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
    for (ClassDeclaration td : o.getClasses()) {
      g("class " + td.getIdentifier().getName() + "\n{");
      genClazz(td);
      g("\n}");
    }
  }

  private void genClazz(final ClassDeclaration object) {

    //fields
    for (VarDeclarator field : object.getFields()) {
      g(field.toString());
    }

    //methods
    for (ClassMethodDeclaration method : object.getMethods()) {
      genMethod(object, method);
    }

    //constructors 
    for (ClassMethodDeclaration constructor : object.getConstructors()) {
      genMethod(object, constructor);
    }

    //destructor
    if (object.getDestructor() != null) {
      //genMethod(object, object.getDestructor());
    }

  }

  private void genMethod(final ClassDeclaration object, final ClassMethodDeclaration method) {

    g(method.getType().toString() + " " + method.getIdentifier() + "()");

    if (!method.isDestructor()) {
      for (VarDeclarator fp : method.getParameters()) {
      }
    }
    
    genBlock(method.getBlock());

    // //body
    // final StmtBlock body = method.getBlock();
    // for (StmtBlockItem block : body.getBlockItems()) {
    // 
    //   // method variables
    //   final VarDeclarator var = block.getLocalVariable();
    //   if (var != null) {
    //     genVar(var);
    //     genInitializer(var);
    //   }
    // 
    //   genStatement(block.getStatement());
    // }
    // 
    // g("\n}");

  }

  private void genStatement(StmtStatement statement) {
    if (statement == null) {
      return;
    }

    StatementBase base = statement.getBase();
    if (base == StatementBase.SFOREACH_TMP) {
    } else if (base == StatementBase.SIF) {
    } else if (base == StatementBase.SEXPR) {
      genExprStmt(statement);
    } else if (base == StatementBase.SBLOCK) {
      genBlock(statement.getBlockStmt());
    } else if (base == StatementBase.SRETURN) {
    } else if (base == StatementBase.SWHILE) {
    } else if (base == StatementBase.SFOR) {
    } else {
      throw new AstParseException("unimpl. stmt.:" + base.toString());
    }
  }

  private void genExprStmt(StmtStatement statement) {
    ExprExpression expr = statement.getExprStmt();
    TacGenerator tcg = new TacGenerator();
    tcg.gen(expr);
    String res = tcg.txt1(";\n");
    g("// " + expr.toString());
    g(res);
  }

  private void genBlock(StmtBlock blockStmt) {
    g("\n{\n");
    for (StmtBlockItem item : blockStmt.getBlockItems()) {
      if (item.isVarDeclarationItem()) {
        genVar(item.getLocalVariable());
      } else {
        genStatement(item.getStatement());
      }
    }
    g("\n}\n");
  }

  private void genVar(VarDeclarator localVariable) {
    g(localVariable.toString());
  }

  private void genInitializer(VarDeclarator var) {

  }
}
