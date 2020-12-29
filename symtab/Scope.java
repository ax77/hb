package njast.symtab;

import java.util.HashMap;
import java.util.Map.Entry;

/// Represent one scope
/// file or block
public class Scope<K, V> {
  private final HashMap<K, V> scope;

  private final ScopeLevels level;
  private final String name; // for debug

  public Scope(ScopeLevels level, String name) {
    this.scope = new HashMap<K, V>();
    this.level = level;
    this.name = name;
  }

  public void put(K key, V value) {
    scope.put(key, value);
  }

  public V get(K key) {
    return scope.get(key);
  }

  public HashMap<K, V> getScope() {
    return scope;
  }

  public boolean isEmpty() {
    return scope.isEmpty();
  }

  public void dump() {
    for (Entry<K, V> e : scope.entrySet()) {
      System.out.println(name + ": " + e.getKey().toString() + " " + e.getValue().toString());
      System.out.println();
    }

  }

  public ScopeLevels getLevel() {
    return level;
  }

  public String getName() {
    return name;
  }

}
