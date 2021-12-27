package ast_class;

import java.util.List;

import ast_expr.ExprAssign;
import ast_expr.ExprBinary;
import ast_expr.ExprBuiltinFunc;
import ast_expr.ExprCast;
import ast_expr.ExprClassCreation;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_expr.ExprSizeof;
import ast_expr.ExprTernaryOperator;
import ast_expr.ExprTypeof;
import ast_expr.ExprUnary;
import ast_expr.ExpressionBase;
import ast_method.ClassMethodDeclaration;
import ast_sourceloc.SourceLocation;
import ast_stmt.StatementBase;
import ast_stmt.StmtBlock;
import ast_stmt.StmtFor;
import ast_stmt.StmtReturn;
import ast_stmt.StmtSelect;
import ast_stmt.StmtStatement;
import ast_types.Type;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import errors.ErrorLocation;
import literals.IntLiteral;
import tokenize.Ident;
import tokenize.Token;

/// the tree-traversal class, a template for future use.
/// if should be there, not in a txt files, be cause of the refactoring.
/// we have to keep this file clean and updated.

public class TestVisitor {

  public void traverseClazz(ClassDeclaration clazz) {
    if (clazz.isInterface()) {
      visitInterface(clazz);
    } else if (clazz.isStaticClass()) {
      visitStaticClass(clazz);
    } else if (clazz.isMainClass()) {
      visitMainClass(clazz);
    } else if (clazz.isNativeClass()) {
      visitNativeClass(clazz);
    } else {
      visitClass(clazz);
    }
  }

  private void visitNativeClass(ClassDeclaration clazz) {
    // TODO Auto-generated method stub
    visitClass(clazz);
  }

  private void visitClass(ClassDeclaration clazz) {

    for (VarDeclarator field : clazz.getFields()) {
      visitField(clazz, field);
    }

    for (ClassMethodDeclaration m : clazz.getConstructors()) {
      visitConstructor(clazz, m);
    }

    for (ClassMethodDeclaration m : clazz.getMethods()) {
      visitMethod(clazz, m);
    }

    visitDestructor(clazz, clazz.getDestructor());

    for (ClassMethodDeclaration m : clazz.getTests()) {
      visitUnittest(clazz, m);
    }

  }

  /// all of the members of the class

  private void visitMethodBlock(StmtBlock block) {
    // TODO Auto-generated method stub

  }

  private void visitUnittest(ClassDeclaration clazz, ClassMethodDeclaration m) {
    visitMethodBlock(m.getBlock());
  }

  private void visitDestructor(ClassDeclaration clazz, ClassMethodDeclaration m) {
    if (m == null) {
      return;
    }
    visitMethodBlock(m.getBlock());
  }

  private void visitMethod(ClassDeclaration clazz, ClassMethodDeclaration m) {
    visitMethodBlock(m.getBlock());
  }

  private void visitConstructor(ClassDeclaration clazz, ClassMethodDeclaration m) {
    visitMethodBlock(m.getBlock());
  }

  private void visitField(ClassDeclaration clazz, VarDeclarator field) {
    // TODO Auto-generated method stub

  }

  /// all of the classes

  private void visitMainClass(ClassDeclaration clazz) {
    // TODO Auto-generated method stub

  }

  private void visitStaticClass(ClassDeclaration clazz) {
    // TODO Auto-generated method stub

  }

  private void visitInterface(ClassDeclaration clazz) {
    // TODO Auto-generated method stub
  }

  /// expressions

  void visitExprSizeof(final ClassDeclaration clazz, ExprSizeof e) {
    Type type = e.getType();
  }

  void visitExprTernaryOperator(final ClassDeclaration clazz, ExprTernaryOperator e) {
    ExprExpression trueresult = e.getTrueResult();
    ExprExpression condition = e.getCondition();
    ExprExpression falseresult = e.getFalseResult();
  }

  void visitExprIdent(final ClassDeclaration clazz, ExprIdent e) {
    VarDeclarator var = e.getVar();
    ClassDeclaration staticclass = e.getStaticClass();
    Ident identifier = e.getIdentifier();
  }

  void visitExprBuiltinFunc(final ClassDeclaration clazz, ExprBuiltinFunc e) {
    Ident name = e.getName();
    List<ExprExpression> args = e.getArgs();
    String filetostring = e.getFileToString();
    String linetostring = e.getLineToString();
    String exprtostring = e.getExprToString();
  }

  void visitExprBinary(final ClassDeclaration clazz, ExprBinary e) {
    Token operator = e.getOperator();
    ExprExpression lhs = e.getLhs();
    ExprExpression rhs = e.getRhs();
  }

  void visitExprTypeof(final ClassDeclaration clazz, ExprTypeof e) {
    Type type = e.getType();
    ExprExpression expr = e.getExpr();
  }

  void visitExprFieldAccess(final ClassDeclaration clazz, ExprFieldAccess e) {
    VarDeclarator field = e.getField();
    ExprExpression object = e.getObject();
    Ident fieldname = e.getFieldName();
  }

  void visitExprCast(final ClassDeclaration clazz, ExprCast e) {
    Type totype = e.getToType();
    ExprExpression expressionforcast = e.getExpressionForCast();
  }

  void visitExprMethodInvocation(final ClassDeclaration clazz, ExprMethodInvocation e) {
    ClassMethodDeclaration method = e.getMethod();
    ExprExpression object = e.getObject();
    List<ExprExpression> arguments = e.getArguments();
    Ident funcname = e.getFuncname();
  }

  void visitExprAssign(final ClassDeclaration clazz, ExprAssign e) {
    Token operator = e.getOperator();
    ExprExpression lvalue = e.getLvalue();
    ExprExpression rvalue = e.getRvalue();
  }

