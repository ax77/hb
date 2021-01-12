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
    sb.append(" /*015*/      public LinkedList() {                                            \n");
    sb.append(" /*016*/      }                                                                \n");
    sb.append(" /*017*/      void pushBack(E e) {                                             \n");
    sb.append(" /*018*/        final Node<E> l = last;                                        \n");
    sb.append(" /*019*/        final Node<E> newNode = new Node<E>(l, e, null);               \n");
    sb.append(" /*020*/        last = newNode;                                                \n");
    sb.append(" /*021*/        if (l == null) {                                               \n");
    sb.append(" /*022*/          first = newNode;                                             \n");
    sb.append(" /*023*/        } else {                                                       \n");
    sb.append(" /*024*/          l.next = newNode;                                            \n");
    sb.append(" /*025*/        }                                                              \n");
    sb.append(" /*026*/        size += 1;                                                     \n");
    sb.append(" /*027*/      }                                                                \n");
    sb.append(" /*028*/      void pushFront(E e) {                                            \n");
    sb.append(" /*029*/        final Node<E> f = first;                                       \n");
    sb.append(" /*030*/        final Node<E> newNode = new Node<E>(null, e, f);               \n");
    sb.append(" /*031*/        first = newNode;                                               \n");
    sb.append(" /*032*/        if (f == null) {                                               \n");
    sb.append(" /*033*/          last = newNode;                                              \n");
    sb.append(" /*034*/        } else {                                                       \n");
    sb.append(" /*035*/          f.prev = newNode;                                            \n");
    sb.append(" /*036*/        }                                                              \n");
    sb.append(" /*037*/        size += 1;                                                     \n");
    sb.append(" /*038*/      }                                                                \n");
    sb.append(" /*039*/      E popFront() {                                                   \n");
    sb.append(" /*040*/        final Node<E> f = first;                                       \n");
    sb.append(" /*041*/        if (f == null) {                                               \n");
    sb.append(" /*042*/        }                                                              \n");
    sb.append(" /*043*/        final E element = f.item;                                      \n");
    sb.append(" /*044*/        final Node<E> next = f.next;                                   \n");
    sb.append(" /*045*/        f.item = null;                                                 \n");
    sb.append(" /*046*/        f.next = null; // help GC                                      \n");
    sb.append(" /*047*/        first = next;                                                  \n");
    sb.append(" /*048*/        if (next == null) {                                            \n");
    sb.append(" /*049*/          last = null;                                                 \n");
    sb.append(" /*050*/        } else {                                                       \n");
    sb.append(" /*051*/          next.prev = null;                                            \n");
    sb.append(" /*052*/        }                                                              \n");
    sb.append(" /*053*/        size -= 1;                                                     \n");
    sb.append(" /*054*/        return element;                                                \n");
    sb.append(" /*055*/      }                                                                \n");
    sb.append(" /*056*/      E popBack() {                                                    \n");
    sb.append(" /*057*/        final Node<E> l = last;                                        \n");
    sb.append(" /*058*/        if (l == null) {                                               \n");
    sb.append(" /*059*/        }                                                              \n");
    sb.append(" /*060*/        final E element = l.item;                                      \n");
    sb.append(" /*061*/        final Node<E> prev = l.prev;                                   \n");
    sb.append(" /*062*/        l.item = null;                                                 \n");
    sb.append(" /*063*/        l.prev = null; // help GC                                      \n");
    sb.append(" /*064*/        last = prev;                                                   \n");
    sb.append(" /*065*/        if (prev == null) {                                            \n");
    sb.append(" /*066*/          first = null;                                                \n");
    sb.append(" /*067*/        } else {                                                       \n");
    sb.append(" /*068*/          prev.next = null;                                            \n");
    sb.append(" /*069*/        }                                                              \n");
    sb.append(" /*070*/        size -= 1;                                                     \n");
    sb.append(" /*071*/        return element;                                                \n");
    sb.append(" /*072*/      }                                                                \n");
    sb.append(" /*073*/      public void clear() {                                            \n");
    sb.append(" /*074*/        for (Node<E> x = first; x != null;) {                          \n");
    sb.append(" /*075*/          Node<E> next = x.next;                                       \n");
    sb.append(" /*076*/          x.item = null;                                               \n");
    sb.append(" /*077*/          x.next = null;                                               \n");
    sb.append(" /*078*/          x.prev = null;                                               \n");
    sb.append(" /*079*/          x = next;                                                    \n");
    sb.append(" /*080*/        }                                                              \n");
    sb.append(" /*081*/        first = null;                                                  \n");
    sb.append(" /*082*/        last = null;                                                   \n");
    sb.append(" /*083*/        size = 0;                                                      \n");
    sb.append(" /*084*/      }                                                                \n");
    sb.append(" /*085*/      Node<E> node(int index) {                                        \n");
    sb.append(" /*086*/        if (index < (size / 2)) {                                     \n");
    sb.append(" /*087*/          Node<E> x = first;                                           \n");
    sb.append(" /*088*/          for (int i = 0; i < index; i += 1) {                         \n");
    sb.append(" /*089*/            x = x.next;                                                \n");
    sb.append(" /*090*/          }                                                            \n");
    sb.append(" /*091*/          return x;                                                    \n");
    sb.append(" /*092*/        } else {                                                       \n");
    sb.append(" /*093*/          Node<E> x = last;                                            \n");
    sb.append(" /*094*/          for (int i = size - 1; i > index; i -= 1) {                  \n");
    sb.append(" /*095*/            x = x.prev;                                                \n");
    sb.append(" /*096*/          }                                                            \n");
    sb.append(" /*097*/          return x;                                                    \n");
    sb.append(" /*098*/        }                                                              \n");
    sb.append(" /*099*/      }                                                                \n");
    sb.append(" /*100*/    }                                                                  \n");
    sb.append(" /*101*/    class C {                                                          \n");
    sb.append(" /*102*/      public void testList() {                                         \n");
    sb.append(" /*103*/        LinkedList<LinkedList<int> > table = new LinkedList<LinkedList<int> >();                 \n");
    sb.append(" /*108*/      }                                                                \n");
    sb.append(" /*109*/    }                                                                  \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TopLevelCompilationUnit unit = p.parse();

    InstantiationUnit instantiationUnit = new InstatantiationUnitBuilder(unit).getInstantiationUnit();
    for (ClassDeclaration clazz : instantiationUnit.getClasses()) {
      System.out.println(UtilSrcToStringLevel.tos(clazz.toString()));
    }

  }

}
