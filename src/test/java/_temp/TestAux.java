package _temp;

import org.junit.Test;

public class TestAux {

  class test {
  }

  class token {
    type type;
  }

  class type {
    strtemp value;
  }

  class strtemp {
    int x;
  }

  void token_init_2(token _this_, type type) {
    // this.type = type

    token t3 = _this_;
    type t4 = t3.type;
    type t5 = type;
    t3.type = t5;
  }

  void type_init_1(type _this_, strtemp value) {
    // this.value = value

    type t7 = _this_;
    strtemp t8 = t7.value;
    strtemp t9 = value;
    t7.value = t9;
  }

  void strtemp_init_0(strtemp _this_, int x) {
    // this.x = x

    strtemp t11 = _this_;
    int t12 = t11.x;
    int t13 = x;
    t11.x = t13;
  }

  @Test
  public void test_fn_3() {
    // new token(new type(new strtemp(1)))

    token tok1 = new token();
    type t0 = new type();
    strtemp t1 = new strtemp();
    int t2 = 1;
    strtemp_init_0(t1, t2);
    type_init_1(t0, t1);
    token_init_2(tok1, t0);

  }
  // vars: [M:tok1]

}
