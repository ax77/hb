package njast.ast_parsers;

import static jscan.tokenize.T.TOKEN_CHAR;
import static jscan.tokenize.T.TOKEN_NUMBER;
import static jscan.tokenize.T.TOKEN_STRING;
import static jscan.tokenize.T.T_AND;
import static jscan.tokenize.T.T_AND_AND;
import static jscan.tokenize.T.T_COLON;
import static jscan.tokenize.T.T_DIVIDE;
import static jscan.tokenize.T.T_EQ;
import static jscan.tokenize.T.T_GE;
import static jscan.tokenize.T.T_GT;
import static jscan.tokenize.T.T_LE;
import static jscan.tokenize.T.T_LEFT_PAREN;
import static jscan.tokenize.T.T_LSHIFT;
import static jscan.tokenize.T.T_LT;
import static jscan.tokenize.T.T_MINUS;
import static jscan.tokenize.T.T_NE;
import static jscan.tokenize.T.T_OR;
import static jscan.tokenize.T.T_OR_OR;
import static jscan.tokenize.T.T_PERCENT;
import static jscan.tokenize.T.T_PLUS;
import static jscan.tokenize.T.T_QUESTION;
import static jscan.tokenize.T.T_RIGHT_PAREN;
import static jscan.tokenize.T.T_RSHIFT;
import static jscan.tokenize.T.T_TIMES;
import static jscan.tokenize.T.T_XOR;

import java.util.ArrayList;
import java.util.List;

import jscan.cstrtox.C_strtox;
import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_checkers.IsIdent;
import njast.ast_kinds.ExpressionBase;
import njast.ast_nodes.expr.Binary;
import njast.ast_nodes.expr.ExpressionNode;
import njast.ast_nodes.expr.FieldAccess;
import njast.ast_nodes.expr.MethodInvocation;
import njast.ast_nodes.expr.Ternary;
import njast.ast_nodes.expr.Unary;
import njast.ast_utils.ExprUtil;
import njast.parse.Parse;
import njast.parse.ParseState;

public class ParseExpression {
  private final Parse parser;

  public ParseExpression(Parse parser) {
    this.parser = parser;
  }

  private ExpressionNode build_unary(Token op, ExpressionNode operand) {
    return new ExpressionNode(new Unary(op, operand));
  }

  private ExpressionNode build_binary(Token operator, ExpressionNode lhs, ExpressionNode rhs) {
    return new ExpressionNode(new Binary(operator, lhs, rhs));
  }

  private ExpressionNode build_ternary(ExpressionNode cnd, ExpressionNode btrue, ExpressionNode bfalse, Token tok) {
    return new ExpressionNode(new Ternary(cnd, btrue, bfalse));
  }

  private ExpressionNode build_assign(Token tok, ExpressionNode lvalue, ExpressionNode rvalue) {
    return new ExpressionNode(new Binary(tok, lvalue, rvalue));
  }

  private ExpressionNode build_comma(Token tok, ExpressionNode lhs, ExpressionNode rhs) {
    return new ExpressionNode(new Binary(tok, lhs, rhs));
  }

  // numeric-char-constants
  private ExpressionNode build_number(C_strtox e, Token token) {
    return new ExpressionNode(e, token);
  }

  public ExpressionNode e_expression() {
    ExpressionNode e = e_assign();

    while (parser.tp() == T.T_COMMA) {
      Token saved = parser.checkedMove(T.T_COMMA);
      e = build_comma(saved, e, e_expression());
    }

    return e;
  }

  public ExpressionNode e_const_expr() {
    return e_cnd();
  }

  public ExpressionNode getExprInParen() {
    parser.checkedMove(T_LEFT_PAREN);
    ExpressionNode e = e_expression();
    parser.checkedMove(T.T_RIGHT_PAREN);
    return e;
  }

  private boolean isCompoundAssign(Token what) {
    return IsIdent.isAssignOperator(what) && !what.ofType(T.T_ASSIGN);
  }

