package _st2_annotate;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_symtab.ScopeLevels;
import ast_symtab.Symtab;
import ast_vars.VarDeclarator;
import errors.ErrorLocation;
import tokenize.Ident;

public class SymbolTable {

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

  private int blocksLevel;

  public SymbolTable() {
    this.typeNames = new Symtab<>();
    this.variablesClass = new Symtab<>();
    this.variablesMethod = new Symtab<>();
    this.variablesBlock = new Symtab<>();
    this.blocksLevel = 0;
  }

  /// SCOPES

  public void openFileScope() {
    this.typeNames.pushscope(ScopeLevels.FILE_SCOPE);
  }

  public void closeFileScope() {
    this.typeNames.popscope();
  }

  public void openClassScope() {
    this.variablesClass.pushscope(ScopeLevels.CLASS_SCOPE);
  }

  public void closeClassScope() {
    this.variablesClass.popscope();
  }

  public void openMethodScope(ClassMethodDeclaration method) {
    this.variablesMethod.pushscope(ScopeLevels.METHOD_SCOPE);
    blocksLevel += 1;
  }

  public void closeMethodScope() {
    this.variablesMethod.popscope();
    blocksLevel -= 1;
  }

  public void openBlockScope() {
    this.variablesBlock.pushscope(ScopeLevels.BLOCK_SCOPE);
    blocksLevel += 1;
  }

  public void closeBlockScope() {
    this.variablesBlock.popscope();
    blocksLevel -= 1;
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

  /// SYMTAB

  public void defineFunctionParameter(ClassMethodDeclaration method, VarDeclarator var) {
    Symbol maybeAlreadyDefined = findVar(var.getIdentifier(), F_METHOD);
    checkRedefinition(var, maybeAlreadyDefined);
    variablesMethod.addsym(var.getIdentifier(), varsym(var));
  }

  public void defineMethodVariable(ClassMethodDeclaration method, VarDeclarator var) {
    /// method variables may redefine class-fields
    /// it is not an error

    Symbol maybeAlreadyDefined = findVar(var.getIdentifier(), F_METHOD);
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

  public void defineClazz(ClassDeclaration td) {
    typeNames.addsym(td.getIdentifier(), new Symbol(td));
  }

  /// BLOCKS stack

  public int howMuchBlocks() {
    return blocksLevel;
  }

}
