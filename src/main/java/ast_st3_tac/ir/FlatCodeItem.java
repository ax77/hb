package ast_st3_tac.ir;

public class FlatCodeItem {
  private final Opc opcode;

  //@formatter:off
  private AssignVarAllocObject     assignVarAllocObject;
  private AssignVarBinop           assignVarBinop;
  private AssignVarFieldAccess     assignVarFieldAccess;
  private AssignVarFlatCallResult  assignVarFlatCallResult;
  private AssignVarNull            assignVarNull;
  private AssignVarNum             assignVarNum;
  private AssignVarUnop            assignVarUnop;
  private AssignVarVar             assignVarVar;
  //                               
  private FlatCallConstructor      flatCallConstructor;
  private FlatCallResult           flatCallResult;
  private FlatCallVoid             flatCallVoid;
  //                               
  private StoreArrayAssignOpCall   storeArrayAssignOpCall;
  private StoreArrayVar            storeArrayVar;
  //                               
  private StoreFieldAssignOpCall   storeFieldAssignOpCall;
  private StoreFieldVar            storeFieldVar;
  //                               
  private StoreVarAssignOpCall     storeVarAssignOpCall;
  private StoreVarVar              storeVarVar;
  //
  private AssignVarFlatCallClassCreationTmp assignVarFlatCallClassCreationTmp;
  private FlatCallClassCreationTmp flatCallClassCreationTmp;

  public FlatCodeItem(AssignVarBinop assignVarBinop)                      { this.opcode = Opc.AssignVarBinop;               this.assignVarBinop = assignVarBinop; }
  public FlatCodeItem(AssignVarFieldAccess assignVarFieldAccess)          { this.opcode = Opc.AssignVarFieldAccess;         this.assignVarFieldAccess = assignVarFieldAccess; }
  public FlatCodeItem(AssignVarFlatCallResult assignVarFlatCallResult)    { this.opcode = Opc.AssignVarFlatCallResult;      this.assignVarFlatCallResult = assignVarFlatCallResult; }
  public FlatCodeItem(AssignVarNull assignVarNull)                        { this.opcode = Opc.AssignVarNull;                this.assignVarNull = assignVarNull; }
  public FlatCodeItem(AssignVarNum assignVarNum)                          { this.opcode = Opc.AssignVarNum;                 this.assignVarNum = assignVarNum; }
  public FlatCodeItem(AssignVarUnop assignVarUnop)                        { this.opcode = Opc.AssignVarUnop;                this.assignVarUnop = assignVarUnop; }
  public FlatCodeItem(AssignVarVar assignVarVar)                          { this.opcode = Opc.AssignVarVar;                 this.assignVarVar = assignVarVar; }
  public FlatCodeItem(FlatCallConstructor flatCallConstructor)            { this.opcode = Opc.FlatCallConstructor;          this.flatCallConstructor = flatCallConstructor; }
  public FlatCodeItem(FlatCallResult flatCallResult)                      { this.opcode = Opc.FlatCallResult;               this.flatCallResult = flatCallResult; }
  public FlatCodeItem(FlatCallVoid flatCallVoid)                          { this.opcode = Opc.FlatCallVoid;                 this.flatCallVoid = flatCallVoid; }
  public FlatCodeItem(StoreFieldAssignOpCall storeFieldAssignOpCall)      { this.opcode = Opc.StoreFieldAssignOpCall;       this.storeFieldAssignOpCall = storeFieldAssignOpCall; }
  public FlatCodeItem(StoreFieldVar storeFieldVar)                        { this.opcode = Opc.StoreFieldVar;                this.storeFieldVar = storeFieldVar; }
  public FlatCodeItem(StoreVarAssignOpCall storeVarAssignOpCall)          { this.opcode = Opc.StoreVarAssignOpCall;         this.storeVarAssignOpCall = storeVarAssignOpCall; }
  public FlatCodeItem(StoreVarVar storeVarVar)                            { this.opcode = Opc.StoreVarVar;                  this.storeVarVar = storeVarVar; }
  public FlatCodeItem(FlatCallClassCreationTmp flatCallClassCreationTmp)  { this.opcode = Opc.FlatCallClassCreationTmp;     this.flatCallClassCreationTmp = flatCallClassCreationTmp; }
  
