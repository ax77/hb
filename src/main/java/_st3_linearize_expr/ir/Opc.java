package _st3_linearize_expr.ir;

//@formatter:off
public enum Opc {  
    AssignVarAllocObject               // type a = new a()
  , AssignVarBinop                     // type a = b + c
  , AssignVarFalse                     // type a = false
  , AssignVarFieldAccess               // type a = b.c
  , AssignVarFlatCallClassCreationTmp  // temporary
  , AssignVarFlatCallResult            // type a = f(b, c)
  , AssignVarNull                      // type a = null
  , AssignVarNum                       // type a = 1
  , AssignVarString                    // type a = "..."
  , AssignVarTernaryOp                 // type a = ?(varCnd, varTrue, varFalse)
  , AssignVarTrue                      // type a = true
  , AssignVarUnop                      // type a = -b
  , AssignVarVar                       // type a = b
  , FlatCallConstructor                // initialize(_this_, t1, t2) -> void
  , FlatCallVoid                       // f(a, t1, t2) -> void
  , StoreFieldVar                      // a.b = c
  , StoreFieldVarAssignOp              // a.b = opAssign(a.b, c)
  , StoreVarField                      // a = b.c
  , StoreVarFieldAssignOp              // a = opAssign(a, b.c)
  , StoreVarVar                        // a = b
  , StoreVarVarAssignOp                // a = opAssign(a, b)
}
