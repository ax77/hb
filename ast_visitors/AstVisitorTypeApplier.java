package njast.ast_visitors;

import java.util.List;

import jscan.symtab.Ident;
import njast.ast_nodes.clazz.ClassConstructorDeclaration;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.ClassFieldDeclaration;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.clazz.methods.FormalParameter;
import njast.ast_nodes.clazz.vars.VarDeclarationLocal;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.clazz.vars.VarDeclaratorsList;
import njast.ast_nodes.expr.ExprBinary;
import njast.ast_nodes.expr.ExprExpression;
import njast.ast_nodes.expr.ExprFieldAccess;
import njast.ast_nodes.expr.ExprMethodInvocation;
import njast.ast_nodes.expr.ExprNumericConstant;
import njast.ast_nodes.expr.ExprTernary;
import njast.ast_nodes.expr.ExprUnary;
import njast.ast_nodes.stmt.StmtBlock;
import njast.ast_nodes.stmt.StmtBlockItem;
import njast.ast_nodes.stmt.StmtStatement;
import njast.ast_nodes.top.TopLevelCompilationUnit;
import njast.ast_nodes.top.TopLevelTypeDeclaration;
import njast.types.Type;

public class AstVisitorTypeApplier {

  //////////////////////////////////////////////////////////////////////
  // SYMTAB 
  //
  private void defineFunctionParameter(ClassMethodDeclaration method, Type paramType, Ident paramName) {
  }

  private void defineMethodVariable(ClassMethodDeclaration method, VarDeclarator var, Type type) {
  }

  private void defineClassField(ClassFieldDeclaration o, Type type, VarDeclarator var) {
  }

  private void defineMethod(ClassDeclaration o, ClassMethodDeclaration m) {
    System.out.println(m.getIdentifier().getName());
  }
  //
  //////////////////////////////////////////////////////////////////////

  public void visit(ClassConstructorDeclaration o) {
  }

  public void visit(ClassDeclaration object) {

    System.out.println(object.getIdentifier().getName());

    //fields
    for (ClassFieldDeclaration f : object.getFieldDeclaration()) {
      Type type = f.getType();
      VarDeclaratorsList vars = f.getVariables();
      for (VarDeclarator var : vars.getVariables()) {
        defineClassField(f, type, var); // check redefinition
      }
    }

    //methods
    for (ClassMethodDeclaration method : object.getMethodDeclaration()) {
      defineMethod(object, method); // check overloading/redefinition/etc

      for (FormalParameter fp : method.getFormalParameterList().getParameters()) {
        Type type = fp.getType();
        Ident name = fp.getName();
        defineFunctionParameter(method, type, name);
      }

      //body
      final StmtBlock body = method.getBody();
      final List<StmtBlockItem> blockStatements = body.getBlockStatements();

      for (StmtBlockItem block : blockStatements) {

        // declarations
        final VarDeclarationLocal localVars = block.getLocalVars();
        if (localVars != null) {
          Type type = localVars.getType();
          VarDeclaratorsList vars = localVars.getVars();
          for (VarDeclarator var : vars.getVariables()) {
            defineMethodVariable(method, var, type);
          }
        }

        // statements
        final StmtStatement statement = block.getStatement();
        if (statement != null) {
          //
        }
      }
    }

    //
  }

  public void visit(ExprBinary o) {
    // TODO Auto-generated method stub

  }

  public void visit(ExprExpression o) {
    // TODO Auto-generated method stub

  }

  public void visit(ExprFieldAccess o) {
    // TODO Auto-generated method stub

  }

  public void visit(ExprMethodInvocation o) {
    // TODO Auto-generated method stub

  }

  public void visit(ExprNumericConstant o) {
    // TODO Auto-generated method stub

  }

  public void visit(ExprTernary o) {
    // TODO Auto-generated method stub

  }

  public void visit(ExprUnary o) {
    // TODO Auto-generated method stub

  }

  public void visit(TopLevelCompilationUnit o) {
    for (TopLevelTypeDeclaration td : o.getTypeDeclarations()) {
      visit(td);
    }
  }

  public void visit(TopLevelTypeDeclaration o) {
    visit(o.getClassDeclaration());
  }

}
