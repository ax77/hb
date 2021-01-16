package njast;

import org.junit.Test;

import njast.ast.nodes.ClassDeclaration;
import njast.ast.nodes.unit.CompilationUnit;
import njast.ast.nodes.unit.InstantiationUnit;
import njast.parse.Parse;
import njast.parse.main.ParserMain;
import njast.templates.InstatantiationUnitBuilder;
import njast.utils.UtilSrcToStringLevel;

public class TestImportStd {

  @Test
  public void testImportStdList() throws Exception {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  import std.list;         \n");
    sb.append(" /*002*/  class test {             \n");
    sb.append(" /*003*/    func main() -> int {   \n");
    sb.append(" /*004*/      var e: list<int>;    \n");
    sb.append(" /*005*/      return 0;            \n");
    sb.append(" /*006*/    }                      \n");
    sb.append(" /*007*/  }                        \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    CompilationUnit unit = p.parse();

    InstantiationUnit instantiationUnit = new InstatantiationUnitBuilder(unit).getInstantiationUnit();
    for (ClassDeclaration clazz : instantiationUnit.getClasses()) {
      System.out.println(UtilSrcToStringLevel.tos(clazz.toString()));
    }

  }

}
