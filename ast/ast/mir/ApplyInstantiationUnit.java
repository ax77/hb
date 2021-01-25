package ast.ast.mir;

import java.util.ArrayList;
import java.util.List;

import ast.ast.kinds.ExpressionBase;
import ast.ast.kinds.StatementBase;
import ast.ast.nodes.ClassDeclaration;
import ast.ast.nodes.expr.ExprArrayAccess;
import ast.ast.nodes.expr.ExprAssign;
import ast.ast.nodes.expr.ExprBinary;
import ast.ast.nodes.expr.ExprClassCreation;
import ast.ast.nodes.expr.ExprExpression;
import ast.ast.nodes.expr.ExprFieldAccess;
import ast.ast.nodes.expr.ExprIdent;
import ast.ast.nodes.expr.ExprMethodInvocation;
import ast.ast.nodes.expr.FuncArg;
import ast.ast.nodes.method.ClassMethodDeclaration;
import ast.ast.nodes.method.MethodParameter;
import ast.ast.nodes.stmt.StmtBlock;
import ast.ast.nodes.stmt.StmtBlockItem;
import ast.ast.nodes.stmt.StmtFor;
import ast.ast.nodes.stmt.StmtStatement;
import ast.ast.nodes.stmt.Stmt_if;
import ast.ast.nodes.unit.InstantiationUnit;
import ast.ast.nodes.vars.VarDeclarator;
import ast.ast.nodes.vars.VarInitializer;
import ast.parse.AstParseException;
import ast.types.ArrayType;
import ast.types.ClassType;
import ast.types.Type;
import ast.types.TypeBindings;
import jscan.tokenize.Token;

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
      for (MethodParameter fp : method.getParameters()) {
        symtabApplier.defineFunctionParameter(method, fp);
      }
    }

    //body
    final StmtBlock body = method.getBlock();
    for (StmtBlockItem block : body.getBlockStatements()) {

      // method variables
      final VarDeclarator var = block.getLocalVariable();
      if (var != null) {
        symtabApplier.initVarZero(var);
        symtabApplier.defineMethodVariable(method, var);

        initVarInitializer(var, var.getInitializer());
      }

      applyStatement(object, method, block.getStatement());
    }

    symtabApplier.closeMethodScope();
  }

  private void initVarInitializer(VarDeclarator var, List<VarInitializer> initializer) {

    if (initializer == null) {
      return;
    }

    // TODO Auto-generated method stub
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
      StmtFor forloop = statement.getForStmt();

      // 1)
      final ExprExpression collection = forloop.getAuxCollection();
      applyExpression(object, collection);

      // 2)
      ForLoopRewriter.rewriteForLoop(object, forloop);

      // 3) normal for-loop here, in its pure-huge form
      for (VarDeclarator var : forloop.getDecl()) {
        visitLocalVar(object, var);
      }

      applyExpression(object, forloop.getTest());
      applyExpression(object, forloop.getStep());
      applyStatement(object, method, forloop.getLoop());
    }

    else if (base == StatementBase.SIF) {
      Stmt_if sif = statement.getIfStmt();
      applyExpression(object, sif.getCondition());
      applyStatement(object, method, sif.getTrueStatement());
      applyStatement(object, method, sif.getOptionalElseStatement());

      if (!sif.getCondition().getResultType().is_boolean()) {
        throw new AstParseException("if condition must be only a boolean type");
      }
    }

    else if (base == StatementBase.SEXPR) {
      applyExpression(object, statement.getExprStmt());
    }

    else if (base == StatementBase.SBLOCK) {
      visitBlock(object, method, statement.getBlockStmt());
    }

    else if (base == StatementBase.SRETURN) {
      final ExprExpression retExpression = statement.getExprStmt();
      applyExpression(object, retExpression);
    }

    else {
      throw new AstParseException("unimpl. stmt.:" + base.toString());
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
      final ClassDeclaration clazz = e.getSelfExpression().getClazz();
      final ArrayList<Type> typeArguments = new ArrayList<>();
      final ClassType ref = new ClassType(clazz, typeArguments);
      e.setResultType(new Type(ref));
    }

    else if (base == ExpressionBase.EPRIMARY_NUMBER) {
      e.setResultType(TypeBindings.make_i64()); // TODO:
    }

    else if (base == ExpressionBase.EPRIMARY_NULL_LITERAL) {
      // TODO:
    }

    else if (base == ExpressionBase.ECLASS_INSTANCE_CREATION) {
      applyClassInstanceCreation(object, e);
    }

    else if (base == ExpressionBase.ESTRING_CONST) {
      String strconst = e.getStringConst();
      ArrayType arrtype = new ArrayType(TypeBindings.make_u8(), strconst.length());
      e.setResultType(new Type(arrtype));
    }

    else if (base == ExpressionBase.EARRAY_ACCESS) {
      ExprArrayAccess arrayAccess = e.getArrayAccess();
      applyExpression(object, arrayAccess.getArray());
      applyExpression(object, arrayAccess.getIndex());
      Type arrtype = arrayAccess.getArray().getResultType();
      if (!arrtype.is_array()) {
        throw new AstParseException("expect array");
      }
      Type arrof = arrtype.getArrayType().getArrayOf();
      e.setResultType(arrof);
    }

    else {
      throw new AstParseException("unimpl. expr.:" + base.toString());
    }

  }

  public void applyClassInstanceCreation(ClassDeclaration object, ExprExpression e) {
    ExprClassCreation classInstanceCreation = e.getClassCreation();

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

  private void applyBinary(ClassDeclaration object, ExprExpression e) {
    ExprBinary node = e.getBinary();
    Token operator = node.getOperator();

    final ExprExpression LHS = node.getLhs();
    final ExprExpression RHS = node.getRhs();

    applyExpression(object, LHS);
    applyExpression(object, RHS);

    ExprTypeSetters.setBinaryType(e, operator);

  }

  private void applyIdentifier(ClassDeclaration object, ExprExpression e) {

    final ExprIdent primaryIdent = e.getIdent();

    final Symbol sym = symtabApplier.findBindingFromIdentifierToTypename(primaryIdent.getIdentifier());

    if (sym.isClassType()) {
      throw new AstParseException("unimpl.");
    }

    // set type to expression
    final VarDeclarator variable = sym.getVariable();
    e.setResultType(variable.getType());

    if (variable.getInitializer() != null) {
      //applyExpression(object, variable.getInitializer().getInitializer());
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
    if (resultTypeOfObject == null || resultTypeOfObject.is_primitive()) {
      throw new AstParseException("expect reference for field access like [a.b] -> a must be a class.");
    }

    final ClassDeclaration whereWeWantToFindTheField = resultTypeOfObject.getClassType();
    final VarDeclarator field = whereWeWantToFindTheField.getField(fieldAccess.getFieldName());

    if (field == null) {
      throw new AstParseException("class has no field: " + fieldAccess.getFieldName().getName());
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
      if (resultTypeOfObject == null || resultTypeOfObject.is_primitive()) {
        throw new AstParseException("expect reference for method invocation like [a.b()] -> a must be a class.");
      }

      final ClassDeclaration whereWeWantToFindTheMethod = resultTypeOfObject.getClassType();
      final ClassMethodDeclaration method = whereWeWantToFindTheMethod.getMethod(methodInvocation.getFuncname(),
          arguments);

      if (method == null) {
        throw new AstParseException("class has no method: " + methodInvocation.getFuncname().getName());
      }

      e.setResultType(method.getType());

      // remember method
      methodInvocation.setMethod(method);

    }

    else {

      // function: fn(1,2,3)
      ClassMethodDeclaration func = object.getMethod(methodInvocation.getFuncname(), arguments);
      if (func == null) {
        throw new AstParseException("class has no method: " + methodInvocation.getFuncname().getName());
      }
      e.setResultType(func.getType());

      // remember method
      methodInvocation.setMethod(func);
    }

  }

}
