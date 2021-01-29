package ast_st2_annotate;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_method.MethodParameter;
import ast_modifiers.Modifiers;
import ast_symtab.ScopeLevels;
import ast_symtab.Symtab;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import errors.ErrorLocation;
import tokenize.Ident;

public class SymbolTable {

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

  // search masks
  //@formatter:off
  public static final int F_FILE    = 0x0001;
  public static final int F_CLASS   = 0x0002;
  public static final int F_METHOD  = 0x0004; // include parameters
  public static final int F_BLOCK   = 0x0008;
  public static final int F_ALL     = F_FILE|F_CLASS|F_METHOD|F_BLOCK;
  //@formatter:on

  private Symtab<Ident, Symbol> typeNames;
  private Symtab<Ident, Symbol> variablesClass; // fields
  private Symtab<Ident, Symbol> variablesMethod; // parameters+locals_outside_block
  private Symtab<Ident, Symbol> variablesBlock; // locals_inside_block

  public SymbolTable() {
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

  public void defineFunctionParameter(ClassMethodDeclaration method, MethodParameter param) {
    final Modifiers mod = Mods.letModifiers();
    VarDeclarator var = new VarDeclarator(VarBase.METHOD_PARAMETER, mod, param.getType(), param.getName(),
        method.getBeginPos());

    Symbol maybeAlreadyDefined = findVar(var.getIdentifier(), F_METHOD);
    checkRedefinition(var, maybeAlreadyDefined);
    variablesMethod.addsym(param.getName(), varsym(var));
  }

  public void defineMethodVariable(ClassMethodDeclaration method, VarDeclarator var) {
    Symbol maybeAlreadyDefined = findVar(var.getIdentifier(), F_METHOD); // | F_CLASS ???
    checkRedefinition(var, maybeAlreadyDefined);
    variablesMethod.addsym(var.getIdentifier(), varsym(var));
  }

  public void defineBlockVar(VarDeclarator var) {
    Symbol maybeAlreadyDefined = findVar(var.getIdentifier(), F_BLOCK | F_METHOD);
    checkRedefinition(var, maybeAlreadyDefined);
    variablesBlock.addsym(var.getIdentifier(), varsym(var));
  }

  private boolean hasBit(int option, int mask) {
    return (option & mask) != 0;
  }

  public Symbol findVar(Ident name, int option) {

    Symbol var = null;

    if (hasBit(option, F_BLOCK)) {
      var = variablesBlock.getsym(name);
    }

    if (var == null && hasBit(option, F_METHOD)) {
      var = variablesMethod.getsym(name);
    }

    if (var == null && hasBit(option, F_CLASS)) {
      var = variablesClass.getsym(name);
    }

    if (var == null && hasBit(option, F_FILE)) {
      var = typeNames.getsym(name);
    }

    return var;
  }

  public void defineClassField(ClassDeclaration object, VarDeclarator field) {
    variablesClass.addsym(field.getIdentifier(), varsym(field));
  }

  private void checkRedefinition(VarDeclarator var, Symbol maybeAlreadyDefined) {
    if (maybeAlreadyDefined != null) {
      ErrorLocation.errorVarRedefinition(var, maybeAlreadyDefined.getVariable());
    }
  }

}
