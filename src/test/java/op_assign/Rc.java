package op_assign;

public abstract class Rc {

  public static MyRefCountedClass opAssign(final MyRefCountedClass lvalue, final MyRefCountedClass rvalue) {
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

  public static MyRefCountedClass ref(final MyRefCountedClass obj) {
    if (obj != null) {
      obj.ref();
      return obj;
    }
    return null;
  }

  public static MyRefCountedClass unref(final MyRefCountedClass obj) {
    if (obj != null) {
      obj.unref();
      if (obj.getRefCount() == 0) {
        return null;
      }
    }
    return null;
  }

  public static MyRefCountedClass deinit(final MyRefCountedClass obj) {
    return unref(obj);
  }

}
