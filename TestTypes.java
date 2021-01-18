package njast;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import njast.ast.nodes.ClassDeclaration;
import njast.ast.nodes.unit.CompilationUnit;
import njast.ast.nodes.unit.InstantiationUnit;
import njast.parse.Parse;
import njast.parse.main.Imports;
import njast.parse.main.ParserMain;
import njast.templates.InstatantiationUnitBuilder;
import njast.types.Type;
import njast.utils.UtilSrcToStringLevel;

public class TestTypes {

  @Test
  public void test() throws IOException {
    assertTrue(Type.I16_TYPE.is_integer());
    assertFalse(Type.F32_TYPE.is_integer());
    assertTrue(Type.F64_TYPE.is_floating());

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  import std.list;        \n");
    sb.append(" /*002*/  class test {            \n");
    sb.append(" /*003*/    var a: [i8];          \n");
    sb.append(" /*004*/    var b: list<i32>;     \n");
    sb.append(" /*005*/    var c: u64;           \n");
    sb.append(" /*006*/    var d: boolean;       \n");
    sb.append(" /*007*/    var e: f32;           \n");
    sb.append(" /*008*/    var f: i16;           \n");
    sb.append(" /*009*/    var g: list<[u32]>;   \n");
    sb.append(" /*010*/  }                       \n");
    //@formatter:on

    Parse mainParser = new ParserMain(new Imports(sb).getSource()).initiateParse();

    CompilationUnit unit = mainParser.parse();
    InstantiationUnit instantiationUnit = new InstatantiationUnitBuilder(unit).getInstantiationUnit();
    for (ClassDeclaration clazz : instantiationUnit.getClasses()) {
      System.out.println(UtilSrcToStringLevel.tos(clazz.toString()));
    }

  }

}
