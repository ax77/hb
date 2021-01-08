package njast.ast_visitors;

import java.util.List;

import jscan.symtab.Ident;
import jscan.tokenize.Token;
import njast.ast_kinds.ExpressionBase;
import njast.ast_kinds.StatementBase;
import njast.ast_nodes.clazz.ClassConstructorDeclaration;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.clazz.methods.FormalParameter;
import njast.ast_nodes.clazz.vars.VarBase;
import njast.ast_nodes.clazz.vars.VarDeclarator;
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
import njast.symtab.ScopeLevels;
import njast.symtab.Symtab;
import njast.types.Type;

public class ApplyInstantiationUnit {

  private Symtab<Ident, Symbol> typeNames;
  private Symtab<Ident, Symbol> variablesClass; // fields
  private Symtab<Ident, Symbol> variablesMethod; // parameters+locals_outside_block
  private Symtab<Ident, Symbol> variablesBlock; // locals_inside_block

  public ApplyInstantiationUnit() {
    this.typeNames = new Symtab<>();
    this.variablesClass = new Symtab<>();
    this.variablesMethod = new Symtab<>();
    this.variablesBlock = new Symtab<>();
  }

  private void openFileScope() {
    this.typeNames.pushscope(ScopeLevels.FILE_SCOPE, "compilation_unit_scope");
  }

  private void closeFileScope() {
    this.typeNames.popscope();
  }

  private void openClassScope(String className) {
    this.variablesClass.pushscope(ScopeLevels.CLASS_SCOPE, className);
  }

  private void closeClassScope() {
    this.variablesClass.popscope();
  }

  private void openMethodScope(String methodName) {
    this.variablesMethod.pushscope(ScopeLevels.METHOD_SCOPE, methodName);
  }

  private void closeMethodScope() {
    this.variablesMethod.popscope();
  }

  private void openBlockScope(String name) {
    this.variablesBlock.pushscope(ScopeLevels.BLOCK_SCOPE, name);
  }

  private void closeBlockScope() {
    this.variablesBlock.popscope();
  }

  private void dump() {
    typeNames.dump();
    variablesClass.dump();
    variablesMethod.dump();
    variablesBlock.dump();
  }

  //to define a symbol in a function or nested block and check redefinition
  //1) check the current block 
  //2) check the whole function scope
  //
  //to bind a symbol in a expression - 
  //1) block scope
  //2) function scope
  //3) class scope
  //4) file scope
  //
  //note: function parameters also a variables in a function scope

  // you have to initialize each field with its default value
  // you have to initialize each LHS like that, before you'll apply RHS
  //
  //  class Idn {
  //    int i = func();
  //    int func() {
  //      return this.i;
  //    }
  //  }

  //////////////////////////////////////////////////////////////////////
  // SYMTAB 
  //

  private Symbol varsym(VarDeclarator var) {
    return new Symbol(var);
  }

  private void defineFunctionParameter(ClassMethodDeclaration method, Type paramType, Ident paramName) {
    VarDeclarator var = new VarDeclarator(VarBase.METHOD_PARAMETER, method.getLocation(), paramType, paramName);
    variablesMethod.addsym(paramName, varsym(var));
  }

  private Symbol findVarMethodClass(Ident name) {
    Symbol var = variablesMethod.getsym(name);
    if (var == null) {
      var = variablesClass.getsym(name);
    }
    return var;
  }

  private void defineMethodVariable(ClassMethodDeclaration method, VarDeclarator var) {

    Symbol maybeAlreadyDefined = findVarMethodClass(var.getIdentifier());
    if (maybeAlreadyDefined != null) {
      throw new EParseException(errorVarRedefinition(var, maybeAlreadyDefined.getVariable()));
    }

    variablesMethod.addsym(var.getIdentifier(), varsym(var));
  }

  private Symbol findVarBlockMethod(Ident name) {
    Symbol var = variablesBlock.getsym(name);
    if (var == null) {
      var = variablesMethod.getsym(name);
    }
    return var;
  }

  private void defineBlockVar(VarDeclarator var) {

    Symbol maybeAlreadyDefined = findVarBlockMethod(var.getIdentifier());
    if (maybeAlreadyDefined != null) {
      throw new EParseException(errorVarRedefinition(var, maybeAlreadyDefined.getVariable()));
    }

    variablesBlock.addsym(var.getIdentifier(), varsym(var));
  }

  private Symbol findBindingFromIdentifierToTypename(Ident name) {

    Symbol var = variablesBlock.getsym(name);
    if (var != null) {
      return var;
    }

    var = variablesMethod.getsym(name);
    if (var != null) {
      return var;
    }

    var = variablesClass.getsym(name);
    if (var != null) {
      return var;
    }

    var = typeNames.getsym(name);
    if (var != null) {
      return var;
    }

    else {
      throw new EParseException("type not found for id: " + name.getName());
    }

  }

  private void defineMethod(ClassDeclaration o, ClassMethodDeclaration m) {
  }

  private void defineConstructor(ClassDeclaration object, ClassConstructorDeclaration constructor) {
  }

  private void initVarZero(VarDeclarator var) {
  }

  private void defineClassField(ClassDeclaration object, VarDeclarator field) {
    variablesClass.addsym(field.getIdentifier(), varsym(field));
  }

