package ast_expr;

import static tokenize.T.T_AND;
import static tokenize.T.T_AND_EQUAL;
import static tokenize.T.T_DIVIDE;
import static tokenize.T.T_DIVIDE_EQUAL;
import static tokenize.T.T_LSHIFT;
import static tokenize.T.T_LSHIFT_EQUAL;
import static tokenize.T.T_MINUS;
import static tokenize.T.T_MINUS_EQUAL;
import static tokenize.T.T_OR;
import static tokenize.T.T_OR_EQUAL;
import static tokenize.T.T_PERCENT;
import static tokenize.T.T_PERCENT_EQUAL;
import static tokenize.T.T_PLUS;
import static tokenize.T.T_PLUS_EQUAL;
import static tokenize.T.T_RSHIFT;
import static tokenize.T.T_RSHIFT_EQUAL;
import static tokenize.T.T_TIMES;
import static tokenize.T.T_TIMES_EQUAL;
import static tokenize.T.T_XOR;
import static tokenize.T.T_XOR_EQUAL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ast_types.Type;
import ast_types.TypeBindings;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import literals.IntLiteral;
import tokenize.T;
import tokenize.Token;

public abstract class ExprUtil {

  public static Token copyTokenAddNewType(Token from, T newtype, String newvalue) {
    return new Token(from, newvalue, newtype);
  }

  public static Token assignOperator(Token from) {
    return copyTokenAddNewType(from, T.T_ASSIGN, "=");
  }

  public static String funcArgsToString(List<ExprExpression> list) {
    StringBuilder sb = new StringBuilder();
    sb.append("(");

    for (int i = 0; i < list.size(); i++) {
      ExprExpression param = list.get(i);
      sb.append(param.toString());
      if (i + 1 < list.size()) {
        sb.append(", ");
      }
    }

    sb.append(")");
    return sb.toString();
  }

  public static String varsTos(List<VarDeclarator> variables) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < variables.size(); i++) {
      VarDeclarator var = variables.get(i);
      sb.append(var.getIdentifier().getName());
      if (i + 1 < variables.size()) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }

  //@formatter:off
  private static Map<T, T> asopmap = new HashMap<>();
  static {
    asopmap.put(T_TIMES_EQUAL    , T_TIMES);
    asopmap.put(T_PERCENT_EQUAL  , T_PERCENT);
    asopmap.put(T_DIVIDE_EQUAL   , T_DIVIDE);
    asopmap.put(T_PLUS_EQUAL     , T_PLUS);
    asopmap.put(T_MINUS_EQUAL    , T_MINUS);
    asopmap.put(T_LSHIFT_EQUAL   , T_LSHIFT);
    asopmap.put(T_RSHIFT_EQUAL   , T_RSHIFT);
    asopmap.put(T_AND_EQUAL      , T_AND);
    asopmap.put(T_XOR_EQUAL      , T_XOR);
    asopmap.put(T_OR_EQUAL       , T_OR);
  }
  private static Map<T, String> ops = new HashMap<>();
  static {
    ops.put(T_TIMES_EQUAL   , "*");
    ops.put(T_PERCENT_EQUAL , "%");
    ops.put(T_DIVIDE_EQUAL  , "/");
    ops.put(T_PLUS_EQUAL    , "+");
    ops.put(T_MINUS_EQUAL   , "-");
    ops.put(T_LSHIFT_EQUAL  , "<<");
    ops.put(T_RSHIFT_EQUAL  , ">>");
    ops.put(T_AND_EQUAL     , "&");
    ops.put(T_XOR_EQUAL     , "^");
    ops.put(T_OR_EQUAL      , "|");
  }
  //@formatter:on

  // from '+=' we should build a '+' operator
  //
  public static Token getOperatorFromCompAssign(Token from) {
    final String value = ops.get(from.getType());
    final T type = asopmap.get(from.getType());
    if (value == null || type == null) {
      throw new AstParseException("error assign operator: " + from.getLocationToString());
    }
    return new Token(from, value, type);
  }

  public static ExprExpression getEmptyPrimitive(Type tp, Token beginPos) {
    //@formatter:off
    if(tp.is_char())  { return new ExprExpression(make_iconst_i8 (beginPos), beginPos); }
    if(tp.is_short()) { return new ExprExpression(make_iconst_i16(beginPos), beginPos); }
    if(tp.is_int()) { return new ExprExpression(make_iconst_i32(beginPos), beginPos); }
    if(tp.is_long()) { return new ExprExpression(make_iconst_i64(beginPos), beginPos); }
    if(tp.is_float()) { return new ExprExpression(make_iconst_f32(beginPos), beginPos); }
    if(tp.is_double()) { return new ExprExpression(make_iconst_f64(beginPos), beginPos); }
    return null;
    //@formatter:on
  }

  // empty constants for each primitive type
  public static IntLiteral make_iconst_i8(Token beginPos) {
    return new IntLiteral("0_i8", TypeBindings.make_char(beginPos), 0);
  }

  public static IntLiteral make_iconst_i16(Token beginPos) {
    return new IntLiteral("0_i16", TypeBindings.make_short(beginPos), 0);
  }

  public static IntLiteral make_iconst_i32(Token beginPos) {
    return new IntLiteral("0_i32", TypeBindings.make_int(beginPos), 0);
  }

  public static IntLiteral make_iconst_i64(Token beginPos) {
    return new IntLiteral("0_i64", TypeBindings.make_long(beginPos), 0);
  }

  public static IntLiteral make_iconst_f32(Token beginPos) {
    return new IntLiteral("0_f32", TypeBindings.make_float(beginPos), 0);
  }

  public static IntLiteral make_iconst_f64(Token beginPos) {
    return new IntLiteral("0_f64", TypeBindings.make_double(beginPos), 0);
  }

}