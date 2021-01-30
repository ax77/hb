package ast_st2_annotate;

import ast_modifiers.Modifiers;
import ast_symtab.Keywords;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public abstract class Mods {

  public static Modifiers varMods() {
    Ident[] mods = { Keywords.var_ident };
    return new Modifiers(mods);
  }

  public static Modifiers letMods() {
    Ident[] mods = { Keywords.let_ident };
    return new Modifiers(mods);
  }

  //@formatter:off
  
  public static boolean isAnyModifier(Token what) {
    return what.ofType(T.TOKEN_IDENT) && isAnyModifier(what.getIdent());
  }
  
  public static boolean isAnyModifier(Ident what) {
    return what.equals(Keywords.weak_ident) 
        || what.equals(Keywords.var_ident) 
        || what.equals(Keywords.let_ident)
        || what.equals(Keywords.private_ident)
        || what.equals(Keywords.public_ident)
        || what.equals(Keywords.native_ident)
        || what.equals(Keywords.static_ident)
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

  //@formatter:on

}
