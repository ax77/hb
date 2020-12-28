package njast.ast_flow.expr;

//@formatter:off
public enum CExpressionBase {
   EASSIGN
 , EBINARY
 , ECOMMA
 , ETERNARY
 , EUNARY
 , EPRIMARY_IDENT
 , EPRIMARY_STRING
 , EPRIMARY_NUMBER
 , ECAST
 , EMETHOD_INVOCATION
 , EPREINCDEC
 , EPOSTINCDEC
 , EFIELD_ACCESS
 , EGET_POINTER_TO_CLASS
}
