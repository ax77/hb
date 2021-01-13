package njast.templates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import njast.ast_kinds.ExpressionBase;
import njast.ast_kinds.StatementBase;
import njast.ast_nodes.FuncArg;
import njast.ast_nodes.ModTypeNameHeader;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.expr.ExprClassInstanceCreation;
import njast.ast_nodes.expr.ExprExpression;
import njast.ast_nodes.expr.ExprMethodInvocation;
import njast.ast_nodes.stmt.StmtBlock;
import njast.ast_nodes.stmt.StmtBlockItem;
import njast.ast_nodes.stmt.StmtFor;
import njast.ast_nodes.stmt.StmtStatement;
import njast.ast_nodes.stmt.Stmt_if;
import njast.errors.EParseException;

public class GetAllTypeSetters {

  // find all references!
  // then - do what we want with its types - expand in template, etc.
  private final List<TypeSetter> typeSetters;

  public GetAllTypeSetters(ClassDeclaration clazz) {
    this.typeSetters = new ArrayList<>();
    visit(clazz);
  }

  private void push(TypeSetter tp) {
    this.typeSetters.add(tp);
  }

  public List<TypeSetter> getTypeSetters() {
    return Collections.unmodifiableList(typeSetters);
  }

  private void visit(ClassDeclaration object) {

    //fields
    for (VarDeclarator var : object.getFields()) {
      visitVariable(object, var);
    }

    //methods
    for (ClassMethodDeclaration method : object.getMethods()) {

      //return
      if (!method.isVoid()) {
        push(method.getHeader());
      }

      //parameters
      for (ModTypeNameHeader fp : method.getParameters()) {
        push(fp);
      }

      //body
      visitBlock(method.getBlock(), object);

    }

    //constructors
    for (ClassMethodDeclaration constructor : object.getConstructors()) {
      visitBlock(constructor.getBlock(), object);
    }

  }

  private void visitBlock(StmtBlock body, ClassDeclaration object) {

    for (StmtBlockItem block : body.getBlockStatements()) {

      // locals
      visitVariable(object, block.getLocalVariable());

      // or statements
      visitStatement(object, block.getStatement());

    }
  }

  private void visitVariable(ClassDeclaration object, VarDeclarator var) {

    // that's ok
    if (var == null) {
      return;
    }

    push(var.getHeader());
    if (var.getInitializer() != null) {
      visitExpression(object, var.getInitializer().getInitializer());
    }

  }

  private void visitStatement(final ClassDeclaration object, final StmtStatement statement) {

    // that's ok, loop without loop, etc.
    if (statement == null) {
      return;
    }

    StatementBase base = statement.getBase();

    if (base == StatementBase.SFOR) {
      StmtFor forloop = statement.getSfor();
      // visitVariables(object, forloop.getDecl());
      visitExpression(object, forloop.getTest());
      visitExpression(object, forloop.getStep());
      visitStatement(object, forloop.getLoop());
    }

    else if (base == StatementBase.SIF) {
      Stmt_if sif = statement.getSif();
      visitExpression(object, sif.getCondition());
      visitStatement(object, sif.getTrueStatement());
      visitStatement(object, sif.getOptionalElseStatement());
    }

    else if (base == StatementBase.SEXPR) {
      visitExpression(object, statement.getSexpression());
    }

    else if (base == StatementBase.SBLOCK) {
      visitBlock(statement.getCompound(), object);
    }

    else if (base == StatementBase.SRETURN) {
      visitExpression(object, statement.getSexpression());
    }

    else {
      throw new EParseException("unimpl. stmt.:" + base.toString());
    }

  }

  private void visitExpression(ClassDeclaration object, ExprExpression expression) {

    // that's ok, we don't check for example has for-loop condition or doesn't has, ets.
    if (expression == null) {
      return;
    }

    ExpressionBase base = expression.getBase();

    if (base == ExpressionBase.EBINARY) {
      visitExpression(object, expression.getBinary().getLhs());
      visitExpression(object, expression.getBinary().getRhs());
    }

    else if (base == ExpressionBase.EASSIGN) {
      visitExpression(object, expression.getAssign().getLvalue());
      visitExpression(object, expression.getAssign().getRvalue());
    }

    else if (base == ExpressionBase.EPRIMARY_IDENT) {
    }

    else if (base == ExpressionBase.EMETHOD_INVOCATION) {
      final ExprMethodInvocation methodInvocation = expression.getMethodInvocation();
      visitExpression(object, methodInvocation.getObject());

      for (FuncArg arg : methodInvocation.getArguments()) {
        visitExpression(object, arg.getExpression());
      }
    }

    else if (base == ExpressionBase.EFIELD_ACCESS) {
      visitExpression(object, expression.getFieldAccess().getObject());
    }

    else if (base == ExpressionBase.ESELF) {
    }

    else if (base == ExpressionBase.EPRIMARY_NUMBER) {
    }

    else if (base == ExpressionBase.EPRIMARY_NULL_LITERAL) {
    }

    else if (base == ExpressionBase.ECLASS_INSTANCE_CREATION) {

      final ExprClassInstanceCreation classInstanceCreation = expression.getClassInstanceCreation();
      push(classInstanceCreation);

      for (FuncArg arg : classInstanceCreation.getArguments()) {
        visitExpression(object, arg.getExpression());
      }
    }

    else {
      throw new EParseException("unimpl. expr.:" + base.toString());
    }

  }

}
