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
    sb.append(" /*001*/  class Node<T> {                                              \n");
    sb.append(" /*002*/      let value: T;                                            \n");
    sb.append(" /*003*/      var next: Node<T>;                                       \n");
    sb.append(" /*004*/      weak var prev: Node<T>;                                  \n");
    sb.append(" /*005*/      init(value: T) {                                         \n");
    sb.append(" /*006*/          self.value = value;                                  \n");
    sb.append(" /*007*/          self.next = null;                                     \n");
    sb.append(" /*008*/          self.prev = null;                                     \n");
    sb.append(" /*009*/      }                                                        \n");
    sb.append(" /*010*/      deinit {                                                 \n");
    sb.append(" /*011*/      }                                                        \n");
    sb.append(" /*012*/  }                                                            \n");
    sb.append(" /*013*/  class List<T> {                                              \n");
    sb.append(" /*014*/      var head: Node<T>;                                       \n");
    sb.append(" /*015*/      var tail: Node<T>;                                       \n");
    sb.append(" /*016*/      var size: int;                                           \n");
    sb.append(" /*017*/      init() {                                                 \n");
    sb.append(" /*018*/          self.head = null;                                     \n");
    sb.append(" /*019*/          self.tail = null;                                     \n");
    sb.append(" /*020*/          self.size = 0;                                       \n");
    sb.append(" /*021*/      }                                                        \n");
    sb.append(" /*022*/      func append(value: T) {                                  \n");
    sb.append(" /*023*/          // 1                                                 \n");
    sb.append(" /*024*/          let new_node: Node<T> = new Node<T>(value: value);   \n");
    sb.append(" /*025*/          // 2                                                 \n");
    sb.append(" /*026*/          if self.head == null {                                \n");
    sb.append(" /*027*/              // 3                                             \n");
    sb.append(" /*028*/              self.head = new_node;                            \n");
    sb.append(" /*029*/              self.tail = new_node;                            \n");
    sb.append(" /*030*/              self.size += 1;                                  \n");
    sb.append(" /*031*/              return;                                          \n");
    sb.append(" /*032*/          }                                                    \n");
    sb.append(" /*033*/          // 4                                                 \n");
    sb.append(" /*034*/          self.tail.next = new_node;                           \n");
    sb.append(" /*035*/          new_node.prev = self.tail;                           \n");
    sb.append(" /*036*/          self.tail = new_node;                                \n");
    sb.append(" /*037*/          // 5                                                 \n");
    sb.append(" /*038*/          self.size += 1;                                      \n");
    sb.append(" /*039*/      }                                                        \n");
    sb.append(" /*040*/      deinit {                                                 \n");
    sb.append(" /*041*/      }                                                        \n");
    sb.append(" /*042*/  }                                                            \n");
    sb.append(" /*043*/  class C {                                                \n");
    sb.append(" /*044*/      func test() {  let matrix: [3:int] = new [3:int];                   \n");
    sb.append(" /*045*/           let list: List<[2:int]> = new List<[2:int]>();               \n");
//    sb.append(" /*046*/          list.append(value: 1);                               \n");
//    sb.append(" /*047*/          list.append(value: 2);                               \n");
//    sb.append(" /*048*/          list.append(value: 3);                               \n");
    sb.append(" /*049*/      }                                                        \n");
    sb.append(" /*050*/  }                                                            \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TopLevelCompilationUnit unit = p.parse();

    InstantiationUnit instantiationUnit = new InstatantiationUnitBuilder(unit).getInstantiationUnit();
    for (ClassDeclaration clazz : instantiationUnit.getClasses()) {
      System.out.println(UtilSrcToStringLevel.tos(clazz.toString()));
    }

  }

}
