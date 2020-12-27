package njast.ast_visitors;

import java.util.LinkedList;
import java.util.List;

import njast.ast_top.TypeDeclaration;
import njast.ast_top.CompilationUnit;

public class AstVisitorXml implements AstVisitor {

  private StringBuilder text;
  private int level;
  private List<String> tagStack;

  public AstVisitorXml() {
    text = new StringBuilder();
    text.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n");

    level = 0;
    tagStack = new LinkedList<String>();
  }

  @Override
  public String getText() {
    return text.toString();
  }

  private void openTag(String tag) {
    text.append(pad() + "<" + tag + ">\n");

    ++level;
    tagStack.add(0, tag);
  }

  private void closeTag() {
    String tag = tagStack.remove(0);

    --level;
    text.append(pad() + "</" + tag + ">\n");
  }

  private void put(String name) {
    text.append(pad());
    text.append(name);
    text.append("\n");
  }

  private String sname(Object o) {
    return o.getClass().getSimpleName();
  }

  private String pad() {
    StringBuilder res = new StringBuilder();
    for (int i = 0; i < level; i++) {
      res.append("  ");
    }
    return res.toString();
  }

  private String q(String trim) {
    return "\"" + trim + "\"";
  }

  @Override
  public void visit(CompilationUnit o) {
    openTag(sname(o));

    for (TypeDeclaration elem : o.getTypeDeclarations()) {
      visit(elem);
    }

    closeTag();

  }

  @Override
  public void visit(TypeDeclaration o) {
    openTag(sname(o));

    put(o.getClassDeclaration().getIdentifier().getName());

    closeTag();

  }

}
