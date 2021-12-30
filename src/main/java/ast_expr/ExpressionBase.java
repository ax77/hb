package ast_expr;

//@formatter:off
public enum ExpressionBase {
   EASSIGN
 , EBINARY
 , EUNARY
 , EPRIMARY_IDENT
 , ECAST
 , EMETHOD_INVOCATION
 , EFIELD_ACCESS
 , ECLASS_CREATION
 , ETHIS
 , EPRIMARY_STRING
 , EPRIMARY_CHAR
 , EPRIMARY_NUMBER
 , EBOOLEAN_LITERAL
 , ETERNARY_OPERATOR
 , ESIZEOF
 , ETYPEOF
 , EBUILTIN_FUNC
 , EFOR_LOOP_STEP_COMMA // for(;; i+=1, j+=1)
 , EDEFAULT_VALUE_FOR_TYPE
 , ESTATIC_ACCESS
}
