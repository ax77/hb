package std_bootstrap;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<E> implements Iterable<E> {
  int size = 0;
  ListNode<E> first;
  ListNode<E> last;
  int modCount = 0;

  LinkedList() {
  }

  private void linkFirst(E e) {
    final ListNode<E> f = first;
    final ListNode<E> newNode = new ListNode<>(null, e, f);
    first = newNode;
    if (f == null) {
      last = newNode;
    } else {
      f.prev = newNode;
    }
    size += 1;
    modCount += 1;
  }

  void linkLast(E e) {
    final ListNode<E> l = last;
    final ListNode<E> newNode = new ListNode<>(l, e, null);
    last = newNode;
    if (l == null) {
      first = newNode;
    } else {
      l.next = newNode;
    }
    size += 1;
    modCount += 1;
  }

  void linkBefore(E e, ListNode<E> succ) {
    // assert succ != null;
    final ListNode<E> pred = succ.prev;
    final ListNode<E> newNode = new ListNode<>(pred, e, succ);
    succ.prev = newNode;
    if (pred == null) {
      first = newNode;
    } else {
      pred.next = newNode;
    }
    size += 1;
    modCount += 1;
  }

  private E unlinkFirst(ListNode<E> f) {
    // assert f == first && f != null;
    final E element = f.item;
    final ListNode<E> next = f.next;
    f.item = null;
    f.next = null; // help GC
    first = next;
    if (next == null) {
      last = null;
    } else {
      next.prev = null;
    }
    size -= 1;
    modCount += 1;
    return element;
  }

  private E unlinkLast(ListNode<E> l) {
    // assert l == last && l != null;
    final E element = l.item;
    final ListNode<E> prev = l.prev;
    l.item = null;
    l.prev = null; // help GC
    last = prev;
    if (prev == null) {
      first = null;
    } else {
      prev.next = null;
    }
    size -= 1;
    modCount += 1;
    return element;
  }

  E unlink(ListNode<E> x) {
    // assert x != null;
    final E element = x.item;
    final ListNode<E> next = x.next;
    final ListNode<E> prev = x.prev;

    if (prev == null) {
      first = next;
    } else {
      prev.next = next;
      x.prev = null;
    }

    if (next == null) {
      last = prev;
    } else {
      next.prev = prev;
      x.next = null;
    }

    x.item = null;
    size -= 1;
    modCount += 1;
    return element;
  }

  public E getFirst() {
    final ListNode<E> f = first;
    if (f == null) {
      throw new NoSuchElementException();
    }
    return f.item;
  }

  public E getLast() {
    final ListNode<E> l = last;
    if (l == null) {
      throw new NoSuchElementException();
    }
    return l.item;
  }

  public E removeFirst() {
    final ListNode<E> f = first;
    if (f == null) {
      throw new NoSuchElementException();
    }
    return unlinkFirst(f);
  }

  public E removeLast() {
    final ListNode<E> l = last;
    if (l == null) {
      throw new NoSuchElementException();
    }
    return unlinkLast(l);
  }

  public void addFirst(E e) {
    linkFirst(e);
  }

  public void addLast(E e) {
    linkLast(e);
  }

  public boolean contains(E o) {
    return indexOf(o) != -1;
  }

  public int size() {
    return size;
  }

  public boolean add(E e) {
    linkLast(e);
    return true;
  }

  public boolean remove(E o) {
    if (o == null) {
      for (ListNode<E> x = first; x != null; x = x.next) {
        if (x.item == null) {
          unlink(x);
          return true;
        }
      }
    } else {
      for (ListNode<E> x = first; x != null; x = x.next) {
        if (o.equals(x.item)) {
          unlink(x);
          return true;
        }
      }
    }
    return false;
  }

  public int indexOf(E o) {
    int index = 0;
    if (o == null) {
      for (ListNode<E> x = first; x != null; x = x.next) {
        if (x.item == null) {
          return index;
        }
        index += 1;
      }
    } else {
      for (ListNode<E> x = first; x != null; x = x.next) {
        if (o.equals(x.item)) {
          return index;
        }
        index += 1;
      }
    }
    return -1;
  }

  public E get(int index) {
    checkElementIndex(index);
    return node(index).item;
  }

  public E set(int index, E element) {
    checkElementIndex(index);
    ListNode<E> x = node(index);
    E oldVal = x.item;
    x.item = element;
    return oldVal;
  }

  ListNode<E> node(int index) {
    // assert isElementIndex(index);

    if (index < (size >> 1)) {
      ListNode<E> x = first;
      for (int i = 0; i < index; i += 1) {
        x = x.next;
      }
      return x;
    } else {
      ListNode<E> x = last;
      for (int i = size - 1; i > index; i -= 1) {
        x = x.prev;
      }
      return x;
    }
  }

  private boolean isElementIndex(int index) {
    return index >= 0 && index < size;
  }

  private boolean isPositionIndex(int index) {
    return index >= 0 && index <= size;
  }

  private String outOfBoundsMsg(int index) {
    return "Index: " + index + ", Size: " + size;
  }

  private void checkElementIndex(int index) {
    if (!isElementIndex(index)) {
      throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
  }

  private void checkPositionIndex(int index) {
    if (!isPositionIndex(index)) {
      throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
  }

  public void add(int index, E element) {
    checkPositionIndex(index);

    if (index == size) {
      linkLast(element);
    } else {
      linkBefore(element, node(index));
    }
  }

  public E remove(int index) {
    checkElementIndex(index);
    return unlink(node(index));
  }

  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public Iterator<E> iterator() {
    return new ListIterator<E>(this);
  }

}
