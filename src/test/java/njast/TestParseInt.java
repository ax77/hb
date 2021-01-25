package njast;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ast.IntLiteral;
import ast.ParseIntLiteral;
import ast.types.TypeBase;

public class TestParseInt {

  @Test
  public void testDec() throws Exception {
    String buffer = "0_1_2_3";
    IntLiteral literal = new ParseIntLiteral(buffer).parse();
    assertEquals(TypeBase.TP_I32, literal.getType());
    assertEquals(123, literal.getInteger());
    assertEquals(buffer, literal.getInput());
  }

}
