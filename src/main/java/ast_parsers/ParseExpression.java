package ast_parsers;

import static tokenize.T.T_AND;
import static tokenize.T.T_AND_AND;
import static tokenize.T.T_DIVIDE;
import static tokenize.T.T_EQ;
import static tokenize.T.T_EXCLAMATION;
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
import static tokenize.T.T_TILDE;
import static tokenize.T.T_TIMES;
import static tokenize.T.T_XOR;

import java.util.ArrayList;
import java.util.List;

import ast_builtins.BuiltinNames;
import ast_class.ClassDeclaration;
import ast_expr.ExprAssign;
import ast_expr.ExprBinary;
import ast_expr.ExprCast;
import ast_expr.ExprClassCreation;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_expr.ExprThis;
import ast_expr.ExprUnary;
import ast_expr.ExprUtil;
import ast_expr.ExpressionBase;
import ast_symtab.Keywords;
import ast_types.ClassTypeRef;
import ast_types.Type;
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
    return new ExprExpression(new ExprUnary(op, operand), op);
  }

  private ExprExpression build_binary(Token operator, ExprExpression lhs, ExprExpression rhs) {
    return new ExprExpression(new ExprBinary(operator, lhs, rhs), operator);
  }

  private ExprExpression build_assign(Token tok, ExprExpression lvalue, ExprExpression rvalue) {
    return new ExprExpression(new ExprAssign(tok, lvalue, rvalue), tok);
  }

  //@formatter:off
  private boolean isAssignOperator(Token what) {
    return what.ofType(T.T_ASSIGN)
        || what.ofType(T.T_TIMES_EQUAL)
        || what.ofType(T.T_PERCENT_EQUAL)
        || what.ofType(T.T_DIVIDE_EQUAL)
        || what.ofType(T.T_PLUS_EQUAL)
        || what.ofType(T.T_MINUS_EQUAL)
        || what.ofType(T.T_LSHIFT_EQUAL)
        || what.ofType(T.T_RSHIFT_EQUAL)
        || what.ofType(T.T_AND_EQUAL)
        || what.ofType(T.T_XOR_EQUAL)
        || what.ofType(T.T_OR_EQUAL);
  }

  // & * + - ~ !
  private boolean isUnaryOperator(Token what) {
    return what.ofType(T_AND)
        || what.ofType(T_TIMES)
        || what.ofType(T_PLUS)
        || what.ofType(T_MINUS)
        || what.ofType(T_TILDE)
        || what.ofType(T_EXCLAMATION);
  }
  //@formatter:on

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
    return isAssignOperator(what) && !what.ofType(T.T_ASSIGN);
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

    if (isAssignOperator(parser.tok())) {

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

  private boolean isRshift() {
    if (parser.is(T.T_GT) && parser.peek().ofType(T.T_GT)) {
      return true;
    }
    return false;
  }

  private Token buildRshiftToken() {
    Token tok = parser.tok();
    parser.move(); // >
    parser.move(); // >
    return ExprUtil.copyTokenAddNewType(tok, T.T_RSHIFT, ">>");
  }

  private ExprExpression e_shift() {
    ExprExpression e = e_add();
    while (parser.is(T_LSHIFT) || isRshift()) {
      if (parser.is(T_LSHIFT)) {
        Token saved = parser.tok();
        parser.move();
        e = build_binary(saved, e, e_add());
      } else {
        Token saved = buildRshiftToken();
        e = build_binary(saved, e, e_add());
      }
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
    return e_unary();
  }

  private ExprExpression e_unary() {

    // [& * + - ~ !]
    if (isUnaryOperator(parser.tok())) {
      Token operator = parser.tok();
      parser.move();
      return build_unary(operator, e_unary());
    }

    // ++ --
    if (parser.is(T.T_PLUS_PLUS) || parser.is(T.T_MINUS_MINUS)) {
      parser.perror("pre-increment/pre-decrement are deprecated by design.");
    }

    // cast(unary: type)
    if (parser.is(Keywords.cast_ident)) {
      final Token beginPos = parser.checkedMove(Keywords.cast_ident);
      parser.lparen();

      final ExprExpression expressionForCast = e_unary();
      parser.checkedMove(T.T_COLON);

      final Type toType = new ParseType(parser).getType();
      parser.rparen();

      return new ExprExpression(new ExprCast(toType, expressionForCast), beginPos);
    }

    return e_postfix();
  }

  private ExprExpression e_postfix() {

    ExprExpression lhs = e_prim();

    while (!parser.isEof()) {
      if (parser.is(T.T_DOT)) {

        ParseState parseState = new ParseState(parser);

        parser.checkedMove(T.T_DOT);
        final Token peek = parser.peek();

        final boolean dot_ident_lparen = parser.isUserDefinedIdentNoKeyword(parser.tok())
            && peek.ofType(T.T_LEFT_PAREN);

        if (dot_ident_lparen) {
          Ident ident = parser.getIdent();

          // TODO: think about it, is it correct?
          // the result tree is correct as I think, but I'm not sure.
          // maybe I have to rebuild the node from field-access to func-call
          // for example: the path like "a().b().c"
          if (lhs.getBase() == ExpressionBase.EFIELD_ACCESS) {
          }

          lhs = methodInvocation(lhs, ident);
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
        parser.errorArray();
      }

      else {
        break;
      }
    }

    return lhs;
  }

  private ExprExpression fieldAccess(final ExprExpression obj) {
    final Token dot = parser.checkedMove(T.T_DOT);
    final Token fieldName = parser.checkedMove(T.TOKEN_IDENT);
    return new ExprExpression(new ExprFieldAccess(obj, fieldName.getIdent()), dot);
  }

  private ExprExpression methodInvocation(final Ident funcname) {
    // apply <this.> before function name, more convenient
    // the method call has the form: a.b()
    // if 'a' is not specified - it means that we want
    // to call the method from current class,
    // and we can apply the 'self' before method-name easily here
    // we'll check that class in a 'self' expression has that method
    // in a stage-2
    // the form of the method call expression will be: 'self.method_name()' 
    // instead of just 'method_name()'
    Token tok = parser.tok();

    ExprExpression selfExpression = new ExprExpression(new ExprThis(parser.getCurrentClass(true)), tok);
    List<ExprExpression> arglist = parseArglist();

    tok = parser.tok();
    return new ExprExpression(new ExprMethodInvocation(selfExpression, funcname, arglist), tok);
  }

  private ExprExpression methodInvocation(final ExprExpression obj, final Ident funcname) {
    List<ExprExpression> arglist = parseArglist();
    Token tok = parser.tok();
    return new ExprExpression(new ExprMethodInvocation(obj, funcname, arglist), tok);
  }

  private List<ExprExpression> parseArglist() {
    return new ParseFcallArgs(parser).parse();
  }

  private ExprExpression e_prim() {

    if (parser.is(BuiltinNames.std_ident)) {
      return new ParseBuiltinsFn(parser).parse();
    }

    if (parser.is(T.TOKEN_STRING)) {
      return parseStringLiteral();
    }

    if (parser.is(T.TOKEN_NUMBER)) {
      Token saved = parser.moveget();
      return new ExprExpression(saved.getNumconst(), saved);
    }

    if (parser.is(T.TOKEN_CHAR)) {
      // TODO:
      Token saved = parser.moveget();
      return new ExprExpression(ExpressionBase.ECHAR_CONST, saved);
    }

    // new ClassName<i32>(x, y, z) 
    // new [2: i32]
    if (parser.is(Keywords.new_ident)) {
      return parseNewExpression();
    }

    if (parser.is(Keywords.this_ident)) {
      Token saved = parser.moveget();
      ExprExpression thisexpr = new ExprExpression(new ExprThis(parser.getCurrentClass(true)), saved);
      return thisexpr;
    }

    if (parser.is(Keywords.true_ident)) {
      Token saved = parser.moveget();
      return new ExprExpression(true, saved);
    }

    if (parser.is(Keywords.false_ident)) {
      Token saved = parser.moveget();
      return new ExprExpression(false, saved);
    }

    if (parser.is(Keywords.null_ident)) {
      Token saved = parser.moveget();
      ExprExpression nullexpr = new ExprExpression(ExpressionBase.EPRIMARY_NULL_LITERAL, saved);
      return nullexpr;
    }

    // simple name
    if (parser.isUserDefinedIdentNoKeyword(parser.tok())) {
      Token saved = parser.moveget();
      return new ExprExpression(new ExprIdent(saved.getIdent()), saved);
    }

    // ( expression )
    if (parser.tp() == T_LEFT_PAREN) {
      parser.lparen();
      ExprExpression e = e_expression();
      parser.rparen();
      return e;
    }

    parser.perror("something wrong in expression...");
    return null; // you never return this ;)

  }

  private ExprExpression parseStringLiteral() {

    Token saved = parser.moveget();

    final ClassDeclaration stringClass = parser.getClassType(BuiltinNames.String_ident);
    if (!stringClass.isComplete()) {
      // parser.perror("string-class is incomplete");
    }

    final List<ExprExpression> argums = new ArrayList<>();
    argums.add(new ExprExpression(ExpressionBase.ESTRING_CONST, saved));

    final ArrayList<Type> emptyTypeArgs = new ArrayList<>();
    final ClassTypeRef ref = new ClassTypeRef(stringClass, emptyTypeArgs);
    final ExprClassCreation classCreation = new ExprClassCreation(new Type(ref, saved), argums);

    return new ExprExpression(classCreation, saved);
  }

  private ExprExpression parseNewExpression() {

    Token saved = parser.moveget();

    // new [2: i32] 
    // this will be rewritten later:
    // we can replace 'new [2: i32]' to 'new array<i32>()'
    if (parser.is(T.T_LEFT_BRACKET)) {
      parser.errorArray();
    }

    // new list<i32>(0)

    final Type classtype = new ParseType(parser).getType();
    if (!classtype.is_class()) {
      parser.perror("expect class for 'new', but was: " + classtype.toString());
    }

    final List<ExprExpression> arguments = parseArglist();
    final ExprClassCreation classInstanceCreation = new ExprClassCreation(classtype, arguments);

    // it is important to register type-setter for `current` class
    // not for the class is created in `new` expression 
    parser.getCurrentClass(true).registerTypeSetter(classInstanceCreation);
    return new ExprExpression(classInstanceCreation, saved);
  }

}
