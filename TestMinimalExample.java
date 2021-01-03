package njast;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jscan.hashed.Hash_ident;
import jscan.symtab.Ident;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.top.TopLevelCompilationUnit;
import njast.ast_visitors.ApplyCompilationUnit;
import njast.main.ParserMain;
import njast.parse.Parse;
import njast.types.ReferenceType;

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
    sb.append(" /*001*/  class Pair<K,V> {           \n");
    sb.append(" /*002*/    K key; V value;                  \n");
    sb.append(" /*004*/  }                         \n");
    sb.append(" /*001*/  class Tree<T> {           \n");
    sb.append(" /*002*/   int fn(T a, T b) {  } T lhs; T rhs;                  \n");
    sb.append(" /*004*/  }                         \n");
    sb.append(" /*005*/  class Node1<T> {              \n");
    sb.append(" /*006*/   T fn(T xxx, T yyy) {}  T value;              \n");
    sb.append(" /*007*/  }                         \n");
    sb.append(" /*005*/  class Node2 {              \n");
    sb.append(" /*006*/    int value;              \n");
    sb.append(" /*007*/  }                         \n");
    sb.append(" /*008*/  class UsageOfTemplate {   \n");
    sb.append(" /*009*/    Pair<Tree<Node1<Node2> >, Pair<Node1<Tree<Node2> >, Node2> > root;        \n");
    sb.append(" /*010*/  }                         \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TopLevelCompilationUnit unit = p.parse();

    ApplyCompilationUnit applier = new ApplyCompilationUnit();
    applier.visit(unit);

    //
    ReferenceType ap = unit.getTypeDeclarations().get(4).getClassDeclaration()
        .getField(Hash_ident.getHashedIdent("root")).getType().getReferenceType();

    List<ReferenceType> togen = new ArrayList<>();
    ReferenceType res = TemplateCodegen.getType(ap, togen);
    for (ReferenceType ref : togen) {
      System.out.println(ref.getTypeName().toString());
    }
  }

}
