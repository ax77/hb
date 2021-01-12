package njast.ast_visitors;

import jscan.symtab.Ident;
import njast.ModTypeNameHeader;
import njast.ast_nodes.clazz.ClassConstructorDeclaration;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.clazz.vars.VarBase;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.errors.EParseException;
import njast.symtab.ScopeLevels;
import njast.symtab.Symtab;

public class SymtabApplier {

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

  // here we build the symbol table, bind all identifiers to its types
  // also we annotate each expression with its type too
  // deep semantic check will be later.
  // we need to build middle-level AST here...

  private Symtab<Ident, Symbol> typeNames;
  private Symtab<Ident, Symbol> variablesClass; // fields
  private Symtab<Ident, Symbol> variablesMethod; // parameters+locals_outside_block
  private Symtab<Ident, Symbol> variablesBlock; // locals_inside_block

  public SymtabApplier() {
    this.typeNames = new Symtab<>();
    this.variablesClass = new Symtab<>();
    this.variablesMethod = new Symtab<>();
    this.variablesBlock = new Symtab<>();
  }

  public void openFileScope() {
    this.typeNames.pushscope(ScopeLevels.FILE_SCOPE, "compilation_unit_scope");
  }

  public void closeFileScope() {
    this.typeNames.popscope();
  }

  public void openClassScope(String className) {
    this.variablesClass.pushscope(ScopeLevels.CLASS_SCOPE, className);
  }

  public void closeClassScope() {
    this.variablesClass.popscope();
  }

  public void openMethodScope(String methodName) {
    this.variablesMethod.pushscope(ScopeLevels.METHOD_SCOPE, methodName);
  }

  public void closeMethodScope() {
    this.variablesMethod.popscope();
  }

  public void openBlockScope(String name) {
    this.variablesBlock.pushscope(ScopeLevels.BLOCK_SCOPE, name);
  }

  public void closeBlockScope() {
    this.variablesBlock.popscope();
  }

  public void dump() {
    typeNames.dump();
    variablesClass.dump();
    variablesMethod.dump();
    variablesBlock.dump();
  }

  public Symbol varsym(VarDeclarator var) {
    return new Symbol(var);
  }

  // SYMTAB

  public void defineFunctionParameter(ClassMethodDeclaration method, ModTypeNameHeader header) {
    VarDeclarator var = new VarDeclarator(VarBase.METHOD_PARAMETER, header);
    variablesMethod.addsym(header.getIdentifier(), varsym(var));
  }

  public Symbol findVarMethodClass(Ident name) {
    Symbol var = variablesMethod.getsym(name);
    if (var == null) {
      var = variablesClass.getsym(name);
    }
    return var;
  }

  public void defineMethodVariable(ClassMethodDeclaration method, VarDeclarator var) {

    Symbol maybeAlreadyDefined = findVarMethodClass(var.getIdentifier());
    if (maybeAlreadyDefined != null) {
      throw new EParseException(errorVarRedefinition(var, maybeAlreadyDefined.getVariable()));
    }

    variablesMethod.addsym(var.getIdentifier(), varsym(var));
  }

  public Symbol findVarBlockMethod(Ident name) {
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

  public void defineMethod(ClassDeclaration o, ClassMethodDeclaration m) {
  }

  public void defineConstructor(ClassDeclaration object, ClassConstructorDeclaration constructor) {
  }

  public void initVarZero(VarDeclarator var) {
  }

  public void defineClassField(ClassDeclaration object, VarDeclarator field) {
    variablesClass.addsym(field.getIdentifier(), varsym(field));
  }

  public String errorVarRedefinition(VarDeclarator varYouWantToDefine, VarDeclarator varDefinedPreviously) {

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
}
