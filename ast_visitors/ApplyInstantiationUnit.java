package njast.ast_visitors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_kinds.ExpressionBase;
import njast.ast_kinds.StatementBase;
import njast.ast_nodes.FuncArg;
import njast.ast_nodes.ModTypeNameHeader;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.expr.ExprAssign;
import njast.ast_nodes.expr.ExprBinary;
import njast.ast_nodes.expr.ExprClassInstanceCreation;
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
import njast.types.Ref;
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
      applyClazz(td);
    }
    symtabApplier.closeFileScope();
  }

  private void applyClazz(ClassDeclaration object) {

    symtabApplier.openClassScope(object.getIdentifier().getName());

    //fields
    for (VarDeclarator field : object.getFields()) {
      symtabApplier.defineClassField(object, field); // check redefinition
    }

    //methods
    for (ClassMethodDeclaration method : object.getMethods()) {
      applyMethod(object, method);
    }

    //constructors 
    for (ClassMethodDeclaration constructor : object.getConstructors()) {
      applyMethod(object, constructor);
    }

    //destructor
    if (object.getDestructor() != null) {
      applyMethod(object, object.getDestructor());
    }

    symtabApplier.closeClassScope();
  }

  private void applyMethod(ClassDeclaration object, ClassMethodDeclaration method) {

    StringBuilder sb = new StringBuilder();
    sb.append(object.getIdentifier().getName());
    sb.append("_");
    sb.append(method.getBase().toString());
    sb.append("_");
    sb.append(method.getUniqueIdToString());

    symtabApplier.openMethodScope(sb.toString());

    if (!method.isDestructor()) {
      for (ModTypeNameHeader fp : method.getParameters()) {
        symtabApplier.defineFunctionParameter(method, fp);
      }
    }

    //body
    final StmtBlock body = method.getBlock();
    for (StmtBlockItem block : body.getBlockStatements()) {

      // method variables
      final VarDeclarator localVars = block.getLocalVariable();
      if (localVars != null) {
        symtabApplier.initVarZero(localVars);
        symtabApplier.defineMethodVariable(method, localVars);
      }

      applyStatement(object, method, block.getStatement());
    }

    symtabApplier.closeMethodScope();
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
      //visitLocalVars(object, forloop.getDecl());
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
      visitLocalVar(object, block.getLocalVariable());
      applyStatement(object, method, block.getStatement());
    }
  }

  private void visitLocalVar(final ClassDeclaration object, VarDeclarator var) {

    if (var == null) {
      return;
    }

    symtabApplier.initVarZero(var);
    symtabApplier.defineBlockVar(var);
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
      applyAssign(object, e);
    }

    else if (base == ExpressionBase.EPRIMARY_IDENT) {
      applyIdentifier(object, e);
    }

    else if (base == ExpressionBase.EMETHOD_INVOCATION) {
      applyMethodInvocation(object, e);
    }

    else if (base == ExpressionBase.EFIELD_ACCESS) {
      applyFieldAccess(object, e);
    }

    else if (base == ExpressionBase.ESELF) {
      e.setResultType(new Type(new Ref(object, new ArrayList<>())));
    }

    else if (base == ExpressionBase.EPRIMARY_NUMBER) {
      e.setResultType(Type.INT_TYPE); // TODO:
    }

    else if (base == ExpressionBase.EPRIMARY_NULL_LITERAL) {
      // TODO:
    }

    else if (base == ExpressionBase.ECLASS_INSTANCE_CREATION) {
      applyClassInstanceCreation(object, e);
    }

    else {
      throw new EParseException("unimpl. expr.:" + base.toString());
    }

  }

  public void applyClassInstanceCreation(ClassDeclaration object, ExprExpression e) {
    ExprClassInstanceCreation classInstanceCreation = e.getClassInstanceCreation();

    for (FuncArg arg : classInstanceCreation.getArguments()) {
      applyExpression(object, arg.getExpression());
    }

    e.setResultType(classInstanceCreation.getType());
  }

  public void applyAssign(ClassDeclaration object, ExprExpression e) {
    ExprAssign node = e.getAssign();

    final ExprExpression lvalue = node.getLvalue();
    final ExprExpression rvalue = node.getRvalue();

    applyExpression(object, lvalue);
    applyExpression(object, rvalue);

    // TODO: type-checking, is assignable, etc...
    e.setResultType(node.getLvalue().getResultType());
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

    else {
      throw new EParseException("unimpl:[" + operator.getValue() + "]");
    }

  }

  private void applyIdentifier(ClassDeclaration object, ExprExpression e) {

    final ExprPrimaryIdent primaryIdent = e.getLiteralIdentifier();

    final Symbol sym = symtabApplier.findBindingFromIdentifierToTypename(primaryIdent.getIdentifier());

    if (sym.isClassType()) {
      throw new EParseException("unimpl.");
    }

    // set type to expression
    final VarDeclarator variable = sym.getVariable();
    e.setResultType(variable.getType());

    if (variable.getInitializer() != null) {
      applyExpression(object, variable.getInitializer().getInitializer());
    }

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

    final List<FuncArg> arguments = methodInvocation.getArguments();
    for (FuncArg arg : arguments) {
      applyExpression(object, arg.getExpression());
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
