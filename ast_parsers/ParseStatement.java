package njast.ast_parsers;

import static jscan.tokenize.T.T_RIGHT_PAREN;
import static jscan.tokenize.T.T_SEMI_COLON;
import static njast.symtab.IdentMap.for_ident;
import static njast.symtab.IdentMap.return_ident;

import java.util.ArrayList;
import java.util.List;

import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.expr.ExprExpression;
import njast.ast_nodes.stmt.StmtBlock;
import njast.ast_nodes.stmt.StmtBlockItem;
import njast.ast_nodes.stmt.StmtFor;
import njast.ast_nodes.stmt.StmtReturn;
import njast.ast_nodes.stmt.StmtStatement;
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
      return new StmtBlock(new ArrayList<StmtBlockItem>(0));
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
      List<VarDeclarator> vars = new ParseVarDeclaratorsList(parser).parse();
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

    // return ... ;
    // return ;

    if (parser.is(return_ident)) {
      Token from = parser.checkedMove(return_ident);

      if (parser.tp() == T_SEMI_COLON) {
        parser.move();
        return new StmtStatement(new StmtReturn(null));
      }

      ExprExpression expr = e_expression();

      parser.checkedMove(T_SEMI_COLON);
      return new StmtStatement(new StmtReturn(expr));
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
          decl = new ParseVarDeclaratorsList(parser).parse();
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

      loop = parseStatement();

      //      popLoop();
      //      parser.popscope(); // TODO:
      return new StmtStatement(new StmtFor(decl, init, test, step, loop));
    }

    // {  }

    if (parser.is(T.T_LEFT_BRACE)) {
      return parseCompoundStatement();
    }

    // expression-statement by default
    //
    StmtStatement ret = new StmtStatement(e_expression());
    parser.semicolon();
    return ret;
  }

}
