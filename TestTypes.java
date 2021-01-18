package njast;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import njast.types.Type;

public class TestTypes {

  @Test
  public void test() {
    assertTrue(Type.I16_TYPE.is_integer());
    assertFalse(Type.F32_TYPE.is_integer());
    assertTrue(Type.F64_TYPE.is_floating());
  }

}
