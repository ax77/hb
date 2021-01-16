package njast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import jscan.hashed.Hash_ident;
import njast.ast_checkers.IteratorChecker;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.top.InstantiationUnit;
import njast.ast_nodes.top.TopLevelCompilationUnit;
import njast.main.ParserMain;
import njast.parse.Parse;
import njast.templates.InstatantiationUnitBuilder;
import njast.types.TypeBase;

public class TestIteratorRecognizer {

  @Test
  public void testIteratorRecognizer() throws Exception {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  class node<T>                                    \n");
    sb.append(" /*002*/  {                                                \n");
    sb.append(" /*003*/      var prev: node<T>;                           \n");
    sb.append(" /*004*/      var item: T;                                 \n");
    sb.append(" /*005*/      var next: node<T>;                           \n");
    sb.append(" /*006*/      init(prev: node<T>,                          \n");
    sb.append(" /*007*/           item: T,                                \n");
    sb.append(" /*008*/           next: node<T>)                          \n");
    sb.append(" /*009*/      {                                            \n");
    sb.append(" /*010*/          self.prev = prev;                        \n");
    sb.append(" /*011*/          self.next = next;                        \n");
    sb.append(" /*012*/          self.item = item;                        \n");
    sb.append(" /*013*/      }                                            \n");
    sb.append(" /*014*/  }                                                \n");
    sb.append(" /*015*/  class list<T>                                    \n");
    sb.append(" /*016*/  {                                                \n");
    sb.append(" /*017*/      var first: node<T>;                          \n");
    sb.append(" /*018*/      var last: node<T>;                           \n");
    sb.append(" /*019*/      var size: int;                               \n");
    sb.append(" /*020*/      func push_back(e: T)                         \n");
    sb.append(" /*021*/      {                                            \n");
    sb.append(" /*022*/          var l: node<T> = self.last;              \n");
    sb.append(" /*023*/          var n: node<T> = new node<T>(            \n");
    sb.append(" /*024*/              prev: l,                             \n");
    sb.append(" /*025*/              item: e,                             \n");
    sb.append(" /*026*/              next: null                           \n");
    sb.append(" /*027*/          );                                       \n");
    sb.append(" /*028*/          self.last = n;                           \n");
    sb.append(" /*029*/          if (l == null) {                         \n");
    sb.append(" /*030*/              self.first = n;                      \n");
    sb.append(" /*031*/          } else {                                 \n");
    sb.append(" /*032*/              l.next = n;                          \n");
    sb.append(" /*033*/          }                                        \n");
    sb.append(" /*034*/          self.size += 1;                          \n");
    sb.append(" /*035*/      }                                            \n");
    sb.append(" /*036*/      func get_iterator() -> list_iterator<T>      \n");
    sb.append(" /*037*/      {                                            \n");
    sb.append(" /*038*/          return new list_iterator<T>(             \n");
    sb.append(" /*039*/              collection: self                     \n");
    sb.append(" /*040*/          );                                       \n");
    sb.append(" /*041*/      }                                            \n");
    sb.append(" /*042*/  }                                                \n");
    sb.append(" /*043*/  class list_iterator<T> {                         \n");
    sb.append(" /*044*/      var collection: list<T>;                     \n");
    sb.append(" /*045*/      var last_returned: node<T>;                  \n");
    sb.append(" /*046*/      var next: node<T>;                           \n");
    sb.append(" /*047*/      var next_index: int;                         \n");
    sb.append(" /*048*/      init(collection: list<T>)                    \n");
    sb.append(" /*049*/      {                                            \n");
    sb.append(" /*050*/          self.collection = collection;            \n");
    sb.append(" /*051*/          self.last_returned = collection.first;   \n");
    sb.append(" /*052*/          self.next = collection.first;            \n");
    sb.append(" /*053*/          self.next_index = 0;                     \n");
    sb.append(" /*054*/      }                                            \n");
    sb.append(" /*055*/      func current() -> T                          \n");
    sb.append(" /*056*/      {                                            \n");
    sb.append(" /*057*/          return last_returned.item;               \n");
    sb.append(" /*058*/      }                                            \n");
    sb.append(" /*059*/      func has_next() -> boolean                   \n");
    sb.append(" /*060*/      {                                            \n");
    sb.append(" /*061*/          return next_index < collection.size;     \n");
    sb.append(" /*062*/      }                                            \n");
    sb.append(" /*063*/      func get_next() -> T                         \n");
    sb.append(" /*064*/      {                                            \n");
    sb.append(" /*065*/          self.last_returned = next;               \n");
    sb.append(" /*066*/          self.next = next.next;                   \n");
    sb.append(" /*067*/          self.next_index += 1;                    \n");
    sb.append(" /*068*/          return last_returned.item;               \n");
    sb.append(" /*069*/      }                                            \n");
    sb.append(" /*070*/  }                                                \n");
    sb.append(" /*071*/  class test                                       \n");
    sb.append(" /*072*/  {                                                \n");
    sb.append(" /*073*/      var list: list<int>;                         \n");
    sb.append(" /*074*/  }                                                \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TopLevelCompilationUnit unit = p.parse();

    InstantiationUnit instantiationUnit = new InstatantiationUnitBuilder(unit).getInstantiationUnit();
    for (ClassDeclaration clazz : instantiationUnit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(clazz.toString()));
    }

    VarDeclarator var = instantiationUnit.getClasses().get(3).getField(Hash_ident.getHashedIdent("list"));

    IteratorChecker checker = new IteratorChecker(var);
    assertTrue(checker.isIterable());
    assertEquals(checker.getElemType().getBase(), TypeBase.TP_INT);

  }

}
