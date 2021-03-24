package fmt;

import org.junit.Test;

public class TestFmt {
  
  @Test
  public void test1() {
    /// print("error: {0}, because: {1}", 1024, "file does not exists");
    /// 1) char/int/short/long/float/double/boolean -> we know 100% how to format them
    /// 2) object: we'll try to find 'to_string()' method, which should return the string
    ///    if we found it - we'll use it, if not - the default format is the name+address
    ///
  }
  
}
