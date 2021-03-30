package _st2_annotate;

import ast_class.ClassDeclaration;
import ast_class.InterfaceChecker;
import ast_expr.ExprExpression;
import ast_types.Type;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import errors.ErrorLocation;

public class ApplyInitializer {

  private final SymbolTable symtabApplier;

  public ApplyInitializer(SymbolTable symtabApplier) {
    this.symtabApplier = symtabApplier;
  }

  public void applyInitializer(final ClassDeclaration object, final VarDeclarator var) {
    if (var.isArrayInitializer()) {
      throw new AstParseException("unimpl. array-inits.");
    }

    final ExprExpression init = var.getSimpleInitializer();

    if (init == null) {
      throw new AstParseException("uninitialized variables are deprecated: " + var.getLocationToString());
    }

    ApplyExpression applier = new ApplyExpression(symtabApplier);
    applier.applyExpression(object, init);

    final Type lhsType = var.getType();
    final Type rhsType = init.getResultType();

    final boolean typesAreTheSame = lhsType.isEqualTo(rhsType);

    if (!typesAreTheSame) {
      if (lhsType.isClass() && rhsType.isClass()) {
        ClassDeclaration lhsClass = lhsType.getClassTypeFromRef();
        ClassDeclaration rhsClass = rhsType.getClassTypeFromRef();
        if (lhsClass.isInterface() && InterfaceChecker.classFullyImplementsTheInterface(rhsClass, lhsClass)) {
          //var.setType(rhsType);
          return;
        }
      }

      ErrorLocation.errorExpression("the type of variable is different from type of its initilizer", init);
    }
  }

}
