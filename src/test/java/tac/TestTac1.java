package tac;

import java.io.IOException;

import org.junit.Test;

import ast_expr.ExprExpression;
import ast_main.ParserMain;
import ast_st3_tac.TacGenerator;
import ast_unit.InstantiationUnit;

public class TestTac1 {

  @Test
  public void test5() throws IOException {

    InstantiationUnit unit = new ParserMain("tests/test_tac").parseInstantiationUnit();
    ExprExpression expression2 = unit.getClasses().get(2).getMethods().get(1).getBlock().getBlockStatements().get(4)
        .getStatement().getExprStmt();

    TacGenerator generator = new TacGenerator();
    generator.gen(expression2);

    // for (Entry<ResultName, Quad> e : allops.entrySet()) {
    //   System.out.println(e.getKey().toString() + "::" +e.getValue().getType().toString());
    // }

    // // const folding: that is :)
    // List<Quad> toRemove = new ArrayList<>();
    // for (Quad q : quads) {
    //   if (q.getBase() == QuadOpc.NUM_DECL) {
    //     toRemove.add(q);
    //     hashResultName.get(q.getResult().getResult()).setResult(q.getLhs().toString());
    //   }
    // }
    // for (Quad q : toRemove) {
    //   quads.remove(q);
    // }

    System.out.println(generator.txt1(";\n"));

  }
}
