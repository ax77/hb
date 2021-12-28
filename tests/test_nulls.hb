
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
    this.first = null;
    this.last = null;
  }
  
  void push_front(E e) {
    node<E> f = this.first;
    node<E> newnode = new node<E>(null, e, f);
    this.first = newnode;
    if(f == null) {
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
    if(next == null) {
      this.last = null;
    } else {
      next.prev = null; 
    }
    this.size -= 1;
    return element;
  }
  
  boolean is_empty() {
    return size == 0;
  }

}

class main_class {
  int main() {
    
    list<int> list = new list<int>();
    for(int i=0; i<32; i+=1) {
      list.push_front(i);
    }
    while(!list.is_empty()) {
      fmt.print(list.pop_front());
    }
    
    return 0;
  }
}
