package njast.ast_visitors;

import java.util.List;

import jscan.symtab.Ident;
import njast.ast_kinds.ExpressionBase;
import njast.ast_nodes.expr.ExpressionNode;
import njast.ast_nodes.expr.FieldAccess;
import njast.ast_nodes.expr.MethodInvocation;

public class AstVisitorXml implements AstVisitor {

  private StringBuilder text;
  private int level;

  public AstVisitorXml() {
    text = new StringBuilder();
    level = 0;
  }

  @Override
  public String getText() {
    return text.toString();
  }

  private void put(String name) {
    text.append(pad());
    text.append(name);
    text.append("\n");
  }

  private String sname(Object o) {
    return o.getClass().getSimpleName();
  }

  private String q(String trim) {
    return "\"" + trim + "\"";
  }

  private String pad() {
    StringBuilder res = new StringBuilder();
    for (int i = 0; i < level; i++) {
      res.append("  ");
    }
    return res.toString();
  }

  @Override
  public void visit(ExpressionNode o) {

    if (o == null) {
      System.out.println(">> warn: null expression");
      return;
    }

    ExpressionBase base = o.getBase();

    if (base == ExpressionBase.EMETHOD_INVOCATION) {
      visit(o.getMethodInvocation());
    }

    if (base == ExpressionBase.EFIELD_ACCESS) {
      visit(o.getFieldAccess());
    }

    if (base == ExpressionBase.EPRIMARY_IDENT) {
      visit(o.getSymbol());
    }

  }

  @Override
  public void visit(MethodInvocation o) {
    if (o.isMethodInvocation()) {
      visit(o.getFunction());
    }

    final List<ExpressionNode> arguments = o.getArguments();
    for (int i = arguments.size(); --i >= 0;) {
      ExpressionNode e = arguments.get(i);
      if (e.getCnumber() == null) {
        throw new RuntimeException("wanna numbers for test");
      }
      put("push " + String.format("%d", e.getCnumber().getClong()));
    }

    put("call " + o.getFuncname().getName());
    put("mov eax, return_val");
    put("push eax\n");
  }

  @Override
  public void visit(Ident o) {
    put("id=" + o.getName());
  }

  @Override
  public void visit(FieldAccess o) {
    visit(o.getExpression());
    put("mov eax, offset_of_field [" + o.getName().getName() + "]");
    put("push eax\n");
  }

}
