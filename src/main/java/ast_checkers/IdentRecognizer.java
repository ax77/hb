package ast_checkers;

import ast_symtab.Keywords;
import tokenize.Token;

public abstract class IdentRecognizer {

  public static boolean is_any_modifier(Token what) {
    return what.isIdent(Keywords.weak_ident) || what.isIdent(Keywords.var_ident) || what.isIdent(Keywords.let_ident);
  }

}
