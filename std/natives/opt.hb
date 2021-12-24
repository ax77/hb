
/// We do not have 'null' literal.
/// So - we should use optional type
/// if sometimes we need nullable refs.

native class opt<T> {
  native opt();
  native opt(T value);
  native boolean is_some();
  native boolean is_none();
  native T get();
  native boolean equals(opt<T> another);
}
