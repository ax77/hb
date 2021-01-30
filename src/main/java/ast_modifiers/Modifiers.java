package ast_modifiers;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import ast_checkers.IdentRecognizer;
import ast_symtab.Keywords;
import errors.AstParseException;
import tokenize.Ident;
import tokenize.Token;

public class Modifiers implements Serializable {
  private static final long serialVersionUID = -1005349552132021584L;

  private Set<Ident> modifiers;

  public Modifiers() {
    this.modifiers = new LinkedHashSet<>();
  }

  public Modifiers(Ident[] mods) {
    this.modifiers = new LinkedHashSet<>();
    for (Ident mod : mods) {
      modifiers.add(mod);
    }
  }

  public void put(Token tok) {
    final String value = tok.getValue();
    final Ident ident = tok.getIdent();
    if (!IdentRecognizer.isAnyModifier(tok)) {
      throw new AstParseException("not a modifier: " + value);
    }
    if (modifiers.contains(ident)) {
      throw new AstParseException("duplicate modifier: " + value);
    }
    modifiers.add(ident);
  }

  public Set<Ident> getModifiers() {
    return modifiers;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Ident id : modifiers) {
      sb.append(id.getName());
      sb.append(" ");
    }
    return sb.toString().trim();
  }
  
  //@formatter:off
  public boolean isWeak()    { return modifiers.contains(Keywords.weak_ident); }
  public boolean isVar()     { return modifiers.contains(Keywords.var_ident); }
  public boolean isLet()     { return modifiers.contains(Keywords.let_ident); }
  public boolean isPrivate() { return modifiers.contains(Keywords.private_ident); }
  public boolean isPublic()  { return modifiers.contains(Keywords.public_ident); }
  public boolean isNative()  { return modifiers.contains(Keywords.native_ident); }
  public boolean isStatic()  { return modifiers.contains(Keywords.static_ident); }
  //@formatter:on

}
