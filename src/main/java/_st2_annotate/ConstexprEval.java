package _st2_annotate;

import ast_expr.ExprBinary;
import ast_expr.ExprExpression;
import ast_expr.ExprTernaryOperator;
import ast_expr.ExprUnary;
import ast_expr.ExpressionBase;
import ast_types.Type;
import errors.ErrorLocation;
import literals.IntLiteral;
import tokenize.T;

public abstract class ConstexprEval {

  public static long ce(ExprExpression e) {
    ExpressionBase base = e.getBase();

    if (base == ExpressionBase.ETERNARY_OPERATOR) {
      ExprTernaryOperator ternaryOperator = e.getTernaryOperator();
      long result = ce(ternaryOperator.getCondition());
      if (result == 0) {
        return ce(ternaryOperator.getFalseResult());
      } else {
        return ce(ternaryOperator.getTrueResult());
      }
    }

    if (base == ExpressionBase.EBINARY) {
      ExprBinary expression = e.getBinary();
      T op = expression.getOperator().getType();

      //2)
      if (op == T.T_OR_OR) {
        return ((ce(expression.getLhs()) != 0 || ce(expression.getRhs()) != 0) ? 1 : 0);
      }
      //3)
      if (op == T.T_AND_AND) {
        return ((ce(expression.getLhs()) != 0 && ce(expression.getRhs()) != 0) ? 1 : 0);
      }
      //4)
      if (op == T.T_OR) {
        return ce(expression.getLhs()) | ce(expression.getRhs());
      }
      //5)
      if (op == T.T_XOR) {
        return ce(expression.getLhs()) ^ ce(expression.getRhs());
      }
      //6)
      if (op == T.T_AND) {
        return ce(expression.getLhs()) & ce(expression.getRhs());
      }
      //7)
      if (op == T.T_EQ) {
        return ((ce(expression.getLhs()) == ce(expression.getRhs())) ? 1 : 0);
      }
      //8)
      if (op == T.T_NE) {
        return ((ce(expression.getLhs()) != ce(expression.getRhs())) ? 1 : 0);
      }

      //9)
      if (op == T.T_LT) {
        return ((ce(expression.getLhs()) < ce(expression.getRhs())) ? 1 : 0);
      }
      //10)
      if (op == T.T_GT) {
        return ((ce(expression.getLhs()) > ce(expression.getRhs())) ? 1 : 0);
      }
      //11)
      if (op == T.T_LE) {
        return ((ce(expression.getLhs()) <= ce(expression.getRhs())) ? 1 : 0);
      }
      //12)
      if (op == T.T_GE) {
        return ((ce(expression.getLhs()) >= ce(expression.getRhs())) ? 1 : 0);
      }

      //14)
      if (op == T.T_LSHIFT) {
        return ce(expression.getLhs()) << ce(expression.getRhs());
      }
      //15
      if (op == T.T_RSHIFT) {
        return ce(expression.getLhs()) >> ce(expression.getRhs());
      }

      //16)
      if (op == T.T_PLUS) {
        return ce(expression.getLhs()) + ce(expression.getRhs());
      }
      //17)
      if (op == T.T_MINUS) {
        return ce(expression.getLhs()) - ce(expression.getRhs());
      }

      //18)
      if (op == T.T_TIMES) {
        return ce(expression.getLhs()) * ce(expression.getRhs());
      }
      //19)
      if (op == T.T_DIVIDE) {
        return ce(expression.getLhs()) / ce(expression.getRhs());
      }
      //20)
      if (op == T.T_PERCENT) {
        return ce(expression.getLhs()) % ce(expression.getRhs());
      }

      ErrorLocation.errorExpression("unknown binary operator: " + expression.getOperator().getValue(), e);
    }

    if (base == ExpressionBase.EUNARY) {
      ExprUnary expression = e.getUnary();
      T t = expression.getOperator().getType();

      //21)
      if (t == T.T_MINUS) {
        return -ce(expression.getOperand());
      }
      //22)
      if (t == T.T_PLUS) {
        return ce(expression.getOperand());
      }

      //23)
      if (t == T.T_TILDE) {
        return ~ce(expression.getOperand());
      }
      //24)
      if (t == T.T_EXCLAMATION) {
        long r = ce(expression.getOperand());
        return (r == 0 ? 1 : 0);
      }

      ErrorLocation.errorExpression("unknown unary operator: " + expression.getOperator().getValue(), e);
    }

    ///////////////////////////////////////////
    // all others...

    if (base == ExpressionBase.EPRIMARY_NUMBER) {
      final IntLiteral number = e.getNumber();
      Type tp = number.getType();
      if (!tp.isInteger()) {
        ErrorLocation.errorExpression("expect integer: " + number.toString(), e);
      }
      return number.getInteger();
    }

    ErrorLocation.errorExpression("not a constant expression ", e);
    return 0;
  }
}
