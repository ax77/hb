package njast.ast_parsers;

import static jscan.tokenize.T.*;
import static njast.symtab.IdentMap.*;

import java.util.ArrayList;
import java.util.List;

import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_checkers.IsIdent;
import njast.ast_nodes.clazz.vars.VarDeclarationLocal;
import njast.ast_nodes.clazz.vars.VarDeclaratorsList;
import njast.ast_nodes.expr.ExprExpression;
import njast.ast_nodes.stmt.StmtBlock;
import njast.ast_nodes.stmt.StmtBlockStatement;
import njast.ast_nodes.stmt.StmtBlockStatements;
import njast.ast_nodes.stmt.StmtFor;
import njast.ast_nodes.stmt.StmtReturn;
import njast.ast_nodes.stmt.StmtStatement;
import njast.parse.Parse;
import njast.types.Type;

public class ParseStatement {
  private final Parse parser;

  public ParseStatement(Parse parser) {
    this.parser = parser;
  }

  private ExprExpression e_expression() {
    return new ParseExpression(parser).e_expression();
  }

  public StmtBlockStatements parseBlockStamentList() {
    List<StmtBlockStatement> bs = new ArrayList<StmtBlockStatement>();

    StmtBlockStatement oneBlock = parseOneBlock();
    for (;;) {
      bs.add(oneBlock);
      if (parser.tp() == T.T_RIGHT_BRACE) {
        break;
      }
      if (oneBlock == null) {
        break;
      }
      oneBlock = parseOneBlock();
    }

    return new StmtBlockStatements(bs);
  }

  public StmtBlock parseBlock() {

    Token lbrace = parser.checkedMove(T.T_LEFT_BRACE);
    if (parser.tp() == T.T_RIGHT_BRACE) {
      Token rbrace = parser.checkedMove(T.T_RIGHT_BRACE);
      return new StmtBlock(new StmtBlockStatements());
    }

    StmtBlockStatements blockStatements = parseBlockStamentList();
    StmtBlock block = new StmtBlock(blockStatements);

    Token rbrace = parser.checkedMove(T.T_RIGHT_BRACE);

    return block;
  }

  public StmtStatement parseCompoundStatement() {

    Token lbrace = parser.tok();

    StmtBlock block = parseBlock();

    return new StmtStatement(lbrace, block);
  }

  private StmtStatement parseStatement() {

    // return ... ;
    // return ;

    if (parser.tok().isIdent(return_ident)) {
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

    if (parser.tok().isIdent(for_ident)) {
      VarDeclarationLocal decl = null;
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

          Type type = new ParseType(parser).parse();
          VarDeclaratorsList vars = new ParseVarDeclaratorsList(parser).parse();
          decl = new VarDeclarationLocal(type, vars);
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

    if (parser.tok().ofType(T.T_LEFT_BRACE)) {
      return parseCompoundStatement();
    }

    // expression-statement by default
    //
    StmtStatement ret = new StmtStatement(e_expression());
    parser.semicolon();
    return ret;
  }

  private StmtBlockStatement parseOneBlock() {

    if (parser.isClassName() || IsIdent.isBasicTypeIdent(parser.tok())) {

      Type type = new ParseType(parser).parse();
      VarDeclaratorsList vars = new ParseVarDeclaratorsList(parser).parse();

      VarDeclarationLocal decls = new VarDeclarationLocal(type, vars);

      return new StmtBlockStatement(decls);
    }

    StmtStatement stmt = parseStatement();
    if (stmt != null) {
      return new StmtBlockStatement(stmt);
    }

    return null;
  }

}
