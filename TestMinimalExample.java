package njast;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jscan.hashed.Hash_ident;
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
   sb.append(" class Node<E> {          \n");
   sb.append("   E item;                \n");
   sb.append("   Node<E> next;          \n");
   sb.append("   Node<E> prev;          \n");
   sb.append(" }                        \n");
   sb.append(" class LinkedList<E> {    \n");
   sb.append("   Node<E> first;         \n");
   sb.append("   Node<E> last;          \n");
   sb.append(" }                        \n");
   sb.append(" class Idn{} class C {    \n");
   sb.append("   LinkedList<Idn> list;  \n");
   sb.append(" }                        \n");
   //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TopLevelCompilationUnit unit = p.parse();

    ApplyCompilationUnit applier = new ApplyCompilationUnit();
    applier.visit(unit);

    //
    ReferenceType ap = unit.getTypeDeclarations().get(3).getClassDeclaration()
        .getField(Hash_ident.getHashedIdent("list")).getType().getReferenceType();

    List<ReferenceType> togen = new ArrayList<>();
    ReferenceType res = TemplateCodegen.getType(ap, togen);
    for (ReferenceType ref : togen) {
      System.out.println(ref.getClassType().toString());
    }
  }

}
