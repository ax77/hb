package njast;

import org.junit.Ignore;
import org.junit.Test;

import njast.ast.nodes.ClassDeclaration;
import njast.ast.nodes.unit.CompilationUnit;
import njast.ast.nodes.unit.InstantiationUnit;
import njast.parse.Parse;
import njast.parse.main.Imports;
import njast.parse.main.ParserMain;
import njast.templates.InstatantiationUnitBuilder;
import njast.utils.UtilSrcToStringLevel;

public class TestImportStd {

  @Ignore
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
      System.out.println(UtilSrcToStringLevel.tos(clazz.toString()));
    }

  }

  private String simpleName(String path) {
    final String res = path.substring(path.lastIndexOf('/') + 1).trim();
    return res;
  }

}
