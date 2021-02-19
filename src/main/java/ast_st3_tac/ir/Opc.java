package ast_st3_tac.ir;

//@formatter:off
public enum Opc {  
    AssignVarAllocObject               // type a = new a()
  , AssignVarArrayAccess               // type a = b[c]
  , AssignVarBinop                     // type a = b + c
  , AssignVarFalse                     // type a = false
  , AssignVarFieldAccess               // type a = b.c
  , AssignVarFlatCallClassCreationTmp  // temporary
  , AssignVarFlatCallResult            // type a = f(b, c)
  , AssignVarNull                      // type a = null
  , AssignVarNum                       // type a = 1
  , AssignVarString                    // type a = "..."
  , AssignVarTrue                      // type a = true
  , AssignVarUnop                      // type a = -b
  , AssignVarVar                       // type a = b
  , FlatCallConstructor                // initialize(_this_, t1, t2) -> void
  , FlatCallVoid                       // f(a, t1, t2) -> void
  , StoreArrayAssignOpCall             // a[b] = opAssign(a[b], c)
  , StoreArrayVar                      // a[b] = c
  , StoreFieldAssignOpCall             // a.b = opAssign(a.b, c)
  , StoreFieldVar                      // a.b = c
  , StoreVarAssignOpCall               // a = opAssign(a, b)
  , StoreVarVar                        // a = b
}
