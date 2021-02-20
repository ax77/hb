package ast_st2_annotate;

import ast_expr.ExprExpression;
import ast_st3_tac.FlatCode;
import ast_st3_tac.TacGenerator;
import ast_vars.VarDeclarator;

public abstract class GetCodeItems {

  public static FlatCode getFlatCode(ExprExpression e) {
    if (e == null) {
      return new FlatCode();
    }
    TacGenerator tcg = new TacGenerator(e);
    return new FlatCode(tcg.getRv());
  }

  public static FlatCode getFlatCode(VarDeclarator e) {
    if (e == null) {
      return new FlatCode();
    }
    TacGenerator tcg = new TacGenerator(e);
    return new FlatCode(tcg.getRv());
  }

}