  private String errorVarRedefinition(VarDeclarator varYouWantToDefine, VarDeclarator varDefinedPreviously) {

    final String name = varYouWantToDefine.getIdentifier().getName();
    final String location = varYouWantToDefine.getLocationToString();

    StringBuilder sb = new StringBuilder();
    sb.append("\nError: ");
    sb.append(location);
    sb.append("\n");
    sb.append("duplicate variable: ");
    sb.append(name);
    sb.append("\n");
    sb.append("previously defined here: ");
    sb.append(varDefinedPreviously.getLocationToString());
    sb.append("\n");

    return sb.toString();
  }

  //
  //////////////////////////////////////////////////////////////////////

  private void visit(ClassDeclaration object) {

    openClassScope(object.getIdentifier().getName());

    //fields
    for (VarDeclarator field : object.getFields()) {
      defineClassField(object, field); // check redefinition
    }

    //methods
    for (ClassMethodDeclaration method : object.getMethods()) {
      openMethodScope(method.getIdentifier().getName());

      defineMethod(object, method); // check overloading/redefinition/etc

      for (FormalParameter fp : method.getFormalParameterList().getParameters()) {
        Type type = fp.getType();
        Ident name = fp.getIdentifier();
        defineFunctionParameter(method, type, name);
      }

      //body
      final StmtBlock body = method.getBody();
      final List<StmtBlockItem> blocks = body.getBlockStatements();

      for (StmtBlockItem block : blocks) {

        // method variables
        final List<VarDeclarator> localVars = block.getLocalVars();
        if (localVars != null) {
          for (VarDeclarator var : localVars) {
            initVarZero(var);
            defineMethodVariable(method, var);
          }
        }

        applyStatement(object, block.getStatement());
      }

      closeMethodScope();
    }

    //constructors (the last, it works with methods and fields)
    for (ClassConstructorDeclaration constructor : object.getConstructors()) {
      defineConstructor(object, constructor); // check overloading/redefinition/etc
    }

    closeClassScope();
  }

  //////////////////////////////////////////////////////////////////////
  // STATEMENTS 
  //
  private void applyStatement(final ClassDeclaration object, final StmtStatement statement) {

    if (statement == null) {
      return;
    }

    StatementBase base = statement.getBase();
    boolean hasItsOwnScope = (base == StatementBase.SBLOCK) || (base == StatementBase.SFOR);

    if (hasItsOwnScope) {
      openBlockScope(base.toString());
    }

    if (base == StatementBase.SFOR) {
      StmtFor forloop = statement.getSfor();
      visitLocalVars(object, forloop.getDecl());
      applyExpression(object, forloop.getTest());
      applyExpression(object, forloop.getStep());
      applyStatement(object, forloop.getLoop());
    }

    else if (base == StatementBase.SIF) {
      Stmt_if sif = statement.getSif();
      applyExpression(object, sif.getIfexpr());
      applyStatement(object, sif.getIfstmt());
      applyStatement(object, sif.getIfelse());
    }

    else if (base == StatementBase.SEXPR) {
      applyExpression(object, statement.getSexpression());
    }

    else if (base == StatementBase.SBLOCK) {
      visitBlock(object, statement.getCompound());
    }

    else if (base == StatementBase.SRETURN) {
      applyExpression(object, statement.getSexpression());
    }

    else {
      throw new EParseException("unimpl. stmt.:" + base.toString());
    }

    if (hasItsOwnScope) {
      closeBlockScope();
    }

  }

  private void visitBlock(final ClassDeclaration object, final StmtBlock body) {
    for (StmtBlockItem block : body.getBlockStatements()) {
      visitLocalVars(object, block.getLocalVars());
      applyStatement(object, block.getStatement());
    }
  }

  private void visitLocalVars(final ClassDeclaration object, List<VarDeclarator> vars) {
    if (vars == null) {
      return;
    }

    for (VarDeclarator var : vars) {
      initVarZero(var);
      defineBlockVar(var);
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

  private void applyBinary(ClassDeclaration object, ExprExpression e) {
    ExprBinary node = e.getBinary();
    Token operator = node.getOperator();

    final ExprExpression LHS = node.getLhs();
    final ExprExpression RHS = node.getRhs();

    applyExpression(object, LHS);
    applyExpression(object, RHS);

    e.setResultType(node.getLhs().getResultType());
  }

  private void applyIdentifier(ExprExpression e) {
    ExprPrimaryIdent primaryIdent = e.getLiteralIdentifier();

    Symbol sym = findBindingFromIdentifierToTypename(primaryIdent.getIdentifier());

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

    ExprFieldAccess fieldAccess = e.getFieldAccess();
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

  }

  private void applyMethodInvocation(ClassDeclaration object, ExprExpression e) {

    ExprMethodInvocation methodInvocation = e.getMethodInvocation();
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

    } else {
      // function: fn(1,2,3)
      ClassMethodDeclaration func = object.getMethod(methodInvocation.getFuncname(), arguments);
      if (func == null) {
        throw new EParseException("class has no method: " + methodInvocation.getFuncname().getName());
      }
      e.setResultType(func.getType());
    }

  }

  //////////////////////////////////////////////////////////////////////
  // ENTRY
  //
  public void visit(InstantiationUnit o) {
    openFileScope();
    for (ClassDeclaration td : o.getClasses()) {
      visit(td);
    }
    closeFileScope();
  }

}
