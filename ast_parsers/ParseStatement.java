package njast.ast_parsers;

import static jscan.tokenize.T.T_SEMI_COLON;
import static njast.symtab.IdentMap.return_ident;

import java.util.ArrayList;
import java.util.List;

import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_checkers.IsIdent;
import njast.ast_class.vars.LocalVarDeclaration;
import njast.ast_class.vars.VarDeclaratorsList;
import njast.ast_flow.Block;
import njast.ast_flow.BlockStatement;
import njast.ast_flow.BlockStatements;
import njast.ast_flow.CExpression;
import njast.ast_flow.CStatement;
import njast.ast_flow.CStatementBase;
import njast.parse.Parse;
import njast.types.Type;

public class ParseStatement {
  private final Parse parser;

  public ParseStatement(Parse parser) {
    this.parser = parser;
  }

  private CExpression e_expression() {
    return new ParseExpression(parser).e_expression();
  }

  public BlockStatements parseBlockStamentList() {
    List<BlockStatement> bs = new ArrayList<BlockStatement>();

    BlockStatement oneBlock = parseOneBlock();
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

    return new BlockStatements(bs);
  }

  public Block parseBlock() {

    Token lbrace = parser.checkedMove(T.T_LEFT_BRACE);
    if (parser.tp() == T.T_RIGHT_BRACE) {
      Token rbrace = parser.checkedMove(T.T_RIGHT_BRACE);
      return new Block(new BlockStatements());
    }

    BlockStatements blockStatements = parseBlockStamentList();
    Block block = new Block(blockStatements);

    Token rbrace = parser.checkedMove(T.T_RIGHT_BRACE);

    return block;
  }

  public CStatement parseCompoundStatement() {

    Token lbrace = parser.tok();

    Block block = parseBlock();

    return new CStatement(lbrace, block);
  }

  private CStatement parseStatement() {

    // return ... ;
    // return ;

    if (parser.tok().isIdent(return_ident)) {
      Token from = parser.checkedMove(return_ident);

      if (parser.tp() == T_SEMI_COLON) {
        parser.move();
        return new CStatement(from, CStatementBase.SRETURN, null);
      }

      CExpression expr = e_expression();

      parser.checkedMove(T_SEMI_COLON);
      return new CStatement(from, CStatementBase.SRETURN, expr);
    }

    // {  }

    if (parser.tok().ofType(T.T_LEFT_BRACE)) {
      return parseCompoundStatement();
    }

    // expression-statement by default
    //
    CStatement ret = new CStatement(parser.tok(), CStatementBase.SEXPR, e_expression());
    parser.semicolon();
    return ret;
  }

  private BlockStatement parseOneBlock() {

    if (IsIdent.isBasicTypeIdent(parser.tok())) {

      Type type = new ParseType(parser).parse();
      VarDeclaratorsList vars = new ParseVarDeclaratorsList(parser).parse();

      LocalVarDeclaration decls = new LocalVarDeclaration(type, vars);

      return new BlockStatement(decls);
    }

    CStatement stmt = parseStatement();
    if (stmt != null) {
      return new BlockStatement(stmt);
    }

    return null;
  }

}
