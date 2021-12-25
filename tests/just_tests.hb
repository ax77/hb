
class node<a,b,c> {
  node<a,b,c> prev;
  node<a,b,c> next;
  a a;
  b b;
  c c;
}

class list<e> {
  e element;
  node<char, int, e> first;
  node<e,e,double> last;
}

class tester {
  node<char,int,long> a;
  list<list<list<list<list<list<int>>>>>> b;
}