  public ExpressionNode e_assign() {
    ExpressionNode lhs = e_cnd();

    // if simple, then: this...
    //
    //    if (parser.isAssignOperator()) {
    //      Token saved = parser.tok();
    //      parser.move();
    //      final CExpression rhs = e_assign();
    //      lhs = build_assign(saved, lhs, rhs);
    //    }

    if (IsIdent.isAssignOperator(parser.tok())) {

      Token saved = parser.tok();

      if (isCompoundAssign(saved)) {
        parser.move();

        // linearize compound assign
        // a+=b :: a=a+b
        //
        // += lhs(a) rhs(b)
        // = lhs(a) rhs( + lhs(a) rhs(b) )

        Token assignOperator = ExprUtil.assignOperator(saved);
        Token binaryOperator = ExprUtil.getOperatorFromCompAssign(saved);

        ExpressionNode rhs = build_binary(binaryOperator, lhs, e_assign());
        lhs = build_assign(assignOperator, lhs, rhs);
      }

      else {

        parser.move();
        lhs = build_assign(saved, lhs, e_assign());
      }

    }

    return lhs;
  }

  private ExpressionNode e_cnd() {
    ExpressionNode res = e_lor();

    if (parser.tp() != T_QUESTION) {
      return res;
    }

    Token saved = parser.tok();
    parser.move();

    ExpressionNode btrue = e_expression();
    parser.checkedMove(T_COLON);

    return build_ternary(res, btrue, e_cnd(), saved);
  }

