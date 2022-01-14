package _st3_linearize_expr.ir;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import _st3_linearize_expr.items.AssignVarAllocObject;
import _st3_linearize_expr.items.AssignVarBinop;
import _st3_linearize_expr.items.AssignVarBool;
import _st3_linearize_expr.items.AssignVarCastExpression;
import _st3_linearize_expr.items.AssignVarDefaultValueForType;
import _st3_linearize_expr.items.AssignVarFieldAccess;
import _st3_linearize_expr.items.AssignVarFieldAccessStatic;
import _st3_linearize_expr.items.AssignVarFlatCallClassCreationTmp;
import _st3_linearize_expr.items.AssignVarFlatCallResult;
import _st3_linearize_expr.items.AssignVarFlatCallResultStatic;
import _st3_linearize_expr.items.AssignVarFlatCallStringCreationTmp;
import _st3_linearize_expr.items.AssignVarNum;
import _st3_linearize_expr.items.AssignVarSizeof;
import _st3_linearize_expr.items.AssignVarUnop;
import _st3_linearize_expr.items.AssignVarVar;
import _st3_linearize_expr.items.BuiltinFuncAssertTrue;
import _st3_linearize_expr.items.FlatCallConstructor;
import _st3_linearize_expr.items.FlatCallVoid;
import _st3_linearize_expr.items.FlatCallVoidStatic;
import _st3_linearize_expr.items.SelectionShortCircuit;
import _st3_linearize_expr.items.StoreFieldVar;
import _st3_linearize_expr.items.StoreVarField;
import _st3_linearize_expr.items.StoreVarVar;
import _st3_linearize_expr.leaves.Var;
import errors.AstParseException;

public class FlatCodeItem {
  private final Opc opcode;

  //generated code begin
  //@formatter:off
  private AssignVarAllocObject assignVarAllocObject;
  private AssignVarBinop assignVarBinop;
  private AssignVarBool assignVarBool;
  private AssignVarCastExpression assignVarCastExpression;
  private AssignVarDefaultValueForType assignVarDefaultValueForType;
  private AssignVarFieldAccess assignVarFieldAccess;
  private AssignVarFieldAccessStatic assignVarFieldAccessStatic;
  private AssignVarFlatCallClassCreationTmp assignVarFlatCallClassCreationTmp;
  private AssignVarFlatCallResult assignVarFlatCallResult;
  private AssignVarFlatCallResultStatic assignVarFlatCallResultStatic;
  private AssignVarFlatCallStringCreationTmp assignVarFlatCallStringCreationTmp;
  private AssignVarNum assignVarNum;
  private AssignVarSizeof assignVarSizeof;
  private AssignVarUnop assignVarUnop;
  private AssignVarVar assignVarVar;
  private BuiltinFuncAssertTrue builtinFuncAssertTrue;
  private FlatCallConstructor flatCallConstructor;
  private FlatCallVoid flatCallVoid;
  private FlatCallVoidStatic flatCallVoidStatic;
  private SelectionShortCircuit selectionShortCircuit;
  private StoreFieldVar storeFieldVar;
  private StoreVarField storeVarField;
  private StoreVarVar storeVarVar;
  
