package ast_st3_tac;

import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_expr.ExpressionBase;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_printers.ExprPrinters;
import ast_printers.VarPrinters;
import ast_stmt.StatementBase;
import ast_stmt.StmtBlock;
import ast_stmt.StmtBlockItem;
import ast_stmt.StmtReturn;
import ast_stmt.StmtSelect;
import ast_stmt.StmtStatement;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_unit.InstantiationUnit;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import hashed.Hash_ident;

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

    for (ClassDeclaration c : o.getClasses()) {
      g("/// METHODS: " + c.getIdentifier().getName());
      genClazzMethods(c);
    }

  }

  private void genStructs(InstantiationUnit o) {

    for (ClassDeclaration c : o.getClasses()) {
      g("class " + c.getIdentifier().getName() + "\n{");

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

    Type paramType = new Type(new ClassTypeRef(object, object.getTypeParametersT()), object.getBeginPos());
    List<VarDeclarator> params = method.getParameters();
    params.add(0, new VarDeclarator(VarBase.METHOD_PARAMETER, new Modifiers(), paramType,
        Hash_ident.getHashedIdent("_this_"), method.getBeginPos()));

    g(method.getType().toString() + " " + NamesGen.getMethodName(method) + "(" + VarPrinters.varsTosCode(params) + ")");

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
    } else if (base == StatementBase.SWHILE) {
    } else if (base == StatementBase.SFOR) {
    } else {
      throw new AstParseException("unimpl. stmt.:" + base.toString());
    }
  }

  private void genSelection(StmtSelect ifStmt) {

    ExprExpression expr = ifStmt.getCondition();
    TacGenerator tcg = new TacGenerator();
    tcg.gen(expr);
    String res = tcg.txt1(";\n");
    g("// " + expr.toString());
    g(res);
    String last = tcg.getLastResultNameToString();

    g("if(" + last + ")");
    genStatement(ifStmt.getTrueStatement());

    if (ifStmt.hasElse()) {
      boolean isElseIf = ifStmt.isElseIf();

      String header = "else";
      if (isElseIf) {
        header = "else {\n";
      }
      g(header);

      genStatement(ifStmt.getOptionalElseStatement());

      String footer = "";
      if (isElseIf) {
        footer = "\n}\n";
      }
      g(footer);
    }
  }

  private void genReturn(StmtReturn returnStmt) {

    String last = "";
    if (returnStmt.hasExpression()) {
      ExprExpression expr = returnStmt.getExpression();
      TacGenerator tcg = new TacGenerator();
      tcg.gen(expr);
      String res = tcg.txt1(";\n");
      g("// return " + expr.toString());
      g(res);
      last = tcg.getLastResultNameToString();
    }

    g("return " + last + ";");
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
    g("\n}" + VarPrinters.bindedVarsComment(blockStmt.getRelatedVariables()));
  }

  private void genVar(VarDeclarator localVariable) {

    final ExprExpression expr = localVariable.getSimpleInitializer();

    if (expr.is(ExpressionBase.ECLASS_INSTANCE_CREATION)) {
      TacGenerator tcg = new TacGenerator();
      tcg.gen(expr);
      String res = tcg.txt1(";\n");
      g("// " + localVariable.toString());
      g(res);
    }

    else {

      TacGenerator tcg = new TacGenerator();
      tcg.gen(expr);
      String res = tcg.txt1(";\n");
      g("// " + localVariable.toString());
      g(res);
      String last = tcg.getLastResultNameToString();
      g(localVariable.getType().toString() + " " + localVariable.getIdentifier().getName() + " = " + last + ";");

    }
  }

}
