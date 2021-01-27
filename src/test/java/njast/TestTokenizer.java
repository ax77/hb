package njast;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import tokenize.Stream;

public class TestTokenizer {

  @Test
  public void testTokenizerSimple() throws Exception {
    // 3:beg+end+eof

    assertEquals(4 + 3, new Stream(".", "+-*/").getTokenlist().size());
    assertEquals(2 + 3, new Stream(".", ">>>>=").getTokenlist().size());
    assertEquals(8 + 3, new Stream(".", "+=-=*=/=>>=<<===!=").getTokenlist().size());
    assertEquals(8 + 3, new Stream(".", "abc a b c 123 1 2 3").getTokenlist().size());
    assertEquals(2 + 3, new Stream(".", "'1' \"0\"").getTokenlist().size());
    assertEquals(1 + 3, new Stream(".", "/*comment*/\n\n//comment\n\n 0").getTokenlist().size());
    assertEquals(0 + 3, new Stream(".", "/*comment*/\n\n//comment\n\n").getTokenlist().size());
    assertEquals(6 + 3, new Stream(".", "()[]{}").getTokenlist().size());
    assertEquals(2 + 3, new Stream(".", ".....").getTokenlist().size());
    assertEquals(2 + 3, new Stream(".", "......").getTokenlist().size());
    assertEquals(3 + 3, new Stream(".", ". . .").getTokenlist().size());
    assertEquals(8 + 3, new Stream(".", ":;!#%^&*").getTokenlist().size());

  }
}
