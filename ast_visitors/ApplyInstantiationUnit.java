package njast.ast_visitors;

import java.util.HashSet;
import java.util.List;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_kinds.ExpressionBase;
import njast.ast_kinds.StatementBase;
import njast.ast_nodes.clazz.ClassConstructorDeclaration;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.clazz.methods.FormalParameter;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.expr.ExprAssign;
import njast.ast_nodes.expr.ExprBinary;
import njast.ast_nodes.expr.ExprExpression;
import njast.ast_nodes.expr.ExprFieldAccess;
import njast.ast_nodes.expr.ExprMethodInvocation;
import njast.ast_nodes.expr.ExprPrimaryIdent;
import njast.ast_nodes.stmt.StmtBlock;
import njast.ast_nodes.stmt.StmtBlockItem;
import njast.ast_nodes.stmt.StmtFor;
import njast.ast_nodes.stmt.StmtStatement;
import njast.ast_nodes.stmt.Stmt_if;
import njast.ast_nodes.top.InstantiationUnit;
import njast.errors.EParseException;
import njast.types.Type;

public class ApplyInstantiationUnit {

  private final SymtabApplier symtabApplier;

  public ApplyInstantiationUnit() {
    this.symtabApplier = new SymtabApplier();
  }

  //////////////////////////////////////////////////////////////////////
  // ENTRY
  //
  public void visit(InstantiationUnit o) {
    symtabApplier.openFileScope();
    for (ClassDeclaration td : o.getClasses()) {
      visit(td);
    }
    symtabApplier.closeFileScope();
  }

  private void visit(ClassDeclaration object) {

    symtabApplier.openClassScope(object.getIdentifier().getName());

    //fields
    for (VarDeclarator field : object.getFields()) {
      symtabApplier.defineClassField(object, field); // check redefinition
    }

    //methods
    for (ClassMethodDeclaration method : object.getMethods()) {
      symtabApplier.openMethodScope(method.getIdentifier().getName());

      symtabApplier.defineMethod(object, method); // check overloading/redefinition/etc

      for (FormalParameter fp : method.getFormalParameterList().getParameters()) {
        Type type = fp.getType();
        Ident name = fp.getIdentifier();
        symtabApplier.defineFunctionParameter(method, type, name);
      }

      //body
      final StmtBlock body = method.getBody();
      for (StmtBlockItem block : body.getBlockStatements()) {

        // method variables
        final List<VarDeclarator> localVars = block.getLocalVars();
        if (localVars != null) {
          for (VarDeclarator var : localVars) {
            symtabApplier.initVarZero(var);
            symtabApplier.defineMethodVariable(method, var);
          }
        }

        applyStatement(object, method, block.getStatement());
      }

      symtabApplier.closeMethodScope();
    }

    //constructors (the last, it works with methods and fields)
    for (ClassConstructorDeclaration constructor : object.getConstructors()) {
      symtabApplier.defineConstructor(object, constructor); // check overloading/redefinition/etc
    }

    symtabApplier.closeClassScope();
  }

