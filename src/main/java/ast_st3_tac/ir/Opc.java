package ast_st3_tac.ir;

//@formatter:off
public enum Opc {  
    AssignVarVar                       // type a = b
  , AssignVarFlatCallResult            // type a = f(b, c)
  , AssignVarArrayAccess               // type a = b[c]
  , AssignVarFieldAccess               // type a = b.c
  , AssignVarBinop                     // type a = b + c
  , AssignVarUnop                      // type a = -b
  , AssignVarAllocObject               // type a = new a()
  , AssignVarTrue                      // type a = true
  , AssignVarFalse                     // type a = false
  , AssignVarNull                      // type a = null
  , AssignVarNum                       // type a = 1
  , AssignVarString                    // type a = "..."
                                       // 
  , StoreArrayAssignOpCall             // a[b] = opAssign(a[b], c)
  , StoreArrayVar                      // a[b] = c
  , StoreFieldAssignOpCall             // a.b = opAssign(a.b, c)
  , StoreFieldVar                      // a.b = c
  , StoreVarAssignOpCall               // a = opAssign(a, b)
  , StoreVarVar                        // a = b
                                       //
  , FlatCallVoid                       // f(a, t1, t2) -> void
  , FlatCallConstructor                // initialize(_this_, t1, t2) -> void
                                       //
  , AssignVarFlatCallClassCreationTmp  // temporary
}
