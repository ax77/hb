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
import ast_modifiers.ModifiersChecker;
import ast_stmt.StatementBase;
import ast_stmt.StmtBlock;
import ast_stmt.StmtBreak;
import ast_stmt.StmtContinue;
import ast_stmt.StmtFor;
import ast_stmt.StmtReturn;
import ast_stmt.StmtSelect;
import ast_stmt.StmtStatement;
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

  private final List<StmtBlock> blocks;
  private final List<StmtFor> loops;

  public ParseStatement(Parse parser) {
    this.parser = parser;
    this.blocks = new ArrayList<>();
    this.loops = new ArrayList<>();
  }

  public StmtBlock parseBlock(VarBase varBase) {

    final StmtBlock block = new StmtBlock();
    pushBlock(block);

    parser.lbrace();

    if (parser.is(T.T_RIGHT_BRACE)) {
      parser.rbrace();
      popBlock();
      return block;
    }

    while (!parser.is(T.T_RIGHT_BRACE)) {
      final StmtStatement item = parseOneBlockItem(varBase);
      block.pushItemBack(item);
    }

    parser.rbrace();
    popBlock();
    return block;
  }

  private StmtStatement parseOneBlockItem(VarBase varBase) {

    if (isLocalVarBegin()) {
      Token beginPos = parser.tok();
      return new StmtStatement(getLocalVar(varBase), beginPos);
    }

    final StmtStatement stmt = parseStatement();
    if (stmt == null) {
      parser.perror("something wrong with a statement");
    }
    return stmt;
  }

  private VarDeclarator getLocalVar(VarBase varBase) {
    final Modifiers mods = new ParseModifiers(parser).parse();
    final Type type = new ParseType(parser).getType();
    final Token beginPos = parser.checkedMove(T.TOKEN_IDENT);
    final Ident name = beginPos.getIdent();
    final VarDeclarator localVar = new ParseVarDeclarator(parser).parse(varBase, mods, type, name, beginPos);

    final ClassDeclaration currentClass = parser.getCurrentClass(true);
    currentClass.registerTypeSetter(localVar);

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
    if (ModifiersChecker.isAnyModifier(parser.tok())) {
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

    // class tok { int value; }
    // class test { void fn() { tok tok; tok.value = 1; } }

    @SuppressWarnings("unused")
    final Type type = new ParseType(parser).getType();

    if (!parser.is(T.TOKEN_IDENT)) {
      parser.restoreState(state);
      return false;
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

    if (parser.is(return_ident)) {
      return parseReturn();
    }

    if (parser.is(while_ident)) {
      return parseWhile();
    }

    if (parser.is(for_ident)) {
      return parseForLoop();
    }

    if (parser.is(if_ident)) {
      return parseSelection();
    }

    if (parser.is(Keywords.break_ident)) {
      StmtFor currentLoop = peekLoop();
      Token beginPos = parser.checkedMove(Keywords.break_ident);
      parser.semicolon();
      return new StmtStatement(new StmtBreak(currentLoop), beginPos);
    }

    if (parser.is(Keywords.continue_ident)) {
      StmtFor currentLoop = peekLoop();
      Token beginPos = parser.checkedMove(Keywords.continue_ident);
      parser.semicolon();
      return new StmtStatement(new StmtContinue(currentLoop), beginPos);
    }

    if (parser.is(T.T_LEFT_BRACE)) {
      return new StmtStatement(parseBlock(VarBase.LOCAL_VAR), parser.tok());
    }

    // expression-statement by default
    //
    final Token beginPos = parser.tok();
    final ExprExpression expression = new ParseExpression(parser).e_expression();

    parser.semicolon();
    return new StmtStatement(expression, beginPos);
  }

  private StmtStatement parseWhile() {
    final Token beginPos = parser.checkedMove(while_ident);

    StmtFor stmtFor = new StmtFor();
    pushLoop(stmtFor);

    stmtFor.setTest(new ParseExpression(parser).e_expression());
    stmtFor.setBlock(parseBlock(VarBase.LOCAL_VAR));

    return new StmtStatement(stmtFor, beginPos);
  }

  private StmtStatement parseReturn() {
    final Token beginPos = parser.checkedMove(return_ident);
    final StmtReturn ret = new StmtReturn();

    if (parser.tp() == T_SEMI_COLON) {
      parser.move();
      return new StmtStatement(ret, beginPos);
    }

    final ExprExpression expr = new ParseExpression(parser).e_expression();
    ret.setExpression(expr);

    parser.semicolon();
    return new StmtStatement(ret, beginPos);
  }

  private StmtStatement parseSelection() {
    Token ifKeyword = parser.checkedMove(if_ident);

    final ExprExpression condition = new ParseExpression(parser).e_expression();
    shouldBeLeftBrace();
    final StmtBlock trueStatement = parseBlock(VarBase.LOCAL_VAR);

    if (parser.is(else_ident)) {
      final Token elseKeyword = parser.checkedMove(else_ident);
      shouldBeIfOrLeftBrace();

      StmtStatement tmp = parseStatement();
      StmtBlock block = new StmtBlock();

      if (tmp.getBase() == StatementBase.SIF) {
        block.pushItemBack(tmp);
      } else if (tmp.getBase() == StatementBase.SBLOCK) {
        block = tmp.getBlockStmt();
      } else {
        parser.perror("else if statement error.");
      }

      return new StmtStatement(new StmtSelect(condition, trueStatement, block), elseKeyword);
    }

    return new StmtStatement(new StmtSelect(condition, trueStatement, null), ifKeyword);
  }

  private StmtStatement parseForLoop() {

    Token beginPos = parser.checkedMove(for_ident);
    parser.lparen();

    StmtBlock forBlock = new StmtBlock();
    pushBlock(forBlock);

    StmtFor forStmt = new StmtFor();
    pushLoop(forStmt);

    // 1) for (int i = 0; ;)
    // 2) for (i = 0; ;)
    if (parser.tp() != T_SEMI_COLON) {
      if (isLocalVarBegin()) {
        //VarDeclarator decl = getLocalVar(VarBase.LOCAL_VAR);
        //forBlock.pushItemBack(new StmtStatement(decl, beginPos));

        List<VarDeclarator> vars = new ParseVarDeclarator(parser).parseVarDeclaratorListForLoop();
        for (VarDeclarator decl : vars) {
          forBlock.pushItemBack(new StmtStatement(decl, beginPos));
        }

      } else {
        ExprExpression init = parseForLoopExpressions();
        forBlock.pushItemBack(new StmtStatement(init, beginPos));
        parser.semicolon();
      }
    } else {
      parser.semicolon();
    }

    if (parser.tp() != T_SEMI_COLON) {
      forStmt.setTest(parseForLoopExpressions());
    }
    parser.semicolon();

    if (parser.tp() != T.T_RIGHT_PAREN) {
      forStmt.setStep(parseForLoopExpressions());
    }
    parser.rparen();

    checkSemicolonAndLbrace();
    forStmt.setBlock(parseBlock(VarBase.LOCAL_VAR));

    forBlock.pushItemBack(new StmtStatement(forStmt, beginPos));
    final StmtStatement result = new StmtStatement(forBlock, beginPos);

    popLoop();
    popBlock();
    return result;
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

  /// LOOPS stack

  private void pushLoop(StmtFor s) {
    loops.add(0, s);
  }

  private void popLoop() {
    loops.remove(0);
  }

  private StmtFor peekLoop() {
    if (loops.isEmpty()) {
      parser.perror("there is no current loop");
    }
    return loops.get(0);
  }

  /// BLOCKS stack

  public void pushBlock(StmtBlock e) {
    this.blocks.add(0, e);
  }

  public void popBlock() {
    this.blocks.remove(0);
  }

  public StmtBlock peekBlock() {
    return blocks.get(0);
  }

}
