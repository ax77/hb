package njast.ast_flow;

import static njast.ast_flow.CExpressionBase.EASSIGN;
import static njast.ast_flow.CExpressionBase.EBINARY;
import static njast.ast_flow.CExpressionBase.ECAST;
import static njast.ast_flow.CExpressionBase.ECOMMA;
import static njast.ast_flow.CExpressionBase.EFCALL;
import static njast.ast_flow.CExpressionBase.EPOSTINCDEC;
import static njast.ast_flow.CExpressionBase.EPREINCDEC;
import static njast.ast_flow.CExpressionBase.ETERNARY;
import static njast.ast_flow.CExpressionBase.EUNARY;

import jscan.cstrtox.C_strtox;
import jscan.symtab.Ident;
import jscan.tokenize.Token;
import njast.errors.EParseException;

public class CExpression {

  private static final int LHS_INDEX = 0;
  private static final int RHS_INDEX = 1;
  private static final int CND_INDEX = 2;

  private final CExpressionBase base; // what union contains
  private final Token token; // operator, position
  private final CExpression tree[];

  // primary
  private NumericConstant cnumber;
  private Ident symbol;

  private void assertBaseIsOneOf(CExpressionBase... bases) {
    boolean contains = false;
    for (CExpressionBase b : bases) {
      if (base == b) {
        contains = true;
        break;
      }
    }
    if (!contains) {
      throw new EParseException("you want get tree-node that doe's not exists for this base: " + base.toString());
    }
  }

  public CExpression getLhs() {
    assertBaseIsOneOf(EUNARY, EPREINCDEC, EPOSTINCDEC, EASSIGN, EBINARY, ETERNARY, ECOMMA, ECAST, EFCALL);
    return tree[LHS_INDEX];
  }

  public CExpression getRhs() {
    assertBaseIsOneOf(EASSIGN, EBINARY, ETERNARY, ECOMMA);
    return tree[RHS_INDEX];
  }

  private void setLhs(CExpression e) {
    tree[LHS_INDEX] = e;
  }

  private void setRhs(CExpression e) {
    tree[RHS_INDEX] = e;
  }

  public CExpression getCnd() {
    assertBaseIsOneOf(ETERNARY);
    return tree[CND_INDEX];
  }

  private void setCnd(CExpression condition) {
    tree[CND_INDEX] = condition;
  }

  private CExpression[] emptyTree() {
    return new CExpression[3];
  }

  public Token getToken() {
    return token;
  }

  // binary, asssign, comma, array-subscript
  public CExpression(CExpressionBase base, CExpression lhs, CExpression rhs, Token token) {
    this.base = base;
    this.token = token;
    this.tree = emptyTree();

    setLhs(lhs);
    setRhs(rhs);
  }

  public CExpression(C_strtox e, Token token) {
    e.ev(); // XXX:

    this.tree = emptyTree();
    this.token = token;
    this.base = CExpressionBase.EPRIMARY_NUMBER;

    NumericConstant number = null;
    if (e.isIntegerKind()) {
      number = new NumericConstant(e.getClong(), e.getNumtype());
    } else {
      number = new NumericConstant(e.getCdouble(), e.getNumtype());
    }

    this.cnumber = number;
  }

  // ternary
  public CExpression(CExpression condition, CExpression branchTrue, CExpression branchFalse, Token token) {
    this.tree = emptyTree();
    this.token = token;
    this.base = CExpressionBase.ETERNARY;

    setCnd(condition);
    setLhs(branchTrue);
    setRhs(branchFalse);
  }

  // unary
  public CExpression(Token op, CExpression lhs) {
    this.base = CExpressionBase.EUNARY;
    this.token = op;
    this.tree = emptyTree();

    setLhs(lhs);
  }

  // identifier.
  public CExpression(Token saved) {
    this.base = CExpressionBase.EPRIMARY_IDENT;
    this.token = saved;
    this.tree = emptyTree();
    this.symbol = saved.getIdent();
  }

  public CExpression[] getTree() {
    return tree;
  }

  private String tokenTos(Token t) {
    return " " + t.getValue() + " ";
  }

  @Override
  public String toString() {

    switch (base) {

    case EASSIGN: {
      return "(" + getLhs().toString().trim() + tokenTos(getToken()) + getRhs().toString().trim() + ")";
    }

    case EBINARY: {
      String lhs = getLhs().toString();
      String rhs = getRhs().toString();
      return "(" + lhs + tokenTos(getToken()) + rhs + ")";
    }

    case ECOMMA: {
      return getLhs().toString() + tokenTos(getToken()) + getRhs().toString();
    }

    case ETERNARY: {
      return "(" + getCnd().toString().trim() + " ? " + getLhs().toString().trim() + " : " + getRhs().toString().trim()
          + ")";
    }

    case EUNARY: {
      return "(" + getToken().getValue() + getLhs().toString() + ")";
    }

    case ECAST: {
      return "(cast)";//"(" + resultType.toString() + ") " + "(" + getLhs().toString() + ")";
    }

    case EFCALL: {
      StringBuilder sb = new StringBuilder();
      //      sb.append(getLhs().toString() + "(");
      //
      //      int argc = 0;
      //      for (CExpression e : arglist) {
      //        sb.append(e.toString());
      //        if (argc < arglist.size() - 1) {
      //          sb.append(",");
      //        }
      //        ++argc;
      //      }

      sb.append(")");
      return sb.toString();
    }

    case EPREINCDEC: {
      return "(" + getToken().getValue() + getLhs().toString() + ")";
    }

    case EPOSTINCDEC: {
      return "(" + getLhs().toString() + getToken().getValue() + ")";
    }

    case EPRIMARY_IDENT: {
      if (symbol == null) {
        return "<null>";
      }
      return symbol.getName();
    }

    case EPRIMARY_NUMBER: {
      if (cnumber.isInteger()) {
        return String.format("%d", cnumber.getClong());
      } else {
        return String.format("%f", cnumber.getCdouble());
      }
    }

    case EPRIMARY_STRING: {
      return "";
    }

    default: {
      throw new EParseException("unknown: " + base.toString());
    }
    }

  }

  public NumericConstant getCnumber() {
    return cnumber;
  }

  public void setCnumber(NumericConstant cnumber) {
    this.cnumber = cnumber;
  }

}
