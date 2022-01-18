package _st3_linearize_expr.ir;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import _st3_linearize_expr.items.AssignVarAllocObject;
import _st3_linearize_expr.items.AssignVarBinop;
import _st3_linearize_expr.items.AssignVarBool;
import _st3_linearize_expr.items.AssignVarCastExpression;
import _st3_linearize_expr.items.AssignVarFieldAccess;
import _st3_linearize_expr.items.AssignVarFlatCallResult;
import _st3_linearize_expr.items.AssignVarNum;
import _st3_linearize_expr.items.AssignVarSizeof;
import _st3_linearize_expr.items.AssignVarStaticLabel;
import _st3_linearize_expr.items.AssignVarUnop;
import _st3_linearize_expr.items.AssignVarVar;
import _st3_linearize_expr.items.BuiltinFuncAssertTrue;
import _st3_linearize_expr.items.FlatCallVoid;
import _st3_linearize_expr.items.SelectionShortCircuit;
import _st3_linearize_expr.items.StoreFieldLiteral;
import _st3_linearize_expr.items.StoreVarLiteral;
import _st3_linearize_expr.rvalues.Var;
import errors.AstParseException;

public class FlatCodeItem {
  private final Opc opcode;

  //generated code begin
  //@formatter:off
  private AssignVarAllocObject assignVarAllocObject;
  private AssignVarBinop assignVarBinop;
  private AssignVarBool assignVarBool;
  private AssignVarCastExpression assignVarCastExpression;
  private AssignVarFieldAccess assignVarFieldAccess;
  private AssignVarFlatCallResult assignVarFlatCallResult;
  private AssignVarNum assignVarNum;
  private AssignVarSizeof assignVarSizeof;
  private AssignVarStaticLabel assignVarStaticLabel;
  private AssignVarUnop assignVarUnop;
  private AssignVarVar assignVarVar;
  private BuiltinFuncAssertTrue builtinFuncAssertTrue;
  private FlatCallVoid flatCallVoid;
  private SelectionShortCircuit selectionShortCircuit;
  private StoreFieldLiteral storeFieldLiteral;
  private StoreVarLiteral storeVarLiteral;


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

  public FlatCodeItem(AssignVarAllocObject assignVarAllocObject) { this.uid = genUid(); this.opcode = Opc.AssignVarAllocObject; this.assignVarAllocObject = assignVarAllocObject; }
  public FlatCodeItem(AssignVarBinop assignVarBinop) { this.uid = genUid(); this.opcode = Opc.AssignVarBinop; this.assignVarBinop = assignVarBinop; }
  public FlatCodeItem(AssignVarBool assignVarBool) { this.uid = genUid(); this.opcode = Opc.AssignVarBool; this.assignVarBool = assignVarBool; }
  public FlatCodeItem(AssignVarCastExpression assignVarCastExpression) { this.uid = genUid(); this.opcode = Opc.AssignVarCastExpression; this.assignVarCastExpression = assignVarCastExpression; }
  public FlatCodeItem(AssignVarFieldAccess assignVarFieldAccess) { this.uid = genUid(); this.opcode = Opc.AssignVarFieldAccess; this.assignVarFieldAccess = assignVarFieldAccess; }
  public FlatCodeItem(AssignVarFlatCallResult assignVarFlatCallResult) { this.uid = genUid(); this.opcode = Opc.AssignVarFlatCallResult; this.assignVarFlatCallResult = assignVarFlatCallResult; }
  public FlatCodeItem(AssignVarNum assignVarNum) { this.uid = genUid(); this.opcode = Opc.AssignVarNum; this.assignVarNum = assignVarNum; }
  public FlatCodeItem(AssignVarSizeof assignVarSizeof) { this.uid = genUid(); this.opcode = Opc.AssignVarSizeof; this.assignVarSizeof = assignVarSizeof; }
  public FlatCodeItem(AssignVarStaticLabel assignVarStaticLabel) { this.uid = genUid(); this.opcode = Opc.AssignVarStaticLabel; this.assignVarStaticLabel = assignVarStaticLabel; }
  public FlatCodeItem(AssignVarUnop assignVarUnop) { this.uid = genUid(); this.opcode = Opc.AssignVarUnop; this.assignVarUnop = assignVarUnop; }
  public FlatCodeItem(AssignVarVar assignVarVar) { this.uid = genUid(); this.opcode = Opc.AssignVarVar; this.assignVarVar = assignVarVar; }
  public FlatCodeItem(BuiltinFuncAssertTrue builtinFuncAssertTrue) { this.uid = genUid(); this.opcode = Opc.BuiltinFuncAssertTrue; this.builtinFuncAssertTrue = builtinFuncAssertTrue; }
  public FlatCodeItem(FlatCallVoid flatCallVoid) { this.uid = genUid(); this.opcode = Opc.FlatCallVoid; this.flatCallVoid = flatCallVoid; }
  public FlatCodeItem(SelectionShortCircuit selectionShortCircuit) { this.uid = genUid(); this.opcode = Opc.SelectionShortCircuit; this.selectionShortCircuit = selectionShortCircuit; }
  public FlatCodeItem(StoreFieldLiteral storeFieldLiteral) { this.uid = genUid(); this.opcode = Opc.StoreFieldLiteral; this.storeFieldLiteral = storeFieldLiteral; }
  public FlatCodeItem(StoreVarLiteral storeVarLiteral) { this.uid = genUid(); this.opcode = Opc.StoreVarLiteral; this.storeVarLiteral = storeVarLiteral; }

