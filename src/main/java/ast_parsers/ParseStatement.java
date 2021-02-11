package ast_parsers;

import static ast_symtab.Keywords.do_ident;
import static ast_symtab.Keywords.else_ident;
import static ast_symtab.Keywords.for_ident;
import static ast_symtab.Keywords.if_ident;
import static ast_symtab.Keywords.return_ident;
import static ast_symtab.Keywords.while_ident;
import static tokenize.T.T_SEMI_COLON;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_modifiers.Modifiers;
import ast_st2_annotate.Mods;
import ast_stmt.StatementBase;
import ast_stmt.StmtBlock;
import ast_stmt.StmtBlockItem;
import ast_stmt.StmtFor;
import ast_stmt.StmtForeach;
import ast_stmt.StmtSelect;
import ast_stmt.StmtStatement;
import ast_stmt.StmtWhile;
import ast_symtab.Keywords;
import ast_types.Type;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import parse.Parse;
import parse.ParseState;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public class ParseStatement {
  private final Parse parser;

  public ParseStatement(Parse parser) {
    this.parser = parser;
  }

  public StmtBlock parseBlock(VarBase varBase) {

    parser.lbrace();

    if (parser.is(T.T_RIGHT_BRACE)) {
      parser.rbrace();
      return new StmtBlock();
    }

    final List<StmtBlockItem> items = new ArrayList<StmtBlockItem>();
    while (!parser.is(T.T_RIGHT_BRACE)) {
      final StmtBlockItem oneBlock = parseOneBlock(varBase);
      items.add(oneBlock);
    }
    parser.rbrace();

    return new StmtBlock(items);
  }

  private StmtBlockItem parseOneBlock(VarBase varBase) {

    if (isLocalVarBegin()) {
      final VarDeclarator localVar = getLocalVar(varBase);
      final ClassDeclaration currentClass = parser.getCurrentClass(true);
      currentClass.registerTypeSetter(localVar);

      return new StmtBlockItem(localVar);
    }

    StmtStatement stmt = parseStatement();
    if (stmt == null) {
      parser.perror("something wrong in a statement");
    }
    return new StmtBlockItem(stmt);
  }

  private VarDeclarator getLocalVar(VarBase varBase) {
    final Modifiers mods = new ParseModifiers(parser).parse();
    final Type type = new ParseType(parser).getType();
    final Token beginPos = parser.checkedMove(T.TOKEN_IDENT);
    final Ident name = beginPos.getIdent();
    final VarDeclarator localVar = new ParseVarDeclarator(parser).parse(varBase, mods, type, name, beginPos);
    return localVar;
  }

  /// this is SOOO ambiguous:
  /// between expression-statement and variable-declaration
  /// the clean syntax is: [let varname: int;]
  /// and we 100% sure if we see the 'let' or 'var' keyword - that
  /// we should parse declaration instead of expression
  /// here we need to lookahead, and make the decision:
  /// is this a expression-statement, or it is a declaration.
  /// yach...
  ///
  /// 1) final int x = 1;
  /// 2) int x = 1;
  /// 3) x = 1;
  ///
  private boolean isLocalVarBegin() {

    // we should save the state before any modification
    final ParseState state = new ParseState(parser);

    /// 100% short-circuit
    /// `final` int a; -> this is a declaration, not an expression
    if (Mods.isAnyModifier(parser.tok())) {
      parser.restoreState(state);
      return true;
    }

    @SuppressWarnings("unused")
    final Modifiers mods = new ParseModifiers(parser).parse();
    final ParseType typeRecognizer = new ParseType(parser);

    if (!typeRecognizer.isType()) {
      parser.restoreState(state);
      return false;
    }

    final Type type = new ParseType(parser).getType();
    if (type.is_class()) {
      final ClassDeclaration clazz = type.getClassTypeFromRef();
      final boolean isAbstractMethodFcall = clazz.getModifiers().isAbstractOnly() && parser.is(T.T_DOT)
          && parser.peek().ofType(T.TOKEN_IDENT);
      if (isAbstractMethodFcall) {
        // util.read()
        // unit.write()
        // etc...
        parser.restoreState(state);
        return false;
      }
    }

    /// it means that it is not a variable-declaration
    /// it is an expression-statement
    ///
    parser.restoreState(state);
    return true;
  }

  private StmtStatement parseStatement() {

    parser.errorStraySemicolon();

    if (parser.is(do_ident)) {
      parser.perror("do loops unimpl.");
    }

    // return ... ;
    // return ;

    if (parser.is(return_ident)) {
      Token beginPos = parser.checkedMove(return_ident);

      if (parser.tp() == T_SEMI_COLON) {
        parser.move();
        return new StmtStatement(StatementBase.SRETURN, null, beginPos);
      }

      final ExprExpression expr = new ParseExpression(parser).e_expression();
      parser.semicolon();
      return new StmtStatement(StatementBase.SRETURN, expr, beginPos);
    }

    // while condition {  }

    if (parser.is(while_ident)) {
      final Token beginPos = parser.checkedMove(while_ident);
      final ExprExpression condition = new ParseExpression(parser).e_expression();
      final StmtBlock block = parseBlock(VarBase.LOCAL_VAR);
      return new StmtStatement(new StmtWhile(condition, block), beginPos);
    }

    // for item in collection {  }

    if (parser.is(for_ident)) {
      return parseForLoop();
      //return parseForeach();
    }

    // if 

    if (parser.is(if_ident)) {
      return parseSelection();
    }

    // {  }

    if (parser.is(T.T_LEFT_BRACE)) {
      final Token beginPos = parser.tok();
      return new StmtStatement(parseBlock(VarBase.LOCAL_VAR), beginPos);
    }

    // expression-statement by default
    //
    final Token beginPos = parser.tok();
    final ExprExpression expression = new ParseExpression(parser).e_expression();
    parser.semicolon();
    return new StmtStatement(StatementBase.SEXPR, expression, beginPos);
  }

  private StmtStatement parseSelection() {
    Token ifKeyword = parser.checkedMove(if_ident);

    final ExprExpression condition = new ParseExpression(parser).e_expression();
    shouldBeLeftBrace();
    final StmtStatement trueStatement = parseStatement();

    StmtStatement optionalElseStatement = null;
    if (parser.is(else_ident)) {
      final Token elseKeyword = parser.checkedMove(else_ident);
      shouldBeIfOrLeftBrace();
      optionalElseStatement = parseStatement();
      return new StmtStatement(new StmtSelect(condition, trueStatement, optionalElseStatement), elseKeyword);
    }

    return new StmtStatement(new StmtSelect(condition, trueStatement, optionalElseStatement), ifKeyword);
  }

  @SuppressWarnings("unused")
  private StmtStatement parseForeach() {

    // for item in list {}

    final Token beginPos = parser.checkedMove(for_ident);
    final Ident iter = parser.getIdent();

    parser.checkedMove(Keywords.in_ident);
    final Ident collection = parser.getIdent();

    final StmtBlock loop = parseBlock(VarBase.LOCAL_VAR);
    return new StmtStatement(new StmtForeach(iter, collection, loop, beginPos), beginPos);
  }

  private StmtStatement parseForLoop() {

    VarDeclarator decl = null;
    ExprExpression init = null;
    ExprExpression test = null;
    ExprExpression step = null;
    StmtStatement loop = null;

    Token from = parser.checkedMove(for_ident);
    parser.lparen();

    // 1) for (int i = 0; ;)
    // 2) for (i = 0; ;)
    if (parser.tp() != T_SEMI_COLON) {
      if (isLocalVarBegin()) {
        decl = getLocalVar(VarBase.LOCAL_VAR);
      } else {
        init = parseForLoopExpressions();
        parser.semicolon();
      }
    } else {
      parser.semicolon();
    }

    if (parser.tp() != T_SEMI_COLON) {
      test = parseForLoopExpressions();
    }
    parser.semicolon();

    if (parser.tp() != T.T_RIGHT_PAREN) {
      step = parseForLoopExpressions();
    }
    parser.rparen();

    checkSemicolonAndLbrace();
    loop = parseStatement();

    return new StmtStatement(new StmtFor(decl, init, test, step, loop), from);
  }

  private ExprExpression parseForLoopExpressions() {
    return new ParseExpression(parser).e_expression();
  }

  private void checkSemicolonAndLbrace() {
    parser.errorStraySemicolon();
    if (!parser.is(T.T_LEFT_BRACE)) {
      parser.perror("unbraced statements are deprecated by design.");
    }
  }

  private void shouldBeIfOrLeftBrace() {
    if (parser.is(if_ident)) {
      return;
    }
    if (parser.is(T.T_LEFT_BRACE)) {
      return;
    }
    parser.perror("expected '{' or 'if', but was: " + parser.tok().getValue());
  }

  private void shouldBeLeftBrace() {
    if (parser.is(T.T_LEFT_BRACE)) {
      return;
    }
    parser.perror("expected '{', but was: " + parser.tok().getValue());
  }

}
