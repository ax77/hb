package njast.ast_visitors;

import java.util.LinkedList;
import java.util.List;

import jscan.symtab.Ident;
import njast.ast_flow.expr.CExpression;
import njast.ast_flow.expr.CExpressionBase;
import njast.ast_flow.expr.FieldAccess;
import njast.ast_flow.expr.MethodInvocation;

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
    // text.append(pad() + "<" + tag + ">\n");
    text.append(pad() + tag + ": {\n");

    ++level;
    tagStack.add(0, tag);
  }

  private void closeTag() {
    String tag = tagStack.remove(0);

    --level;
    // text.append(pad() + "</" + tag + ">\n");
    text.append(pad() + "}\n");
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
  public void visit(CExpression o) {

    CExpressionBase base = o.getBase();
    openTag(base.toString());

    if (base == CExpressionBase.EMETHOD_INVOCATION) {
      visit(o.getMethodInvocation());
    }

    if (base == CExpressionBase.EFIELD_ACCESS) {
      visit(o.getFieldAccess());
    }

    if (base == CExpressionBase.EPRIMARY_IDENT) {
      visit(o.getSymbol());
    }

    closeTag();
  }

  @Override
  public void visit(MethodInvocation o) {

    put("callee:");
    visit(o.getFunction());

  }

  @Override
  public void visit(Ident o) {
    put("id=" + o.getName());
  }

  @Override
  public void visit(FieldAccess o) {
    put("property: " + o.getName().getName());
    put("object: ");

    visit(o.getExpression());
  }

}