  public boolean isAssignVarAllocObject() { return this.opcode == Opc.AssignVarAllocObject; }
  public boolean isAssignVarBinop() { return this.opcode == Opc.AssignVarBinop; }
  public boolean isAssignVarBool() { return this.opcode == Opc.AssignVarBool; }
  public boolean isAssignVarCastExpression() { return this.opcode == Opc.AssignVarCastExpression; }
  public boolean isAssignVarFieldAccess() { return this.opcode == Opc.AssignVarFieldAccess; }
  public boolean isAssignVarFlatCallResult() { return this.opcode == Opc.AssignVarFlatCallResult; }
  public boolean isAssignVarNum() { return this.opcode == Opc.AssignVarNum; }
  public boolean isAssignVarSizeof() { return this.opcode == Opc.AssignVarSizeof; }
  public boolean isAssignVarStaticLabel() { return this.opcode == Opc.AssignVarStaticLabel; }
  public boolean isAssignVarUnop() { return this.opcode == Opc.AssignVarUnop; }
  public boolean isAssignVarVar() { return this.opcode == Opc.AssignVarVar; }
  public boolean isBuiltinFuncAssertTrue() { return this.opcode == Opc.BuiltinFuncAssertTrue; }
  public boolean isFlatCallVoid() { return this.opcode == Opc.FlatCallVoid; }
  public boolean isSelectionShortCircuit() { return this.opcode == Opc.SelectionShortCircuit; }
  public boolean isStoreFieldLiteral() { return this.opcode == Opc.StoreFieldLiteral; }
  public boolean isStoreVarLiteral() { return this.opcode == Opc.StoreVarLiteral; }

  public List<Var> getAllVars() {
    if ( isAssignVarAllocObject() ) { return assignVarAllocObject.getAllVars(); }
    if ( isAssignVarBinop() ) { return assignVarBinop.getAllVars(); }
    if ( isAssignVarBool() ) { return assignVarBool.getAllVars(); }
    if ( isAssignVarCastExpression() ) { return assignVarCastExpression.getAllVars(); }
    if ( isAssignVarFieldAccess() ) { return assignVarFieldAccess.getAllVars(); }
    if ( isAssignVarFlatCallResult() ) { return assignVarFlatCallResult.getAllVars(); }
    if ( isAssignVarNum() ) { return assignVarNum.getAllVars(); }
    if ( isAssignVarSizeof() ) { return assignVarSizeof.getAllVars(); }
    if ( isAssignVarStaticLabel() ) { return assignVarStaticLabel.getAllVars(); }
    if ( isAssignVarUnop() ) { return assignVarUnop.getAllVars(); }
    if ( isAssignVarVar() ) { return assignVarVar.getAllVars(); }
    if ( isBuiltinFuncAssertTrue() ) { return builtinFuncAssertTrue.getAllVars(); }
    if ( isFlatCallVoid() ) { return flatCallVoid.getAllVars(); }
    if ( isSelectionShortCircuit() ) { return selectionShortCircuit.getAllVars(); }
    if ( isStoreFieldLiteral() ) { return storeFieldLiteral.getAllVars(); }
    if ( isStoreVarLiteral() ) { return storeVarLiteral.getAllVars(); }
    err();
    return null;
  }

  public Var getLvalue() {
    if ( isAssignVarAllocObject() ) { return assignVarAllocObject.getLvalue(); }
    if ( isAssignVarBinop() ) { return assignVarBinop.getLvalue(); }
    if ( isAssignVarBool() ) { return assignVarBool.getLvalue(); }
    if ( isAssignVarCastExpression() ) { return assignVarCastExpression.getLvalue(); }
    if ( isAssignVarFieldAccess() ) { return assignVarFieldAccess.getLvalue(); }
    if ( isAssignVarFlatCallResult() ) { return assignVarFlatCallResult.getLvalue(); }
    if ( isAssignVarNum() ) { return assignVarNum.getLvalue(); }
    if ( isAssignVarSizeof() ) { return assignVarSizeof.getLvalue(); }
    if ( isAssignVarStaticLabel() ) { return assignVarStaticLabel.getLvalue(); }
    if ( isAssignVarUnop() ) { return assignVarUnop.getLvalue(); }
    if ( isAssignVarVar() ) { return assignVarVar.getLvalue(); }
    err();
    return null;
  }

