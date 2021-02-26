package _st3_linearize_expr.ir;

import _st3_linearize_expr.items.AssignVarAllocObject;
import _st3_linearize_expr.items.AssignVarArrayAccess;
import _st3_linearize_expr.items.AssignVarBinop;
import _st3_linearize_expr.items.AssignVarFalse;
import _st3_linearize_expr.items.AssignVarFieldAccess;
import _st3_linearize_expr.items.AssignVarFlatCallClassCreationTmp;
import _st3_linearize_expr.items.AssignVarFlatCallResult;
import _st3_linearize_expr.items.AssignVarNull;
import _st3_linearize_expr.items.AssignVarNum;
import _st3_linearize_expr.items.AssignVarString;
import _st3_linearize_expr.items.AssignVarTernaryOp;
import _st3_linearize_expr.items.AssignVarTrue;
import _st3_linearize_expr.items.AssignVarUnop;
import _st3_linearize_expr.items.AssignVarVar;
import _st3_linearize_expr.items.FlatCallConstructor;
import _st3_linearize_expr.items.FlatCallVoid;
import _st3_linearize_expr.items.StoreArrayVar;
import _st3_linearize_expr.items.StoreArrayVarAssignOp;
import _st3_linearize_expr.items.StoreFieldVar;
import _st3_linearize_expr.items.StoreFieldVarAssignOp;
import _st3_linearize_expr.items.StoreVarField;
import _st3_linearize_expr.items.StoreVarFieldAssignOp;
import _st3_linearize_expr.items.StoreVarVar;
import _st3_linearize_expr.items.StoreVarVarAssignOp;
import _st3_linearize_expr.leaves.Var;
import errors.AstParseException;

public class FlatCodeItem {
  private final Opc opcode;

  //@formatter:off
  private AssignVarAllocObject assignVarAllocObject;
  private AssignVarArrayAccess assignVarArrayAccess;
  private AssignVarBinop assignVarBinop;
  private AssignVarFalse assignVarFalse;
  private AssignVarFieldAccess assignVarFieldAccess;
  private AssignVarFlatCallClassCreationTmp assignVarFlatCallClassCreationTmp;
  private AssignVarFlatCallResult assignVarFlatCallResult;
  private AssignVarNull assignVarNull;
  private AssignVarNum assignVarNum;
  private AssignVarString assignVarString;
  private AssignVarTernaryOp assignVarTernaryOp;
  private AssignVarTrue assignVarTrue;
  private AssignVarUnop assignVarUnop;
  private AssignVarVar assignVarVar;
  private FlatCallConstructor flatCallConstructor;
  private FlatCallVoid flatCallVoid;
  private StoreArrayVar storeArrayVar;
  private StoreArrayVarAssignOp storeArrayVarAssignOp;
  private StoreFieldVar storeFieldVar;
  private StoreFieldVarAssignOp storeFieldVarAssignOp;
  private StoreVarField storeVarField;
  private StoreVarFieldAssignOp storeVarFieldAssignOp;
  private StoreVarVar storeVarVar;
  private StoreVarVarAssignOp storeVarVarAssignOp;

