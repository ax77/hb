package tac;

import java.io.IOException;

import org.junit.Test;

import ast_main.ParserMain;
import ast_st3_tac.TacGenerator;
import ast_unit.InstantiationUnit;

public class TestTac1 {

  @Test
  public void test5() throws IOException {

    InstantiationUnit unit = new ParserMain("tests/test_tac").parseInstantiationUnit();

    TacGenerator generator = new TacGenerator();

    generator.gen(unit.getClasses().get(2).getMethods().get(1).getBlock().getBlockStatements().get(2).getStatement()
        .getExprStmt());
    System.out.println(generator.txt1(";\n"));

    System.out.println("\n");

    generator = new TacGenerator();
    generator.gen(unit.getClasses().get(2).getMethods().get(1).getBlock().getBlockStatements().get(4).getStatement()
        .getExprStmt());
    System.out.println(generator.txt1(";\n"));

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

  }
}
