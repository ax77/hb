package njast;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import jscan.hashed.Hash_ident;
import jscan.symtab.Ident;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.top.TopLevelCompilationUnit;
import njast.ast_visitors.ApplyCompilationUnit;
import njast.main.ParserMain;
import njast.parse.Parse;
import njast.types.PrimitiveType;
import njast.types.Type;

public class TestMinimalExample {

  //to define a symbol in a function or nested block and check redefinition
  //1) check the whole function scope
  //2) check the current block
  //
  //to bind a symbol in a expression - 
  //1) block scope
  //2) function scope
  //3) class scope
  //4) file scope
  //
  //note: function parameters also a variables in a function scope

  @Test
  public void testSymtab() throws Exception {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  class Tree<T> {           \n");
    sb.append(" /*002*/    T lhs;                  \n");
    sb.append(" /*003*/    T rhs; T fn(T a, T b) {  }                  \n");
    sb.append(" /*004*/  }                         \n");
    sb.append(" /*005*/  class Node {              \n");
    sb.append(" /*006*/    int value;              \n");
    sb.append(" /*007*/  }                         \n");
    sb.append(" /*008*/  class UsageOfTemplate {   \n");
    sb.append(" /*009*/    Tree<Node> root;        \n");
    sb.append(" /*010*/  }                         \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TopLevelCompilationUnit unit = p.parse();

    ApplyCompilationUnit applier = new ApplyCompilationUnit();
    applier.visit(unit);

    //
    Map<Ident, Type> bindings = new HashMap<>();
    bindings.put(Hash_ident.getHashedIdent("T"), new Type(PrimitiveType.TP_INT));

    ClassDeclaration cd = unit.getTypeDeclarations().get(0).getClassDeclaration();
    ClassDeclaration result = new TemplateCodegen().expandTemplate(cd, bindings);
    System.out.println();
  }

}
