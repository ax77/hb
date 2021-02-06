package tokenizers;

import static org.junit.Assert.assertEquals;

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
    assertEquals(2 + 1, new Stream(".", "//comment\n\n 0").getTokenlist().size());
    assertEquals(1 + 1, new Stream(".", "//comment\n\n").getTokenlist().size());
    assertEquals(6 + 1, new Stream(".", "()[]{}").getTokenlist().size());
    assertEquals(2 + 1, new Stream(".", ".....").getTokenlist().size());
    assertEquals(2 + 1, new Stream(".", "......").getTokenlist().size());
    assertEquals(3 + 1, new Stream(".", ". . .").getTokenlist().size());
    assertEquals(8 + 1, new Stream(".", ":;!#%^&*").getTokenlist().size());
    assertEquals(3 + 1, new Stream(".", "@`$\\\n").getTokenlist().size());
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
    sb.append("class test_array {                            \n");
    sb.append("  func fn() {                                 \n");
    sb.append("    var array: [[i32]] = new [2: [2: i32]];   \n");
    sb.append("    var x: u64 = array.length;                \n");
    sb.append("    var z: boolean = true;                    \n");
    sb.append("    var y: boolean = false;                   \n");
    sb.append("    var xxx: i32;                             \n");
    sb.append("    var yyy: u8;                              \n");
    sb.append("    var zzz: f32;                             \n");
    sb.append("    var arr: [i64];                           \n");
    sb.append("    var booooo: boolean;                      \n");
    sb.append("    var chch: u8;                             \n");
    sb.append("    var eself: test_array;                    \n");
    sb.append("  }                                           \n");
    sb.append("}                                             \n");
    //@formatter:on

    Stream s = new Stream("...test...", sb.toString());
    List<Token> tokens = s.getTokenlist();
    for (Token tok : tokens) {
      // System.out.printf("[%s]:[%s]\n", tok.loc(), tok.getValue());
    }

  }

  @Test
  public void testTokenComment() throws Exception {

//    //@formatter:off
//    StringBuilder sb = new StringBuilder();
//    sb.append("/// documentation about the class 1            \n");
//    sb.append("/// documentation about the class 2           \n");
//    sb.append("/// documentation about the class 3           \n");
//    sb.append("class test_array {                            \n");
//    sb.append("  func fn() {                                 \n");
//    sb.append("    // some notes about this variable         \n");
//    sb.append("    var array: [[i32]] = new [2: [2: i32]];   \n");
//    sb.append("    // comment 1                              \n");
//    sb.append("    // comment 2                              \n");
//    sb.append("    var y: boolean = false; // eol comment    \n");
//    sb.append("  }                                           \n");
//    sb.append("}                                             \n");
//    //@formatter:on

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class node<T>                 //001 \n");
    sb.append("{                             //002 \n");
    sb.append("    weak var prev: node<T>;   //003 \n");
    sb.append("    var item: T;              //004 \n");
    sb.append("    var next: node<T>;        //005 \n");
    sb.append("    init(prev: node<T>,       //006 \n");
    sb.append("         item: T,             //007 \n");
    sb.append("         next: node<T>)       //008 \n");
    sb.append("    {                         //009 \n");
    sb.append("        self.prev = prev;     //010 \n");
    sb.append("        self.next = next;     //011 \n");
    sb.append("        self.item = item;     //012 \n");
    sb.append("    }                         //013 \n");
    sb.append("}                             //014 \n");
    //@formatter:on

    Stream s = new Stream("...test...", sb.toString());
    List<Token> tokens = s.getTokenlist();
    for (Token tok : tokens) {
      // System.out.printf("[%s]:[%s]:[%s]\n", (tok.isAtBol() ? "BOL" : "   "), tok.getLocationToString(), tok.getValue());
    }

  }
}
