package ast_modifiers;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

  public Modifiers(List<Ident> mods) {
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
    if (!ModifiersChecker.isAnyModifierId(mod)) {
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

  public boolean isPrivate() {
    return modifiers.contains(Keywords.private_ident);
  }

  public boolean isPublic() {
    return modifiers.contains(Keywords.public_ident);
  }

  public boolean isNative() {
    return modifiers.contains(Keywords.native_ident);
  }

  public boolean isFinal() {
    return modifiers.contains(Keywords.final_ident);
  }

  public boolean isStatic() {
    return modifiers.contains(Keywords.static_ident);
  }

  public boolean isNativeOnly() {
    return modifiers.size() == 1 && modifiers.contains(Keywords.native_ident);
  }

}
