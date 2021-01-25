package njast;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import literals.IntLiteral;
import literals.ParseIntLiteral;

public class TestParseInt {

  @Test
  public void testDec() throws Exception {
    String buffer = "0_1_2_3";
    IntLiteral literal = new ParseIntLiteral(buffer).parse();
    assertEquals(123, literal.getInteger());
    assertEquals(buffer, literal.getInput());
  }

}
