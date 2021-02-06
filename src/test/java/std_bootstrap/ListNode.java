package std_bootstrap;

public class ListNode<E> {
  E item;
  ListNode<E> next;
  ListNode<E> prev;

  ListNode(ListNode<E> prev, E element, ListNode<E> next) {
    this.item = element;
    this.next = next;
    this.prev = prev;
  }
}