  private ExpressionNode e_lor() {
    ExpressionNode e = e_land();
    while (parser.tp() == T_OR_OR) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_land());
    }
    return e;
  }

  private ExpressionNode e_land() {
    ExpressionNode e = e_bor();
    while (parser.tp() == T_AND_AND) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_bor());
    }
    return e;
  }

  private ExpressionNode e_bor() {
    ExpressionNode e = e_bxor();
    while (parser.tp() == T_OR) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_bxor());
    }
    return e;
  }

  private ExpressionNode e_bxor() {
    ExpressionNode e = e_band();
    while (parser.tp() == T_XOR) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_band());
    }
    return e;
  }

  private ExpressionNode e_band() {
    ExpressionNode e = e_equality();
    while (parser.tp() == T_AND) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_equality());
    }
    return e;
  }

  private ExpressionNode e_equality() {
    ExpressionNode e = e_relational();
    while (parser.tp() == T_EQ || parser.tp() == T_NE) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_relational());
    }
    return e;
  }

  private ExpressionNode e_relational() {
    ExpressionNode e = e_shift();
    while (parser.tp() == T_LT || parser.tp() == T_GT || parser.tp() == T_LE || parser.tp() == T_GE) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_shift());
    }
    return e;
  }

  private ExpressionNode e_shift() {
    ExpressionNode e = e_add();
    while (parser.tp() == T_LSHIFT || parser.tp() == T_RSHIFT) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_add());
    }
    return e;
  }

  private ExpressionNode e_add() {
    ExpressionNode e = e_mul();
    while (parser.tp() == T_PLUS || parser.tp() == T_MINUS) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_mul());
    }
    return e;
  }

  private ExpressionNode e_mul() {
    ExpressionNode e = e_cast();
    while (parser.tp() == T_TIMES || parser.tp() == T_DIVIDE || parser.tp() == T_PERCENT) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_cast());
    }
    return e;
  }

  private ExpressionNode e_cast() {

    //    if (parser.tp() == T_LEFT_PAREN) {
    //      ParseState state = new ParseState(parser);
    //
    //      Token peek = parser.peek();
    //      if (parser.isDeclSpecStart(peek)) {
    //
    //        Token lparen = parser.lparen();
    //        CType typeName = parser.parseTypename();
    //        parser.rparen();
    //
    //        // ambiguous
    //        // "(" type-name ")" "{" initializer-list "}"
    //        // "(" type-name ")" "{" initializer-list "," "}"
    //
    //        if (parser.tp() != T.T_LEFT_BRACE) {
    //          final CExpression tocast = e_cast();
    //          return build_cast(parser, typeName, tocast, lparen);
    //        }
    //      }
    //
    //      parser.restoreState(state);
    //
    //    }

    return e_unary();
  }

  private ExpressionNode e_unary() {

    // [& * + - ~ !]
    if (IsIdent.isUnaryOperator(parser.tok())) {
      Token operator = parser.tok();
      parser.move();
      return build_unary(operator, e_cast());
    }

    return e_postfix();
  }

  private ExpressionNode e_postfix() {

    ExpressionNode lhs = e_prim();

    for (;;) {
      if (parser.is(T.T_DOT)) {

        ParseState parseState = new ParseState(parser);

        final Token dot = parser.moveget();
        final Token peek = parser.peek();

        if (IsIdent.isUserDefinedIdentNoKeyword(parser.tok()) && peek.ofType(T.T_LEFT_PAREN)) {
          Ident ident = parser.getIdent();

          // TODO: think about it, is it correct?
          // the result tree is correct as I think, but I'm not sure.
          // maybe I have to rebuild the node from field-access to func-call
          // for example: the path like "a().b().c"
          if (lhs.getBase() == ExpressionBase.EFIELD_ACCESS) {
          }

          lhs = methodInvocation(ident, lhs);
        }

        else {
          parser.restoreState(parseState); // normal field access like: "a.b.c.d.e"
          lhs = fieldAccess(lhs);
        }

      }

      else if (parser.is(T_LEFT_PAREN)) {
        Ident funcname = lhs.getSymbol();
        if (funcname == null) {
          parser.perror("expect function name");
        }

        lhs = methodInvocation(funcname); // TODO: more clean, more precise.
      }

      else {
        break;
      }
    }

    //    for (;;) {
    //
    //      // function - call
    //      //
    //      if (parser.tp() == T_LEFT_PAREN) {
    //        Token lparen = parser.lparen();
    //
    //        List<CExpression> arglist = new ArrayList<CExpression>();
    //
    //        if (parser.tp() != T_RIGHT_PAREN) {
    //          CExpression onearg = e_assign();
    //          arglist.add(onearg);
    //
    //          while (parser.tp() == T.T_COMMA) {
    //            parser.move();
    //
    //            CExpression oneargSeq = e_assign();
    //            arglist.add(oneargSeq);
    //          }
    //        }
    //
    //        lhs = build_fcall(lhs, arglist, lparen);
    //        parser.rparen();
    //      }
    //
    //      // direct|indirect selection
    //      //
    //      else if (parser.tp() == T_DOT || parser.tp() == T_ARROW) {
    //        Token operator = parser.tok();
    //        parser.move(); // move . or ->
    //
    //        Ident fieldName = parser.getIdent();
    //        TypeApplier.applytype(lhs, TypeApplierStage.stage_start);
    //
    //        // a->b :: (*a).b
    //        if (operator.ofType(T_ARROW)) {
    //
    //          final Token operatorDeref = ExprUtil.derefOperator(operator);
    //          final Token operatorDot = ExprUtil.dotOperator(operator);
    //          final CStructField field = getFieldArrow(lhs, fieldName);
    //
    //          CExpression inBrace = build_unary(operatorDeref, lhs);
    //          lhs = build_compsel(inBrace, operatorDot, field);
    //        }
    //
    //        else {
    //
    //          final CStructField field = getFieldDot(lhs, fieldName);
    //          lhs = build_compsel(lhs, operator, field);
    //        }
    //
    //      }
    //
    //      // ++ --
    //      //
    //      else if (parser.tp() == T.T_PLUS_PLUS || parser.tp() == T_MINUS_MINUS) {
    //        Token operator = parser.tok();
    //        parser.move();
    //        lhs = build_incdec(CExpressionBase.EPOSTINCDEC, operator, lhs);
    //      }
    //
    //      // array-subscript
    //      //
    //      else if (parser.tp() == T.T_LEFT_BRACKET) {
    //        while (parser.tp() == T_LEFT_BRACKET) {
    //          Token lbrack = parser.lbracket();
    //
    //          // a[5] :: *(a+5)
    //          Token operatorPlus = ExprUtil.plusOperator(lbrack);
    //          Token operatorDeref = ExprUtil.derefOperator(lbrack);
    //
    //          CExpression inBrace = build_binary(operatorPlus, lhs, e_expression());
    //          lhs = build_unary(operatorDeref, inBrace);
    //
    //          parser.rbracket();
    //        }
    //      }
    //
    //      else {
    //        break;
    //      }
    //    }

    return lhs;
  }

  private ExpressionNode fieldAccess(ExpressionNode lhs) {

    Token operator = parser.moveget();
    Token identifier = parser.checkedMove(T.TOKEN_IDENT);

    lhs = new ExpressionNode(new FieldAccess(identifier.getIdent(), lhs));
    return lhs;
  }

  private ExpressionNode methodInvocation(Ident funcname) {
    Token lparen = parser.lparen();
    List<ExpressionNode> arglist = parseArglist();
    Token rparen = parser.rparen();
    return new ExpressionNode(new MethodInvocation(funcname, arglist));
  }

  private ExpressionNode methodInvocation(Ident funcname, ExpressionNode lhs) {
    Token lparen = parser.lparen();

    List<ExpressionNode> arglist = parseArglist();

    lhs = new ExpressionNode(new MethodInvocation(funcname, lhs, arglist));

    Token rparen = parser.rparen();
    return lhs;
  }

  private List<ExpressionNode> parseArglist() {
    List<ExpressionNode> arglist = new ArrayList<ExpressionNode>();

    if (parser.tp() != T_RIGHT_PAREN) {
      ExpressionNode onearg = e_assign();
      arglist.add(onearg);

      while (parser.tp() == T.T_COMMA) {
        parser.move();

        ExpressionNode oneargSeq = e_assign();
        arglist.add(oneargSeq);
      }
    }
    return arglist;
  }

  //primary-expression:
  //    | literal
  //    | simple-name
  //    | parenthesized-expression
  //    | member-access
  //    | invocation-expression
  //       
  //member-access:
  //    | primary-expression . identifier
  //    
  //invocation-expression:
  //    | primary-expression ( argument-listopt )
  //    
  //literal:
  //    | boolean-literal
  //    | integer-literal
  //    | real-literal
  //    | character-literal
  //    | string-literal
  //    | null-literal
  //    
  //simple-name:
  //    | identifier
  //    
  //--->>>
  //primary-expression:
  //    | literal
  //    | simple-name
  //    | parenthesized-expression
  //    | primary-expression . identifier
  //    | primary-expression ( argument-listopt )

  private ExpressionNode e_prim() {

    if (parser.tp() == TOKEN_NUMBER || parser.tp() == TOKEN_CHAR || parser.tp() == TOKEN_STRING) {
      Token saved = parser.moveget();

      if (saved.ofType(TOKEN_STRING)) {
        parser.unimplemented("string constants");
      } else {
        return primaryNumber(saved);
      }
    }

    if (parser.tp() == T.TOKEN_IDENT) {
      Token saved = parser.moveget();
      return new ExpressionNode(saved.getIdent());
    }

    if (parser.tp() == T_LEFT_PAREN) {
      Token lparen = parser.moveget();
      ExpressionNode e = e_expression();
      Token rparen = parser.checkedMove(T_RIGHT_PAREN);
      return e;
    }

    parser.perror("something wrong in expression...");
    return null; // you never return this ;)

  }

  private ExpressionNode primaryNumber(Token saved) {
    //TODO:NUMBERS
    String toeval = "";
    if (saved.ofType(TOKEN_CHAR)) {
      toeval = String.format("%d", saved.getCharconstant().getV());
    } else {
      toeval = saved.getValue();
    }

    // TODO:NUMBERS
    C_strtox strtox = new C_strtox(toeval);
    return build_number(strtox, saved);
  }

}
