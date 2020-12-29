package njast.ast_parsers;

import static jscan.tokenize.T.T_SEMI_COLON;
import static njast.symtab.IdentMap.return_ident;

import java.util.ArrayList;
import java.util.List;

import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_checkers.IsIdent;
import njast.ast_kinds.StatementBase;
import njast.ast_nodes.clazz.vars.LocalVarDeclaration;
import njast.ast_nodes.clazz.vars.VarDeclaratorsList;
import njast.ast_nodes.expr.ExpressionNode;
import njast.ast_nodes.stmt.Block;
import njast.ast_nodes.stmt.BlockStatement;
import njast.ast_nodes.stmt.BlockStatements;
import njast.ast_nodes.stmt.Return;
import njast.ast_nodes.stmt.StatementNode;
import njast.parse.Parse;
import njast.types.Type;

public class ParseStatement {
  private final Parse parser;

  public ParseStatement(Parse parser) {
    this.parser = parser;
  }

  private ExpressionNode e_expression() {
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

  public StatementNode parseCompoundStatement() {

    Token lbrace = parser.tok();

    Block block = parseBlock();

    return new StatementNode(lbrace, block);
  }

  private StatementNode parseStatement() {

    // return ... ;
    // return ;

    if (parser.tok().isIdent(return_ident)) {
      Token from = parser.checkedMove(return_ident);

      if (parser.tp() == T_SEMI_COLON) {
        parser.move();
        return new StatementNode(new Return(null));
      }

      ExpressionNode expr = e_expression();

      parser.checkedMove(T_SEMI_COLON);
      return new StatementNode(new Return(expr));
    }

    // {  }

    if (parser.tok().ofType(T.T_LEFT_BRACE)) {
      return parseCompoundStatement();
    }

    // expression-statement by default
    //
    StatementNode ret = new StatementNode(e_expression());
    parser.semicolon();
    return ret;
  }

  private BlockStatement parseOneBlock() {

    if (parser.isClassName() || IsIdent.isBasicTypeIdent(parser.tok())) {

      Type type = new ParseType(parser).parse();
      VarDeclaratorsList vars = new ParseVarDeclaratorsList(parser).parse();

      LocalVarDeclaration decls = new LocalVarDeclaration(type, vars);

      return new BlockStatement(decls);
    }

    StatementNode stmt = parseStatement();
    if (stmt != null) {
      return new BlockStatement(stmt);
    }

    return null;
  }

}
