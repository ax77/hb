package njast;

import org.junit.Test;

import ast.ast.nodes.ClassDeclaration;
import ast.ast.nodes.unit.CompilationUnit;
import ast.ast.nodes.unit.InstantiationUnit;
import ast.parse.Parse;
import ast.parse.main.Imports;
import ast.parse.main.ParserMain;
import ast.templates.InstatantiationUnitBuilder;

public class TestImportStd {

  @Test
  public void testImportStdList() throws Exception {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  import std.string; import std.list;         \n");
    sb.append(" /*002*/  class test {             \n");
    sb.append(" /*003*/    func main() -> i64 {   \n");
    sb.append(" /*004*/     var t:string = io.read_file();  var e: list<i64>;    \n");
    sb.append(" /*005*/      return 0;            \n");
    sb.append(" /*006*/    }                      \n");
    sb.append(" /*007*/  }                        \n");
    //@formatter:on

    Parse mainParser = new ParserMain(new Imports(sb).getSource()).initiateParse();

    CompilationUnit unit = mainParser.parse();
    InstantiationUnit instantiationUnit = new InstatantiationUnitBuilder(unit).getInstantiationUnit();
    for (ClassDeclaration clazz : instantiationUnit.getClasses()) {
      //System.out.println(UtilSrcToStringLevel.tos(clazz.toString()));
    }

  }

}