  public FlatCodeItem(AssignVarAllocObject assignVarAllocObject) { this.opcode = Opc.AssignVarAllocObject; this.assignVarAllocObject = assignVarAllocObject; }
  public FlatCodeItem(AssignVarArrayAccess assignVarArrayAccess) { this.opcode = Opc.AssignVarArrayAccess; this.assignVarArrayAccess = assignVarArrayAccess; }
  public FlatCodeItem(AssignVarBinop assignVarBinop) { this.opcode = Opc.AssignVarBinop; this.assignVarBinop = assignVarBinop; }
  public FlatCodeItem(AssignVarFalse assignVarFalse) { this.opcode = Opc.AssignVarFalse; this.assignVarFalse = assignVarFalse; }
  public FlatCodeItem(AssignVarFieldAccess assignVarFieldAccess) { this.opcode = Opc.AssignVarFieldAccess; this.assignVarFieldAccess = assignVarFieldAccess; }
  public FlatCodeItem(AssignVarFlatCallClassCreationTmp assignVarFlatCallClassCreationTmp) { this.opcode = Opc.AssignVarFlatCallClassCreationTmp; this.assignVarFlatCallClassCreationTmp = assignVarFlatCallClassCreationTmp; }
  public FlatCodeItem(AssignVarFlatCallResult assignVarFlatCallResult) { this.opcode = Opc.AssignVarFlatCallResult; this.assignVarFlatCallResult = assignVarFlatCallResult; }
  public FlatCodeItem(AssignVarNull assignVarNull) { this.opcode = Opc.AssignVarNull; this.assignVarNull = assignVarNull; }
  public FlatCodeItem(AssignVarNum assignVarNum) { this.opcode = Opc.AssignVarNum; this.assignVarNum = assignVarNum; }
  public FlatCodeItem(AssignVarString assignVarString) { this.opcode = Opc.AssignVarString; this.assignVarString = assignVarString; }
  public FlatCodeItem(AssignVarTernaryOp assignVarTernaryOp) { this.opcode = Opc.AssignVarTernaryOp; this.assignVarTernaryOp = assignVarTernaryOp; }
  public FlatCodeItem(AssignVarTrue assignVarTrue) { this.opcode = Opc.AssignVarTrue; this.assignVarTrue = assignVarTrue; }
  public FlatCodeItem(AssignVarUnop assignVarUnop) { this.opcode = Opc.AssignVarUnop; this.assignVarUnop = assignVarUnop; }
  public FlatCodeItem(AssignVarVar assignVarVar) { this.opcode = Opc.AssignVarVar; this.assignVarVar = assignVarVar; }
  public FlatCodeItem(FlatCallConstructor flatCallConstructor) { this.opcode = Opc.FlatCallConstructor; this.flatCallConstructor = flatCallConstructor; }
  public FlatCodeItem(FlatCallVoid flatCallVoid) { this.opcode = Opc.FlatCallVoid; this.flatCallVoid = flatCallVoid; }
  public FlatCodeItem(StoreArrayVar storeArrayVar) { this.opcode = Opc.StoreArrayVar; this.storeArrayVar = storeArrayVar; }
  public FlatCodeItem(StoreArrayVarAssignOp storeArrayVarAssignOp) { this.opcode = Opc.StoreArrayVarAssignOp; this.storeArrayVarAssignOp = storeArrayVarAssignOp; }
  public FlatCodeItem(StoreFieldVar storeFieldVar) { this.opcode = Opc.StoreFieldVar; this.storeFieldVar = storeFieldVar; }
  public FlatCodeItem(StoreFieldVarAssignOp storeFieldVarAssignOp) { this.opcode = Opc.StoreFieldVarAssignOp; this.storeFieldVarAssignOp = storeFieldVarAssignOp; }
  public FlatCodeItem(StoreVarField storeVarField) { this.opcode = Opc.StoreVarField; this.storeVarField = storeVarField; }
  public FlatCodeItem(StoreVarFieldAssignOp storeVarFieldAssignOp) { this.opcode = Opc.StoreVarFieldAssignOp; this.storeVarFieldAssignOp = storeVarFieldAssignOp; }
  public FlatCodeItem(StoreVarVar storeVarVar) { this.opcode = Opc.StoreVarVar; this.storeVarVar = storeVarVar; }
  public FlatCodeItem(StoreVarVarAssignOp storeVarVarAssignOp) { this.opcode = Opc.StoreVarVarAssignOp; this.storeVarVarAssignOp = storeVarVarAssignOp; }

