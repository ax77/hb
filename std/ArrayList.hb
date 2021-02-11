
class ArrayList<T> {
  private final std.array_declare<T> table;

  void add(T element) {
	  std.array_add<T>(table, element);
	}

  int size() {
    return std.array_size < T > (table);
  }

  T get(int index) {
	  return std.array_get < T > (table, index);
	}

  T set(int index, T element) {
    final T old = get(index);
	  std.array_set<T>(table, index, element);
	  return old;
	}

  // ArrayListIterator<T> get_iterator() {
  //   return new ArrayListIterator<T>(this);
  // }

}

public class ArrayListIterator<E> {
  final ArrayList<E> collection;
  int offset;

  ArrayListIterator(final ArrayList<E> collection) {
    this.collection = collection;
    this.offset = 0;
  }

  public boolean has_next() {
    return offset != collection.size();
  }

  public E get_next() {
    final E elem = collection.get(offset);
    offset += 1;
    return elem;
  }

}
