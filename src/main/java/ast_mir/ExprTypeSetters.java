package ast_mir;

import static tokenize.T.T_AND;
import static tokenize.T.T_AND_AND;
import static tokenize.T.T_DIVIDE;
import static tokenize.T.T_EQ;
import static tokenize.T.T_EXCLAMATION;
import static tokenize.T.T_GE;
import static tokenize.T.T_GT;
import static tokenize.T.T_LE;
import static tokenize.T.T_LSHIFT;
import static tokenize.T.T_LT;
import static tokenize.T.T_MINUS;
import static tokenize.T.T_NE;
import static tokenize.T.T_OR;
import static tokenize.T.T_OR_OR;
import static tokenize.T.T_PERCENT;
import static tokenize.T.T_PLUS;
import static tokenize.T.T_RSHIFT;
import static tokenize.T.T_TILDE;
import static tokenize.T.T_TIMES;
import static tokenize.T.T_XOR;

import ast_expr.ExprBinary;
import ast_expr.ExprExpression;
import ast_expr.ExpressionBase;
import ast_types.Type;
import ast_types.TypeBindings;
import errors.AstParseException;
import tokenize.T;
import tokenize.Token;
import utils_oth.NullChecker;

public class ExprTypeSetters {

  private static boolean in(T op, T[] where) {
    for (T t : where) {
      if (op.equals(t)) {
        return true;
      }
    }
    return false;
  }

  public static void setBinaryType(final ExprExpression e, Token operator) {

    final T op = operator.getType();
    checkOpIsBinary(op);

    final ExprBinary binary = e.getBinary();
    final ExprExpression lhs = binary.getLhs();
    final ExprExpression rhs = binary.getRhs();
    final Type lhsType = lhs.getResultType();
    final Type rhsType = rhs.getResultType();

    if (!in(op, new T[] { T_EQ, T_NE })) {
      checkTypeNotNull(lhsType);
      checkTypeNotNull(rhsType);
      if (!lhsType.is_equal_to(rhsType)) {
        errorNoComatible(e, lhs, op, rhs);
      }
    }

    // == !=
    if (in(op, new T[] { T_EQ, T_NE })) {
      // T_EQ,T_NE->boolean
      // is_numeric|is_numeric
      // is_boolean|is_boolean
      // is_reference|is_reference

      final Type resultType = TypeBindings.make_boolean();

      if (lhsType != null && rhsType != null) {
        if (!lhsType.is_equal_to(rhsType)) {
          errorNoComatible(e, lhs, op, rhs);
        }
        if (lhsType.is_numeric() && rhsType.is_numeric()) {
          e.setResultType(resultType);
        }
        if (lhsType.is_boolean() && rhsType.is_boolean()) {
          e.setResultType(resultType);
        }
        if (lhsType.is_reference() && rhsType.is_reference()) {
          e.setResultType(resultType);
        }
      }

      else {
        // is_reference|is_null_literal
        // is_null_literal|is_reference
        if (lhsType == null) {
          NullChecker.check(rhsType);
          if (lhs.is(ExpressionBase.EPRIMARY_NULL_LITERAL) && rhsType.is_reference()) {
            e.setResultType(resultType);
          }
        }
        if (rhsType == null) {
          NullChecker.check(lhsType);
          if (lhsType.is_reference() && rhs.is(ExpressionBase.EPRIMARY_NULL_LITERAL)) {
            e.setResultType(resultType);
          }
        }
      }

    }

    // + -
    else if (in(op, new T[] { T_PLUS, T_MINUS })) {
      // T_PLUS,T_MINUS->numeric
      // is_numeric|is_numeric
      if (lhsType.is_numeric() && rhsType.is_numeric()) {
        e.setResultType(lhsType);
      }
    }

    // << >>
    else if (in(op, new T[] { T_LSHIFT, T_RSHIFT })) {
      // T_LSHIFT,T_RSHIFT->integral
      // is_integer|is_integer
      if (lhsType.is_integer() && rhsType.is_integer()) {
        e.setResultType(lhsType);
      }
    }

    // < <= > >=
    else if (in(op, new T[] { T_LT, T_LE, T_GT, T_GE })) {
      // T_LT,T_LE,T_GT,T_GE->boolean
      // is_numeric|is_numeric
      if (lhsType.is_numeric() && rhsType.is_numeric()) {
        e.setResultType(TypeBindings.make_boolean());
      }
    }

    // && ||
    else if (in(op, new T[] { T_AND_AND, T_OR_OR })) {
      // T_AND_AND,T_OR_OR->boolean
      // is_boolean|is_boolean
      if (lhsType.is_boolean() && rhsType.is_boolean()) {
        e.setResultType(lhsType);
      }
    }

    // & | ^
    else if (in(op, new T[] { T_AND, T_OR, T_XOR })) {
      // T_AND,T_OR,T_XOR->integral
      // is_integer|is_integer
      if (lhsType.is_integer() && rhsType.is_integer()) {
        e.setResultType(lhsType);
      }
    }

    else {
      throw new AstParseException("unimpl. op: " + operator.getValue());
    }

    if (e.getResultType() == null) {
      errorNoComatible(e, lhs, op, rhs);
    }

  }

  private static void errorNoComatible(ExprExpression e, ExprExpression lhs, T op, ExprExpression rhs) {
    throw new AstParseException("TODO: no-compat: " + e.toString());

  }

  private static void checkTypeNotNull(Type tp) {
    NullChecker.check(tp);
  }

  private static void checkOpIsBinary(T op) {
    //@formatter:off
    boolean isOk = 
           op == T_EQ          
        || op == T_NE          
        || op == T_LT          
        || op == T_LE          
        || op == T_GT          
        || op == T_GE          
        || op == T_LSHIFT      
        || op == T_RSHIFT      
        || op == T_EXCLAMATION 
        || op == T_AND_AND     
        || op == T_OR_OR       
        || op == T_PLUS        
        || op == T_MINUS       
        || op == T_TIMES       
        || op == T_DIVIDE      
        || op == T_PERCENT     
        || op == T_AND         
        || op == T_OR          
        || op == T_TILDE       
        || op == T_XOR;  
    //@formatter:on
    if (!isOk) {
      throw new AstParseException("not an binary operator: " + op.toString());
    }
  }
}
