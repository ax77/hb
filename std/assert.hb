static class assert 
{
  static void is_true(boolean condition) {
    native_assert_true(condition);
  }
  
  static void panic(string because) {
    native_panic(because);
  }

  native void native_panic(string because);
  native void native_assert_true(boolean condition);
}

