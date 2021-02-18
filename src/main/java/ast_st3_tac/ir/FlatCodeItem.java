package ast_st3_tac.ir;

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
  private AssignVarTrue assignVarTrue;
  private AssignVarUnop assignVarUnop;
  private AssignVarVar assignVarVar;
  private FlatCallClassCreationTmp flatCallClassCreationTmp;
  private FlatCallConstructor flatCallConstructor;
  private FlatCallResult flatCallResult;
  private FlatCallVoid flatCallVoid;
  private StoreArrayAssignOpCall storeArrayAssignOpCall;
  private StoreArrayVar storeArrayVar;
  private StoreFieldAssignOpCall storeFieldAssignOpCall;
  private StoreFieldVar storeFieldVar;
  private StoreVarAssignOpCall storeVarAssignOpCall;
  private StoreVarVar storeVarVar;

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
  public FlatCodeItem(AssignVarTrue assignVarTrue) { this.opcode = Opc.AssignVarTrue; this.assignVarTrue = assignVarTrue; }
  public FlatCodeItem(AssignVarUnop assignVarUnop) { this.opcode = Opc.AssignVarUnop; this.assignVarUnop = assignVarUnop; }
  public FlatCodeItem(AssignVarVar assignVarVar) { this.opcode = Opc.AssignVarVar; this.assignVarVar = assignVarVar; }
  public FlatCodeItem(FlatCallClassCreationTmp flatCallClassCreationTmp) { this.opcode = Opc.FlatCallClassCreationTmp; this.flatCallClassCreationTmp = flatCallClassCreationTmp; }
  public FlatCodeItem(FlatCallConstructor flatCallConstructor) { this.opcode = Opc.FlatCallConstructor; this.flatCallConstructor = flatCallConstructor; }
  public FlatCodeItem(FlatCallResult flatCallResult) { this.opcode = Opc.FlatCallResult; this.flatCallResult = flatCallResult; }
  public FlatCodeItem(FlatCallVoid flatCallVoid) { this.opcode = Opc.FlatCallVoid; this.flatCallVoid = flatCallVoid; }
  public FlatCodeItem(StoreArrayAssignOpCall storeArrayAssignOpCall) { this.opcode = Opc.StoreArrayAssignOpCall; this.storeArrayAssignOpCall = storeArrayAssignOpCall; }
  public FlatCodeItem(StoreArrayVar storeArrayVar) { this.opcode = Opc.StoreArrayVar; this.storeArrayVar = storeArrayVar; }
  public FlatCodeItem(StoreFieldAssignOpCall storeFieldAssignOpCall) { this.opcode = Opc.StoreFieldAssignOpCall; this.storeFieldAssignOpCall = storeFieldAssignOpCall; }
  public FlatCodeItem(StoreFieldVar storeFieldVar) { this.opcode = Opc.StoreFieldVar; this.storeFieldVar = storeFieldVar; }
  public FlatCodeItem(StoreVarAssignOpCall storeVarAssignOpCall) { this.opcode = Opc.StoreVarAssignOpCall; this.storeVarAssignOpCall = storeVarAssignOpCall; }
  public FlatCodeItem(StoreVarVar storeVarVar) { this.opcode = Opc.StoreVarVar; this.storeVarVar = storeVarVar; }

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
  public boolean isAssignVarTrue() { return this.opcode == Opc.AssignVarTrue; }
  public boolean isAssignVarUnop() { return this.opcode == Opc.AssignVarUnop; }
  public boolean isAssignVarVar() { return this.opcode == Opc.AssignVarVar; }
  public boolean isFlatCallClassCreationTmp() { return this.opcode == Opc.FlatCallClassCreationTmp; }
  public boolean isFlatCallConstructor() { return this.opcode == Opc.FlatCallConstructor; }
  public boolean isFlatCallResult() { return this.opcode == Opc.FlatCallResult; }
  public boolean isFlatCallVoid() { return this.opcode == Opc.FlatCallVoid; }
  public boolean isStoreArrayAssignOpCall() { return this.opcode == Opc.StoreArrayAssignOpCall; }
  public boolean isStoreArrayVar() { return this.opcode == Opc.StoreArrayVar; }
  public boolean isStoreFieldAssignOpCall() { return this.opcode == Opc.StoreFieldAssignOpCall; }
  public boolean isStoreFieldVar() { return this.opcode == Opc.StoreFieldVar; }
  public boolean isStoreVarAssignOpCall() { return this.opcode == Opc.StoreVarAssignOpCall; }
  public boolean isStoreVarVar() { return this.opcode == Opc.StoreVarVar; }
  
  @Override
  public String toString() {
    if(isAssignVarAllocObject()) { return assignVarAllocObject.toString(); }
    else if(isAssignVarArrayAccess()) { return assignVarArrayAccess.toString(); }
    else if(isAssignVarBinop()) { return assignVarBinop.toString(); }
    else if(isAssignVarFalse()) { return assignVarFalse.toString(); }
    else if(isAssignVarFieldAccess()) { return assignVarFieldAccess.toString(); }
    else if(isAssignVarFlatCallClassCreationTmp()) { return assignVarFlatCallClassCreationTmp.toString(); }
    else if(isAssignVarFlatCallResult()) { return assignVarFlatCallResult.toString(); }
    else if(isAssignVarNull()) { return assignVarNull.toString(); }
    else if(isAssignVarNum()) { return assignVarNum.toString(); }
    else if(isAssignVarString()) { return assignVarString.toString(); }
    else if(isAssignVarTrue()) { return assignVarTrue.toString(); }
    else if(isAssignVarUnop()) { return assignVarUnop.toString(); }
    else if(isAssignVarVar()) { return assignVarVar.toString(); }
    else if(isFlatCallClassCreationTmp()) { return flatCallClassCreationTmp.toString(); }
    else if(isFlatCallConstructor()) { return flatCallConstructor.toString(); }
    else if(isFlatCallResult()) { return flatCallResult.toString(); }
    else if(isFlatCallVoid()) { return flatCallVoid.toString(); }
    else if(isStoreArrayAssignOpCall()) { return storeArrayAssignOpCall.toString(); }
    else if(isStoreArrayVar()) { return storeArrayVar.toString(); }
    else if(isStoreFieldAssignOpCall()) { return storeFieldAssignOpCall.toString(); }
    else if(isStoreFieldVar()) { return storeFieldVar.toString(); }
    else if(isStoreVarAssignOpCall()) { return storeVarAssignOpCall.toString(); }
    else if(isStoreVarVar()) { return storeVarVar.toString(); }
    return "???Item";
  }
  //@formatter:on

  public Opc getOpcode() {
    return opcode;
  }

  public AssignVarAllocObject getAssignVarAllocObject() {
    return assignVarAllocObject;
  }

  public AssignVarArrayAccess getAssignVarArrayAccess() {
    return assignVarArrayAccess;
  }

  public AssignVarBinop getAssignVarBinop() {
    return assignVarBinop;
  }

  public AssignVarFalse getAssignVarFalse() {
    return assignVarFalse;
  }

  public AssignVarFieldAccess getAssignVarFieldAccess() {
    return assignVarFieldAccess;
  }

  public AssignVarFlatCallClassCreationTmp getAssignVarFlatCallClassCreationTmp() {
    return assignVarFlatCallClassCreationTmp;
  }

  public AssignVarFlatCallResult getAssignVarFlatCallResult() {
    return assignVarFlatCallResult;
  }

  public AssignVarNull getAssignVarNull() {
    return assignVarNull;
  }

  public AssignVarNum getAssignVarNum() {
    return assignVarNum;
  }

  public AssignVarString getAssignVarString() {
    return assignVarString;
  }

  public AssignVarTrue getAssignVarTrue() {
    return assignVarTrue;
  }

  public AssignVarUnop getAssignVarUnop() {
    return assignVarUnop;
  }

  public AssignVarVar getAssignVarVar() {
    return assignVarVar;
  }

  public FlatCallClassCreationTmp getFlatCallClassCreationTmp() {
    return flatCallClassCreationTmp;
  }

  public FlatCallConstructor getFlatCallConstructor() {
    return flatCallConstructor;
  }

  public FlatCallResult getFlatCallResult() {
    return flatCallResult;
  }

  public FlatCallVoid getFlatCallVoid() {
    return flatCallVoid;
  }

  public StoreArrayAssignOpCall getStoreArrayAssignOpCall() {
    return storeArrayAssignOpCall;
  }

  public StoreArrayVar getStoreArrayVar() {
    return storeArrayVar;
  }

  public StoreFieldAssignOpCall getStoreFieldAssignOpCall() {
    return storeFieldAssignOpCall;
  }

  public StoreFieldVar getStoreFieldVar() {
    return storeFieldVar;
  }

  public StoreVarAssignOpCall getStoreVarAssignOpCall() {
    return storeVarAssignOpCall;
  }

  public StoreVarVar getStoreVarVar() {
    return storeVarVar;
  }

}
