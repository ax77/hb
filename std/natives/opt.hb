native class opt<T> {
  native opt();
  native opt(T value);
  native boolean is_some();
  native boolean is_none();
  native T get();
}