  //////////////////////////////////////////////////////////////////////
  // STATEMENTS 
  //
  private void applyStatement(final ClassDeclaration object, ClassMethodDeclaration method,
      final StmtStatement statement) {

    if (statement == null) {
      return;
    }

    StatementBase base = statement.getBase();
    boolean hasItsOwnScope = (base == StatementBase.SBLOCK) || (base == StatementBase.SFOR);

    if (hasItsOwnScope) {
      symtabApplier.openBlockScope(base.toString());
    }

    if (base == StatementBase.SFOR) {
      StmtFor forloop = statement.getSfor();
      visitLocalVars(object, forloop.getDecl());
      applyExpression(object, forloop.getTest());
      applyExpression(object, forloop.getStep());
      applyStatement(object, method, forloop.getLoop());
    }

    else if (base == StatementBase.SIF) {
      Stmt_if sif = statement.getSif();
      applyExpression(object, sif.getCondition());
      applyStatement(object, method, sif.getTrueStatement());
      applyStatement(object, method, sif.getOptionalElseStatement());

      if (!sif.getCondition().getResultType().equals(Type.BOOLEAN_TYPE)) {
        throw new EParseException("if condition must be only a boolean type");
      }
    }

    else if (base == StatementBase.SEXPR) {
      applyExpression(object, statement.getSexpression());
    }

    else if (base == StatementBase.SBLOCK) {
      visitBlock(object, method, statement.getCompound());
    }

    else if (base == StatementBase.SRETURN) {

      final ExprExpression retExpression = statement.getSexpression();

      if (method.isVoid() && retExpression != null) {
        throw new EParseException("method returning void...");
      }

      applyExpression(object, retExpression);
    }

    else {
      throw new EParseException("unimpl. stmt.:" + base.toString());
    }

    if (hasItsOwnScope) {
      symtabApplier.closeBlockScope();
    }

  }

  private void visitBlock(final ClassDeclaration object, ClassMethodDeclaration method, final StmtBlock body) {
    for (StmtBlockItem block : body.getBlockStatements()) {
      visitLocalVars(object, block.getLocalVars());
      applyStatement(object, method, block.getStatement());
    }
  }

  private void visitLocalVars(final ClassDeclaration object, List<VarDeclarator> vars) {
    if (vars == null) {
      return;
    }

    for (VarDeclarator var : vars) {
      symtabApplier.initVarZero(var);
      symtabApplier.defineBlockVar(var);
    }

  }

  //////////////////////////////////////////////////////////////////////
  // EXPRESSIONS 
  //
  private void applyExpression(ClassDeclaration object, ExprExpression e) {

    if (e == null) {
      return;
    }
    if (e.getResultType() != null) {
      return;
    }

    ExpressionBase base = e.getBase();

    if (base == ExpressionBase.EBINARY) {
      applyBinary(object, e);
    }

    else if (base == ExpressionBase.EASSIGN) {
      ExprAssign node = e.getAssign();
      Token operator = node.getOperator();

      final ExprExpression LHS = node.getLvalue();
      final ExprExpression RHS = node.getRvalue();

      applyExpression(object, LHS);
      applyExpression(object, RHS);

      // TODO: type-checking
      e.setResultType(node.getLvalue().getResultType());
    }

    else if (base == ExpressionBase.EPRIMARY_IDENT) {
      applyIdentifier(e);
    }

    else if (base == ExpressionBase.EMETHOD_INVOCATION) {
      applyMethodInvocation(object, e);
    }

    else if (base == ExpressionBase.EFIELD_ACCESS) {
      applyFieldAccess(object, e);
    }

    else if (base == ExpressionBase.ETHIS) {
      e.setResultType(new Type(object));
    }

    else if (base == ExpressionBase.EPRIMARY_NUMBER) {
      e.setResultType(Type.INT_TYPE); // TODO:
    }

    else if (base == ExpressionBase.EPRIMARY_NULL_LITERAL) {
      // TODO:
    }

    else {
      throw new EParseException("unimpl. expr.:" + base.toString());
    }

  }

  private static HashSet<T> forLongType = new HashSet<>();
  static {

    // by now only:
    // add, mul, sub, div

    forLongType.add(T.T_PLUS);
    forLongType.add(T.T_MINUS);
    forLongType.add(T.T_TIMES);
    forLongType.add(T.T_DIVIDE);
  }

  private static HashSet<T> forBooleanType = new HashSet<>();
  static {

    // == ` != ` > ` >= ` < ` <=

    forBooleanType.add(T.T_EQ);
    forBooleanType.add(T.T_NE);

    forBooleanType.add(T.T_GT);
    forBooleanType.add(T.T_GE);

    forBooleanType.add(T.T_LT);
    forBooleanType.add(T.T_LE);
  }

