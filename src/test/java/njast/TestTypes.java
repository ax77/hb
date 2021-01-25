package njast;

import java.io.IOException;

import org.junit.Test;

import ast_class.ClassDeclaration;
import ast_templates.InstatantiationUnitBuilder;
import ast_unit.CompilationUnit;
import ast_unit.InstantiationUnit;
import parse.ImportsResolver;
import parse.Parse;
import parse.ParserMain;

public class TestTypes {

  @Test
  public void test1() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  import std.list;        \n");
    sb.append(" /*002*/  class test {            \n");
    sb.append(" /*003*/    var a: [2:[3:i32]];          \n");
    sb.append(" /*004*/    var b: list<i32>;     \n");
    sb.append(" /*005*/    var c: u64;           \n");
    sb.append(" /*006*/    var d: boolean;       \n");
    sb.append(" /*007*/    var e: f32;           \n");
    sb.append(" /*008*/    var f: i16;           \n");
    sb.append(" /*009*/    var g: list<[u32]>;   \n");
    sb.append(" /*010*/  }                       \n");
    //@formatter:on

    Parse mainParser = new ParserMain(new ImportsResolver(sb).getSource()).initiateParse();

    CompilationUnit unit = mainParser.parse();
    InstantiationUnit instantiationUnit = new InstatantiationUnitBuilder(unit).getInstantiationUnit();
    for (ClassDeclaration clazz : instantiationUnit.getClasses()) {
      //System.out.println(UtilSrcToStringLevel.tos(clazz.toString()));
    }

  }

  @Test
  public void testString() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  class test_arrays {   \n");
    sb.append(" /*002*/    func test() {       \n");
    sb.append(" /*003*/      var arr: [[u8]];    \n");
    sb.append(" /*004*/      var x: u8;        \n");
    sb.append(" /*005*/      arr[0][0] = 1;       \n");
    sb.append(" /*006*/      x = arr[0][1];       \n");
    sb.append(" /*007*/    }                   \n");
    sb.append(" /*008*/  }                     \n");
    //@formatter:on

    Parse mainParser = new ParserMain(new ImportsResolver(sb).getSource()).initiateParse();

    CompilationUnit unit = mainParser.parse();
    InstantiationUnit instantiationUnit = new InstatantiationUnitBuilder(unit).getInstantiationUnit();
    for (ClassDeclaration clazz : instantiationUnit.getClasses()) {
      //System.out.println(UtilSrcToStringLevel.tos(clazz.toString()));
    }

  }

}
