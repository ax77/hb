
class list_node<E> {

  E item;
  list_node<E> next;
  list_node<E> prev;

  public list_node(list_node<E> prev, E element, list_node<E> next) {
    this.item = element;
    this.next = next;
    this.prev = prev;
  }
}

class list<E> {

  private int size;
  private list_node<E> first;
  private list_node<E> last;

  public list() {
    this.size = 0;
    this.first = std.zero<list_node<E>>();
    this.last = std.zero<list_node<E>>();
  }

  public list_node<E> first_node() {
    return first;
  }

  public list_node<E> last_node() {
    return last;
  }

  public void push_front(E e) {
    final list_node<E> f = first;
    final list_node<E> new_node = new list_node<E>(std.zero<list_node<E>>(), e, f);
    first = new_node;
    if (f == std.zero<list_node<E>>()) {
      last = new_node;
    } else {
      f.prev = new_node;
    }
    size += 1;
  }

  public void push_back(E e) {
    final list_node<E> l = last;
    final list_node<E> new_node = new list_node<E>(l, e, std.zero<list_node<E>>());
    last = new_node;
    if (l == std.zero<list_node<E>>()) {
      first = new_node;
    } else {
      l.next = new_node;
    }
    size += 1;
  }

  public E pop_front(list_node<E> f) {
    // assert f == first && f != null;
    final E element = f.item;
    final list_node<E> next = f.next;
    //f.item = null;
    //f.next = null; // help GC
    first = next;
    if (next == std.zero<list_node<E>>()) {
      last = std.zero<list_node<E>>();
    } else {
      next.prev = std.zero<list_node<E>>();
    }
    size -= 1;
    return element;
  }

  public E pop_back(list_node<E> l) {
    // assert l == last && l != null;
    final E element = l.item;
    final list_node<E> prev = l.prev;
    //l.item = null;
    //l.prev = null; // help GC
    last = prev;
    if (prev == std.zero<list_node<E>>()) {
      first = std.zero<list_node<E>>();
    } else {
      prev.next = std.zero<list_node<E>>();
    }
    size -= 1;
    return element;
  }

  public int size() {
    return size;
  }

}

class list_iter<E> {

  private list<E> collection;
  private list_node<E> last_returned;
  private list_node<E> next;
  private int next_index;

  public list_iter(list<E> collection) {
    // assert isPositionIndex(index);
    this.collection = collection;
    this.last_returned = std.zero<list_node<E>>();
    this.next = collection.first_node();
    this.next_index = 0;
  }

  public boolean has_next() {
    return next_index < collection.size();
  }

  public E get_next() {
    if (!has_next()) {
      //throw new AstParseException();
    }

    last_returned = next;
    next = next.next;
    next_index += 1;
    return last_returned.item;
  }

}

