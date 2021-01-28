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

  public static String exprListCommaToString1(List<FuncArg> list) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < list.size(); i++) {
      FuncArg param = list.get(i);
      sb.append(param.toString());
      if (i + 1 < list.size()) {
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
    if(tp.is_i8())  { return new ExprExpression(make_iconst_i8() , beginPos); }
    if(tp.is_u8())  { return new ExprExpression(make_iconst_u8() , beginPos); }
    if(tp.is_i16()) { return new ExprExpression(make_iconst_i16(), beginPos); }
    if(tp.is_u16()) { return new ExprExpression(make_iconst_u16(), beginPos); }
    if(tp.is_i32()) { return new ExprExpression(make_iconst_i32(), beginPos); }
    if(tp.is_u32()) { return new ExprExpression(make_iconst_u32(), beginPos); }
    if(tp.is_i64()) { return new ExprExpression(make_iconst_i64(), beginPos); }
    if(tp.is_u64()) { return new ExprExpression(make_iconst_u64(), beginPos); }
    if(tp.is_f32()) { return new ExprExpression(make_iconst_f32(), beginPos); }
    if(tp.is_f64()) { return new ExprExpression(make_iconst_f64(), beginPos); }
    return null;
    //@formatter:on
  }

  // empty constants for each primitive type
  public static IntLiteral make_iconst_i8() {
    return new IntLiteral("0_i8", TypeBindings.make_i8(), 0);
  }

  public static IntLiteral make_iconst_u8() {
    return new IntLiteral("0_u8", TypeBindings.make_u8(), 0);
  }

  public static IntLiteral make_iconst_i16() {
    return new IntLiteral("0_i16", TypeBindings.make_i16(), 0);
  }

  public static IntLiteral make_iconst_u16() {
    return new IntLiteral("0_u16", TypeBindings.make_u16(), 0);
  }

  public static IntLiteral make_iconst_i32() {
    return new IntLiteral("0_i32", TypeBindings.make_i32(), 0);
  }

  public static IntLiteral make_iconst_u32() {
    return new IntLiteral("0_u32", TypeBindings.make_u32(), 0);
  }

  public static IntLiteral make_iconst_i64() {
    return new IntLiteral("0_i64", TypeBindings.make_i64(), 0);
  }

  public static IntLiteral make_iconst_u64() {
    return new IntLiteral("0_u64", TypeBindings.make_u64(), 0);
  }

  public static IntLiteral make_iconst_f32() {
    return new IntLiteral("0_f32", TypeBindings.make_f32(), 0);
  }

  public static IntLiteral make_iconst_f64() {
    return new IntLiteral("0_f64", TypeBindings.make_f64(), 0);
  }

}