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
import ast_stmt.StmtForeach;
import ast_stmt.StmtSelect;
import ast_stmt.StmtStatement;
import ast_stmt.StmtWhile;
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
      return parseForeach();
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

  private StmtStatement parseForeach() {

    // for item in list {}

    final Token beginPos = parser.checkedMove(for_ident);
    final Ident iter = parser.getIdent();

    parser.checkedMove(Keywords.in_ident);
    final Ident collection = parser.getIdent();

    final StmtBlock loop = parseBlock(VarBase.LOCAL_VAR);
    return new StmtStatement(new StmtForeach(iter, collection, loop, beginPos), beginPos);
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
