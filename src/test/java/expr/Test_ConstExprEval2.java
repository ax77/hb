package expr;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import ast_expr.ExprExpression;
import ast_main.ParserMain;
import ast_parsers.ParseExpression;
import ast_st2_annotate.ConstexprEval;
import parse.Parse;

public class Test_ConstExprEval2 {

  @Test
  public void ceEval() throws IOException {
    Map<String, Integer> toeval = new HashMap<String, Integer>();

    //@formatter:off
    toeval.put("1 + ?(0==0, 1, 2) + 3", 5);
    toeval.put("1 + ?(0==0, ?(1==0, 1, 2), 2) + 4", 7);
    toeval.put("1 + ?(1==1, 2, 1/0)", 3);
    //@formatter:on

    for (Entry<String, Integer> ent : toeval.entrySet()) {
      String str = ent.getKey();
      long expected = ent.getValue().longValue();

      Parse p = new ParserMain(new StringBuilder(ent.getKey())).initiateParse();

      ExprExpression expr = new ParseExpression(p).e_const_expr();
      long actual = ConstexprEval.ce(expr);

      if (expected != actual) {
        System.out.println("NO: " + str);
      }

      assertEquals(expected, actual);
    }

  }

}
