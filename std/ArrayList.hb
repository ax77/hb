
class ArrayList<T> {
  private final builtin.array_declare<T> table;

  void add(T element) {
	  builtin.array_add<T>(table, element);
	}

  int size() {
    return builtin.array_size < T > (table);
  }

  T get(int index) {
	  return builtin.array_get<T>(table, index);
	}

  T set(int index, T element) {
	  T old = get(index);
	  builtin.array_set<T>(table, index, element);
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
    E elem = collection.get(offset);
    offset += 1;
    return elem;
  }

}
