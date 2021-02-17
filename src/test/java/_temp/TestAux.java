package _temp;

import org.junit.Test;

public class TestAux {
  class strtemp {
    int x;
  }

  class main_class {

    /// METHODS: main_class
    void main() {

      // strtemp x1 = new strtemp(1);
      int __t0 = 1;
      strtemp __t1 = new strtemp();
      strtemp_init_0(__t1, __t0);
      strtemp x1 = __t1;

      // int zzz = x1.x;
      strtemp __t2 = x1;
      int __t3 = __t2.x;
      int zzz = __t3;
    }

    /// METHODS: strtemp
    void strtemp_init_0(strtemp _this_, int x) {

      // this.x = x
      strtemp __t4 = _this_;
      int __t5 = __t4.x;
      int __t6 = x;
      __t4.x = __t6;
    }
  }

}
