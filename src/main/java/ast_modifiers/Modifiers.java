package ast_modifiers;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import ast_st2_annotate.Mods;
import ast_symtab.Keywords;
import errors.AstParseException;
import tokenize.Ident;
import tokenize.T;
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
      check(mod);
      modifiers.add(mod);
    }
  }

  public void put(Token tok) {
    if (!tok.ofType(T.TOKEN_IDENT)) {
      throw new AstParseException("aux-error. not a modifier: " + tok.getValue());
    }
    final Ident mod = tok.getIdent();
    check(mod);
    modifiers.add(mod);
  }

  private void check(Ident mod) {
    final String name = mod.getName();
    if (!Mods.isAnyModifierId(mod)) {
      throw new AstParseException("not a modifier: " + name);
    }
    if (contains(mod)) {
      throw new AstParseException("duplicate modifier: " + name);
    }
  }

  public boolean contains(Ident what) {
    return modifiers.contains(what);
  }

  public boolean isEmpty() {
    return modifiers.isEmpty();
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
