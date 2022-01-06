package _st2_annotate;

import ast_class.ClassDeclaration;
import ast_class.InterfaceChecker;
import ast_expr.ExprExpression;
import ast_method.ClassMethodDeclaration;
import ast_types.Type;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import errors.ErrorLocation;

public class ApplyInitializer {

  private final SymbolTable symtabApplier;
  private final ClassDeclaration object;
  private final ClassMethodDeclaration method;

  public ApplyInitializer(SymbolTable symtabApplier, ClassDeclaration object, ClassMethodDeclaration method) {
    this.symtabApplier = symtabApplier;
    this.object = object;
    this.method = method;
  }

  public void applyInitializer(final VarDeclarator var) {

    final ExprExpression init = var.getSimpleInitializer();

    if (init == null) {
      throw new AstParseException("uninitialized variables are deprecated: " + var.getLocationToString());
    }

    ApplyExpression applier = new ApplyExpression(symtabApplier, object, method);
    applier.applyExpression(init);

    final Type lhsType = var.getType();
    final Type rhsType = init.getResultType();

    final boolean typesAreTheSame = lhsType.isEqualTo(rhsType);

    if (!typesAreTheSame) {
      if (lhsType.isClass() && rhsType.isClass()) {
        ClassDeclaration lhsClass = lhsType.getClassTypeFromRef();
        ClassDeclaration rhsClass = rhsType.getClassTypeFromRef();
        if (lhsClass.isInterface()) {
          if (InterfaceChecker.classFullyImplementsTheInterface(rhsClass, lhsClass)) {
            //var.setType(rhsType);
            return;
          }
        }
      }

      ErrorLocation.errorInitializer("the type of variable is different from type of its initilizer", var, init);
    }
  }

}
