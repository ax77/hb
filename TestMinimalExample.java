package njast;

import java.io.IOException;

import org.junit.Test;

import njast.ast_flow.BlockStatement;
import njast.ast_flow.expr.CExpression;
import njast.ast_top.CompilationUnit;
import njast.ast_visitors.AstVisitorXml;
import njast.main.ParserMain;
import njast.parse.Parse;

public class TestMinimalExample {

  // "List": [
  //   {
  //     "X": {
  //       "Sel": {
  //         "Name": "c",
  //         "_type": "Ident"
  //       },
  //       "X": {
  //         "Args": [],
  //         "Ellipsis": 0,
  //         "Fun": {
  //           "Sel": {
  //             "Name": "b",
  //             "_type": "Ident"
  //           },
  //           "X": {
  //             "Args": [],
  //             "Ellipsis": 0,
  //             "Fun": {
  //               "Name": "a",
  //               "_type": "Ident"
  //             },
  //             "_type": "CallExpr"
  //           },
  //           "_type": "SelectorExpr"
  //         },
  //         "_type": "CallExpr"
  //       },
  //       "_type": "SelectorExpr"
  //     },
  //     "_type": "ExprStmt"
  //   }
  // ],

  //  <EFIELD_ACCESS>
  //  <FieldAccess>
  //    <Ident>
  //      c
  //    </Ident>
  //    <EMETHOD_INVOCATION>
  //      <EXPR:EFIELD_ACCESS>
  //        <EFIELD_ACCESS>
  //          <FieldAccess>
  //            <Ident>
  //              b
  //            </Ident>
  //            <EMETHOD_INVOCATION>
  //              <EXPR:EPRIMARY_IDENT>
  //                <EPRIMARY_IDENT>
  //                  <Ident>
  //                    a
  //                  </Ident>
  //                </EPRIMARY_IDENT>
  //              </EXPR:EPRIMARY_IDENT>
  //            </EMETHOD_INVOCATION>
  //          </FieldAccess>

  @Test
  public void testMinimalClass() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*002*/  public class C {                              \n");
    sb.append(" /*015*/    public int func1() {            \n");
    sb.append(" /*017*/      return a().b().c;   \n");
    sb.append(" /*018*/    }                                           \n");
    sb.append(" /*019*/  }                                             \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    CompilationUnit unit = p.parse();

    for (BlockStatement bs : unit.getTypeDeclarations().get(0).getClassDeclaration().getMethodDeclaration().get(0)
        .getBody().getBlockStatements().getBlockStatements()) {
      if (bs.getStatement() != null) {
        final CExpression expr = bs.getStatement().getExpr();
        AstVisitorXml vis = new AstVisitorXml();
        expr.accept(vis);
        System.out.println(vis.getText());
      }
    }

  }

}
