package ast_st3_tac.vars.store;

import ast_st3_tac.vars.arith.Binop;
import ast_st3_tac.vars.arith.Unop;
import literals.IntLiteral;
import utils_oth.NullChecker;

public class ERvalue {

  ///     L | R
  /// 1 ) e = f
  /// 2 ) e = 1
  /// 3 ) e = "1"
  /// 4 ) e = f(h)
  /// 5 ) e = f[h]
  /// 6 ) e = f.h
  /// 7 ) e = f + h
  /// 8 ) e = -f
  /// 9 ) e = new classname(!NO:ARGS!)
  ///
  /// 3 lhs * 9 rhs = 27 combination of assign

  private Var var;
  private IntLiteral num;
  private String str;
  private Call call;
  private ArrayAccess arrayAccess;
  private FieldAccess fieldAccess;
  private Binop binop;
  private Unop unop;
  private AllocObject allocObject;

  //@formatter:off
  public ERvalue(Var e) { NullChecker.check(e); this.var = e; }
  public ERvalue(IntLiteral e) { NullChecker.check(e); this.num = e; }
  public ERvalue(String e) { NullChecker.check(e); this.str = e; }
  public ERvalue(Call e) { NullChecker.check(e); this.call = e; }
  public ERvalue(ArrayAccess e) { NullChecker.check(e); this.arrayAccess = e; }
  public ERvalue(FieldAccess e) { NullChecker.check(e); this.fieldAccess = e; }
  public ERvalue(Binop e) { NullChecker.check(e); this.binop = e; }
  public ERvalue(Unop e) { NullChecker.check(e); this.unop = e; }
  public ERvalue(AllocObject e) { NullChecker.check(e); this.allocObject = e; }
  
  public boolean isVar() { return var != null; }
  public boolean isNum() { return num != null; }
  public boolean isStr() { return str != null; }
  public boolean isCall() { return call != null; }
  public boolean isArrayAccess() { return arrayAccess != null; }
  public boolean isFieldAccess() { return fieldAccess != null; }
  public boolean isBinop() { return binop != null; }
  public boolean isUnop() { return unop != null; }
  public boolean isAllocObject() { return allocObject != null; }
  
  @Override
  public String toString() {
    
    if (isVar()) { return var.toString(); }
    if (isNum()) { return num.toString(); }
    if (isStr()) { return str.toString(); }
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

  public IntLiteral getNum() {
    return num;
  }

  public String getStr() {
    return str;
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