  private void applyBinary(ClassDeclaration object, ExprExpression e) {
    ExprBinary node = e.getBinary();
    Token operator = node.getOperator();

    final ExprExpression LHS = node.getLhs();
    final ExprExpression RHS = node.getRhs();

    applyExpression(object, LHS);
    applyExpression(object, RHS);

    // TODO: type checker, compatible, etc...

    if (forLongType.contains(operator.getType())) {
      e.setResultType(Type.LONG_TYPE);
    }

    else if (forBooleanType.contains(operator.getType())) {
      e.setResultType(Type.BOOLEAN_TYPE);
    }

    // TODO: 

    else if (operator.ofType(T.T_COMMA)) {
      e.setResultType(e.getBinary().getRhs().getResultType());
    }

    else {
      throw new EParseException("unimpl:[" + operator.getValue() + "]");
    }

  }

  private void applyIdentifier(ExprExpression e) {

    final ExprPrimaryIdent primaryIdent = e.getLiteralIdentifier();

    final Symbol sym = symtabApplier.findBindingFromIdentifierToTypename(primaryIdent.getIdentifier());

    if (sym.isClassType()) {
      throw new EParseException("unimpl.");
    }

    // set type to expression
    final VarDeclarator variable = sym.getVariable();
    e.setResultType(variable.getType());

    // remember var
    primaryIdent.setVariable(variable);

  }

  private void applyFieldAccess(ClassDeclaration object, ExprExpression e) {

    final ExprFieldAccess fieldAccess = e.getFieldAccess();
    applyExpression(object, fieldAccess.getObject());

    // find the field, and get its type
    //
    final Type resultTypeOfObject = fieldAccess.getObject().getResultType(); // must be a reference!
    if (resultTypeOfObject == null || resultTypeOfObject.isPrimitive()) {
      throw new EParseException("expect reference for field access like [a.b] -> a must be a class.");
    }

    final ClassDeclaration whereWeWantToFindTheField = resultTypeOfObject.getClassType();
    final VarDeclarator field = whereWeWantToFindTheField.getField(fieldAccess.getFieldName());

    if (field == null) {
      throw new EParseException("class has no field: " + fieldAccess.getFieldName().getName());
    }

    e.setResultType(field.getType());

    // remember field
    fieldAccess.setField(field);

  }

  private void applyMethodInvocation(ClassDeclaration object, ExprExpression e) {

    final ExprMethodInvocation methodInvocation = e.getMethodInvocation();
    applyExpression(object, methodInvocation.getObject());

    final List<ExprExpression> arguments = methodInvocation.getArguments();
    for (ExprExpression arg : arguments) {
      applyExpression(object, arg);
    }

    if (methodInvocation.isMethodInvocation()) {
      // method: a.fn(1,2,3)

      final Type resultTypeOfObject = methodInvocation.getObject().getResultType(); // must be a reference!
      if (resultTypeOfObject == null || resultTypeOfObject.isPrimitive()) {
        throw new EParseException("expect reference for method invocation like [a.b()] -> a must be a class.");
      }

      final ClassDeclaration whereWeWantToFindTheMethod = resultTypeOfObject.getClassType();
      final ClassMethodDeclaration method = whereWeWantToFindTheMethod.getMethod(methodInvocation.getFuncname(),
          arguments);

      if (method == null) {
        throw new EParseException("class has no method: " + methodInvocation.getFuncname().getName());
      }

      e.setResultType(method.getType());

      // remember method
      methodInvocation.setMethod(method);

    }

    else {

      // function: fn(1,2,3)
      ClassMethodDeclaration func = object.getMethod(methodInvocation.getFuncname(), arguments);
      if (func == null) {
        throw new EParseException("class has no method: " + methodInvocation.getFuncname().getName());
      }
      e.setResultType(func.getType());

      // remember method
      methodInvocation.setMethod(func);
    }

  }

}
