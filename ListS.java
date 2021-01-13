package njast;

public class ListS {

  class Node<E> {
    E item;
    Node<E> next;
    Node<E> prev;

    Node(Node<E> prev, E element, Node<E> next) {
      this.item = element;
      this.next = next;
      this.prev = prev;
    }
  }

  class LinkedList<E> {
    int size;
    Node<E> first;
    Node<E> last;

    void pushBack(E e) {
      final Node<E> l = last;
      final Node<E> newNode = new Node<E>(l, e, null);
      last = newNode;
      if (l == null) {
        first = newNode;
      } else {
        l.next = newNode;
      }
      size += 1;
    }
  }

  class C {
    public void testList() {
      LinkedList<String> table = new LinkedList<String>();
    }
  }

}
