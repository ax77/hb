import std.natives.opt::opt;
import std.natives.fmt::fmt;
import std.natives.string::string;

/// using an optional type instead of null looks horrible!

class node<E> {
    E item;
    opt<node<E>> next;
    opt<node<E>> prev;

    node(opt<node<E>> prev, E element, opt<node<E>> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }
}

class list<E> {
  int size;
  opt<node<E>> first;
  opt<node<E>> last;
  
  list() {
    this.size = 0;
    this.first = emptynode();
    this.last = emptynode();
  }
  
  opt<node<E>> emptynode() {
    return new opt<node<E>>();
  }
  
  void push_front(E e) {
    opt<node<E>> f = this.first;
    node<E> newnodeentry = new node<E>(emptynode(), e, f);
    opt<node<E>> newnode = new opt<node<E>>(newnodeentry);
    this.first = newnode;
    if(f.is_none()) {
      this.last = newnode;
    } else {
      f.get().prev = newnode; //NULL_COALESCING_HERE
    }
    size += 1;
  }
  
  E pop_front() {
    opt<node<E>> f = this.first;
    E element = f.get().item; //NULL_COALESCING_HERE
    opt<node<E>> next = f.get().next; //NULL_COALESCING_HERE
    this.first = next;
    if(next.is_none()) {
      this.last = emptynode();
    } else {
      next.get().prev = emptynode(); //NULL_COALESCING_HERE
    }
    this.size -= 1;
    return element;
  }
  
}

class dummy {
  int f;
  dummy() { f = 32; }
}

static class testopts {
  test "test optional types using a class" {
    list<int> root = new list<int>();
    root.push_front(1);
    root.push_front(2);
    root.push_front(3);
    
    assert_true(3 == root.pop_front());
    assert_true(2 == root.pop_front());
    assert_true(1 == root.pop_front());
  }
}


class main_class {
  int main() {
    return 0;
  }
}
































































