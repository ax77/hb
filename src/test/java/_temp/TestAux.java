package _temp;

import org.junit.Test;

public class TestAux {
  class str {

    private int refCount;
    int x;

    public str() {
      refCount = 1;
    }

    public void ref() {
      refCount += 1;
    }

    public void unref() {
      refCount -= 1;
    }

    public int getRefCount() {
      return refCount;
    }

    public void outOfScope() {
      unref();
    }
  }

  @Test
  public void test() {

    // str x1 = new str(1);
    int __t7 = 1;
    str __t8 = new str();
    str_init_0(__t8, __t7);
    str x1 = null;
    x1 = str_opAssign_6(x1, __t8);

    // str x2 = new str(2);
    int __t9 = 2;
    str __t10 = new str();
    str_init_0(__t10, __t9);
    str x2 = null;
    x2 = str_opAssign_6(x2, __t10);

    // str x3 = x2;
    str __t11 = null;
    __t11 = str_opAssign_6(__t11, x2);
    str x3 = null;
    x3 = str_opAssign_6(x3, __t11);

    __t8 = outOfScope(__t8);
    x1 = outOfScope(x1);
    __t10 = outOfScope(__t10);
    x2 = outOfScope(x2);
    __t11 = outOfScope(__t11);
    x3 = outOfScope(x3);

  }

  /// METHODS: str
  void str_init_0(str _this_, int x) {

    // this.x = x
    str __t12 = null;
    __t12 = str_opAssign_6(__t12, _this_);
    int __t14 = x;
    __t12.x = __t14;

    __t12 = outOfScope(__t12);
  }

  str str_opAssign_6(str lvalue, str rvalue) {
    if (lvalue == null) {
      if (rvalue != null) {
        /// LVALUE:null
        /// RVALUE:object
        rvalue.ref();
        return rvalue;
      } else {
        /// LVALUE:null
        /// RVALUE:null
      }
    } else {
      if (rvalue != null) {
        /// LVALUE:object
        /// RVALUE:object
        if (lvalue != rvalue) {
          lvalue.unref();
          rvalue.ref();
          return rvalue;
        }
      } else {
        /// LVALUE:object
        /// RVALUE:null
        lvalue.unref();
        if (lvalue.getRefCount() != 0) {
          // throw new RuntimeException("expect RC == 0");
        }
        return null;
      }
    }
    throw new RuntimeException();
  }

  public static str ref(final str obj) {
    if (obj != null) {
      obj.ref();
      return obj;
    }
    return null;
  }

  public static str unref(final str obj) {
    if (obj != null) {
      obj.unref();
      if (obj.getRefCount() == 0) {
        return null;
      }
    }
    return obj;
  }

  public static str outOfScope(final str obj) {
    return unref(obj);
  }

}