  public FlatCodeItem(AssignVarFlatCallClassCreationTmp assignVarFlatCallClassCreationTmp) {
    this.opcode = Opc.AssignVarFlatCallClassCreationTmp;
    this.assignVarFlatCallClassCreationTmp = assignVarFlatCallClassCreationTmp;
  }
  
  public boolean isAssignVarBinop()                 { return this.opcode == Opc.AssignVarBinop;                         }
  public boolean isAssignVarFieldAccess()           { return this.opcode == Opc.AssignVarFieldAccess;                  }
  public boolean isAssignVarFlatCallResult()        { return this.opcode == Opc.AssignVarFlatCallResult;               }
  public boolean isAssignVarNull()                  { return this.opcode == Opc.AssignVarNull;                          }
  public boolean isAssignVarNum()                   { return this.opcode == Opc.AssignVarNum;                           }
  public boolean isAssignVarUnop()                  { return this.opcode == Opc.AssignVarUnop;                          }
  public boolean isAssignVarVar()                   { return this.opcode == Opc.AssignVarVar;                           }
  public boolean isFlatCallConstructor()            { return this.opcode == Opc.FlatCallConstructor;                     }
  public boolean isFlatCallResult()                 { return this.opcode == Opc.FlatCallResult;                          }
  public boolean isFlatCallVoid()                   { return this.opcode == Opc.FlatCallVoid;                            }
  public boolean isStoreFieldAssignOpCall()         { return this.opcode == Opc.StoreFieldAssignOpCall;               }
  public boolean isStoreFieldVar()                  { return this.opcode == Opc.StoreFieldVar;                          }
  public boolean isStoreVarAssignOpCall()           { return this.opcode == Opc.StoreVarAssignOpCall;                 }
  public boolean isStoreVarVar()                    { return this.opcode == Opc.StoreVarVar;                            }
  public boolean isFlatCallClassCreationTmp()       { return this.opcode == Opc.FlatCallClassCreationTmp;              }
  
  @Override
  public String toString() {
    if(isAssignVarBinop())                          { return assignVarBinop.toString(); }
    if(isAssignVarFieldAccess())                    { return assignVarFieldAccess.toString(); }
    if(isAssignVarFlatCallResult())                 { return assignVarFlatCallResult.toString(); }
    if(isAssignVarNull())                           { return assignVarNull.toString(); }
    if(isAssignVarNum())                            { return assignVarNum.toString(); }
    if(isAssignVarUnop())                           { return assignVarUnop.toString(); }
    if(isAssignVarVar())                            { return assignVarVar.toString(); }
    if(isFlatCallConstructor())                     { return flatCallConstructor.toString(); }
    if(isFlatCallResult())                          { return flatCallResult.toString(); }
    if(isFlatCallVoid())                            { return flatCallVoid.toString(); }
    if(isStoreFieldAssignOpCall())                  { return storeFieldAssignOpCall.toString(); }
    if(isStoreFieldVar())                           { return storeFieldVar.toString(); }
    if(isStoreVarAssignOpCall())                    { return storeVarAssignOpCall.toString(); }
    if(isStoreVarVar())                             { return storeVarVar.toString(); }
    if(isFlatCallClassCreationTmp())                { return flatCallClassCreationTmp.toString(); }
    if(getOpcode().equals(Opc.AssignVarFlatCallClassCreationTmp)) {
      return assignVarFlatCallClassCreationTmp.toString();
    }
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

  public AssignVarAllocObject getAssignVarAllocObject() {
    return assignVarAllocObject;
  }

  public StoreArrayAssignOpCall getStoreArrayAssignOpCall() {
    return storeArrayAssignOpCall;
  }

  public StoreArrayVar getStoreArrayVar() {
    return storeArrayVar;
  }

  public AssignVarFlatCallClassCreationTmp getAssignVarFlatCallClassCreationTmp() {
    return assignVarFlatCallClassCreationTmp;
  }

  public FlatCallClassCreationTmp getFlatCallClassCreationTmp() {
    return flatCallClassCreationTmp;
  }

}
