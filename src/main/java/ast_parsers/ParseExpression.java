package ast_parsers;

import static tokenize.T.TOKEN_CHAR;
import static tokenize.T.TOKEN_NUMBER;
import static tokenize.T.TOKEN_STRING;
import static tokenize.T.T_AND;
import static tokenize.T.T_AND_AND;
import static tokenize.T.T_DIVIDE;
import static tokenize.T.T_EQ;
import static tokenize.T.T_GE;
import static tokenize.T.T_GT;
import static tokenize.T.T_LE;
import static tokenize.T.T_LEFT_PAREN;
import static tokenize.T.T_LSHIFT;
import static tokenize.T.T_LT;
import static tokenize.T.T_MINUS;
import static tokenize.T.T_NE;
import static tokenize.T.T_OR;
import static tokenize.T.T_OR_OR;
import static tokenize.T.T_PERCENT;
import static tokenize.T.T_PLUS;
import static tokenize.T.T_QUESTION;
import static tokenize.T.T_RIGHT_PAREN;
import static tokenize.T.T_RSHIFT;
import static tokenize.T.T_TIMES;
import static tokenize.T.T_XOR;

import java.util.ArrayList;
import java.util.List;

import ast_checkers.IdentRecognizer;
import ast_checkers.TypeRecognizer;
import ast_class.ClassDeclaration;
import ast_expr.ExprArrayAccess;
import ast_expr.ExprArrayCreation;
import ast_expr.ExprAssign;
import ast_expr.ExprBinary;
import ast_expr.ExprClassCreation;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_expr.ExprSelf;
import ast_expr.ExprUnary;
import ast_expr.ExprUtil;
import ast_expr.ExpressionBase;
import ast_expr.FuncArg;
import ast_symtab.IdentMap;
import ast_types.ClassType;
import ast_types.Type;
import hashed.Hash_ident;
import parse.Parse;
import parse.ParseState;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public class ParseExpression {
  private final Parse parser;

  public ParseExpression(Parse parser) {
    this.parser = parser;
  }

  private ExprExpression build_unary(Token op, ExprExpression operand) {
    return new ExprExpression(new ExprUnary(op, operand));
  }

  private ExprExpression build_binary(Token operator, ExprExpression lhs, ExprExpression rhs) {
    return new ExprExpression(new ExprBinary(operator, lhs, rhs));
  }

  private ExprExpression build_assign(Token tok, ExprExpression lvalue, ExprExpression rvalue) {
    return new ExprExpression(new ExprAssign(tok, lvalue, rvalue));
  }

  public ExprExpression e_expression() {
    ExprExpression e = e_assign();

    if (parser.is(T.T_COMMA)) {
      parser.errorCommaExpression();
    }

    return e;
  }

  public ExprExpression e_const_expr() {
    return e_cnd();
  }

  public ExprExpression getExprInParen() {
    parser.checkedMove(T_LEFT_PAREN);
    ExprExpression e = e_expression();
    parser.checkedMove(T.T_RIGHT_PAREN);
    return e;
  }

  private boolean isCompoundAssign(Token what) {
    return IdentRecognizer.isAssignOperator(what) && !what.ofType(T.T_ASSIGN);
  }

  public ExprExpression e_assign() {
    ExprExpression lhs = e_cnd();

    // if simple, then: this...
    //
    //    if (parser.isAssignOperator()) {
    //      Token saved = parser.tok();
    //      parser.move();
    //      final CExpression rhs = e_assign();
    //      lhs = build_assign(saved, lhs, rhs);
    //    }

    if (IdentRecognizer.isAssignOperator(parser.tok())) {

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

        ExprExpression rhs = build_binary(binaryOperator, lhs, e_assign());
        lhs = build_assign(assignOperator, lhs, rhs);
      }

      else {

        parser.move();
        lhs = build_assign(saved, lhs, e_assign());
      }

    }

    checkMultipleAssign(lhs);
    return lhs;
  }

  // we prevent assignment form like: a = b = c = d = e;
  // it makes a mess in your code.
  //
  private void checkMultipleAssign(ExprExpression expression) {
    if (expression.getBase() == ExpressionBase.EASSIGN) {
      final ExprAssign assignExpr = expression.getAssign();

      final ExprExpression lvalue = assignExpr.getLvalue();
      if (lvalue.getBase() == ExpressionBase.EASSIGN) {
        parser.perror("multiple assignment are deprecated: [a=b=c]");
      }

      final ExprExpression rvalue = assignExpr.getRvalue();
      if (rvalue.getBase() == ExpressionBase.EASSIGN) {
        parser.perror("multiple assignment are deprecated: [a=b=c]");
      }

      checkMultipleAssign(lvalue);
      checkMultipleAssign(rvalue);
    }
  }

  private ExprExpression e_cnd() {

    // WAS:
    // ExprExpression res = e_lor();
    //
    // if (parser.tp() != T_QUESTION) {
    //   return res;
    // }
    //
    // Token saved = parser.tok();
    // parser.move();
    //
    // ExprExpression btrue = e_expression();
    // parser.checkedMove(T_COLON);
    //
    // return build_ternary(res, btrue, e_cnd(), saved);

    ExprExpression res = e_lor();
    if (parser.is(T_QUESTION)) {
      parser.perror(
          "conditional expression [cond ? if_true : if_false;] is deprecated by design. use [if(cond) {} else {}] instead.");
    }
    return res;
  }

  private ExprExpression e_lor() {
    ExprExpression e = e_land();
    while (parser.tp() == T_OR_OR) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_land());
    }
    return e;
  }

  private ExprExpression e_land() {
    ExprExpression e = e_bor();
    while (parser.tp() == T_AND_AND) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_bor());
    }
    return e;
  }

  private ExprExpression e_bor() {
    ExprExpression e = e_bxor();
    while (parser.tp() == T_OR) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_bxor());
    }
    return e;
  }

  private ExprExpression e_bxor() {
    ExprExpression e = e_band();
    while (parser.tp() == T_XOR) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_band());
    }
    return e;
  }

  private ExprExpression e_band() {
    ExprExpression e = e_equality();
    while (parser.tp() == T_AND) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_equality());
    }
    return e;
  }

  private ExprExpression e_equality() {
    ExprExpression e = e_relational();
    while (parser.tp() == T_EQ || parser.tp() == T_NE) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_relational());
    }
    return e;
  }

  private ExprExpression e_relational() {
    ExprExpression e = e_shift();
    while (parser.tp() == T_LT || parser.tp() == T_GT || parser.tp() == T_LE || parser.tp() == T_GE) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_shift());
    }
    return e;
  }

  private ExprExpression e_shift() {
    ExprExpression e = e_add();
    while (parser.tp() == T_LSHIFT || parser.tp() == T_RSHIFT) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_add());
    }
    return e;
  }

  private ExprExpression e_add() {
    ExprExpression e = e_mul();
    while (parser.tp() == T_PLUS || parser.tp() == T_MINUS) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_mul());
    }
    return e;
  }

  private ExprExpression e_mul() {
    ExprExpression e = e_cast();
    while (parser.tp() == T_TIMES || parser.tp() == T_DIVIDE || parser.tp() == T_PERCENT) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_cast());
    }
    return e;
  }

  private ExprExpression e_cast() {

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

  private ExprExpression e_unary() {

    // [& * + - ~ !]
    if (IdentRecognizer.isUnaryOperator(parser.tok())) {
      Token operator = parser.tok();
      parser.move();
      return build_unary(operator, e_cast());
    }

    if (parser.is(T.T_PLUS_PLUS) || parser.is(T.T_MINUS_MINUS)) {
      parser.perror("pre-increment/pre-decrement are deprecated by design.");
    }

    return e_postfix();
  }

  private ExprExpression e_postfix() {

    ExprExpression lhs = e_prim();

    for (;;) {
      if (parser.is(T.T_DOT)) {

        ParseState parseState = new ParseState(parser);

        final Token dot = parser.moveget();
        final Token peek = parser.peek();

        final boolean dot_ident_lparen = IdentRecognizer.isUserDefinedIdentNoKeyword(parser.tok())
            && peek.ofType(T.T_LEFT_PAREN);

        if (dot_ident_lparen) {
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
        Ident funcname = lhs.getIdent().getIdentifier();
        if (funcname == null) {
          parser.perror("expect function name");
        }

        lhs = methodInvocation(funcname); // TODO: more clean, more precise.
      }

      else if (parser.is(T.T_PLUS_PLUS) || parser.is(T.T_MINUS_MINUS)) {
        parser.perror("post-increment/post-decrement are deprecated by design.");
      }

      // array-subscript
      //
      else if (parser.is(T.T_LEFT_BRACKET)) {
        parser.lbracket();
        lhs = new ExprExpression(new ExprArrayAccess(lhs, e_expression()));
        parser.rbracket();

        while (parser.is(T.T_LEFT_BRACKET)) {
          parser.lbracket();
          lhs = new ExprExpression(new ExprArrayAccess(lhs, e_expression()));
          parser.rbracket();
        }
      }

      else {
        break;
      }
    }

    return lhs;
  }

  private ExprExpression fieldAccess(ExprExpression lhs) {

    Token operator = parser.moveget();
    Token identifier = parser.checkedMove(T.TOKEN_IDENT);

    lhs = new ExprExpression(new ExprFieldAccess(identifier.getIdent(), lhs));
    return lhs;
  }

  private ExprExpression methodInvocation(Ident funcname) {
    // apply <this.> before function name, more convenient
    ExprExpression selfExpression = new ExprExpression(new ExprSelf(parser.getCurrentClass(true)));
    List<FuncArg> arglist = parseArglist();
    return new ExprExpression(new ExprMethodInvocation(funcname, selfExpression, arglist));
  }

  private ExprExpression methodInvocation(Ident funcname, ExprExpression lhs) {
    List<FuncArg> arglist = parseArglist();
    lhs = new ExprExpression(new ExprMethodInvocation(funcname, lhs, arglist));
    return lhs;
  }

  private List<FuncArg> parseArglist() {

    Token lparen = parser.lparen();
    List<FuncArg> arglist = new ArrayList<>();

    if (parser.tp() != T_RIGHT_PAREN) {
      arglist.add(getOneArg());

      while (parser.tp() == T.T_COMMA) {
        parser.move();
        arglist.add(getOneArg());
      }
    }

    Token rparen = parser.rparen();
    return arglist;
  }

  private FuncArg getOneArg() {
    Token tok = parser.checkedMove(T.TOKEN_IDENT);
    Token colon = parser.colon();

    ExprExpression onearg = e_assign();
    return new FuncArg(tok.getIdent(), onearg);
  }

  private ExprExpression e_prim() {

    if (parser.tp() == TOKEN_NUMBER || parser.tp() == TOKEN_CHAR || parser.tp() == TOKEN_STRING) {
      Token saved = parser.moveget();
      if (saved.ofType(TOKEN_STRING)) {

        // TODO:

        final ClassDeclaration stringClass = new ClassDeclaration(Hash_ident.getHashedIdent("string"));

        final List<FuncArg> argums = new ArrayList<>();
        argums.add(new FuncArg(Hash_ident.getHashedIdent("buffer"), new ExprExpression(saved.getValue())));

        final ArrayList<Type> emptyTypeArgs = new ArrayList<>();
        final ClassType ref = new ClassType(stringClass, emptyTypeArgs);
        final ExprClassCreation classCreation = new ExprClassCreation(new Type(ref), argums);

        return new ExprExpression(classCreation);
      }

      else {
        // TODO: chars.
        return new ExprExpression(saved.getNumconst(), saved);
      }
    }

    // new ClassName(x, y, z)
    if (parser.is(IdentMap.new_ident)) {
      Token saved = parser.moveget();

      // new [2:int] ;
      if (parser.is(T.T_LEFT_BRACKET)) {
        final Type arrayCreator = new TypeRecognizer(parser).getType();
        final ExprArrayCreation arrayCreation = new ExprArrayCreation(arrayCreator);

        // it is important to register type-setter for `current` class
        // not for the class is created in `new` expression 
        parser.getCurrentClass(true).registerTypeSetter(arrayCreation);
        return new ExprExpression(arrayCreation);
      }

      else {

        final ClassDeclaration instantiatedClass = parser.getClassType(parser.getIdent());
        final List<Type> typeArguments = new TypeRecognizer(parser).getTypeArguments();
        final ClassType ref = new ClassType(instantiatedClass, typeArguments);
        final List<FuncArg> arguments = parseArglist();
        final ExprClassCreation classInstanceCreation = new ExprClassCreation(new Type(ref), arguments);

        // it is important to register type-setter for `current` class
        // not for the class is created in `new` expression 
        parser.getCurrentClass(true).registerTypeSetter(classInstanceCreation);
        return new ExprExpression(classInstanceCreation);
      }

    }

    if (parser.is(IdentMap.self_ident)) {
      Token saved = parser.moveget();
      ExprExpression thisexpr = new ExprExpression(new ExprSelf(parser.getCurrentClass(true)));
      return thisexpr;
    }

    if (parser.is(IdentMap.null_ident)) {
      Token saved = parser.moveget();
      ExprExpression nullexpr = new ExprExpression(ExpressionBase.EPRIMARY_NULL_LITERAL);
      return nullexpr;
    }

    // simple name
    if (IdentRecognizer.isUserDefinedIdentNoKeyword(parser.tok())) {
      Token saved = parser.moveget();
      return new ExprExpression(new ExprIdent(saved.getIdent()));
    }

    // ( expression )
    if (parser.tp() == T_LEFT_PAREN) {
      Token lparen = parser.moveget();
      ExprExpression e = e_expression();
      Token rparen = parser.checkedMove(T_RIGHT_PAREN);
      return e;
    }

    parser.perror("something wrong in expression...");
    return null; // you never return this ;)

  }

}
