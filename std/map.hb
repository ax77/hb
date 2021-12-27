
class entry<K,V> {
   K key;
   V val;
   opt<entry<K,V>> next;
   
   entry(K key, V val, opt<entry<K,V>> next) {
     this.key = key;
     this.val = val;
     this.next = next;
   }
}

class map<K, V> {
  array<entry<K,V>> table;
  int size;
  
  int capacity;
  int threshold;
  int loadFactor; // TODO:float:0.75
  
  map() {
    this.size = 0;
    this.capacity = 11;
    this.loadFactor = 2; // TODO:float:0.75
    this.threshold = this.capacity * this.loadFactor;
    this.table = new array<entry<K,V>>(this.capacity);
  }
  
  int index(K key, int for_capacity) {
    return hash(key) % for_capacity;
  }
}

class main_class {
  int main() {
    map<string, char> m = new map<string, char>();
    return 0;
  }
}

