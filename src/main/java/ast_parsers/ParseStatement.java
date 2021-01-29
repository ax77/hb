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
import ast_stmt.StatementBase;
import ast_stmt.StmtBlock;
import ast_stmt.StmtBlockItem;
import ast_stmt.StmtStatement;
import ast_stmt.StmtWhile;
import ast_stmt.StmtForeach;
import ast_stmt.StmtSelect;
import ast_symtab.Keywords;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import parse.Parse;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public class ParseStatement {
  private final Parse parser;

  public ParseStatement(Parse parser) {
    this.parser = parser;
  }

  public StmtBlock parseBlock(VarBase varBase) {

    Token lbrace = parser.checkedMove(T.T_LEFT_BRACE);

    if (parser.is(T.T_RIGHT_BRACE)) {
      Token rbrace = parser.checkedMove(T.T_RIGHT_BRACE);
      return new StmtBlock();
    }

    List<StmtBlockItem> bs = new ArrayList<StmtBlockItem>();
    while (parser.tp() != T.T_RIGHT_BRACE) {
      StmtBlockItem oneBlock = parseOneBlock(varBase);
      bs.add(oneBlock);
    }
    StmtBlock block = new StmtBlock(bs);

    Token rbrace = parser.checkedMove(T.T_RIGHT_BRACE);
    return block;
  }

  private StmtBlockItem parseOneBlock(VarBase varBase) {

    //  BlockStatement:
    //    LocalVariableDeclarationStatement
    //    ClassOrInterfaceDeclaration
    //    [Identifier :] Statement
    //
    //  LocalVariableDeclarationStatement:
    //    { VariableModifier } Type VariableDeclarators ;

    if (parser.isTypeWithOptModifiersBegin()) {
      VarDeclarator localVar = new ParseVarDeclarator(parser).parse(varBase);

      ClassDeclaration currentClass = parser.getCurrentClass(true);
      currentClass.registerTypeSetter(localVar);

      return new StmtBlockItem(localVar);
    }

    StmtStatement stmt = parseStatement();
    if (stmt == null) {
      parser.perror("something wrong in a statement");
    }
    return new StmtBlockItem(stmt);
  }

  private ExprExpression e_expression() {
    return new ParseExpression(parser).e_expression();
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

      final ExprExpression expr = e_expression();
      parser.semicolon();
      return new StmtStatement(StatementBase.SRETURN, expr, beginPos);
    }

    // while condition {  }

    if (parser.is(while_ident)) {
      final Token beginPos = parser.checkedMove(while_ident);
      final ExprExpression condition = e_expression();
      final StmtBlock block = parseBlock(VarBase.LOCAL_VAR);
      return new StmtStatement(new StmtWhile(condition, block), beginPos);
    }

    // for item in collection {  }

    if (parser.is(for_ident)) {
      return parseForLoop();
    }

    // if ( expr ) stmt else stmt

    if (parser.is(if_ident)) {
      return parse_if();
    }

    // {  }

    if (parser.is(T.T_LEFT_BRACE)) {
      Token from = parser.tok();
      return new StmtStatement(parseBlock(VarBase.LOCAL_VAR), from);
    }

    // expression-statement by default
    //
    StmtStatement ret = new StmtStatement(StatementBase.SEXPR, e_expression(), parser.tok());
    parser.semicolon();
    return ret;
  }

  private StmtStatement parse_if() {
    Token from = parser.checkedMove(if_ident);

    final ExprExpression ifexpr = new ParseExpression(parser).e_expression();
    shouldBeLeftBrace();

    final StmtStatement ifstmt = parseStatement();
    StmtStatement ifelse = null;

    if (parser.is(else_ident)) {
      Token elsekw = parser.checkedMove(else_ident);
      shouldBeIfOrLeftBrace();

      ifelse = parseStatement();
      return new StmtStatement(new StmtSelect(ifexpr, ifstmt, ifelse), elsekw);
    }

    return new StmtStatement(new StmtSelect(ifexpr, ifstmt, ifelse), from);
  }

  private StmtStatement parseForLoop() {

    // for item in list {}

    Token from = parser.checkedMove(for_ident);
    Ident iter = parser.getIdent();

    parser.checkedMove(Keywords.in_ident);
    Ident collection = parser.getIdent();

    StmtBlock loop = parseBlock(VarBase.LOCAL_VAR);
    return new StmtStatement(new StmtForeach(iter, collection, loop, from), from);
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
