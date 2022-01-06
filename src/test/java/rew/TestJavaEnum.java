package rew;

import java.lang.reflect.Field;

import org.junit.Test;

public class TestJavaEnum {

  enum toktype {
    TP_STR, TP_IDENT
  }
  
  @Test
  public void test() {
    toktype tp = toktype.TP_IDENT;
    for(Field f : tp.getClass().getFields()) {
      System.out.println(f.getType());
    }
  }
  
}
