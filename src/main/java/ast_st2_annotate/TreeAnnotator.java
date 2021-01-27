package ast_st2_annotate;

import static ast_st2_annotate.TreeScopes.F_ALL;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprArrayAccess;
import ast_expr.ExprArrayCreation;
import ast_expr.ExprAssign;
import ast_expr.ExprBinary;
import ast_expr.ExprClassCreation;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_expr.ExprUnary;
import ast_expr.ExprUtil;
import ast_expr.ExpressionBase;
import ast_expr.FuncArg;
import ast_method.ClassMethodDeclaration;
import ast_method.MethodParameter;
import ast_stmt.StatementBase;
import ast_stmt.StmtBlock;
import ast_stmt.StmtBlockItem;
import ast_stmt.StmtStatement;
import ast_stmt.Stmt_for;
import ast_stmt.Stmt_if;
import ast_types.ArrayType;
import ast_types.ClassType;
import ast_types.Type;
import ast_types.TypeBindings;
import ast_unit.InstantiationUnit;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import errors.ErrorLocation;
import hashed.Hash_ident;
import tokenize.Ident;
import tokenize.Token;

public class TreeAnnotator {

  private final TreeScopes symtabApplier;

  public TreeAnnotator() {
    this.symtabApplier = new TreeScopes();
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
        symtabApplier.defineMethodVariable(method, var);
        applyInitializer(object, var);
      }

