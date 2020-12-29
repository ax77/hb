package njast.ast_visitors;

import jscan.symtab.Ident;
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

public interface AstVisitor {

  void visit(Ident o);

  //@formatter:off
  void visit(ClassConstructorDeclaration o);
  void visit(ClassDeclaration o);
  void visit(ClassFieldDeclaration o);
  void visit(ClassMethodDeclaration o);
  void visit(ExprBinary o);
  void visit(ExprExpression o);
  void visit(ExprFieldAccess o);
  void visit(ExprMethodInvocation o);
  void visit(ExprNumericConstant o);
  void visit(ExprTernary o);
  void visit(ExprUnary o);
  void visit(FormalParameter o);
  void visit(FormalParameterList o);
  void visit(StmtBlock o);
  void visit(StmtBlockItem o);
  void visit(StmtReturn o);
  void visit(StmtStatement o);
  void visit(TopLevelCompilationUnit o);
  void visit(TopLevelTypeDeclaration o);
  void visit(VarDeclarationLocal o);
  void visit(VarDeclarator o);
  void visit(VarDeclaratorsList o);
  void visit(VarInitializer o);
  //@formatter:on

  String getText();

}