  public boolean isAssignVarAllocObject() { return this.opcode == Opc.AssignVarAllocObject; }
  public boolean isAssignVarArrayAccess() { return this.opcode == Opc.AssignVarArrayAccess; }
  public boolean isAssignVarBinop() { return this.opcode == Opc.AssignVarBinop; }
  public boolean isAssignVarFalse() { return this.opcode == Opc.AssignVarFalse; }
  public boolean isAssignVarFieldAccess() { return this.opcode == Opc.AssignVarFieldAccess; }
  public boolean isAssignVarFlatCallClassCreationTmp() { return this.opcode == Opc.AssignVarFlatCallClassCreationTmp; }
  public boolean isAssignVarFlatCallResult() { return this.opcode == Opc.AssignVarFlatCallResult; }
  public boolean isAssignVarNull() { return this.opcode == Opc.AssignVarNull; }
  public boolean isAssignVarNum() { return this.opcode == Opc.AssignVarNum; }
  public boolean isAssignVarString() { return this.opcode == Opc.AssignVarString; }
  public boolean isAssignVarTernaryOp() { return this.opcode == Opc.AssignVarTernaryOp; }
  public boolean isAssignVarTrue() { return this.opcode == Opc.AssignVarTrue; }
  public boolean isAssignVarUnop() { return this.opcode == Opc.AssignVarUnop; }
  public boolean isAssignVarVar() { return this.opcode == Opc.AssignVarVar; }
  public boolean isFlatCallConstructor() { return this.opcode == Opc.FlatCallConstructor; }
  public boolean isFlatCallVoid() { return this.opcode == Opc.FlatCallVoid; }
  public boolean isStoreArrayVar() { return this.opcode == Opc.StoreArrayVar; }
  public boolean isStoreArrayVarAssignOp() { return this.opcode == Opc.StoreArrayVarAssignOp; }
  public boolean isStoreFieldVar() { return this.opcode == Opc.StoreFieldVar; }
  public boolean isStoreFieldVarAssignOp() { return this.opcode == Opc.StoreFieldVarAssignOp; }
  public boolean isStoreVarField() { return this.opcode == Opc.StoreVarField; }
  public boolean isStoreVarFieldAssignOp() { return this.opcode == Opc.StoreVarFieldAssignOp; }
  public boolean isStoreVarVar() { return this.opcode == Opc.StoreVarVar; }
  public boolean isStoreVarVarAssignOp() { return this.opcode == Opc.StoreVarVarAssignOp; }

  @Override
  public String toString() {
    if(isAssignVarAllocObject()) { return assignVarAllocObject.toString(); }
    if(isAssignVarArrayAccess()) { return assignVarArrayAccess.toString(); }
    if(isAssignVarBinop()) { return assignVarBinop.toString(); }
    if(isAssignVarFalse()) { return assignVarFalse.toString(); }
    if(isAssignVarFieldAccess()) { return assignVarFieldAccess.toString(); }
    if(isAssignVarFlatCallClassCreationTmp()) { return assignVarFlatCallClassCreationTmp.toString(); }
    if(isAssignVarFlatCallResult()) { return assignVarFlatCallResult.toString(); }
    if(isAssignVarNull()) { return assignVarNull.toString(); }
    if(isAssignVarNum()) { return assignVarNum.toString(); }
    if(isAssignVarString()) { return assignVarString.toString(); }
    if(isAssignVarTernaryOp()) { return assignVarTernaryOp.toString(); }
    if(isAssignVarTrue()) { return assignVarTrue.toString(); }
    if(isAssignVarUnop()) { return assignVarUnop.toString(); }
    if(isAssignVarVar()) { return assignVarVar.toString(); }
    if(isFlatCallConstructor()) { return flatCallConstructor.toString(); }
    if(isFlatCallVoid()) { return flatCallVoid.toString(); }
    if(isStoreArrayVar()) { return storeArrayVar.toString(); }
    if(isStoreArrayVarAssignOp()) { return storeArrayVarAssignOp.toString(); }
    if(isStoreFieldVar()) { return storeFieldVar.toString(); }
    if(isStoreFieldVarAssignOp()) { return storeFieldVarAssignOp.toString(); }
    if(isStoreVarField()) { return storeVarField.toString(); }
    if(isStoreVarFieldAssignOp()) { return storeVarFieldAssignOp.toString(); }
    if(isStoreVarVar()) { return storeVarVar.toString(); }
    if(isStoreVarVarAssignOp()) { return storeVarVarAssignOp.toString(); }
    return "?UnknownItem"; 
  }

