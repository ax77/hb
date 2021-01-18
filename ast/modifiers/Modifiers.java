package njast.ast.modifiers;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import jscan.symtab.Ident;
import jscan.tokenize.Token;
import njast.ast.checkers.IdentRecognizer;
import njast.parse.AstParseException;

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
    if (!IdentRecognizer.is_any_modifier(tok)) {
      throw new AstParseException("not modifier: " + tok.getValue());
    }
    modifiers.add(tok.getIdent());
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

}
