package njast.ast_utils;

import static jscan.tokenize.T.T_AND;
import static jscan.tokenize.T.T_AND_EQUAL;
import static jscan.tokenize.T.T_DIVIDE;
import static jscan.tokenize.T.T_DIVIDE_EQUAL;
import static jscan.tokenize.T.T_LSHIFT;
import static jscan.tokenize.T.T_LSHIFT_EQUAL;
import static jscan.tokenize.T.T_MINUS;
import static jscan.tokenize.T.T_MINUS_EQUAL;
import static jscan.tokenize.T.T_OR;
import static jscan.tokenize.T.T_OR_EQUAL;
import static jscan.tokenize.T.T_PERCENT;
import static jscan.tokenize.T.T_PERCENT_EQUAL;
import static jscan.tokenize.T.T_PLUS;
import static jscan.tokenize.T.T_PLUS_EQUAL;
import static jscan.tokenize.T.T_RSHIFT;
import static jscan.tokenize.T.T_RSHIFT_EQUAL;
import static jscan.tokenize.T.T_TIMES;
import static jscan.tokenize.T.T_TIMES_EQUAL;
import static jscan.tokenize.T.T_XOR;
import static jscan.tokenize.T.T_XOR_EQUAL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_nodes.FuncArg;
import njast.ast_nodes.expr.ExprExpression;

public abstract class ExprUtil {

  public static Token copyTokenAddNewType(Token from, T newtype, String newvalue) {

    Token ntoken = new Token(from);
    ntoken.setType(newtype);
    ntoken.setValue(newvalue);
    return ntoken;
  }

  public static Token assignOperator(Token from) {
    return copyTokenAddNewType(from, T.T_ASSIGN, "=");
  }

  public static String exprListCommaToString(List<ExprExpression> list) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < list.size(); i++) {
      ExprExpression param = list.get(i);
      sb.append(param.toString());
      if (i + 1 < list.size()) {
        sb.append(", ");
      }
    }

    return sb.toString();
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

  private static Map<T, T> asopmap = new HashMap<T, T>();
  static {
    asopmap.put(T_TIMES_EQUAL, T_TIMES);
    asopmap.put(T_PERCENT_EQUAL, T_PERCENT);
    asopmap.put(T_DIVIDE_EQUAL, T_DIVIDE);
    asopmap.put(T_PLUS_EQUAL, T_PLUS);
    asopmap.put(T_MINUS_EQUAL, T_MINUS);
    asopmap.put(T_LSHIFT_EQUAL, T_LSHIFT);
    asopmap.put(T_RSHIFT_EQUAL, T_RSHIFT);
    asopmap.put(T_AND_EQUAL, T_AND);
    asopmap.put(T_XOR_EQUAL, T_XOR);
    asopmap.put(T_OR_EQUAL, T_OR);
  }

  public static Token getOperatorFromCompAssign(Token from) {
    Token ntoken = new Token(from);
    ntoken.setType(asopmap.get(from.getType()));
    switch (ntoken.getType()) {
    case T_TIMES:
      ntoken.setValue("*");
      break;
    case T_PERCENT:
      ntoken.setValue("%");
      break;
    case T_DIVIDE:
      ntoken.setValue("/");
      break;
    case T_PLUS:
      ntoken.setValue("+");
      break;
    case T_MINUS:
      ntoken.setValue("-");
      break;
    case T_LSHIFT:
      ntoken.setValue("<<");
      break;
    case T_RSHIFT:
      ntoken.setValue(">>");
      break;
    case T_AND:
      ntoken.setValue("&");
      break;
    case T_XOR:
      ntoken.setValue("^");
      break;
    case T_OR:
      ntoken.setValue("|");
      break;
    default:
      break;
    }
    return ntoken;
  }

}