package njast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import njast.types.ReferenceType;
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
    sb.append(" /*004*/  }                         \n");
    sb.append(" /*005*/  class Node {              \n");
    sb.append(" /*006*/    int value;              \n");
    sb.append(" /*007*/  }                         \n");
    sb.append(" /*008*/  class UsageOfTemplate {   \n");
    sb.append(" /*009*/    Tree<Tree<Tree<Node>>> root;        \n");
    sb.append(" /*010*/  }                         \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TopLevelCompilationUnit unit = p.parse();

    ApplyCompilationUnit applier = new ApplyCompilationUnit();
    applier.visit(unit);

    //

    ClassDeclaration cd = unit.getTypeDeclarations().get(2).getClassDeclaration()
        .getField(Hash_ident.getHashedIdent("root")).getType().getReferenceType().getTypeName();

    Ident fp = cd.getTypeParameters().getTypeParameters().get(0);

    ReferenceType ap = unit.getTypeDeclarations().get(2).getClassDeclaration()
        .getField(Hash_ident.getHashedIdent("root")).getType().getReferenceType().getTypeArguments().get(0);

    List<ClassDeclaration> generated = new ArrayList<>();
    TemplateCodegen.expandTemplate(cd, fp, ap, generated);
    System.out.println();
  }

}
