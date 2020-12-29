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

public abstract class IsIdent {

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


  public static boolean isTypeSpec(Token what) {
    return what.isIdent(IdentMap.void_ident)
        || what.isIdent(IdentMap.char_ident)
        || what.isIdent(IdentMap.short_ident)
        || what.isIdent(IdentMap.int_ident)
        || what.isIdent(IdentMap.long_ident)
        || what.isIdent(IdentMap.float_ident)
        || what.isIdent(IdentMap.double_ident);
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

  public static boolean is_class_modifier(Token what) {
    return what.isIdent(IdentMap.public_ident) || what.isIdent(IdentMap.abstract_ident)
        || what.isIdent(IdentMap.final_ident);
  }

  public static boolean is_field_modifier(Token what) {
    return what.isIdent(IdentMap.public_ident) 
        || what.isIdent(IdentMap.protected_ident)
        || what.isIdent(IdentMap.private_ident) 
        || what.isIdent(IdentMap.static_ident)
        || what.isIdent(IdentMap.final_ident) 
        || what.isIdent(IdentMap.transient_ident)
        || what.isIdent(IdentMap.volatile_ident);
  }

  public static boolean is_method_modifier(Token what) {
    return what.isIdent(IdentMap.public_ident) 
        || what.isIdent(IdentMap.protected_ident)
        || what.isIdent(IdentMap.private_ident) 
        || what.isIdent(IdentMap.static_ident)
        || what.isIdent(IdentMap.abstract_ident) 
        || what.isIdent(IdentMap.final_ident)
        || what.isIdent(IdentMap.synchronized_ident) 
        || what.isIdent(IdentMap.native_ident);
  }

  public static boolean is_constructor_modifier(Token what) {
    return what.isIdent(IdentMap.public_ident) 
        || what.isIdent(IdentMap.protected_ident)
        || what.isIdent(IdentMap.private_ident);
  }

  public static boolean is_interface_modifier(Token what) {
    return what.isIdent(IdentMap.public_ident) 
        || what.isIdent(IdentMap.abstract_ident);
  }

  public static boolean is_abstract_method_modifier(Token what) {
    return what.isIdent(IdentMap.public_ident) 
        || what.isIdent(IdentMap.abstract_ident);
  }

  public static boolean is_constant_modifier(Token what) {
    return what.isIdent(IdentMap.public_ident) 
        || what.isIdent(IdentMap.static_ident)
        || what.isIdent(IdentMap.final_ident);
  }

  public static boolean is_any_modifier(Token what) {
    return what.isIdent(IdentMap.public_ident) 
        || what.isIdent(IdentMap.abstract_ident)
        || what.isIdent(IdentMap.final_ident) 
        || what.isIdent(IdentMap.protected_ident)
        || what.isIdent(IdentMap.private_ident) 
        || what.isIdent(IdentMap.static_ident)
        || what.isIdent(IdentMap.transient_ident) 
        || what.isIdent(IdentMap.volatile_ident)
        || what.isIdent(IdentMap.synchronized_ident) 
        || what.isIdent(IdentMap.native_ident);
  }
  
//  // TODO: more clean and fast.
//  public static boolean isKeyword(Token what) {
//    return what.isIdent(IdentMap.abstract_ident            )
//        || what.isIdent(IdentMap.boolean_ident             )
//        || what.isIdent(IdentMap.break_ident               )
//        || what.isIdent(IdentMap.byte_ident                )
//        || what.isIdent(IdentMap.case_ident                )
//        || what.isIdent(IdentMap.catch_ident               )
//        || what.isIdent(IdentMap.char_ident                )
//        || what.isIdent(IdentMap.class_ident               )
//        || what.isIdent(IdentMap.const_ident               )
//        || what.isIdent(IdentMap.continue_ident            )
//        || what.isIdent(IdentMap.default_ident             )
//        || what.isIdent(IdentMap.do_ident                  )
//        || what.isIdent(IdentMap.double_ident              )
//        || what.isIdent(IdentMap.else_ident                )
//        || what.isIdent(IdentMap.enum_ident                )
//        || what.isIdent(IdentMap.extends_ident             )
//        || what.isIdent(IdentMap.final_ident               )
//        || what.isIdent(IdentMap.finally_ident             )
//        || what.isIdent(IdentMap.float_ident               )
//        || what.isIdent(IdentMap.for_ident                 )
//        || what.isIdent(IdentMap.goto_ident                )
//        || what.isIdent(IdentMap.if_ident                  )
//        || what.isIdent(IdentMap.implements_ident          )
//        || what.isIdent(IdentMap.import_ident              )
//        || what.isIdent(IdentMap.instanceof_ident          )
//        || what.isIdent(IdentMap.int_ident                 )
//        || what.isIdent(IdentMap.interface_ident           )
//        || what.isIdent(IdentMap.long_ident                )
//        || what.isIdent(IdentMap.native_ident              )
//        || what.isIdent(IdentMap.new_ident                 )
//        || what.isIdent(IdentMap.package_ident             )
//        || what.isIdent(IdentMap.private_ident             )
//        || what.isIdent(IdentMap.protected_ident           )
//        || what.isIdent(IdentMap.public_ident              )
//        || what.isIdent(IdentMap.return_ident              )
//        || what.isIdent(IdentMap.short_ident               )
//        || what.isIdent(IdentMap.static_ident              )
//        || what.isIdent(IdentMap.super_ident               )
//        || what.isIdent(IdentMap.switch_ident              )
//        || what.isIdent(IdentMap.synchronized_ident        )
//        || what.isIdent(IdentMap.this_ident                )
//        || what.isIdent(IdentMap.throw_ident               )
//        || what.isIdent(IdentMap.throws_ident              )
//        || what.isIdent(IdentMap.transient_ident           )
//        || what.isIdent(IdentMap.try_ident                 )
//        || what.isIdent(IdentMap.void_ident                )
//        || what.isIdent(IdentMap.volatile_ident            )
//        || what.isIdent(IdentMap.while_ident               );
//  }
  
  //@formatter:on

}
