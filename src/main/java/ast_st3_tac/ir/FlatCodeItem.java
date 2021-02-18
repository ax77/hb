package ast_st3_tac.ir;

public class FlatCodeItem {
  private final Opc opcode;

  //@formatter:off
  private AssignVarAllocObject    assignVarAllocObject;
  private AssignVarBinop          assignVarBinop;
  private AssignVarFieldAccess    assignVarFieldAccess;
  private AssignVarFlatCallResult assignVarFlatCallResult;
  private AssignVarNull           assignVarNull;
  private AssignVarNum            assignVarNum;
  private AssignVarUnop           assignVarUnop;
  private AssignVarVar            assignVarVar;
  //
  private FlatCallConstructor     flatCallConstructor;
  private FlatCallResult          flatCallResult;
  private FlatCallVoid            flatCallVoid;
  //
  private StoreArrayAssignOpCall  storeArrayAssignOpCall;
  private StoreArrayVar           storeArrayVar;
  //
  private StoreFieldAssignOpCall  storeFieldAssignOpCall;
  private StoreFieldVar           storeFieldVar;
  //
  private StoreVarAssignOpCall    storeVarAssignOpCall;
  private StoreVarVar             storeVarVar;

  public FlatCodeItem(AssignVarBinop assignVarBinop)                   { this.opcode = Opc.ASSIGN_VAR_BINOP;           this.assignVarBinop = assignVarBinop; }
  public FlatCodeItem(AssignVarFieldAccess assignVarFieldAccess)       { this.opcode = Opc.ASSIGN_VAR_FIELD_ACCESS;    this.assignVarFieldAccess = assignVarFieldAccess; }
  public FlatCodeItem(AssignVarFlatCallResult assignVarFlatCallResult) { this.opcode = Opc.ASSIGN_VAR_FLATCALL_RESULT; this.assignVarFlatCallResult = assignVarFlatCallResult; }
  public FlatCodeItem(AssignVarNull assignVarNull)                     { this.opcode = Opc.ASSIGN_VAR_NULL;            this.assignVarNull = assignVarNull; }
  public FlatCodeItem(AssignVarNum assignVarNum)                       { this.opcode = Opc.ASSIGN_VAR_NUM;             this.assignVarNum = assignVarNum; }
  public FlatCodeItem(AssignVarUnop assignVarUnop)                     { this.opcode = Opc.ASSIGN_VAR_UNOP;            this.assignVarUnop = assignVarUnop; }
  public FlatCodeItem(AssignVarVar assignVarVar)                       { this.opcode = Opc.ASSIGN_VAR_VAR;             this.assignVarVar = assignVarVar; }
  public FlatCodeItem(FlatCallConstructor flatCallConstructor)         { this.opcode = Opc.FLATCALL_CONSTRUCTOR;       this.flatCallConstructor = flatCallConstructor; }
  public FlatCodeItem(FlatCallResult flatCallResult)                   { this.opcode = Opc.FLATCALL_RESULT;            this.flatCallResult = flatCallResult; }
  public FlatCodeItem(FlatCallVoid flatCallVoid)                       { this.opcode = Opc.FLATCALL_VOID;              this.flatCallVoid = flatCallVoid; }
  public FlatCodeItem(StoreFieldAssignOpCall storeFieldAssignOpCall)   { this.opcode = Opc.STORE_FIELD_ASSIGN_OP_CALL; this.storeFieldAssignOpCall = storeFieldAssignOpCall; }
  public FlatCodeItem(StoreFieldVar storeFieldVar)                     { this.opcode = Opc.STORE_FIELD_VAR;            this.storeFieldVar = storeFieldVar; }
  public FlatCodeItem(StoreVarAssignOpCall storeVarAssignOpCall)       { this.opcode = Opc.STORE_VAR_ASSIGN_OP_CALL;   this.storeVarAssignOpCall = storeVarAssignOpCall; }
  public FlatCodeItem(StoreVarVar storeVarVar)                         { this.opcode = Opc.STORE_VAR_VAR;              this.storeVarVar = storeVarVar; }
  
  public boolean isAssignVarBinop()          { return this.opcode == Opc.ASSIGN_VAR_BINOP;           }
  public boolean isAssignVarFieldAccess()    { return this.opcode == Opc.ASSIGN_VAR_FIELD_ACCESS;    }
  public boolean isAssignVarFlatCallResult() { return this.opcode == Opc.ASSIGN_VAR_FLATCALL_RESULT; }
  public boolean isAssignVarNull()           { return this.opcode == Opc.ASSIGN_VAR_NULL;            }
  public boolean isAssignVarNum()            { return this.opcode == Opc.ASSIGN_VAR_NUM;             }
  public boolean isAssignVarUnop()           { return this.opcode == Opc.ASSIGN_VAR_UNOP;            }
  public boolean isAssignVarVar()            { return this.opcode == Opc.ASSIGN_VAR_VAR;             }
  public boolean isFlatCallConstructor()     { return this.opcode == Opc.FLATCALL_CONSTRUCTOR;       }
  public boolean isFlatCallResult()          { return this.opcode == Opc.FLATCALL_RESULT;            }
  public boolean isFlatCallVoid()            { return this.opcode == Opc.FLATCALL_VOID;              }
  public boolean isStoreFieldAssignOpCall()  { return this.opcode == Opc.STORE_FIELD_ASSIGN_OP_CALL; }
  public boolean isStoreFieldVar()           { return this.opcode == Opc.STORE_FIELD_VAR;            }
  public boolean isStoreVarAssignOpCall()    { return this.opcode == Opc.STORE_VAR_ASSIGN_OP_CALL;   }
  public boolean isStoreVarVar()             { return this.opcode == Opc.STORE_VAR_VAR;              }
  
  @Override
  public String toString() {
    if(isAssignVarBinop()) { return assignVarBinop.toString(); }
    if(isAssignVarFieldAccess()) { return assignVarFieldAccess.toString(); }
    if(isAssignVarFlatCallResult()) { return assignVarFlatCallResult.toString(); }
    if(isAssignVarNull()) { return assignVarNull.toString(); }
    if(isAssignVarNum()) { return assignVarNum.toString(); }
    if(isAssignVarUnop()) { return assignVarUnop.toString(); }
    if(isAssignVarVar()) { return assignVarVar.toString(); }
    if(isFlatCallConstructor()) { return flatCallConstructor.toString(); }
    if(isFlatCallResult()) { return flatCallResult.toString(); }
    if(isFlatCallVoid()) { return flatCallVoid.toString(); }
    if(isStoreFieldAssignOpCall()) { return storeFieldAssignOpCall.toString(); }
    if(isStoreFieldVar()) { return storeFieldVar.toString(); }
    if(isStoreVarAssignOpCall()) { return storeVarAssignOpCall.toString(); }
    if(isStoreVarVar()) { return storeVarVar.toString(); }
    return "???Item";
  }
  
  //@formatter:on

  public Opc getOpcode() {
    return opcode;
  }

  public AssignVarBinop getAssignVarBinop() {
    return assignVarBinop;
  }

  public AssignVarFieldAccess getAssignVarFieldAccess() {
    return assignVarFieldAccess;
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

  public AssignVarUnop getAssignVarUnop() {
    return assignVarUnop;
  }

  public AssignVarVar getAssignVarVar() {
    return assignVarVar;
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