  @Override
  public String toString() {
    if(ignore) { return ""; }
    if(isAssignVarAllocObject()) { return assignVarAllocObject.toString(); }
    if(isAssignVarBinop()) { return assignVarBinop.toString(); }
    if(isAssignVarBool()) { return assignVarBool.toString(); }
    if(isAssignVarCastExpression()) { return assignVarCastExpression.toString(); }
    if(isAssignVarFieldAccess()) { return assignVarFieldAccess.toString(); }
    if(isAssignVarFlatCallResult()) { return assignVarFlatCallResult.toString(); }
    if(isAssignVarNum()) { return assignVarNum.toString(); }
    if(isAssignVarSizeof()) { return assignVarSizeof.toString(); }
    if(isAssignVarStaticLabel()) { return assignVarStaticLabel.toString(); }
    if(isAssignVarUnop()) { return assignVarUnop.toString(); }
    if(isAssignVarVar()) { return assignVarVar.toString(); }
    if(isBuiltinFuncAssertTrue()) { return builtinFuncAssertTrue.toString(); }
    if(isFlatCallVoid()) { return flatCallVoid.toString(); }
    if(isSelectionShortCircuit()) { return selectionShortCircuit.toString(); }
    if(isStoreFieldLiteral()) { return storeFieldLiteral.toString(); }
    if(isStoreVarLiteral()) { return storeVarLiteral.toString(); }
    return "?UnknownItem"; 
  }

  public Opc getOpcode() { return this.opcode; }
  public AssignVarAllocObject getAssignVarAllocObject() { return this.assignVarAllocObject; }
  public AssignVarBinop getAssignVarBinop() { return this.assignVarBinop; }
  public AssignVarBool getAssignVarBool() { return this.assignVarBool; }
  public AssignVarCastExpression getAssignVarCastExpression() { return this.assignVarCastExpression; }
  public AssignVarFieldAccess getAssignVarFieldAccess() { return this.assignVarFieldAccess; }
  public AssignVarFlatCallResult getAssignVarFlatCallResult() { return this.assignVarFlatCallResult; }
  public AssignVarNum getAssignVarNum() { return this.assignVarNum; }
  public AssignVarSizeof getAssignVarSizeof() { return this.assignVarSizeof; }
  public AssignVarStaticLabel getAssignVarStaticLabel() { return this.assignVarStaticLabel; }
  public AssignVarUnop getAssignVarUnop() { return this.assignVarUnop; }
  public AssignVarVar getAssignVarVar() { return this.assignVarVar; }
  public BuiltinFuncAssertTrue getBuiltinFuncAssertTrue() { return this.builtinFuncAssertTrue; }
  public FlatCallVoid getFlatCallVoid() { return this.flatCallVoid; }
  public SelectionShortCircuit getSelectionShortCircuit() { return this.selectionShortCircuit; }
  public StoreFieldLiteral getStoreFieldLiteral() { return this.storeFieldLiteral; }
  public StoreVarLiteral getStoreVarLiteral() { return this.storeVarLiteral; }


  public boolean isOneOfAssigns() {
      return 
         isAssignVarAllocObject()  
      || isAssignVarBinop()  
      || isAssignVarBool()  
      || isAssignVarFieldAccess() 
      || isAssignVarStaticLabel()  
      || isAssignVarFlatCallResult()
      || isAssignVarNum() 
      || isAssignVarUnop()   
      || isAssignVarVar()
      || isAssignVarCastExpression() 
      ;
  }

  public Var getDest() {
    if(ignore) { return rvalueDestWrapper; }
    if(isAssignVarAllocObject()) { return assignVarAllocObject.getLvalue(); }
    if(isAssignVarBinop()) { return assignVarBinop.getLvalue(); }
    if(isAssignVarBool()) { return assignVarBool.getLvalue(); }
    if(isAssignVarCastExpression()) { return assignVarCastExpression.getLvalue(); }
    if(isAssignVarFieldAccess()) { return assignVarFieldAccess.getLvalue(); }
    if(isAssignVarFlatCallResult()) { return assignVarFlatCallResult.getLvalue(); }
    if(isAssignVarNum()) { return assignVarNum.getLvalue(); }
    if(isAssignVarSizeof()) { return assignVarSizeof.getLvalue(); }
    if(isAssignVarStaticLabel()) { return assignVarStaticLabel.getLvalue(); }
    if(isAssignVarUnop()) { return assignVarUnop.getLvalue(); }
    if(isAssignVarVar()) { return assignVarVar.getLvalue(); }
    if(isBuiltinFuncAssertTrue()) { err(); }
    if(isFlatCallVoid()) { err(); }
    if(isSelectionShortCircuit()) { return selectionShortCircuit.getDest(); }
    if(isStoreFieldLiteral()) { err(); }
    if(isStoreVarLiteral()) { err(); }
    throw new AstParseException("unknown item for result: " + toString());
  }
  private void err() { throw new AstParseException("unexpected item for result: " + toString()); }
  //@formatter:on
  //generated code end

}
