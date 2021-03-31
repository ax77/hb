package _st2_annotate;

import ast_symtab.Keywords;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public abstract class ExprUtil {

  //@formatter:off
  public static boolean isBuiltinTypeTraitsIdent(Ident name) {
      return name.equals(Keywords.is_void_ident)    
          || name.equals(Keywords.is_boolean_ident)  
          || name.equals(Keywords.is_char_ident)      
          || name.equals(Keywords.is_short_ident)     
          || name.equals(Keywords.is_int_ident)       
          || name.equals(Keywords.is_long_ident)     
          || name.equals(Keywords.is_float_ident)     
          || name.equals(Keywords.is_double_ident)    
          || name.equals(Keywords.is_integral_ident)  
          || name.equals(Keywords.is_floating_ident)  
          || name.equals(Keywords.is_class_ident)    
          || name.equals(Keywords.is_primitive_ident) 
          || name.equals(Keywords.is_arithmetic_ident)
          || name.equals(Keywords.is_reference_ident) 
      ;
  }
  
  public static boolean isBuiltinTypeTraitsIdent(Token tok) {
    return tok.ofType(T.TOKEN_IDENT) && isBuiltinTypeTraitsIdent(tok.getIdent());
  }

}
