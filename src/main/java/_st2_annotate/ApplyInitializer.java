package _st2_annotate;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_expr.ExpressionBase;
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

    final boolean typesAreTheSame = var.getType().isEqualTo(init.getResultType());
    if (!typesAreTheSame) {

      /// TODO:pointers
      if (init.is(ExpressionBase.EBUILTIN_FN)) {
        init.setResultType(var.getType());
      }

      else {
        ErrorLocation.errorExpression("the type of variable is different from type of its initilizer", init);
      }

    }
  }

}
