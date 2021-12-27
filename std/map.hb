
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
    map<string, char> m1 = new map<string, char>();
    map<int, char> m2 = new map<int, char>();
    map<array<char>, char> m3 = new map<array<char>, char>();
    map<opt<int>, long> m4 = new map<opt<int>, long>();
    return 0;
  }
}

