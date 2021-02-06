package std_bootstrap;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListIterator<E> implements Iterator<E> {

  private LinkedList<E> collection;
  private ListNode<E> lastReturned;
  private ListNode<E> next;
  private int nextIndex;

  ListIterator(LinkedList<E> collection) {
    // assert isPositionIndex(index);
    this.collection = collection;
    this.next = collection.first;
    nextIndex = 0;
  }

  public boolean hasNext() {
    return nextIndex < collection.size();
  }

  public E next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }

    lastReturned = next;
    next = next.next;
    nextIndex++;
    return lastReturned.item;
  }

}