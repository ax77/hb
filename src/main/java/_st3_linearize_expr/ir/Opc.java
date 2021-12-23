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
  , AssignVarNull                      // type a = null
  , AssignVarNum                       // type a = 1
  , AssignVarTernaryOp                 // type a = ?(varCnd, varTrue, varFalse)
  , AssignVarTrue                      // type a = true
  , AssignVarUnop                      // type a = -b
  , AssignVarVar                       // type a = b
  , FlatCallConstructor                // initialize(_this_, t1, t2) -> void
  , FlatCallVoid                       // f(a, t1, t2) -> void
  , FlatCallVoidBuiltin                // assert_true -> void
  , FlatCallVoidStaticClassMethod      // constants.write_settings(32)
  , StoreFieldVar                      // a.b = c
  , StoreVarField                      // a = b.c
  , StoreVarVar                        // a = b
  //
  , IntrinsicText                      // everything internal
  , CallListenerVoidMethod             // __before_fcall("main_class.main::another_class.f", 10)
  , CallListenerResultMethod           // __before_fcall("main_class.main::another_class.f", 10)
  , AssignVarCastExpression            // cast(expressiont : to_this_type) -> cast(32:char)
}
