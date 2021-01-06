package njast.ast_parsers;

import static jscan.tokenize.T.T_RIGHT_PAREN;
import static jscan.tokenize.T.*;
import static njast.symtab.IdentMap.do_ident;
import static njast.symtab.IdentMap.for_ident;
import static njast.symtab.IdentMap.*;
import static njast.symtab.IdentMap.return_ident;
import static njast.symtab.IdentMap.switch_ident;
import static njast.symtab.IdentMap.while_ident;

import java.util.ArrayList;
import java.util.List;

import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_kinds.StatementBase;
import njast.ast_nodes.clazz.vars.VarBase;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.expr.ExprExpression;
import njast.ast_nodes.stmt.StmtBlock;
import njast.ast_nodes.stmt.StmtBlockItem;
import njast.ast_nodes.stmt.StmtFor;
import njast.ast_nodes.stmt.StmtStatement;
import njast.ast_nodes.stmt.Stmt_if;
import njast.parse.Parse;

public class ParseStatement {
  private final Parse parser;

  public ParseStatement(Parse parser) {
    this.parser = parser;
  }

  public StmtBlock parseBlock() {

    Token lbrace = parser.checkedMove(T.T_LEFT_BRACE);

    if (parser.is(T.T_RIGHT_BRACE)) {
      Token rbrace = parser.checkedMove(T.T_RIGHT_BRACE);
      return new StmtBlock();
    }

    List<StmtBlockItem> blockStatements = parseBlockStamentList();
    StmtBlock block = new StmtBlock(blockStatements);

    Token rbrace = parser.checkedMove(T.T_RIGHT_BRACE);

    return block;
  }

  public StmtStatement parseCompoundStatement() {
    Token lbrace = parser.tok();
    StmtBlock block = parseBlock();
    return new StmtStatement(lbrace, block);
  }

  private List<StmtBlockItem> parseBlockStamentList() {
    List<StmtBlockItem> bs = new ArrayList<StmtBlockItem>();

    while (parser.tp() != T.T_RIGHT_BRACE) {
      StmtBlockItem oneBlock = parseOneBlock();
      bs.add(oneBlock);
    }

    return bs;
  }

  private StmtBlockItem parseOneBlock() {

    if (parser.isPrimitiveOrReferenceTypeBegin()) {
      List<VarDeclarator> vars = new ParseVarDeclaratorsList(parser).parse(VarBase.METHOD_VAR);
      return new StmtBlockItem(vars);
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

    if (parser.is(while_ident) || parser.is(do_ident)) {
      parser.perror("while/do loops are deprecated by design. use for-loop instead.");
    }
    if (parser.is(switch_ident)) {
      parser.perror("switch-statement deprecated by design. use [if(cond) { } else { }] instead.");
    }
    if (parser.is(goto_ident)) {
      parser.perror("goto-statement deprecated by design. ");
    }

    // return ... ;
    // return ;

    if (parser.is(return_ident)) {
      Token from = parser.checkedMove(return_ident);

      if (parser.tp() == T_SEMI_COLON) {
        parser.move();
        return new StmtStatement(StatementBase.SRETURN, null);
      }

      ExprExpression expr = e_expression();

      parser.checkedMove(T_SEMI_COLON);
      return new StmtStatement(StatementBase.SRETURN, expr);
    }

    // for( ;; )

    if (parser.is(for_ident)) {
      List<VarDeclarator> decl = null;
      ExprExpression init = null;
      ExprExpression test = null;
      ExprExpression step = null;
      StmtStatement loop = null;

      //      pushLoop("for");
      //      parser.pushscope(); // TODO:

      Token from = parser.checkedMove(for_ident);
      parser.lparen();

      if (parser.tp() != T_SEMI_COLON) {

        if (parser.isPrimitiveOrReferenceTypeBegin()) {
          decl = new ParseVarDeclaratorsList(parser).parse(VarBase.LOCAL_VAR);
        }

        else {
          init = e_expression();
          parser.semicolon();
        }
      }

      else {
        parser.semicolon();
      }

      if (parser.tp() != T_SEMI_COLON) {
        test = e_expression();
      }
      parser.semicolon();

      if (parser.tp() != T_RIGHT_PAREN) {
        step = e_expression();
      }
      parser.rparen();

      checkLbrace();
      loop = parseStatement();

      //      popLoop();
      //      parser.popscope(); // TODO:
      return new StmtStatement(new StmtFor(decl, init, test, step, loop));
    }

    // if ( expr ) stmt else stmt

    if (parser.is(if_ident)) {
      Token from = parser.checkedMove(if_ident);

      ExprExpression ifexpr = new ParseExpression(parser).getExprInParen();
      checkLbrace();

      StmtStatement ifstmt = parseStatement();
      StmtStatement ifelse = null;

      if (parser.is(else_ident)) {
        Token elsekw = parser.checkedMove(else_ident);
        ifelse = parseStatement();
        return new StmtStatement(new Stmt_if(ifexpr, ifstmt, ifelse));
      }

      return new StmtStatement(new Stmt_if(ifexpr, ifstmt, ifelse));
    }

    // {  }

    if (parser.is(T.T_LEFT_BRACE)) {
      return parseCompoundStatement();
    }

    // expression-statement by default
    //
    StmtStatement ret = new StmtStatement(StatementBase.SEXPR, e_expression());
    parser.semicolon();
    return ret;
  }

  private void checkLbrace() {
    if (parser.is(T_SEMI_COLON)) {
      parser.perror("stray semicolon");
    }
    if (!parser.is(T.T_LEFT_BRACE)) {
      parser.perror("unbraced statements are deprecated by design.");
    }
  }

}