  public Opc getOpcode() { return this.opcode; }
  public AssignVarAllocObject getAssignVarAllocObject() { return this.assignVarAllocObject; }
  public AssignVarArrayAccess getAssignVarArrayAccess() { return this.assignVarArrayAccess; }
  public AssignVarBinop getAssignVarBinop() { return this.assignVarBinop; }
  public AssignVarFalse getAssignVarFalse() { return this.assignVarFalse; }
  public AssignVarFieldAccess getAssignVarFieldAccess() { return this.assignVarFieldAccess; }
  public AssignVarFlatCallClassCreationTmp getAssignVarFlatCallClassCreationTmp() { return this.assignVarFlatCallClassCreationTmp; }
  public AssignVarFlatCallResult getAssignVarFlatCallResult() { return this.assignVarFlatCallResult; }
  public AssignVarNull getAssignVarNull() { return this.assignVarNull; }
  public AssignVarNum getAssignVarNum() { return this.assignVarNum; }
  public AssignVarString getAssignVarString() { return this.assignVarString; }
  public AssignVarTernaryOp getAssignVarTernaryOp() { return this.assignVarTernaryOp; }
  public AssignVarTrue getAssignVarTrue() { return this.assignVarTrue; }
  public AssignVarUnop getAssignVarUnop() { return this.assignVarUnop; }
  public AssignVarVar getAssignVarVar() { return this.assignVarVar; }
  public FlatCallConstructor getFlatCallConstructor() { return this.flatCallConstructor; }
  public FlatCallVoid getFlatCallVoid() { return this.flatCallVoid; }
  public StoreArrayVar getStoreArrayVar() { return this.storeArrayVar; }
  public StoreArrayVarAssignOp getStoreArrayVarAssignOp() { return this.storeArrayVarAssignOp; }
  public StoreFieldVar getStoreFieldVar() { return this.storeFieldVar; }
  public StoreFieldVarAssignOp getStoreFieldVarAssignOp() { return this.storeFieldVarAssignOp; }
  public StoreVarField getStoreVarField() { return this.storeVarField; }
  public StoreVarFieldAssignOp getStoreVarFieldAssignOp() { return this.storeVarFieldAssignOp; }
  public StoreVarVar getStoreVarVar() { return this.storeVarVar; }
  public StoreVarVarAssignOp getStoreVarVarAssignOp() { return this.storeVarVarAssignOp; }

  public boolean isOneOfAssigns() {
    return 
       isAssignVarAllocObject()  
    || isAssignVarArrayAccess()  
    || isAssignVarBinop()  
    || isAssignVarFalse()  
    || isAssignVarFieldAccess()  
    || isAssignVarFlatCallClassCreationTmp()
    || isAssignVarFlatCallResult()
    || isAssignVarNull()   
    || isAssignVarNum()   
    || isAssignVarString()  
    || isAssignVarTernaryOp()  
    || isAssignVarTrue()   
    || isAssignVarUnop()   
    || isAssignVarVar(); 
  }
  
  public Var getDest() {
    if(isAssignVarAllocObject()) {
      return assignVarAllocObject.getLvalue();
    }
    if(isAssignVarArrayAccess()) {
      return assignVarArrayAccess.getLvalue();
    }
    if(isAssignVarBinop()) {
      return assignVarBinop.getLvalue();
    }
    if(isAssignVarFalse()) {
      return assignVarFalse.getLvalue();
    }
    if(isAssignVarFieldAccess()) {
      return assignVarFieldAccess.getLvalue();
    }
    if(isAssignVarFlatCallClassCreationTmp()) {
      return assignVarFlatCallClassCreationTmp.getLvalue();
    }
    if(isAssignVarFlatCallResult()) {
      return assignVarFlatCallResult.getLvalue();
    }
    if(isAssignVarNull()) {
      return assignVarNull.getLvalue();
    }
    if(isAssignVarNum()) {
      return assignVarNum.getLvalue();
    }
    if(isAssignVarString()) {
      return assignVarString.getLvalue();
    }
    if(isAssignVarTernaryOp()) {
      return assignVarTernaryOp.getLvalue();
    }
    if(isAssignVarTrue()) {
      return assignVarTrue.getLvalue();
    }
    if(isAssignVarUnop()) {
      return assignVarUnop.getLvalue();
    }
    if(isAssignVarVar()) {
      return assignVarVar.getLvalue();
    }
    if(isFlatCallConstructor()) {
      return flatCallConstructor.getThisVar();
    }
    if(isFlatCallVoid()) {
      err();
    }
    if(isStoreArrayVar()) {
      err();
    }
    if(isStoreArrayVarAssignOp()) {
      err();
    }
    if(isStoreFieldVar()) {
      err();
    }
    if(isStoreFieldVarAssignOp()) {
      err();
    }
    if(isStoreVarField()) {
      err();
    }
    if(isStoreVarFieldAssignOp()) {
      err();
    }
    if(isStoreVarVar()) {
      err();
    }
    if(isStoreVarVarAssignOp()) {
      return storeVarVarAssignOp.getDst();
    }
    throw new AstParseException("unknown item for result: " + toString());
  }
  private void err() { throw new AstParseException("unexpected item for result: " + toString()); }
  //@formatter:on

}
