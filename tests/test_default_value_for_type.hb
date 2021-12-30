
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

class main_class {
  int main() {
    // list<int> list = new list<int>();
    // for(int i=0; i<32; i+=1) {
    //   list.push_front(i);
    // }
    // for(node<int> iter = list.first; iter != default(node<int>); iter = iter.next) {
    //   fmt.print(iter.item);
    // }
    
    // array<string> arr = new array<string>();
    // arr.add("1");
    // arr.add("2");
    // fmt.print(arr.get(0));
    // fmt.print(arr.get(1));
    
    string path = "/usr/include/";
    string lf = path.left(5);
    fmt.print(lf);
    
    return lf.length();
  }
}
























