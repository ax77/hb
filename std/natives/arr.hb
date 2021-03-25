native class array<T> 
{
  native array();         
  native array(int size); 
  
  native void add(T element);
  native T get(int index);
  native T set(int index, T element);
  
  native int size();
  native boolean is_empty();
  
  test "get" {
    array<char> arr = new array<char>();
    arr.add('1');
    arr.add('2');
    arr.add('3');
    
    assert_true(arr.get(0) == '1');
    assert_true(arr.size() == 3);
  }
}
