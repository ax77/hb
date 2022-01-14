package _st2_annotate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast_class.ClassDeclaration;
import ast_class.InterfaceChecker;
import ast_expr.ExprAssign;
import ast_expr.ExprExpression;
import ast_expr.ExpressionBase;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_stmt.StmtBlock;
import ast_stmt.StmtStatement;
import ast_symtab.BuiltinNames;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_unit.InstantiationUnit;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import errors.AstParseException;

public class ApplyUnitPostEachClass {

  private final InstantiationUnit instantiationUnit;

  public ApplyUnitPostEachClass(InstantiationUnit instantiationUnit) {
    this.instantiationUnit = instantiationUnit;
  }

  public void postEachClass() {
    for (ClassDeclaration c : instantiationUnit.getClasses()) {
      InterfaceChecker.checkImplementations(c);
      applyMethodsSpecial(c);
      checkAllFieldsAreInitialized(c);
    }
  }

  private void checkAllFieldsAreInitialized(final ClassDeclaration c) {
    if (c.isStaticClass()) {
      return;
    }
    if (c.isEnum()) {
      return;
    }

    /// we 100% know that class has at least one constructor
    for (ClassMethodDeclaration constructor : c.getConstructors()) {
      final List<VarDeclarator> fields = c.getFields();
      final Set<String> initialized = new HashSet<>();
      final StmtBlock block = constructor.getBlock();

      for (StmtStatement stmt : block.getBlockItems()) {
        if (!stmt.isExprStmt()) {
          continue;
        }
        final ExprExpression expr = stmt.getExprStmt();
        if (expr.is(ExpressionBase.EMETHOD_INVOCATION)) {
          continue; // not a var :)
        }
        if (expr.is(ExpressionBase.EBUILTIN_FUNC)) {
          continue;
        }

        final VarDeclarator dest = getDest(expr);
        if (dest == null) {
          throw new AstParseException("unknown: " + expr.toString());
        }
        initialized.add(dest.getIdentifier().getName());
      }

      boolean hasErrors = false;
      for (VarDeclarator var : fields) {
        // TODO: much more clean with native array.
        if (!initialized.contains(var.getIdentifier().getName()) && !c.isNativeArray()
            && var.getModifiers().isFinal() /*&& !var.getMods().isMutable()*/) {
          hasErrors = true;
          System.out.println(var.getLocationToString() + ", error: class-field has no initializer: "
              + c.getIdentifier().toString() + ":" + var.getIdentifier().toString());
        }
      }

      if (hasErrors) {
        throw new AstParseException("you should init all fields");
      }

    }
  }

  private VarDeclarator getDest(final ExprExpression expr) {
    if (expr.is(ExpressionBase.EFIELD_ACCESS)) {
    }

    else if (expr.is(ExpressionBase.EASSIGN)) {
      final ExprAssign eassign = expr.getAssign();
      final ExprExpression lvalue = eassign.getLvalue();

      if (lvalue.is(ExpressionBase.EPRIMARY_IDENT)) {
        VarDeclarator varAssigned = lvalue.getIdent().getVar();
        if (varAssigned.is(VarBase.CLASS_FIELD)) {
          return varAssigned;
        }
      }

      else if (lvalue.is(ExpressionBase.EFIELD_ACCESS)) {
        return lvalue.getFieldAccess().getField();
      }

      else {
        throw new AstParseException("unknown: " + expr.toString());
      }
    }

    return null;
  }

  private void applyMethodsSpecial(ClassDeclaration object) {
    addThisParamToEachMethod(object);
  }

  private void addThisParamToEachMethod(ClassDeclaration object) {
    if (object.isStaticClass()) {
      return;
    }
    if (object.isEnum()) {
      return;
    }

    /// if object is not a static class (i.e. namespace)
    /// and if the object is not a main_class
    /// we have to put parameter named __this at the top
    /// of the parameters list.
    /// 

    for (ClassMethodDeclaration method : object.getMethods()) {
      final VarDeclarator thisParam = createThisParameter(object, method);
      method.pushParameterFront(thisParam);
    }

    for (ClassMethodDeclaration constructor : object.getConstructors()) {
      //constructor.pushParameterFront(createThisParameter(object, constructor));
    }

    final ClassMethodDeclaration destructor = object.getDestructor();
    destructor.pushParameterFront(createThisParameter(object, destructor));

  }

  private VarDeclarator createThisParameter(ClassDeclaration object, ClassMethodDeclaration method) {
    //@formatter:off
    final Type paramType = new Type(new ClassTypeRef(object, object.getTypeParametersT()));
    final VarDeclarator __thisParam = new VarDeclarator(VarBase.METHOD_PARAMETER
        , new Modifiers()
        , paramType
        , BuiltinNames.__this_ident
        , method.getBeginPos()
    );
    return __thisParam;
    //@formatter:on
  }

}
