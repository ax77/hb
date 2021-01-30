package ast_vars;

import java.util.List;

public class VarArrayInitializer {
  private final List<VarArrayInitializerItem> initializers;

  public VarArrayInitializer(List<VarArrayInitializerItem> initializers) {
    this.initializers = initializers;
  }

  public List<VarArrayInitializerItem> getInitializers() {
    return initializers;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    final int size = initializers.size();
    sb.append("[");
    for (int i = 0; i < size; i++) {
      VarArrayInitializerItem init = initializers.get(i);
      sb.append(init.toString());
      if (i + 1 < size) {
        sb.append(", ");
      }
    }
    sb.append("]");
    return sb.toString();
  }

}
