package ast_checkers;

import ast_symtab.Keywords;
import tokenize.Token;

public abstract class IdentRecognizer {

  //@formatter:off
  public static boolean isAnyModifier(Token what) {
    return what.isIdent(Keywords.weak_ident) 
        || what.isIdent(Keywords.var_ident) 
        || what.isIdent(Keywords.let_ident)
        || what.isIdent(Keywords.private_ident)
        || what.isIdent(Keywords.public_ident)
        || what.isIdent(Keywords.native_ident)
        || what.isIdent(Keywords.static_ident)
        ;
  }
  
  public static boolean isMethodModifier(Token what) {
    return what.isIdent(Keywords.private_ident)
        || what.isIdent(Keywords.public_ident)
        || what.isIdent(Keywords.native_ident)
        || what.isIdent(Keywords.static_ident)
        ;
  }
  
  public static boolean isMethodParameterModifier(Token what) {
    return what.isIdent(Keywords.var_ident) 
        || what.isIdent(Keywords.let_ident)
        ;
  }
  
  public static boolean isClassFieldModifier(Token what) {
    return what.isIdent(Keywords.weak_ident) 
        || what.isIdent(Keywords.var_ident) 
        || what.isIdent(Keywords.let_ident)
        || what.isIdent(Keywords.private_ident)
        || what.isIdent(Keywords.public_ident)
        || what.isIdent(Keywords.static_ident)
        ;
  }
  
  public static boolean isConstructorModifier(Token what) {
    return what.isIdent(Keywords.private_ident)
        || what.isIdent(Keywords.public_ident)
        ;
  }

}
