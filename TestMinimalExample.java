package njast;

import org.junit.Test;

import jscan.hashed.Hash_ident;
import njast.ast_nodes.top.TopLevelCompilationUnit;
import njast.ast_visitors.ApplyCompilationUnit;
import njast.main.ParserMain;
import njast.parse.Parse;
import njast.templates.TemplateCodegen;
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
    sb.append(" /*001*/  class Node<E> {                                 \n");
    sb.append(" /*002*/    E item;                                       \n");
    sb.append(" /*003*/    Node<E> next;                                 \n");
    sb.append(" /*004*/    Node<E> prev;                                 \n");
    sb.append(" /*005*/    Node(Node<E> prev, E element, Node<E> next) { \n");
    sb.append(" /*006*/      this.item = element;                        \n");
    sb.append(" /*007*/      this.next = next;                           \n");
    sb.append(" /*008*/      this.prev = prev;                           \n");
    sb.append(" /*009*/    }                                             \n");
    sb.append(" /*010*/  }                                               \n");
    sb.append(" /*011*/  class LinkedList<E> {                           \n");
    sb.append(" /*012*/    int size = 0;                                 \n");
    sb.append(" /*013*/    Node<E> first;                                \n");
    sb.append(" /*014*/    Node<E> last;                                 \n");
    sb.append(" /*015*/    void pushBack(E e) {                          \n");
    sb.append(" /*016*/      Node<E> l = last;                           \n");
    sb.append(" /*017*/      Node<E> newNode = new Node<E>(l, e, null);  \n");  
    sb.append(" /*018*/      last = newNode;                             \n");
    sb.append(" /*019*/      if (l == null) {                            \n");
    sb.append(" /*020*/        first = newNode;                          \n");
    sb.append(" /*021*/      } else {                                    \n");
    sb.append(" /*022*/        l.next = newNode;                         \n");
    sb.append(" /*023*/      }                                           \n");
    sb.append(" /*024*/      size += 1;                                  \n");
    sb.append(" /*025*/    }                                             \n");
    sb.append(" /*026*/  }                                               \n");
    sb.append(" /*027*/  class C {                                       \n");
    sb.append(" /*028*/    LinkedList<int> table;                        \n");
    sb.append(" /*029*/  }                                               \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TopLevelCompilationUnit unit = p.parse();

    ApplyCompilationUnit applier = new ApplyCompilationUnit();
    applier.visit(unit);

    //
    Type ap = unit.getTypeDeclarations().get(2).getClassDeclaration().getField(Hash_ident.getHashedIdent("table"))
        .getType();

    TemplateCodegen templateCodegen = new TemplateCodegen(ap);
    Type res = templateCodegen.getResult();
    for (Type ref : templateCodegen.getGeneratedClasses()) {
      System.out.println(UtilSrcToStringLevel.tos(ref.getClassType().toString()));
    }
    // System.out.println(res.toString());
  }

}
