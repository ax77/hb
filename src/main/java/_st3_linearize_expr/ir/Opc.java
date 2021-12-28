package _st3_linearize_expr.ir;

//@formatter:off
public enum Opc {  
    AssignVarAllocObject               // type a = new a()
  , AssignVarBinop                     // type a = b + c
  , AssignVarFalse                     // type a = false
  , AssignVarFieldAccess               // type a = b.c
  , AssignVarStaticFieldAccess         // type f = constants.flag
  , AssignVarSizeof                    // size_t a = sizeof(b)
  , AssignVarFlatCallClassCreationTmp  // temporary
  , AssignVarFlatCallStringCreationTmp // temporary
  , AssignVarFlatCallResult            // type a = f(b, c)
  , AssignVarFlatCallResultStatic      // type a = io.read(b)
  , AssignVarFlatCallResultHashFn      // int hash(type);
  , AssignVarNum                       // type a = 1
  , AssignVarTernaryOp                 // type a = ?(varCnd, varTrue, varFalse)
  , AssignVarTrue                      // type a = true
  , AssignVarUnop                      // type a = -b
  , AssignVarVar                       // type a = b
  , AssignVarCastExpression            // cast(expression : to_this_type) -> cast(32:char)
  //
  , FlatCallConstructor                // initialize(_this_, t1, t2) -> void
  , FlatCallVoid                       // f(a, t1, t2) -> void
  , FlatCallVoidStaticClassMethod      // constants.write_settings(32)
  //
  , StoreFieldVar                      // a.b = c
  , StoreVarField                      // a = b.c
  , StoreVarVar                        // a = b
  //
  , IntrinsicText                      // everything internal
  , SelectionShortCircuit              // ?(cond, btrue, bfalse) -> if(cond) { btrue; } else { bfalse; }
  
}
