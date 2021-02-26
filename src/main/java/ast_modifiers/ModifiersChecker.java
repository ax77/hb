package ast_modifiers;

import java.util.ArrayList;
import java.util.List;

import ast_symtab.Keywords;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

//@formatter:off
public abstract class ModifiersChecker {

  public static Modifiers varMods() {
    return new Modifiers();
  }

  public static Modifiers letMods() {
    List<Ident> res = new ArrayList<>();
    res.add(Keywords.final_ident);
    return new Modifiers(res);
  }

  public static boolean isAnyModifier(Token what) {
    return what.ofType(T.TOKEN_IDENT) && isAnyModifierId(what.getIdent());
  }
  
  public static boolean isAnyModifierId(Ident what) {
    return 
       what.equals(Keywords.private_ident  )
    || what.equals(Keywords.public_ident   )
    || what.equals(Keywords.native_ident   )
    || what.equals(Keywords.static_ident   )
    || what.equals(Keywords.final_ident    )
    || what.equals(Keywords.abstract_ident )
    || what.equals(Keywords.mut_ident      );
  }


  private static boolean isMethodModifierId(Ident what) {
    return 
        what.equals(Keywords.private_ident  )
     || what.equals(Keywords.public_ident   )
     || what.equals(Keywords.native_ident   )
     || what.equals(Keywords.static_ident   );
  }

  private static boolean isMethodParameterModifierId(Ident what) {
    return what.equals(Keywords.final_ident);
  }

  private static boolean isClassFieldModifierId(Ident what) {
    return 
        what.equals(Keywords.private_ident  )
     || what.equals(Keywords.public_ident   )
     || what.equals(Keywords.static_ident   )
     || what.equals(Keywords.final_ident    )
     || what.equals(Keywords.mut_ident      );
  }

  private static boolean isConstructorModifierId(Ident what) {
    return 
        what.equals(Keywords.private_ident) 
     || what.equals(Keywords.public_ident);
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
