package njast.ast_visitors;

import java.util.List;

import jscan.symtab.Ident;
import njast.ast_nodes.clazz.ClassConstructorDeclaration;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.clazz.methods.FormalParameter;
import njast.ast_nodes.clazz.vars.VarBase;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.stmt.StmtBlock;
import njast.ast_nodes.stmt.StmtBlockItem;
import njast.ast_nodes.stmt.StmtStatement;
import njast.ast_nodes.top.TopLevelCompilationUnit;
import njast.ast_nodes.top.TopLevelTypeDeclaration;
import njast.errors.EParseException;
import njast.symtab.ScopeLevels;
import njast.symtab.Symtab;
import njast.types.Type;

public class ApplyCompilationUnit {

  private Symtab<Ident, Symbol> typeNames;
  private Symtab<Ident, Symbol> variablesClass; // fields
  private Symtab<Ident, Symbol> variablesMethod; // parameters+locals_outside_block
  private Symtab<Ident, Symbol> variablesBlock; // locals_inside_block

  public ApplyCompilationUnit() {
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

  public void openBlockScope(String name) {
    this.variablesBlock.pushscope(ScopeLevels.BLOCK_SCOPE, name);
  }

  public void closeBlockScope() {
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

  public void defineBlockVar(VarDeclarator var) {

    Symbol maybeAlreadyDefined = findVarBlockMethod(var.getIdentifier());
    if (maybeAlreadyDefined != null) {
      throw new EParseException(errorVarRedefinition(var, maybeAlreadyDefined.getVariable()));
    }

    variablesBlock.addsym(var.getIdentifier(), varsym(var));
  }

  public Symbol findBindingFromIdentifierToTypename(Ident name) {

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

  public void initVarZero(VarDeclarator var) {
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

  public void visit(ClassDeclaration object) {

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
        Ident name = fp.getName();
        defineFunctionParameter(method, type, name);
      }

      //body
      final StmtBlock body = method.getBody();
      final List<StmtBlockItem> blocks = body.getBlockStatements();

      for (StmtBlockItem block : blocks) {

        // declarations
        final List<VarDeclarator> localVars = block.getLocalVars();
        if (localVars != null) {
          for (VarDeclarator var : localVars) {
            initVarZero(var);
            defineMethodVariable(method, var);
          }
        }

        // statements
        final StmtStatement statement = block.getStatement();
        if (statement != null) {
          boolean result = new ApplyStmt(this).applyStatement(object, statement);
          if (!result) {
            System.out.println("...??? stmt");
          }
        }

      }

      closeMethodScope();
    }

    //constructors (the last, it works with methods and fields)
    for (ClassConstructorDeclaration constructor : object.getConstructors()) {
      defineConstructor(object, constructor); // check overloading/redefinition/etc
    }

    closeClassScope();
  }

  public void visit(TopLevelCompilationUnit o) {
    openFileScope();
    for (TopLevelTypeDeclaration td : o.getTypeDeclarations()) {
      visit(td.getClassDeclaration());
    }
    closeFileScope();
  }

}