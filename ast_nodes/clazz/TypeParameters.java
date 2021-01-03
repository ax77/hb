package njast.ast_nodes.clazz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jscan.symtab.Ident;
import njast.errors.EParseException;

public class TypeParameters implements Serializable {
  private static final long serialVersionUID = -3124209513520909512L;

  //  TypeParameters:
  //    < TypeParameter { , TypeParameter } >
  //    TypeParameter:
  //    Identifier

  private List<Ident> typeParameters;

  public TypeParameters() {
    this.typeParameters = new ArrayList<Ident>(0);
  }

  public void put(Ident tp) {
    if (typeParameters.contains(tp)) {
      throw new EParseException("duplicate type parameter: " + tp.getName());
    }
    this.typeParameters.add(tp);
  }

  public boolean contains(Ident what) {
    return typeParameters.contains(what);
  }

  public boolean isEmpty() {
    return typeParameters.isEmpty();
  }

  public List<Ident> getTypeParameters() {
    return typeParameters;
  }

}
