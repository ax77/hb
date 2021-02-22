package ast_st2_annotate;

import ast_expr.ExprExpression;
import ast_method.ClassMethodDeclaration;
import ast_st3_tac.FlatCode;
import ast_st3_tac.TacGenerator;
import ast_vars.VarDeclarator;

public abstract class GetCodeItems {

  public static FlatCode getFlatCode(ExprExpression e, ClassMethodDeclaration method) {
    if (e == null) {
      return new FlatCode();
    }
    TacGenerator tcg = new TacGenerator(e, method);
    return new FlatCode(tcg.getRv(), tcg.getVarCreator().getAllVars());
  }

  public static FlatCode getFlatCode(VarDeclarator e, ClassMethodDeclaration method) {
    if (e == null) {
      return new FlatCode();
    }
    TacGenerator tcg = new TacGenerator(e, method);
    return new FlatCode(tcg.getRv(), tcg.getVarCreator().getAllVars());
  }

}
