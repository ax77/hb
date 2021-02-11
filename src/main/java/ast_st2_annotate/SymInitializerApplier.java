package ast_st2_annotate;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_expr.ExprUtil;
import ast_expr.ExpressionBase;
import ast_types.Type;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import errors.ErrorLocation;
import tokenize.Token;

public class SymInitializerApplier {

  private final SymbolTable symtabApplier;

  public SymInitializerApplier(SymbolTable symtabApplier) {
    this.symtabApplier = symtabApplier;
  }

  public void applyInitializer(final ClassDeclaration object, final VarDeclarator var) {
    if (var.isArrayInitializer()) {
      throw new AstParseException("unimpl. array-inits.");
    }

    // TODO:
    // maybeInitVariableByDefault(var);

    final ExprExpression init = var.getSimpleInitializer();

    if (init == null) {
      throw new AstParseException("unexpected, initializer should be there: " + var.getLocationToString());
    }

    if (init.is(ExpressionBase.ECLASS_INSTANCE_CREATION)) {
      //MIR:TREE
      init.getClassCreation().setVar(var);
    }

    SymExpressionApplier applier = new SymExpressionApplier(symtabApplier);
    applier.applyExpression(object, init);

    if (!var.getType().is_equal_to(init.getResultType())) {
      ErrorLocation.errorExpression("the type of variable is different from type of its initilizer", init);
    }
  }

  private void maybeInitVariableByDefault(final VarDeclarator var) {

    final ExprExpression init = var.getSimpleInitializer();
    if (init != null) {
      return;
    }
    if (var.is(VarBase.CLASS_FIELD)) {
      return;
    }

    // initialize variable with its default value:
    // null for arrays and classes
    // zero for primitives
    // false for boolean
    final Type tp = var.getType();
    final Token beginPos = var.getBeginPos();

    if (tp.is_numeric()) {
      ExprExpression zeroExpr = ExprUtil.getEmptyPrimitive(tp, beginPos);
      var.setSimpleInitializer(zeroExpr);
    }

    else if (tp.is_class()) {
      ExprExpression nullExpr = new ExprExpression(ExpressionBase.EPRIMARY_NULL_LITERAL, beginPos);
      var.setSimpleInitializer(nullExpr);
    }

    else if (tp.is_boolean()) {
      ExprExpression falseExpr = new ExprExpression(false, beginPos);
      var.setSimpleInitializer(falseExpr);
    }

  }

}
