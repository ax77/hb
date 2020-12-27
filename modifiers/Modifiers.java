package njast.modifiers;

import java.util.LinkedHashSet;
import java.util.Set;

import jscan.symtab.Ident;
import jscan.tokenize.Token;
import njast.ast_checkers.IsIdent;
import njast.errors.EParseException;
import njast.symtab.IdentMap;

public class Modifiers {
  private Set<Ident> modifiers;

  public Modifiers() {
    this.modifiers = new LinkedHashSet<Ident>();
  }

  public void put(Token tok) {
    if (!IsIdent.is_any_modifier(tok)) {
      throw new EParseException("not modifier: " + tok.getValue());
    }
    modifiers.add(tok.getIdent());
  }

  public Set<Ident> getModifiers() {
    return modifiers;
  }

  //@formatter:off
  public boolean hasPublic() { return modifiers.contains(IdentMap.public_ident); }
  public boolean hasAbstract() { return modifiers.contains(IdentMap.abstract_ident); }
  public boolean hasFinal() { return modifiers.contains(IdentMap.final_ident); }
  public boolean hasProtected() { return modifiers.contains(IdentMap.protected_ident); }
  public boolean hasPrivate() { return modifiers.contains(IdentMap.private_ident); }
  public boolean hasStatic() { return modifiers.contains(IdentMap.static_ident); }
  public boolean hasTransient() { return modifiers.contains(IdentMap.transient_ident); }
  public boolean hasVolatile() { return modifiers.contains(IdentMap.volatile_ident); }
  public boolean hasSynchronized() { return modifiers.contains(IdentMap.synchronized_ident); }
  public boolean hasNative() { return modifiers.contains(IdentMap.native_ident); }
  //@formatter:on

}
