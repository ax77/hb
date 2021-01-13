package njast.ast_checkers;

import static jscan.tokenize.T.T_AND;
import static jscan.tokenize.T.T_EXCLAMATION;
import static jscan.tokenize.T.T_MINUS;
import static jscan.tokenize.T.T_PLUS;
import static jscan.tokenize.T.T_TILDE;
import static jscan.tokenize.T.T_TIMES;

import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.symtab.IdentMap;

public abstract class IdentRecognizer {

  //@formatter:off
  
  public static boolean isAssignOperator(Token what) {
    return what.ofType(T.T_ASSIGN)
        || what.ofType(T.T_TIMES_EQUAL)
        || what.ofType(T.T_PERCENT_EQUAL)
        || what.ofType(T.T_DIVIDE_EQUAL)
        || what.ofType(T.T_PLUS_EQUAL)
        || what.ofType(T.T_MINUS_EQUAL)
        || what.ofType(T.T_LSHIFT_EQUAL)
        || what.ofType(T.T_RSHIFT_EQUAL)
        || what.ofType(T.T_AND_EQUAL)
        || what.ofType(T.T_XOR_EQUAL)
        || what.ofType(T.T_OR_EQUAL);
  }

  // & * + - ~ !
  public static boolean isUnaryOperator(Token what) {
    return what.ofType(T_AND)
        || what.ofType(T_TIMES)
        || what.ofType(T_PLUS)
        || what.ofType(T_MINUS)
        || what.ofType(T_TILDE)
        || what.ofType(T_EXCLAMATION);
  }
  
  public static boolean isBasicTypeIdent(Token what) {
    return what.isIdent(IdentMap.byte_ident)
        || what.isIdent(IdentMap.short_ident)
        || what.isIdent(IdentMap.char_ident)
        || what.isIdent(IdentMap.int_ident)
        || what.isIdent(IdentMap.long_ident)
        || what.isIdent(IdentMap.float_ident)
        || what.isIdent(IdentMap.double_ident)
        || what.isIdent(IdentMap.boolean_ident);
  }

  public static boolean isUserDefinedIdentNoKeyword(Token what) {
    return what.ofType(T.TOKEN_IDENT) && !what.isBuiltinIdent();
  }

  public static boolean is_any_modifier(Token what) {
    return what.isIdent(IdentMap.weak_ident) || what.isIdent(IdentMap.var_ident) || what.isIdent(IdentMap.let_ident);
  }

}
