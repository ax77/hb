package jscan.tokenize;

import java.io.Serializable;

public final class Ident implements Serializable {
  private static final long serialVersionUID = 7844228085581642714L;

  private final String name;
  private int ns;

  public Ident(String _name) {
    name = _name;
    ns = 0;
  }

  public Ident(String _name, int _ns) {
    name = _name;
    ns = _ns;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }

  // XXX:XXX: do __NOT__ compare ident by namespace !!! 
  //

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Ident other = (Ident) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

  public int getNs() {
    return ns;
  }

  public void setNs(int ns) {
    this.ns = ns;
  }

  public boolean isBuiltin() {
    return ns != 0;
  }

}