      applyStatement(object, method, block.getStatement());
    }

    symtabApplier.closeMethodScope();
  }

  private void applyInitializer(final ClassDeclaration object, VarDeclarator var) {
    if (var.isArrayInitializer()) {
      throw new AstParseException("unimpl. array-inits.");
    }

    ExprExpression init = var.getSimpleInitializer();

    // initialize variable with its default value:
    // null for arrays and classes
    // zero for primitives
    // false for boolean
    if (init == null) {
      final Type tp = var.getType();
      final Token beginPos = var.getBeginPos();
      if (tp.is_numeric()) {
        ExprExpression zeroExpr = ExprUtil.getEmptyPrimitive(tp, beginPos);
        var.setSimpleInitializer(zeroExpr);
      } else if (tp.is_class() || tp.is_array()) {
        ExprExpression nullExpr = new ExprExpression(ExpressionBase.EPRIMARY_NULL_LITERAL, beginPos);
        var.setSimpleInitializer(nullExpr);
      } else if (tp.is_boolean()) {
        ExprExpression falseExpr = new ExprExpression(false, beginPos);
        var.setSimpleInitializer(falseExpr);
      }
    }

    init = var.getSimpleInitializer();
    if (init == null) {
      throw new AstParseException("unexpected, init must be present");
    }

    //MIR:TREE
    if (init.is(ExpressionBase.EARRAY_INSTANCE_CREATION)) {
      init.getArrayCreation().setVar(var);
    } else if (init.is(ExpressionBase.ECLASS_INSTANCE_CREATION)) {
      init.getClassCreation().setVar(var);
    }

    applyExpression(object, init);
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
    if (base == StatementBase.SFOR) {
      visitForLoop(object, method, statement);
    } else if (base == StatementBase.SIF) {
      visit_if(object, method, statement);
    } else if (base == StatementBase.SEXPR) {
      applyExpression(object, statement.getExprStmt());
    } else if (base == StatementBase.SBLOCK) {
      visitBlock(object, method, statement.getBlockStmt());
    } else if (base == StatementBase.SRETURN) {
      applyExpression(object, statement.getExprStmt());
    } else {
      throw new AstParseException("unimpl. stmt.:" + base.toString());
    }

  }

  private void visit_if(final ClassDeclaration object, ClassMethodDeclaration method, final StmtStatement statement) {
    Stmt_if sif = statement.getIfStmt();
    applyExpression(object, sif.getCondition());
    visitBlock(object, method, sif.getTrueStatement());
    visitBlock(object, method, sif.getOptionalElseStatement());

    if (!sif.getCondition().getResultType().is_boolean()) {
      throw new AstParseException("if condition must be only a boolean type");
    }
  }

  private void visitForLoop(final ClassDeclaration object, ClassMethodDeclaration method,
      final StmtStatement statement) {
    Stmt_for forloop = statement.getForStmt();

    if (forloop.isShortForm()) {

      // 1)
      final ExprExpression collection = forloop.getAuxCollection();
      applyExpression(object, collection);

      // 2)
      ForLoopRewriter.rewriteForLoop(object, forloop);

      // 3) normal for-loop here, in its pure-huge form

      List<StmtBlockItem> items = new ArrayList<>();
      for (VarDeclarator var : forloop.getDecl()) {
        items.add(new StmtBlockItem(var));
      }
      forloop.setDecl(null);
      forloop.setShortForm(false);

      items.add(new StmtBlockItem(new StmtStatement(forloop)));
      StmtBlock block = new StmtBlock(items);

      statement.replaceForLoopWithBlock(block);
      applyStatement(object, method, statement);

    }

    if (forloop.getDecl() != null) {
      for (VarDeclarator var : forloop.getDecl()) {
        visitLocalVar(object, var);
      }
    }

    applyExpression(object, forloop.getTest());
    applyExpression(object, forloop.getStep());
    visitBlock(object, method, forloop.getLoop());
  }

  private void visitBlock(final ClassDeclaration object, ClassMethodDeclaration method, final StmtBlock body) {
    symtabApplier.openBlockScope("block");

    for (StmtBlockItem block : body.getBlockStatements()) {
      visitLocalVar(object, block.getLocalVariable());
      applyStatement(object, method, block.getStatement());
    }

    symtabApplier.closeBlockScope();
  }

  private void visitLocalVar(final ClassDeclaration object, VarDeclarator var) {
    if (var == null) {
      return;
    }

    symtabApplier.defineBlockVar(var);
    applyInitializer(object, var);
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

    if (e.is(ExpressionBase.EUNARY)) {
      applyUnary(object, e);
    } else if (e.is(ExpressionBase.EBINARY)) {
      applyBinary(object, e);
    } else if (e.is(ExpressionBase.EASSIGN)) {
      applyAssign(object, e);
    } else if (e.is(ExpressionBase.EPRIMARY_IDENT)) {
      applyIdentifier(object, e);
    } else if (e.is(ExpressionBase.EMETHOD_INVOCATION)) {
      applyMethodInvocation(object, e);
    } else if (e.is(ExpressionBase.EFIELD_ACCESS)) {
      applyFieldAccess(object, e);
    } else if (e.is(ExpressionBase.ESELF)) {
      applySelfLiteral(e);
    } else if (e.is(ExpressionBase.EPRIMARY_NUMBER)) {
      applyNumericLiteral(e);
    } else if (e.is(ExpressionBase.EPRIMARY_NULL_LITERAL)) {
      // TODO:
    } else if (e.is(ExpressionBase.ECLASS_INSTANCE_CREATION)) {
      applyClassInstanceCreation(object, e);
    } else if (e.is(ExpressionBase.ESTRING_CONST)) {
      applyStringLiteral(e);
    } else if (e.is(ExpressionBase.EARRAY_ACCESS)) {
      applyArrayAccess(object, e);
    } else if (e.is(ExpressionBase.ECHAR_CONST)) {
      e.setResultType(TypeBindings.make_u8());
    } else if (e.is(ExpressionBase.EBOOLEAN_LITERAL)) {
      e.setResultType(TypeBindings.make_boolean()); // TODO: ?
    } else if (e.is(ExpressionBase.EARRAY_INSTANCE_CREATION)) {
      applyArrayCreation(e);
    } else {
      ErrorLocation.errorExpression("unimpl.expression-type-applier", e);
    }

  }

  private void checkArraySize(ArrayType array, ExprExpression e) {
    if (array.getCount() == 0) {
      ErrorLocation.errorExpression("array-creation with zero-size", e);
    }
    if (array.getArrayOf().is_array()) {
      checkArraySize(array.getArrayOf().getArrayType(), e);
    }
  }

  private void applyArrayCreation(ExprExpression e) {
    final ExprArrayCreation arrayCreation = e.getArrayCreation();
    final Type type = arrayCreation.getType();
    final ArrayType array = type.getArrayType();
    checkArraySize(array, e);
    e.setResultType(type);
  }

  public void applyNumericLiteral(ExprExpression e) {
    e.setResultType(e.getNumber().getType());
  }

  public void applySelfLiteral(ExprExpression e) {
    final ClassDeclaration clazz = e.getSelfExpression().getClazz();
    final ArrayList<Type> typeArguments = new ArrayList<>();
    final ClassType ref = new ClassType(clazz, typeArguments);
    e.setResultType(new Type(ref));
  }

  public void applyStringLiteral(ExprExpression e) {
    String strconst = e.getBeginPos().getValue(); // TODO:__string__
    ArrayType arrtype = new ArrayType(TypeBindings.make_u8(), strconst.length());
    e.setResultType(new Type(arrtype));
  }

  public void applyArrayAccess(ClassDeclaration object, ExprExpression e) {
    ExprArrayAccess arrayAccess = e.getArrayAccess();
    applyExpression(object, arrayAccess.getArray());
    applyExpression(object, arrayAccess.getIndex());
    Type arrtype = arrayAccess.getArray().getResultType();
    if (!arrtype.is_array()) {
      ErrorLocation.errorExpression("expect array", e);
    }
    Type arrof = arrtype.getArrayType().getArrayOf();
    e.setResultType(arrof);
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
    Lvalue.checkHard(lvalue);

    final ExprExpression rvalue = node.getRvalue();

    applyExpression(object, lvalue);
    applyExpression(object, rvalue);

    final Type lhsType = node.getLvalue().getResultType();
    if (rvalue.is(ExpressionBase.EPRIMARY_NULL_LITERAL)) {
      // TODO:
    }

    else {
      final Type rhsType = node.getRvalue().getResultType();
      if (!lhsType.is_equal_to(rhsType)) {
        ErrorLocation.errorExpression("types are different for assign", e);
      }
    }

    e.setResultType(lhsType);
  }

  public void applyUnary(ClassDeclaration object, ExprExpression e) {
    final ExprUnary node = e.getUnary();
    applyExpression(object, node.getOperand());
    ExprTypeSetters.setUnaryType(e);
  }

  private void applyBinary(ClassDeclaration object, ExprExpression e) {
    final ExprBinary node = e.getBinary();
    applyExpression(object, node.getLhs());
    applyExpression(object, node.getRhs());
    ExprTypeSetters.setBinaryType(e);
  }

  private void applyIdentifier(ClassDeclaration object, ExprExpression e) {

    final ExprIdent primaryIdent = e.getIdent();

    final Symbol sym = symtabApplier.findVar(primaryIdent.getIdentifier(), F_ALL);
    if (sym == null) {
      ErrorLocation.errorExpression("symbol was not declared in this scope", e);
    }

    if (sym.isClassType()) {
      throw new AstParseException("unimpl.");
    }

    // set type to expression
    final VarDeclarator variable = sym.getVariable();
    e.setResultType(variable.getType());

    applyInitializer(object, variable);

    //MIR:TREE
    primaryIdent.setVariable(variable);

  }

  private void applyFieldAccess(ClassDeclaration object, ExprExpression e) {

    final ExprFieldAccess fieldAccess = e.getFieldAccess();
    applyExpression(object, fieldAccess.getObject());

    final String fieldNameToString = fieldAccess.getFieldName().getName();

    // find the field, and get its type

    final Type resultTypeOfObject = fieldAccess.getObject().getResultType(); // must be a reference!
    if (resultTypeOfObject == null) {
      ErrorLocation.errorExpression("type unspecified for field-access expression:", e);
    }
    boolean isOkAccess = resultTypeOfObject.is_class() || resultTypeOfObject.is_array();
    if (!isOkAccess) {
      ErrorLocation
          .errorExpression("expect reference for field access like [a.b] -> a must be a class, or array.length", e);
    }

    // array-property access

    if (resultTypeOfObject.is_array()) {
      final Ident expectedProperty = Hash_ident.getHashedIdent("length");
      if (!fieldAccess.getFieldName().equals(expectedProperty)) {
        ErrorLocation.errorExpression("array has no property: " + fieldNameToString, e);
      }
      e.setResultType(TypeBindings.make_u64());

      // remember array
      fieldAccess.setArray(resultTypeOfObject.getArrayType());
    }

    // class-field access

    else {
      final ClassDeclaration whereWeWantToFindTheField = resultTypeOfObject.getClassType();
      final VarDeclarator field = whereWeWantToFindTheField.getField(fieldAccess.getFieldName());

      if (field == null) {
        ErrorLocation.errorExpression("class has no field: " + fieldNameToString, e);
      }

      e.setResultType(field.getType());

      //MIR:TREE
      fieldAccess.setField(field);
    }

  }

  private void applyMethodInvocation(ClassDeclaration object, ExprExpression e) {

    final ExprMethodInvocation methodInvocation = e.getMethodInvocation();
    applyExpression(object, methodInvocation.getObject());

    final List<FuncArg> arguments = methodInvocation.getArguments();
    for (FuncArg arg : arguments) {
      applyExpression(object, arg.getExpression());
    }

    // a.fn(1,2,3)
    // self.fn(1,2,3)

    final Type resultTypeOfObject = methodInvocation.getObject().getResultType(); // must be a reference!
    if (resultTypeOfObject == null || resultTypeOfObject.is_primitive()) {
      ErrorLocation.errorExpression("expect reference for method invocation like [a.b()] -> a must be a class.", e);
    }

    final ClassDeclaration whereWeWantToFindTheMethod = resultTypeOfObject.getClassType();
    final ClassMethodDeclaration method = whereWeWantToFindTheMethod.getMethod(methodInvocation.getFuncname(),
        arguments);

    if (method == null) {
      ErrorLocation.errorExpression("class has no method: " + methodInvocation.getFuncname().getName(), e);
    }

    e.setResultType(method.getType());

    //MIR:TREE
    methodInvocation.setMethod(method);

  }

}
