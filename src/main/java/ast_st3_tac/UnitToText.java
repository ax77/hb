package ast_st3_tac;

import java.util.List;
import java.util.UUID;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_printers.GenericListPrinter;
import ast_st3_tac.vars.CopierNamer;
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

  private final CodegenContext context;
  private final StringBuilder textout;

  @Override
  public String toString() {
    return textout.toString();
  }

  public UnitToText(CodegenContext context, InstantiationUnit unit) {
    this.context = context;
    this.textout = new StringBuilder();
    visit(unit);
  }

  private void g(String what) {
    textout.append(what);
    textout.append("\n");
  }

  private String uid() {
    String ret = UUID.randomUUID().toString();
    ret = ret.replace('-', '_');
    return "_" + ret;
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

    context.setCurrentMethodName(method.getIdentifier());

    Type paramType = new Type(new ClassTypeRef(object, object.getTypeParametersT()), object.getBeginPos());
    List<VarDeclarator> params = method.getParameters();
    if (!method.getIdentifier().getName().equals("opAssign")) {
      params.add(0, new VarDeclarator(VarBase.METHOD_PARAMETER, new Modifiers(), paramType,
          Hash_ident.getHashedIdent("_this_"), method.getBeginPos()));
    }

    if (method.getIdentifier().getName().equals("main")) {
      g("void main()");
    } else {
      g(method.getType().toString() + " " + CopierNamer.getMethodName(method)
          + GenericListPrinter.paramsToStringWithBraces(params));
    }

    genBlock(method.getBlock());

    context.setCurrentMethodName(null);

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
    TacGenerator tcg = new TacGenerator(expr, context);
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
      TacGenerator tcg = new TacGenerator(expr, context);
      String res = tcg.txt1(";\n");
      g("// return " + expr.toString());
      g(res);
      last = tcg.getLastResultNameToString();
    }

    g("return " + last + ";");
  }

  private void genExprStmt(StmtStatement statement) {
    ExprExpression expr = statement.getExprStmt();
    TacGenerator tcg = new TacGenerator(expr, context);
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

    // final ExprExpression expr = localVariable.getSimpleInitializer();
    // TacGenerator tcg = new TacGenerator(expr, context);
    // String res = tcg.txt1(";\n");
    // g("// " + localVariable.toString());
    // g(res);
    // String last = tcg.getLastResultNameToString();
    // g(localVariable.getType().toString() + " " + localVariable.getIdentifier().getName() + " = " + last + ";");

    TacGenerator tcg = new TacGenerator(localVariable, context);
    String res = tcg.txt1(";\n");
    g("// " + localVariable.toString());
    g(res);

  }

}
