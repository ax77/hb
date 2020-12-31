package njast.ast_kinds;

//@formatter:off
public enum ExpressionBase {
   EASSIGN
 , EBINARY
 , ECOMMA
// , ETERNARY
 , EUNARY
 , EPRIMARY_IDENT
 , EPRIMARY_STRING
 , EPRIMARY_NUMBER
 , ECAST
 , EMETHOD_INVOCATION
// , EPREINCDEC
// , EPOSTINCDEC
 , EFIELD_ACCESS
 , ECLASS_INSTANCE_CREATION
 , ETHIS
}
