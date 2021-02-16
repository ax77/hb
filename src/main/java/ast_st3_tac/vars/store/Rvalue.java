package ast_st3_tac.vars.store;

import ast_st3_tac.vars.arith.Binop;
import ast_st3_tac.vars.arith.Unop;
import utils_oth.NullChecker;

public class Rvalue {

  /// L | R
  /// ..|..
  /// e = f
  /// e = 1
  /// e = f(h)
  /// e = f[h]
  /// e = f.h
  /// e = f + h
  /// e = -f
  /// e = new classname(!NO:ARGS!)
  ///
  /// 3 lhs * 8 rhs = 24 combination of assign

  private Var var;
  private Value value;
  private Call call;
  private ArrayAccess arrayAccess;
  private FieldAccess fieldAccess;
  private Binop binop;
  private Unop unop;
  private AllocObject allocObject;

  //@formatter:off
  public Rvalue(Var e) { NullChecker.check(e); this.var = e; }
  public Rvalue(Value e) { NullChecker.check(e); this.value = e; }
  public Rvalue(Call e) { NullChecker.check(e); this.call = e; }
  public Rvalue(ArrayAccess e) { NullChecker.check(e); this.arrayAccess = e; }
  public Rvalue(FieldAccess e) { NullChecker.check(e); this.fieldAccess = e; }
  public Rvalue(Binop e) { NullChecker.check(e); this.binop = e; }
  public Rvalue(Unop e) { NullChecker.check(e); this.unop = e; }
  public Rvalue(AllocObject e) { NullChecker.check(e); this.allocObject = e; }
  
  public boolean isVar() { return var != null; }
  public boolean isValue() { return value != null; }
  public boolean isCall() { return call != null; }
  public boolean isArrayAccess() { return arrayAccess != null; }
  public boolean isFieldAccess() { return fieldAccess != null; }
  public boolean isBinop() { return binop != null; }
  public boolean isUnop() { return unop != null; }
  public boolean isAllocObject() { return allocObject != null; }
  
  @Override
  public String toString() {
    
    if (isVar()) { return var.toString(); }
    if (isValue()) { return value.toString(); }
    if (isCall()) { return call.toString(); }
    if (isArrayAccess()) { return arrayAccess.toString(); }
    if (isFieldAccess()) { return fieldAccess.toString(); }
    if (isBinop()) { return binop.toString(); }
    if (isUnop()) { return unop.toString(); }
    if (isAllocObject()) { return allocObject.toString(); }
    
    return "???Rvalue";
  }
  
  //@formatter:on

  public Var getVar() {
    return var;
  }

  public Value getValue() {
    return value;
  }

  public Call getCall() {
    return call;
  }

  public ArrayAccess getArrayAccess() {
    return arrayAccess;
  }

  public FieldAccess getFieldAccess() {
    return fieldAccess;
  }

  public Binop getBinop() {
    return binop;
  }

  public Unop getUnop() {
    return unop;
  }

  public AllocObject getAllocObject() {
    return allocObject;
  }

}