  // new code
  private final String uid;
  private boolean ignore;
  private Var rvalueDestWrapper;
  public boolean isIgnore() {
    return ignore;
  }
  public void setIgnore(Var rvalueDestWrapper) { // this dest is only for returns, the result
    this.ignore = true;
    this.rvalueDestWrapper = rvalueDestWrapper;
  }
  public Var getLvalue() {
    if( isAssignVarAllocObject()               ) { return  assignVarAllocObject              .getLvalue(); }
    if( isAssignVarBinop()                     ) { return  assignVarBinop                    .getLvalue(); }
    if( isAssignVarBool()                      ) { return  assignVarBool                     .getLvalue(); }
    if( isAssignVarFieldAccess()               ) { return  assignVarFieldAccess              .getLvalue(); }
    if( isAssignVarFieldAccessStatic()         ) { return  assignVarFieldAccessStatic        .getLvalue(); }
    if( isAssignVarFlatCallClassCreationTmp()  ) { return  assignVarFlatCallClassCreationTmp .getLvalue(); }
    if( isAssignVarFlatCallStringCreationTmp() ) { return  assignVarFlatCallStringCreationTmp.getLvalue(); }
    if( isAssignVarFlatCallResult()            ) { return  assignVarFlatCallResult           .getLvalue(); }
    if( isAssignVarFlatCallResultStatic()      ) { return  assignVarFlatCallResultStatic     .getLvalue(); }
    if( isAssignVarNum()                       ) { return  assignVarNum                      .getLvalue(); }
    if( isAssignVarDefaultValueForType()       ) { return  assignVarDefaultValueForType      .getLvalue(); }
    if( isAssignVarUnop()                      ) { return  assignVarUnop                     .getLvalue(); }
    if( isAssignVarVar()                       ) { return  assignVarVar                      .getLvalue(); }
    if( isAssignVarCastExpression()            ) { return  assignVarCastExpression           .getLvalue(); }
    err();
    return null;
  }
  public List<Var> getAllVars() {
    if ( isAssignVarAllocObject()               ) { return  assignVarAllocObject               .getAllVars(); }
    if ( isAssignVarBinop()                     ) { return  assignVarBinop                     .getAllVars(); }
    if ( isAssignVarBool()                      ) { return  assignVarBool                      .getAllVars(); }
    if ( isAssignVarCastExpression()            ) { return  assignVarCastExpression            .getAllVars(); }
    if ( isAssignVarDefaultValueForType()       ) { return  assignVarDefaultValueForType       .getAllVars(); }
    if ( isAssignVarFieldAccess()               ) { return  assignVarFieldAccess               .getAllVars(); }
    if ( isAssignVarFieldAccessStatic()         ) { return  assignVarFieldAccessStatic         .getAllVars(); }
    if ( isAssignVarFlatCallClassCreationTmp()  ) { return  assignVarFlatCallClassCreationTmp  .getAllVars(); }
    if ( isAssignVarFlatCallResult()            ) { return  assignVarFlatCallResult            .getAllVars(); }
    if ( isAssignVarFlatCallResultStatic()      ) { return  assignVarFlatCallResultStatic      .getAllVars(); }
    if ( isAssignVarFlatCallStringCreationTmp() ) { return  assignVarFlatCallStringCreationTmp .getAllVars(); }
    if ( isAssignVarNum()                       ) { return  assignVarNum                       .getAllVars(); }
    if ( isAssignVarSizeof()                    ) { return  assignVarSizeof                    .getAllVars(); }
    if ( isAssignVarUnop()                      ) { return  assignVarUnop                      .getAllVars(); }
    if ( isAssignVarVar()                       ) { return  assignVarVar                       .getAllVars(); }
    if ( isBuiltinFuncAssertTrue()              ) { return  builtinFuncAssertTrue              .getAllVars(); }
    if ( isFlatCallConstructor()                ) { return  flatCallConstructor                .getAllVars(); }
    if ( isFlatCallVoid()                       ) { return  flatCallVoid                       .getAllVars(); }
    if ( isFlatCallVoidStatic()                 ) { return  flatCallVoidStatic                 .getAllVars(); }
    if ( isSelectionShortCircuit()              ) { return  selectionShortCircuit              .getAllVars(); }
    if ( isStoreFieldVar()                      ) { return  storeFieldVar                      .getAllVars(); }
    if ( isStoreVarField()                      ) { return  storeVarField                      .getAllVars(); }
    if ( isStoreVarVar()                        ) { return  storeVarVar                        .getAllVars(); }
    err();
    return null;
  }
  private String genUid() {
    return UUID.randomUUID().toString();
  }
  @Override
  public int hashCode() {
    return Objects.hash(uid);
  }
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FlatCodeItem other = (FlatCodeItem) obj;
    return Objects.equals(uid, other.uid);
  }
  //new code

  public FlatCodeItem(AssignVarAllocObject assignVarAllocObject) { this.uid = genUid(); this.opcode = Opc.AssignVarAllocObject; this.assignVarAllocObject = assignVarAllocObject; }
  public FlatCodeItem(AssignVarBinop assignVarBinop) {this.uid = genUid(); this.opcode = Opc.AssignVarBinop; this.assignVarBinop = assignVarBinop; }
  public FlatCodeItem(AssignVarBool assignVarBool) { this.uid = genUid();this.opcode = Opc.AssignVarBool; this.assignVarBool = assignVarBool; }
  public FlatCodeItem(AssignVarCastExpression assignVarCastExpression) {this.uid = genUid(); this.opcode = Opc.AssignVarCastExpression; this.assignVarCastExpression = assignVarCastExpression; }
  public FlatCodeItem(AssignVarDefaultValueForType assignVarDefaultValueForType) {this.uid = genUid(); this.opcode = Opc.AssignVarDefaultValueForType; this.assignVarDefaultValueForType = assignVarDefaultValueForType; }
  public FlatCodeItem(AssignVarFieldAccess assignVarFieldAccess) { this.uid = genUid();this.opcode = Opc.AssignVarFieldAccess; this.assignVarFieldAccess = assignVarFieldAccess; }
  public FlatCodeItem(AssignVarFieldAccessStatic assignVarFieldAccessStatic) {this.uid = genUid(); this.opcode = Opc.AssignVarFieldAccessStatic; this.assignVarFieldAccessStatic = assignVarFieldAccessStatic; }
  public FlatCodeItem(AssignVarFlatCallClassCreationTmp assignVarFlatCallClassCreationTmp) {this.uid = genUid(); this.opcode = Opc.AssignVarFlatCallClassCreationTmp; this.assignVarFlatCallClassCreationTmp = assignVarFlatCallClassCreationTmp; }
  public FlatCodeItem(AssignVarFlatCallResult assignVarFlatCallResult) {this.uid = genUid(); this.opcode = Opc.AssignVarFlatCallResult; this.assignVarFlatCallResult = assignVarFlatCallResult; }
  public FlatCodeItem(AssignVarFlatCallResultStatic assignVarFlatCallResultStatic) {this.uid = genUid(); this.opcode = Opc.AssignVarFlatCallResultStatic; this.assignVarFlatCallResultStatic = assignVarFlatCallResultStatic; }
  public FlatCodeItem(AssignVarFlatCallStringCreationTmp assignVarFlatCallStringCreationTmp) {this.uid = genUid(); this.opcode = Opc.AssignVarFlatCallStringCreationTmp; this.assignVarFlatCallStringCreationTmp = assignVarFlatCallStringCreationTmp; }
  public FlatCodeItem(AssignVarNum assignVarNum) {this.uid = genUid(); this.opcode = Opc.AssignVarNum; this.assignVarNum = assignVarNum; }
  public FlatCodeItem(AssignVarSizeof assignVarSizeof) {this.uid = genUid(); this.opcode = Opc.AssignVarSizeof; this.assignVarSizeof = assignVarSizeof; }
  public FlatCodeItem(AssignVarUnop assignVarUnop) {this.uid = genUid(); this.opcode = Opc.AssignVarUnop; this.assignVarUnop = assignVarUnop; }
  public FlatCodeItem(AssignVarVar assignVarVar) {this.uid = genUid(); this.opcode = Opc.AssignVarVar; this.assignVarVar = assignVarVar; }
  public FlatCodeItem(BuiltinFuncAssertTrue builtinFuncAssertTrue) { this.uid = genUid();this.opcode = Opc.BuiltinFuncAssertTrue; this.builtinFuncAssertTrue = builtinFuncAssertTrue; }
  public FlatCodeItem(FlatCallConstructor flatCallConstructor) {this.uid = genUid(); this.opcode = Opc.FlatCallConstructor; this.flatCallConstructor = flatCallConstructor; }
  public FlatCodeItem(FlatCallVoid flatCallVoid) {this.uid = genUid(); this.opcode = Opc.FlatCallVoid; this.flatCallVoid = flatCallVoid; }
  public FlatCodeItem(FlatCallVoidStatic flatCallVoidStatic) { this.uid = genUid();this.opcode = Opc.FlatCallVoidStatic; this.flatCallVoidStatic = flatCallVoidStatic; }
  public FlatCodeItem(SelectionShortCircuit selectionShortCircuit) {this.uid = genUid(); this.opcode = Opc.SelectionShortCircuit; this.selectionShortCircuit = selectionShortCircuit; }
  public FlatCodeItem(StoreFieldVar storeFieldVar) {this.uid = genUid();this.opcode = Opc.StoreFieldVar; this.storeFieldVar = storeFieldVar; }
  public FlatCodeItem(StoreVarField storeVarField) {this.uid = genUid(); this.opcode = Opc.StoreVarField; this.storeVarField = storeVarField; }
  public FlatCodeItem(StoreVarVar storeVarVar) {this.uid = genUid();this.opcode = Opc.StoreVarVar; this.storeVarVar = storeVarVar; }

  public boolean isAssignVarAllocObject() { return this.opcode == Opc.AssignVarAllocObject; }
  public boolean isAssignVarBinop() { return this.opcode == Opc.AssignVarBinop; }
  public boolean isAssignVarBool() { return this.opcode == Opc.AssignVarBool; }
  public boolean isAssignVarCastExpression() { return this.opcode == Opc.AssignVarCastExpression; }
  public boolean isAssignVarDefaultValueForType() { return this.opcode == Opc.AssignVarDefaultValueForType; }
  public boolean isAssignVarFieldAccess() { return this.opcode == Opc.AssignVarFieldAccess; }
  public boolean isAssignVarFieldAccessStatic() { return this.opcode == Opc.AssignVarFieldAccessStatic; }
  public boolean isAssignVarFlatCallClassCreationTmp() { return this.opcode == Opc.AssignVarFlatCallClassCreationTmp; }
  public boolean isAssignVarFlatCallResult() { return this.opcode == Opc.AssignVarFlatCallResult; }
  public boolean isAssignVarFlatCallResultStatic() { return this.opcode == Opc.AssignVarFlatCallResultStatic; }
  public boolean isAssignVarFlatCallStringCreationTmp() { return this.opcode == Opc.AssignVarFlatCallStringCreationTmp; }
  public boolean isAssignVarNum() { return this.opcode == Opc.AssignVarNum; }
  public boolean isAssignVarSizeof() { return this.opcode == Opc.AssignVarSizeof; }
  public boolean isAssignVarUnop() { return this.opcode == Opc.AssignVarUnop; }
  public boolean isAssignVarVar() { return this.opcode == Opc.AssignVarVar; }
  public boolean isBuiltinFuncAssertTrue() { return this.opcode == Opc.BuiltinFuncAssertTrue; }
  public boolean isFlatCallConstructor() { return this.opcode == Opc.FlatCallConstructor; }
  public boolean isFlatCallVoid() { return this.opcode == Opc.FlatCallVoid; }
  public boolean isFlatCallVoidStatic() { return this.opcode == Opc.FlatCallVoidStatic; }
  public boolean isSelectionShortCircuit() { return this.opcode == Opc.SelectionShortCircuit; }
  public boolean isStoreFieldVar() { return this.opcode == Opc.StoreFieldVar; }
  public boolean isStoreVarField() { return this.opcode == Opc.StoreVarField; }
  public boolean isStoreVarVar() { return this.opcode == Opc.StoreVarVar; }

  @Override
  public String toString() {
    if(ignore) {
      return "";
    }
    
    if(isAssignVarAllocObject()) { return assignVarAllocObject.toString(); }
    if(isAssignVarBinop()) { return assignVarBinop.toString(); }
    if(isAssignVarBool()) { return assignVarBool.toString(); }
    if(isAssignVarCastExpression()) { return assignVarCastExpression.toString(); }
    if(isAssignVarDefaultValueForType()) { return assignVarDefaultValueForType.toString(); }
    if(isAssignVarFieldAccess()) { return assignVarFieldAccess.toString(); }
    if(isAssignVarFieldAccessStatic()) { return assignVarFieldAccessStatic.toString(); }
    if(isAssignVarFlatCallClassCreationTmp()) { return assignVarFlatCallClassCreationTmp.toString(); }
    if(isAssignVarFlatCallResult()) { return assignVarFlatCallResult.toString(); }
    if(isAssignVarFlatCallResultStatic()) { return assignVarFlatCallResultStatic.toString(); }
    if(isAssignVarFlatCallStringCreationTmp()) { return assignVarFlatCallStringCreationTmp.toString(); }
    if(isAssignVarNum()) { return assignVarNum.toString(); }
    if(isAssignVarSizeof()) { return assignVarSizeof.toString(); }
    if(isAssignVarUnop()) { return assignVarUnop.toString(); }
    if(isAssignVarVar()) { return assignVarVar.toString(); }
    if(isBuiltinFuncAssertTrue()) { return builtinFuncAssertTrue.toString(); }
    if(isFlatCallConstructor()) { return flatCallConstructor.toString(); }
    if(isFlatCallVoid()) { return flatCallVoid.toString(); }
    if(isFlatCallVoidStatic()) { return flatCallVoidStatic.toString(); }
    if(isSelectionShortCircuit()) { return selectionShortCircuit.toString(); }
    if(isStoreFieldVar()) { return storeFieldVar.toString(); }
    if(isStoreVarField()) { return storeVarField.toString(); }
    if(isStoreVarVar()) { return storeVarVar.toString(); }
    return "?UnknownItem"; 
  }

  public Opc getOpcode() { return this.opcode; }
  public AssignVarAllocObject getAssignVarAllocObject() { return this.assignVarAllocObject; }
  public AssignVarBinop getAssignVarBinop() { return this.assignVarBinop; }
  public AssignVarBool getAssignVarBool() { return this.assignVarBool; }
  public AssignVarCastExpression getAssignVarCastExpression() { return this.assignVarCastExpression; }
  public AssignVarDefaultValueForType getAssignVarDefaultValueForType() { return this.assignVarDefaultValueForType; }
  public AssignVarFieldAccess getAssignVarFieldAccess() { return this.assignVarFieldAccess; }
  public AssignVarFieldAccessStatic getAssignVarFieldAccessStatic() { return this.assignVarFieldAccessStatic; }
  public AssignVarFlatCallClassCreationTmp getAssignVarFlatCallClassCreationTmp() { return this.assignVarFlatCallClassCreationTmp; }
  public AssignVarFlatCallResult getAssignVarFlatCallResult() { return this.assignVarFlatCallResult; }
  public AssignVarFlatCallResultStatic getAssignVarFlatCallResultStatic() { return this.assignVarFlatCallResultStatic; }
  public AssignVarFlatCallStringCreationTmp getAssignVarFlatCallStringCreationTmp() { return this.assignVarFlatCallStringCreationTmp; }
  public AssignVarNum getAssignVarNum() { return this.assignVarNum; }
  public AssignVarSizeof getAssignVarSizeof() { return this.assignVarSizeof; }
  public AssignVarUnop getAssignVarUnop() { return this.assignVarUnop; }
  public AssignVarVar getAssignVarVar() { return this.assignVarVar; }
  public BuiltinFuncAssertTrue getBuiltinFuncAssertTrue() { return this.builtinFuncAssertTrue; }
  public FlatCallConstructor getFlatCallConstructor() { return this.flatCallConstructor; }
  public FlatCallVoid getFlatCallVoid() { return this.flatCallVoid; }
  public FlatCallVoidStatic getFlatCallVoidStatic() { return this.flatCallVoidStatic; }
  public SelectionShortCircuit getSelectionShortCircuit() { return this.selectionShortCircuit; }
  public StoreFieldVar getStoreFieldVar() { return this.storeFieldVar; }
  public StoreVarField getStoreVarField() { return this.storeVarField; }
  public StoreVarVar getStoreVarVar() { return this.storeVarVar; }


  public boolean isOneOfAssigns() {
      return 
         isAssignVarAllocObject()  
      || isAssignVarBinop()  
      || isAssignVarBool()  
      || isAssignVarFieldAccess() 
      || isAssignVarFieldAccessStatic()  
      || isAssignVarFlatCallClassCreationTmp()
      || isAssignVarFlatCallStringCreationTmp()
      || isAssignVarFlatCallResult()
      || isAssignVarFlatCallResultStatic() 
      || isAssignVarNum() 
      || isAssignVarDefaultValueForType()     
      || isAssignVarUnop()   
      || isAssignVarVar()
      || isAssignVarCastExpression() 
      ;
  }

  public Var getDest() {
    if(isIgnore()) {
      return rvalueDestWrapper;
    }
    
    if(isAssignVarAllocObject()) {
      return assignVarAllocObject.getLvalue();
    }
    if(isAssignVarBinop()) {
      return assignVarBinop.getLvalue();
    }
    if(isAssignVarBool()) {
      return assignVarBool.getLvalue();
    }
    if(isAssignVarCastExpression()) {
      return assignVarCastExpression.getLvalue();
    }
    if(isAssignVarDefaultValueForType()) {
      return assignVarDefaultValueForType.getLvalue();
    }
    if(isAssignVarFieldAccess()) {
      return assignVarFieldAccess.getLvalue();
    }
    if(isAssignVarFieldAccessStatic()) {
      return assignVarFieldAccessStatic.getLvalue();
    }
    if(isAssignVarFlatCallClassCreationTmp()) {
      return assignVarFlatCallClassCreationTmp.getLvalue();
    }
    if(isAssignVarFlatCallResult()) {
      return assignVarFlatCallResult.getLvalue();
    }
    if(isAssignVarFlatCallResultStatic()) {
      return assignVarFlatCallResultStatic.getLvalue();
    }
    if(isAssignVarFlatCallStringCreationTmp()) {
      return assignVarFlatCallStringCreationTmp.getLvalue();
    }
    if(isAssignVarNum()) {
      return assignVarNum.getLvalue();
    }
    if(isAssignVarSizeof()) {
      return assignVarSizeof.getLvalue();
    }
    if(isAssignVarUnop()) {
      return assignVarUnop.getLvalue();
    }
    if(isAssignVarVar()) {
      return assignVarVar.getLvalue();
    }
    if(isBuiltinFuncAssertTrue()) {
      err();
    }
    if(isFlatCallConstructor()) {
      return flatCallConstructor.getThisVar();
    }
    if(isFlatCallVoid()) {
      err();
    }
    if(isFlatCallVoidStatic()) {
      err();
    }
    if(isSelectionShortCircuit()) {
      return selectionShortCircuit.getDest();
    }
    if(isStoreFieldVar()) {
      err();
    }
    if(isStoreVarField()) {
      err();
    }
    if(isStoreVarVar()) {
      err();
    }
    throw new AstParseException("unknown item for result: " + toString());
  }
  private void err() { throw new AstParseException("unexpected item for result: " + toString()); }
  //@formatter:on
  //generated code end

}
