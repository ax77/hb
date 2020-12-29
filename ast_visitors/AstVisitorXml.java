package njast.ast_visitors;

import java.util.List;

import jscan.symtab.Ident;
import njast.ast_kinds.ExpressionBase;
import njast.ast_nodes.clazz.ClassConstructorDeclaration;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.ClassFieldDeclaration;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.clazz.methods.FormalParameter;
import njast.ast_nodes.clazz.methods.FormalParameterList;
import njast.ast_nodes.clazz.vars.VarDeclarationLocal;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.clazz.vars.VarDeclaratorsList;
import njast.ast_nodes.clazz.vars.VarInitializer;
import njast.ast_nodes.expr.ExprBinary;
import njast.ast_nodes.expr.ExprExpression;
import njast.ast_nodes.expr.ExprFieldAccess;
import njast.ast_nodes.expr.ExprMethodInvocation;
import njast.ast_nodes.expr.ExprNumericConstant;
import njast.ast_nodes.expr.ExprTernary;
import njast.ast_nodes.expr.ExprUnary;
import njast.ast_nodes.stmt.StmtBlock;
import njast.ast_nodes.stmt.StmtBlockItem;
import njast.ast_nodes.stmt.StmtReturn;
import njast.ast_nodes.stmt.StmtStatement;
import njast.ast_nodes.top.TopLevelCompilationUnit;
import njast.ast_nodes.top.TopLevelTypeDeclaration;

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
  public void visit(ExprExpression o) {

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
  public void visit(ExprMethodInvocation o) {
    if (o.isMethodInvocation()) {
      visit(o.getObject());
    }

    final List<ExprExpression> arguments = o.getArguments();
    for (int i = arguments.size(); --i >= 0;) {
      ExprExpression e = arguments.get(i);
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
  public void visit(ExprFieldAccess o) {
    visit(o.getObject());
    put("mov eax, offset_of_field [" + o.getFieldName().getName() + "]");
    put("push eax\n");
  }

  @Override
  public void visit(ClassConstructorDeclaration o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(ClassDeclaration o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(ClassFieldDeclaration o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(ClassMethodDeclaration o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(ExprBinary o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(ExprNumericConstant o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(ExprTernary o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(ExprUnary o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(FormalParameter o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(FormalParameterList o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(StmtBlock o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(StmtBlockItem o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(StmtReturn o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(StmtStatement o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(TopLevelCompilationUnit o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(TopLevelTypeDeclaration o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(VarDeclarationLocal o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(VarDeclarator o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(VarDeclaratorsList o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(VarInitializer o) {
    // TODO Auto-generated method stub

  }

}
