package tokenizers;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import tokenize.Stream;
import tokenize.Token;

public class TestTokenizer {

  @Test
  public void testTokenizerSimple() throws Exception {
    // 3:beg+end+eof

    assertEquals(4 + 1, new Stream(".", "+-*/").getTokenlist().size());
    assertEquals(3 + 1, new Stream(".", ">>>>=").getTokenlist().size());
    assertEquals(8 + 1, new Stream(".", "+=-=*=/=>>=<<===!=").getTokenlist().size());
    assertEquals(8 + 1, new Stream(".", "abc a b c 123 1 2 3").getTokenlist().size());
    assertEquals(2 + 1, new Stream(".", "'1' \"0\"").getTokenlist().size());
    assertEquals(1 + 1, new Stream(".", "/*comment*/\n\n//comment\n\n 0").getTokenlist().size());
    assertEquals(0 + 1, new Stream(".", "/*comment*/\n\n//comment\n\n").getTokenlist().size());
    assertEquals(6 + 1, new Stream(".", "()[]{}").getTokenlist().size());
    assertEquals(2 + 1, new Stream(".", ".....").getTokenlist().size());
    assertEquals(2 + 1, new Stream(".", "......").getTokenlist().size());
    assertEquals(3 + 1, new Stream(".", ". . .").getTokenlist().size());
    assertEquals(8 + 1, new Stream(".", ":;!#%^&*").getTokenlist().size());
    assertEquals(4 + 1, new Stream(".", "@`$\\").getTokenlist().size());
    assertEquals(0 + 1, new Stream(".", "   \n\r\n\r\r\r\n\n\r\r\t\t  ").getTokenlist().size());
    assertEquals(5 + 1, new Stream(".", "int main return 0 ;").getTokenlist().size());
    assertEquals(5 + 1, new Stream(".", ",./?-").getTokenlist().size());
    assertEquals(2 + 1, new Stream(".", "_111 _000").getTokenlist().size());
    assertEquals(2 + 1, new Stream(".", "1u8 2u8").getTokenlist().size());

  }

  @Test
  public void testLocation() throws Exception {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  class test_array {                            \n");
    sb.append(" /*002*/    func fn() {                                 \n");
    sb.append(" /*003*/      var array: [[i32]] = new [2: [2: i32]];   \n");
    sb.append(" /*004*/      var x: u64 = array.length;                \n");
    sb.append(" /*005*/      var z: boolean = true;                    \n");
    sb.append(" /*006*/      var y: boolean = false;                   \n");
    sb.append(" /*007*/      var xxx: i32;                             \n");
    sb.append(" /*008*/      var yyy: u8;                              \n");
    sb.append(" /*009*/      var zzz: f32;                             \n");
    sb.append(" /*010*/      var arr: [i64];                           \n");
    sb.append(" /*011*/      var booooo: boolean;                      \n");
    sb.append(" /*012*/      var chch: u8;                             \n");
    sb.append(" /*013*/      var eself: test_array;                    \n");
    sb.append(" /*014*/    }                                           \n");
    sb.append(" /*015*/  }                                             \n");
    //@formatter:on

    Stream s = new Stream("...test...", sb.toString());
    List<Token> tokens = s.getTokenlist();
    for (Token tok : tokens) {
      // System.out.printf("[%s]:[%s]\n", tok.loc(), tok.getValue());
    }

  }
}
