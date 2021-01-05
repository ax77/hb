package njast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import jscan.hashed.Hash_ident;
import njast.ast_nodes.top.TopLevelCompilationUnit;
import njast.ast_visitors.ApplyCompilationUnit;
import njast.main.ParserMain;
import njast.parse.Parse;
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
    sb.append(" /*001*/    class Node<T> {              \n");
    sb.append(" /*002*/      Node<T> prev;              \n");
    sb.append(" /*003*/      Node<T> next;              \n");
    sb.append(" /*004*/      T item;                    \n");
    sb.append(" /*005*/    }                            \n");
    sb.append(" /*006*/    class List<T> {              \n");
    sb.append(" /*007*/      Node<T> first;             \n");
    sb.append(" /*008*/      Node<T> last;              \n");
    sb.append(" /*009*/      int size;                  \n");
    sb.append(" /*010*/    }                            \n");
    sb.append(" /*011*/    class Entry<K, V> {          \n");
    sb.append(" /*012*/      K key;                     \n");
    sb.append(" /*013*/      V value;                   \n");
    sb.append(" /*014*/      Entry<K, V> next;          \n");
    sb.append(" /*015*/    }                            \n");
    sb.append(" /*016*/    class Map<K, V> {            \n");
    sb.append(" /*017*/      List<Entry<K, V> > table;  \n");
    sb.append(" /*018*/      int capacity;              \n");
    sb.append(" /*019*/    }                            \n");
    sb.append(" /*020*/    class idn {                  \n");
    sb.append(" /*021*/      int stub;                  \n");
    sb.append(" /*022*/    }                            \n");
    sb.append(" /*023*/    class string {               \n");
    sb.append(" /*024*/      int stub;                  \n");
    sb.append(" /*025*/    }                            \n");
    sb.append(" /*026*/    class C {                    \n");
    sb.append(" /*027*/      Node<boolean> table;       \n");
    sb.append(" /*028*/    }                            \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TopLevelCompilationUnit unit = p.parse();

    ApplyCompilationUnit applier = new ApplyCompilationUnit();
    applier.visit(unit);

    //
    Type ap = unit.getTypeDeclarations().get(6).getClassDeclaration().getField(Hash_ident.getHashedIdent("table"))
        .getType();

    HashMap<String, Dto> temps = new HashMap<>();
    List<Type> togen = new ArrayList<>();
    Type res = TemplateCodegen.getType(ap, togen, temps);
    for (Type ref : togen) {
      System.out.println(ref.getClassType().toString());
    }
    // System.out.println(res.toString());
  }

}
