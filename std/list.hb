
class node<E> {
  E item;
  node<E> next;
  node<E> prev;

  node(node<E> prev, E element, node<E> next) {
      this.item = element;
      this.next = next;
      this.prev = prev;
  }
}

class list<E> {
  int size;
  node<E> first;
  node<E> last;
  
  list() {
    this.size = 0;
    this.first = default(node<E>);
    this.last = default(node<E>);
  }
  
  void push_front(E e) {
    node<E> f = this.first;
    node<E> newnode = new node<E>(default(node<E>), e, f);
    this.first = newnode;
    if(f == default(node<E>)) {
      this.last = newnode;
    } else {
      f.prev = newnode; 
    }
    size += 1;
  }
  
  E pop_front() {
    node<E> f = this.first;
    E element = f.item;
    node<E> next = f.next; 
    this.first = next;
    if(next == default(node<E>)) {
      this.last = default(node<E>);
    } else {
      next.prev = default(node<E>); 
    }
    this.size -= 1;
    return element;
  }
  
  boolean is_empty() {
    return size == 0;
  }
  
  int size() {
    return this.size;
  }

}