  void visitExprClassCreation(final ClassDeclaration clazz, ExprClassCreation e) {
    ClassMethodDeclaration constructor = e.getConstructor();
    Type type = e.getType();
    List<ExprExpression> arguments = e.getArguments();
  }

  void visitExprUnary(final ClassDeclaration clazz, ExprUnary e) {
    Token operator = e.getOperator();
    ExprExpression operand = e.getOperand();
  }

  public void visitExpression(final ClassDeclaration object, final ExprExpression e) {

    if (e == null) {
      return;
    }

    SourceLocation location = e.getLocation();
    ExpressionBase base = e.getBase();
    IntLiteral number = e.getNumber();
    ExprAssign assign = e.getAssign();
    Type resulttype = e.getResultType();
    ClassDeclaration selfexpression = e.getSelfExpression();
    ExprUnary unary = e.getUnary();
    ExprBinary binary = e.getBinary();
    ExprIdent ident = e.getIdent();
    ExprMethodInvocation methodinvocation = e.getMethodInvocation();
    ExprFieldAccess fieldaccess = e.getFieldAccess();
    ExprClassCreation classcreation = e.getClassCreation();
    boolean booleanliteral = e.getBooleanLiteral();
    ExprCast castexpression = e.getCastExpression();
    ExprBuiltinFunc exprbuiltinfunc = e.getExprBuiltinFunc();
    ExprSizeof exprsizeof = e.getExprSizeof();
    ExprTernaryOperator ternaryoperator = e.getTernaryOperator();
    String locationtostring = e.getLocationToString();
    Token beginpos = e.getBeginPos();
    ExprTypeof exprtypeof = e.getExprTypeof();

    if (e.is(ExpressionBase.EUNARY)) {
      visitExprUnary(object, e.getUnary());
    }

    else if (e.is(ExpressionBase.EBINARY)) {
      visitExprBinary(object, e.getBinary());

    } else if (e.is(ExpressionBase.EASSIGN)) {
      visitExprAssign(object, e.getAssign());
    }

    else if (e.is(ExpressionBase.EPRIMARY_IDENT)) {
    }

    else if (e.is(ExpressionBase.EMETHOD_INVOCATION)) {
      visitExprMethodInvocation(object, e.getMethodInvocation());
    }

    else if (e.is(ExpressionBase.EFIELD_ACCESS)) {
      visitExprFieldAccess(object, e.getFieldAccess());
    }

    else if (e.is(ExpressionBase.ETHIS)) {
    }

    else if (e.is(ExpressionBase.EPRIMARY_NUMBER)) {
    }

    else if (e.is(ExpressionBase.ECLASS_CREATION)) {
      visitExprClassCreation(object, e.getClassCreation());
    }

    else if (e.is(ExpressionBase.EPRIMARY_STRING)) {
    }

    else if (e.is(ExpressionBase.EPRIMARY_CHAR)) {
    }

    else if (e.is(ExpressionBase.EBOOLEAN_LITERAL)) {
    }

    else if (e.is(ExpressionBase.ECAST)) {
      visitExprCast(object, e.getCastExpression());
    }

    else if (e.is(ExpressionBase.ETERNARY_OPERATOR)) {
      visitExprTernaryOperator(object, e.getTernaryOperator());
    }

    else if (e.is(ExpressionBase.ESIZEOF)) {
      visitExprSizeof(object, e.getExprSizeof());
    }

    else if (e.is(ExpressionBase.ETYPEOF)) {
      visitExprTypeof(object, e.getExprTypeof());
    }

    else if (e.is(ExpressionBase.EBUILTIN_FUNC)) {
      visitExprBuiltinFunc(object, e.getExprBuiltinFunc());
    }

    else {
      ErrorLocation.errorExpression("unimpl.expression-type-applier", e);
    }

  }

  /// statements

  private void visitExprStmt(final ClassDeclaration object, ClassMethodDeclaration method, final StmtStatement node) {
    visitExpression(object, node.getExprStmt());
  }

  private void visitReturn(final ClassDeclaration object, ClassMethodDeclaration method, final StmtReturn node) {
    visitExpression(object, node.getExpression());
  }

  private void visitFor(ClassDeclaration object, ClassMethodDeclaration method, StmtStatement s) {

    StmtFor node = s.getForStmt();

    visitExpression(object, node.getTest());
    visitExpression(object, node.getStep());
    visitBlock(object, method, node.getBlock());
  }

  private void visitSelectionStmt(final ClassDeclaration object, final ClassMethodDeclaration method,
      final StmtSelect node) {
    visitExpression(object, node.getCondition());
    visitBlock(object, method, node.getTrueStatement());
    visitBlock(object, method, node.getOptionalElseStatement());
  }

  private void visitBlock(final ClassDeclaration object, final ClassMethodDeclaration method, final StmtBlock block) {

    /// if without else, etc...
    if (block == null) {
      return;
    }

    for (StmtStatement item : block.getBlockItems()) {
      visitStatement(object, method, item);
    }

  }

  private void visitLocalVar(final ClassDeclaration object, ClassMethodDeclaration method, final VarDeclarator var) {
    visitInitializer(object, var);
  }

  private void visitInitializer(ClassDeclaration object, VarDeclarator var) {

  }

  public void visitStatement(final ClassDeclaration object, final ClassMethodDeclaration method,
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
      // nope
    } else if (base == StatementBase.SCONTINUE) {
      // nope
    } else if (base == StatementBase.SVAR_DECLARATION) {
      visitLocalVar(object, method, s.getLocalVariable());
    } else {
      throw new AstParseException("unimpl. stmt.:" + base.toString());
    }

  }

}
