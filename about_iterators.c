
class node<T> 
{
    var prev: node<T>;
    var item: T;
    var next: node<T>;
    
    init(prev: node<T>, 
         item: T, 
         next: node<T>) 
    {
        self.prev = prev;
        self.next = next;
        self.item = item;
    }
}

class list<T> 
{
    var first: node<T>;
    var last: node<T>;
    var size: int;
    
    func push_back(e: T)
    {
        var l: node<T> = self.last;
        var n: node<T> = new node<T>(
            prev: l, 
            item: e, 
            next: null
        );
        self.last = n;
        if (l == null) {
            self.first = n;
        } else {
            l.next = n;
        }
        self.size += 1;
    }
    
    func get_iterator() -> list_iterator<T> 
    {
        return new list_iterator<T>(
            collection: self
        );
    }
}

class list_iterator<T> {
    
    var collection: list<T>;
    var last_returned: node<T>;
    var next: node<T>;
    var next_index: int;

    init(collection: list<T>) 
    {
        self.collection = collection;
        self.last_returned = collection.first;
        self.next = collection.first;
        self.next_index = 0;
    }
    
    func current() -> T 
    {
        return last_returned.item;
    }
    
    func has_next() -> boolean 
    {
        return next_index < collection.size;
    }
    
    func get_next() -> T 
    {
        self.last_returned = next;
        self.next = next.next;
        self.next_index += 1;
        return last_returned.item;
    } 
}

class test 
{
    var list: list<int>;
}

/// we 100% sure that we can iterate over something if:
/// 0) for item in 'collection' -> collection should be a class or an array
/// 1) it has a method with a name 'get_iterator'
/// 2) this method has no parameters
/// 3) this method should return class-type
/// 4) the returned class type should have three methods:
///    func current() -> (return the type of element in colection)
///    func has_next() -> (return boolean)
///    func get_next() -> (return the type of element in colection)
/// 5) these methods shouldn't have parameters
/// 
/// examples:
///
/// I)
/// for c in "str" {
///    we know here that we have an iterator for string-type
///    we found that class, and we totally know that at each
///    iteration it should return the single 'char'
///    that how it works
/// }
///
/// II)
/// var list: list<int>;
/// for item in list {
///   and here we know that we have a class like: list_iterator
///   and it was expanded as a template for this particularly variable
///   and at every iteration it should return the 'int'
///   so: item must be an 'int' :)
/// }


int main() {
    
    let x: list<int>;
    
    for s in x {
        
    }
    
    let iter: list_iterator<int> = x.get_iterator();
    for(int s = iter.current(); iter.has_next(); s = iter.get_next()) {
        
    }
    
    return 0;
}




















