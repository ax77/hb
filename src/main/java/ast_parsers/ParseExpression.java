package ast_parsers;

import static tokenize.T.T_AND;
import static tokenize.T.T_AND_AND;
import static tokenize.T.T_AND_EQUAL;
import static tokenize.T.T_DIVIDE;
import static tokenize.T.T_DIVIDE_EQUAL;
import static tokenize.T.T_EQ;
import static tokenize.T.T_EXCLAMATION;
import static tokenize.T.T_GE;
import static tokenize.T.T_GT;
import static tokenize.T.T_LE;
import static tokenize.T.T_LEFT_PAREN;
import static tokenize.T.T_LSHIFT;
import static tokenize.T.T_LSHIFT_EQUAL;
import static tokenize.T.T_LT;
import static tokenize.T.T_MINUS;
import static tokenize.T.T_MINUS_EQUAL;
import static tokenize.T.T_NE;
import static tokenize.T.T_OR;
import static tokenize.T.T_OR_EQUAL;
import static tokenize.T.T_OR_OR;
import static tokenize.T.T_PERCENT;
import static tokenize.T.T_PERCENT_EQUAL;
import static tokenize.T.T_PLUS;
import static tokenize.T.T_PLUS_EQUAL;
import static tokenize.T.T_QUESTION;
import static tokenize.T.T_RSHIFT;
import static tokenize.T.T_RSHIFT_EQUAL;
import static tokenize.T.T_TILDE;
import static tokenize.T.T_TIMES;
import static tokenize.T.T_TIMES_EQUAL;
import static tokenize.T.T_XOR;
import static tokenize.T.T_XOR_EQUAL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import _st2_annotate.TypeTraitsUtil;
import ast_class.ClassDeclaration;
import ast_expr.ExprAssign;
import ast_expr.ExprBinary;
import ast_expr.ExprBuiltinFunc;
import ast_expr.ExprCast;
import ast_expr.ExprClassCreation;
import ast_expr.ExprDefaultValueForType;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprForLoopStepComma;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_expr.ExprSizeof;
import ast_expr.ExprStaticAccess;
import ast_expr.ExprTernaryOperator;
import ast_expr.ExprUnary;
import ast_expr.ExpressionBase;
import ast_main.imports.GlobalSymtab;
import ast_symtab.BuiltinNames;
import ast_symtab.Keywords;
import ast_types.ClassTypeRef;
import ast_types.Type;
import errors.AstParseException;
import parse.Parse;
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
  
  public static Token assignOperator(Token from) {
    return new Token(from, "=", T.T_ASSIGN);
  }
  
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
      //TODO:error if it is not in a for-loop step
      //parser.errorCommaExpression();
      Token saved = parser.checkedMove(T.T_COMMA);
      e = new ExprExpression(new ExprForLoopStepComma(e, e_expression()), saved);
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

        Token assignOperator = assignOperator(saved);
        Token binaryOperator = getOperatorFromCompAssign(saved);

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
    return new Token(tok, ">>", T.T_RSHIFT);
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
    ExprExpression e = e_unary();
    while (parser.tp() == T_TIMES || parser.tp() == T_DIVIDE || parser.tp() == T_PERCENT) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_unary());
    }
    return e;
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
      return e_cast();
    }

    return e_postfix();
  }

  private ExprExpression e_cast() {
    final Token beginPos = parser.checkedMove(Keywords.cast_ident);
    parser.lparen();

    // TODO: assign expression is not allowed in casting.
    // rewrite this.
    // TODO: is the grammar - this node should be an 'unary', not an 'expression'.
    // But: what if we want to cast(1+32 : char)
    // we want to cast the whole 1+32 to char, not only the 1.
    final ExprExpression expressionForCast = e_expression();
    parser.checkedMove(T.T_COLON);

    final Type toType = new ParseType(parser).getType();
    parser.rparen();

    return new ExprExpression(new ExprCast(toType, expressionForCast), beginPos);
  }

  private ExprExpression e_postfix() {
    ExprExpression lhs = e_prim();

    while (!parser.isEof()) {
      if (parser.is(T.T_DOT)) {
        final Token dot = parser.moveget();
        final Ident member = parser.getIdent();
        if (parser.is(T_LEFT_PAREN)) {
          final List<ExprExpression> arglist = parseArglist();
          lhs = new ExprExpression(new ExprMethodInvocation(lhs, member, arglist), dot);
        } else {
          lhs = new ExprExpression(new ExprFieldAccess(lhs, member), dot);
        }
      }

      else if (parser.is(T.T_PLUS_PLUS) || parser.is(T.T_MINUS_MINUS)) {
        parser.perror("post-increment/post-decrement are deprecated by design.");
      }

      else if (parser.is(T.T_LEFT_BRACKET)) {
        parser.errorArray();
      }

      else {
        break;
      }
    }

    return lhs;
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

    ExprExpression selfExpression = new ExprExpression(parser.getCurrentClass(true), tok);
    List<ExprExpression> arglist = parseArglist();

    tok = parser.tok();
    return new ExprExpression(new ExprMethodInvocation(selfExpression, funcname, arglist), tok);
  }

  private List<ExprExpression> parseArglist() {
    return new ParseFcallArgs(parser).parse();
  }

  private ExprExpression e_prim() {

    /// ?(condition, trueResult, falseResult) 
    if (parser.is(T.T_QUESTION)) {
      Token beginPos = parser.checkedMove(T.T_QUESTION);
      parser.lparen();

      ExprExpression condition = e_cnd();
      parser.checkedMove(T.T_COMMA);

      ExprExpression trueResult = e_cnd();
      parser.checkedMove(T.T_COMMA);

      ExprExpression falseResult = e_cnd();
      parser.rparen();

      ExprTernaryOperator ternaryOperator = new ExprTernaryOperator(condition, trueResult, falseResult);
      return new ExprExpression(ternaryOperator, beginPos);
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
      return new ExprExpression(ExpressionBase.EPRIMARY_CHAR, saved);
    }

    // new ClassName<i32>(x, y, z) 
    // new [2: i32]
    if (parser.is(Keywords.new_ident)) {
      return parseNewExpression();
    }

    if (parser.is(Keywords.sizeof_ident)) {
      return parseSizeof();
    }

    // assert_true(something)
    // static_assert(something)
    // is_int(something)

    //@formatter:off
    if (   parser.is(Keywords.static_assert_ident) 
        || parser.is(Keywords.assert_true_ident)
        || parser.is(Keywords.types_are_same_ident)
        || TypeTraitsUtil.isBuiltinTypeTraitsIdent(parser.tok())
    //@formatter:on
    ) {

      int argcExpected = 1;
      if (parser.is(Keywords.types_are_same_ident)) {
        argcExpected = 2;
      }

      return parseBuiltinFunc(argcExpected);
    }

    if (parser.is(Keywords.this_ident)) {
      Token saved = parser.moveget();
      ExprExpression thisexpr = new ExprExpression(parser.getCurrentClass(true), saved);
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

    if (parser.is(Keywords.default_ident)) {
      Token saved = parser.moveget();

      // default(T)

      parser.lparen();
      Type type = new ParseType(parser).getType();
      parser.rparen();

      ExprDefaultValueForType defaultValueForType = new ExprDefaultValueForType(type);
      parser.getCurrentClass(true).registerTypeSetter(defaultValueForType);

      return new ExprExpression(defaultValueForType, saved);
    }

    // simple name
    if (parser.isUserDefinedIdentNoKeyword(parser.tok())) {
      Token saved = parser.moveget();
      final ExprIdent symbol = new ExprIdent(saved.getIdent());

      // TODO:STATIC_ACCESS
      // somefunc<int>.print(1);
      ExprExpression sa = possibleGenericStaticClassInstantiationAccess(saved);
      if (sa != null) {
        return sa;
      }

      // this.func()
      if (parser.is(T_LEFT_PAREN)) {
        return methodInvocation(saved.getIdent());
      }

      return new ExprExpression(symbol, saved);
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

  private ExprExpression possibleGenericStaticClassInstantiationAccess(Token saved) {
    ClassDeclaration cd = GlobalSymtab.getClassTypeNoErr(parser, saved.getIdent());
    if (parser.is(T_LT) || parser.is(T_LSHIFT)) {
      if (cd != null && cd.isTemplate() && cd.isStaticClass()) {
        // the specialization and access to the templated namespace method
        // somefunc<int>.print(1);
        //
        List<Type> typeargs = new ParseType(parser).getTypeArguments();
        ExprStaticAccess sa = new ExprStaticAccess(new Type(new ClassTypeRef(cd, typeargs)));
        parser.getCurrentClass(true).registerTypeSetter(sa);

        final ExprExpression sAccess = new ExprExpression(sa, saved);
        return sAccess;
      }
    }
    return null;
  }

  /// put the correct location here, and correct
  /// expression-to-string into the args:
  /// assert_true(arg == 1, file="C:/prj/main.hb", line=15, expr="arg == 1")
  /// void assert_true(int cnd, const char *file, int line, const char *expr)
  ///
  private ExprExpression parseBuiltinFunc(int argcExpected) {
    final Token beginPos = parser.moveget();

    final List<ExprExpression> args = parseArglist();
    if (args.size() != argcExpected) {
      parser.perror(beginPos.getValue() + "() expecting " + String.format("%d", argcExpected) + " argument(s)");
    }

    final String file = beginPos.getLocation().getFilename();
    final String line = String.format("%d", beginPos.getLocation().getLine());
    final String expr = args.get(0).toString();

    ExprBuiltinFunc builtinFunc = new ExprBuiltinFunc(beginPos.getIdent(), args, file, line, expr);
    return new ExprExpression(builtinFunc, beginPos);
  }

  private ExprExpression parseSizeof() {
    Token beginPos = parser.checkedMove(Keywords.sizeof_ident);

    parser.lparen();
    Type tp = new ParseType(parser).getType();
    parser.rparen();

    final ExprSizeof exprSizeof = new ExprSizeof(tp);
    parser.getCurrentClass(true).registerTypeSetter(exprSizeof);

    return new ExprExpression(exprSizeof, beginPos);
  }

  private ExprExpression parseStringLiteral() {
    final Token saved = parser.moveget();
    return createStringCreationFromGivenTok(saved);
  }

  private ExprExpression createStringCreationFromGivenTok(final Token saved) {
    final Type strTypeBootstr = new Type(
        new ClassTypeRef(GlobalSymtab.getClassType(parser, BuiltinNames.str_ident), new ArrayList<>()));

    final List<ExprExpression> arguments = new ArrayList<>();
    final ExprExpression arg = new ExprExpression(ExpressionBase.EPRIMARY_STRING, saved);
    arguments.add(arg);

    final ExprClassCreation classInstanceCreation = new ExprClassCreation(strTypeBootstr, arguments);
    final ExprExpression result = new ExprExpression(classInstanceCreation, saved);
    return result;
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
    if (!classtype.isClass()) {
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
