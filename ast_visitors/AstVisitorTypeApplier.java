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

  //to define a symbol in a function or nested block and check redefinition
  //1) check the current block 
  //2) check the whole function scope
  //
  //to bind a symbol in a expression - 
  //1) block scope
  //2) function scope
  //3) class scope
  //4) file scope
  //
  //note: function parameters also a variables in a function scope

  // you have to initialize each field with its default value
  // you have to initialize each LHS like that, before you'll apply RHS
  //
  //  class Idn {
  //    int i = func();
  //    int func() {
  //      return this.i;
  //    }
  //  }

  //////////////////////////////////////////////////////////////////////
  // SYMTAB 
  //
  private void defineFunctionParameter(ClassMethodDeclaration method, Type paramType, Ident paramName) {
    System.out.println("param_name: " + paramName.getName());
  }

  private void defineMethodVariable(ClassMethodDeclaration method, VarDeclarator var, Type type) {
  }

  private void defineClassField(ClassFieldDeclaration o, Type type, VarDeclarator var) {
  }

  private void defineMethod(ClassDeclaration o, ClassMethodDeclaration m) {
    System.out.println(m.getIdentifier().getName());
  }

  private void defineConstructor(ClassDeclaration object, ClassConstructorDeclaration constructor) {
  }

  private void initVarZero(VarDeclarator var, Type type) {
  }

  //
  //////////////////////////////////////////////////////////////////////

  public void visit(ClassConstructorDeclaration o) {
  }

  public void visit(ClassDeclaration object) {

    System.out.println(object.getIdentifier().getName());

    //fields
    for (ClassFieldDeclaration field : object.getFieldDeclaration()) {
      Type type = field.getType();
      VarDeclaratorsList vars = field.getVariables();
      for (VarDeclarator var : vars.getVariables()) {
        initVarZero(var, type);
        defineClassField(field, type, var); // check redefinition
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
            initVarZero(var, type);
            defineMethodVariable(method, var, type);
          }
        }

        // statements
        final StmtStatement statement = block.getStatement();
        if (statement != null) {
          // block variables here ... 
        }
      }
    }

    //constructors (the last, it works with methods and fields)
    for (ClassConstructorDeclaration constructor : object.getConstructorDeclaration()) {
      defineConstructor(object, constructor); // check overloading/redefinition/etc
    }
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
