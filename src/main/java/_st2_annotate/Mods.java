package _st2_annotate;

import ast_modifiers.Modifiers;
import ast_symtab.Keywords;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public abstract class Mods {

  public static Modifiers varMods() {
    return new Modifiers();
  }

  public static Modifiers letMods() {
    Ident[] mods = { Keywords.final_ident };
    return new Modifiers(mods);
  }

  //@formatter:off
  
  public static final Ident ALL_MODS[] = { 
      Keywords.private_ident,
      Keywords.public_ident, 
      Keywords.native_ident, 
      Keywords.static_ident, 
      Keywords.final_ident,
      Keywords.abstract_ident,
      Keywords.mut_ident,
  };
  
  public static final Ident METHOD_MODS[] = {
      Keywords.private_ident, 
      Keywords.public_ident,  
      Keywords.native_ident,  
      Keywords.static_ident,  
  };
  
  public static final Ident FIELD_MODS[] = { 
      Keywords.private_ident,
      Keywords.public_ident, 
      Keywords.static_ident, 
      Keywords.final_ident,
  };
  
  private static boolean in(Ident what, Ident[] where) {
    for(Ident id: where) {
      if(what.equals(id)) {
        return true;
      }
    }
    return false;
  }
  
  public static boolean isAnyModifier(Token what) {
    return what.ofType(T.TOKEN_IDENT) && isAnyModifierId(what.getIdent());
  }
  
  public static boolean isAnyModifierId(Ident what) {
    return in(what, ALL_MODS);
  }

  //@formatter:on

  private static boolean isMethodModifierId(Ident what) {
    return in(what, METHOD_MODS);
  }

  // TODO:
  private static boolean isMethodParameterModifierId(Ident what) {
    return what.equals(Keywords.final_ident);
  }

  private static boolean isClassFieldModifierId(Ident what) {
    return in(what, FIELD_MODS);
  }

  private static boolean isConstructorModifierId(Ident what) {
    return what.equals(Keywords.private_ident) || what.equals(Keywords.public_ident);
  }

  public static boolean isCorrectMethodMods(Modifiers mods) {
    for (Ident mod : mods.getModifiers()) {
      if (!isMethodModifierId(mod)) {
        return false;
      }
    }
    return true;
  }

  public static boolean isCorrectFieldMods(Modifiers mods) {
    for (Ident mod : mods.getModifiers()) {
      if (!isClassFieldModifierId(mod)) {
        return false;
      }
    }
    return true;
  }

  public static boolean isCorrectConstructorMods(Modifiers mods) {
    for (Ident mod : mods.getModifiers()) {
      if (!isConstructorModifierId(mod)) {
        return false;
      }
    }
    return true;
  }

}
