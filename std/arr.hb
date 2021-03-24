/// it is much clean in my opinion to use an array
/// instead of any optional type where that optional
/// type is necessary - to initialize self-referenced
/// field for example, and so on... 
/// if you want to use any special marker: [string? var = none]
/// or any special class: [opt<string> var = ""]
/// you should allocate the space in both cases, create
/// the optional class, or make a placeholder for a value,
/// but why not to use an array instead of this mess?
/// the design is pretty simple: you do not need any 'null'
/// replacement, if an array is empty, that means that it is
/// empty, and you have no value, like optional, right? but it
/// is NOT, because with ?-notation or opt<type> notation you 
/// should put the 'null' literal in your language, or how
/// will you check that optional holds something, or how will
/// you check that you can unwrap the value from ?-marked
/// variable: only if you put the 'null' as a keyword in your 
/// language: enum<T> with NONE, nil, or somewhere else...
///
/// BUT: with an empty array the design is REALLY clean.
/// this is a simple habit for any programmer: to check that
/// array is not empty before get something from it, and if
/// it is empty - this is a runtime error, and this error
/// presents in every language: nevertheless, has or has not
/// this language the 'null' semantic, if you want to get
/// the value from an empty array - it is just a programming
/// mistake, and not the memory mistake, or something else,
/// the idea is simple: to use an array<T> like an optional 
/// type if some fields are REALLY may be null (I cannot find
/// many examples: lists, trees, maps, not so many), where 
/// the null is using as a terminator: how you can find where 
/// your list is ends? 
/// if the list has the form: "struct list { struct list* next; };"
/// it is impossible to terminate any struct like that without
/// ampty value, and terminate it with non-empty value is an
/// error, because it may cause false-positive scenario, or
/// the value is pure '0' or it holds the data... sic.
///

native class array<T> 
{
  native array();         /// resizable
  native array(int size); /// fixed
  //native array(T element); /// fixed
  
  native T get(int index);
  native T set(int index, T element);
  
  native int size();
  native boolean is_empty();
  //native boolean contains(T element);
  native boolean is_fixed();
  
  native void add(T element);
}
