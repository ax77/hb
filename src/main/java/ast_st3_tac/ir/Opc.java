package ast_st3_tac.ir;

//@formatter:off
public enum Opc {  
    ASSIGN_VAR_VAR               // type a = b
  , ASSIGN_VAR_FLATCALL_RESULT   // type a = f(b, c)
  , ASSIGN_VAR_ARRAY_ACCESS      // type a = b[c]
  , ASSIGN_VAR_FIELD_ACCESS      // type a = b.c
  , ASSIGN_VAR_BINOP             // type a = b + c
  , ASSIGN_VAR_UNOP              // type a = -b
  , ASSIGN_VAR_ALLOC_OBJECT      // type a = new a()
  , ASSIGN_VAR_LITERAL_TRUE      // type a = true
  , ASSIGN_VAR_LITERAL_FALSE     // type a = false
  , ASSIGN_VAR_NULL              // type a = null
  , ASSIGN_VAR_NUM               // type a = 1
  , ASSIGN_VAR_LITERAL_STRING    // type a = "..."
                                 // 
  , STORE_ARRAY_ASSIGN_OP_CALL   // a[b] = opAssign(a[b], c)
  , STORE_ARRAY_VAR              // a[b] = c
  , STORE_FIELD_ASSIGN_OP_CALL   // a.b = opAssign(a.b, c)
  , STORE_FIELD_VAR              // a.b = c
  , STORE_VAR_ASSIGN_OP_CALL     // a = opAssign(a, b)
  , STORE_VAR_VAR                // a = b
                                 //
  , FLATCALL_VOID                // f(a, t1, t2) -> void
  , FLATCALL_RESULT              // f(a, t1, t2) -> type
  , FLATCALL_CONSTRUCTOR         // initialize(_this_, t1, t2) -> void
}
