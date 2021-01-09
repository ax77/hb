package njast;

import org.junit.Test;

import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.top.InstantiationUnit;
import njast.ast_nodes.top.TopLevelCompilationUnit;
import njast.main.ParserMain;
import njast.parse.Parse;
import njast.templates.InstatantiationUnitBuilder;

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
    sb.append(" /*001*/    class Node<E> {                                                    \n");
    sb.append(" /*002*/      E item;                                                          \n");
    sb.append(" /*003*/      Node<E> next;                                                    \n");
    sb.append(" /*004*/      Node<E> prev;                                                    \n");
    sb.append(" /*005*/      Node(Node<E> prev, E element, Node<E> next) {                    \n");
    sb.append(" /*006*/        this.item = element;                                           \n");
    sb.append(" /*007*/        this.next = next;                                              \n");
    sb.append(" /*008*/        this.prev = prev;                                              \n");
    sb.append(" /*009*/      }                                                                \n");
    sb.append(" /*010*/    }                                                                  \n");
    sb.append(" /*011*/    class LinkedList<E> {                                              \n");
    sb.append(" /*012*/      int size = 0;                                                    \n");
    sb.append(" /*013*/      Node<E> first;                                                   \n");
    sb.append(" /*014*/      Node<E> last;                                                    \n");
    sb.append(" /*015*/      void pushBack(final E e) {                                             \n");
    sb.append(" /*016*/        final Node<E> l = last;                                    \n");
    sb.append(" /*017*/        final Node<E> newNode = new Node<E>(l, e, null);           \n");
    sb.append(" /*018*/        last = newNode;                                                \n");
    sb.append(" /*019*/        if (l == null) {                                               \n");
    sb.append(" /*020*/          first = newNode;                                             \n");
    sb.append(" /*021*/        } else {                                                       \n");
    sb.append(" /*022*/          l.next = newNode;                                            \n");
    sb.append(" /*023*/        }                                                              \n");
    sb.append(" /*024*/        size += 1;                                                     \n");
    sb.append(" /*025*/      }                                                                \n");
    sb.append(" /*026*/      void pushFront(final E e) {                                            \n");
    sb.append(" /*027*/        final Node<E> f = first;                                   \n");
    sb.append(" /*028*/        final Node<E> newNode = new Node<E>(null, e, f);           \n");
    sb.append(" /*029*/        first = newNode;                                               \n");
    sb.append(" /*030*/        if (f == null) {                                               \n");
    sb.append(" /*031*/          last = newNode;                                              \n");
    sb.append(" /*032*/        } else {                                                       \n");
    sb.append(" /*033*/          f.prev = newNode;                                            \n");
    sb.append(" /*034*/        }                                                              \n");
    sb.append(" /*035*/        size += 1;                                                     \n");
    sb.append(" /*036*/      }                                                                \n");
    sb.append(" /*037*/      E popFront() {                                                   \n");
    sb.append(" /*038*/        final Node<E> f = first;                                   \n");
    sb.append(" /*039*/        if (f == null) {                                               \n");
    sb.append(" /*040*/        }                                                              \n");
    sb.append(" /*041*/        final E element = f.item;                                  \n");
    sb.append(" /*042*/        final Node<E> next = f.next;                               \n");
    sb.append(" /*043*/        f.item = null;                                                 \n");
    sb.append(" /*044*/        f.next = null; // help GC                                      \n");
    sb.append(" /*045*/        first = next;                                                  \n");
    sb.append(" /*046*/        if (next == null) {                                            \n");
    sb.append(" /*047*/          last = null;                                                 \n");
    sb.append(" /*048*/        } else {                                                       \n");
    sb.append(" /*049*/          next.prev = null;                                            \n");
    sb.append(" /*050*/        }                                                              \n");
    sb.append(" /*051*/        size -= 1;                                                     \n");
    sb.append(" /*052*/        return element;                                                \n");
    sb.append(" /*053*/      }                                                                \n");
    sb.append(" /*054*/      E popBack() {                                                    \n");
    sb.append(" /*055*/        final Node<E> l = last;                                    \n");
    sb.append(" /*056*/        if (l == null) {                                               \n");
    sb.append(" /*057*/        }                                                              \n");
    sb.append(" /*058*/        final E element = l.item;                                  \n");
    sb.append(" /*059*/        final Node<E> prev = l.prev;                               \n");
    sb.append(" /*060*/        l.item = null;                                                 \n");
    sb.append(" /*061*/        l.prev = null; // help GC                                      \n");
    sb.append(" /*062*/        last = prev;                                                   \n");
    sb.append(" /*063*/        if (prev == null) {                                            \n");
    sb.append(" /*064*/          first = null;                                                \n");
    sb.append(" /*065*/        } else {                                                       \n");
    sb.append(" /*066*/          prev.next = null;                                            \n");
    sb.append(" /*067*/        }                                                              \n");
    sb.append(" /*068*/        size -= 1;                                                     \n");
    sb.append(" /*069*/        return element;                                                \n");
    sb.append(" /*070*/      }                                                                \n");
    sb.append(" /*071*/    }                                                                  \n");
    sb.append(" /*072*/    class C {                                                          \n");
    sb.append(" /*073*/      public void testList() { LinkedList<char> empty;                 \n");
    sb.append(" /*074*/        LinkedList<int> table = new LinkedList<int>();                 \n");
    sb.append(" /*075*/        table.pushFront(1);                                            \n");
    sb.append(" /*076*/        table.pushFront(2);                                            \n");
    sb.append(" /*077*/        for (Node<int> e = table.first; e != null; e = e.next) {       \n");
    sb.append(" /*078*/        }                                                              \n");
    sb.append(" /*079*/      }                                                                \n");
    sb.append(" /*080*/    }                                                                  \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TopLevelCompilationUnit unit = p.parse();

    InstantiationUnit instantiationUnit = new InstatantiationUnitBuilder(unit).getInstantiationUnit();
    for (ClassDeclaration clazz : instantiationUnit.getClasses()) {
      System.out.println(UtilSrcToStringLevel.tos(clazz.toString()));
    }

  }

}
